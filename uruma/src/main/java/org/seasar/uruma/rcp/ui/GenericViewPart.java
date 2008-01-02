/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.uruma.rcp.ui;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.StringUtil;
import org.seasar.uruma.annotation.SelectionListener;
import org.seasar.uruma.binding.method.MethodBindingSupport;
import org.seasar.uruma.binding.method.SingleParamTypeMethodBinding;
import org.seasar.uruma.binding.value.ValueBindingSupport;
import org.seasar.uruma.component.Template;
import org.seasar.uruma.component.UIComponentContainer;
import org.seasar.uruma.component.rcp.ViewPartComponent;
import org.seasar.uruma.context.ApplicationContext;
import org.seasar.uruma.context.ContextFactory;
import org.seasar.uruma.context.PartContext;
import org.seasar.uruma.context.WidgetHandle;
import org.seasar.uruma.context.WindowContext;
import org.seasar.uruma.core.ComponentUtil;
import org.seasar.uruma.core.TemplateManager;
import org.seasar.uruma.core.UrumaConstants;
import org.seasar.uruma.core.UrumaMessageCodes;
import org.seasar.uruma.exception.RenderException;
import org.seasar.uruma.log.UrumaLogger;
import org.seasar.uruma.rcp.UrumaAppActivator;
import org.seasar.uruma.rcp.binding.GenericSelectionListener;
import org.seasar.uruma.rcp.binding.NullGenericSelectionListener;
import org.seasar.uruma.util.AnnotationUtil;
import org.seasar.uruma.util.S2ContainerUtil;

/**
 * 汎用的な {@link IViewPart} クラスです。<br />
 * <p>
 * 本クラスのインタンスは、画面定義テンプレートで指定された ID をキーとして {@link S2Container} へ登録されます。
 * </p>
 * <p>
 * また、当該 {@link IViewPart} の中で使用されている {@link Viewer} が一つしか存在しない場合、その
 * {@link Viewer} を自動的に {@link ISelectionProvider} として
 * {@link IWorkbenchPartSite} へ登録します。<br />
 * {@link Viewer} が複数存在する場合、自動登録は行いません。<br />
 * </p>
 * 
 * @author y-komori
 */
public class GenericViewPart extends ViewPart {
    private UrumaAppActivator activator = UrumaAppActivator.getInstance();

    private static final UrumaLogger logger = UrumaLogger
            .getLogger(GenericViewPart.class);

    /**
     * {@link TemplateManager} オブジェクト
     */
    public TemplateManager templateManager;

    /**
     * {@link ApplicationContext} オブジェクト
     */
    public ApplicationContext applicationContext;

    private PartContext partContext;

    private ViewPartComponent viewPart;

    private String componentId;

    private Object partAction;

    private List<ISelectionListener> listeners = new ArrayList<ISelectionListener>();

    /*
     * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite,
     *      org.eclipse.ui.IMemento)
     */
    @Override
    public void init(final IViewSite site, final IMemento memento)
            throws PartInitException {
        super.init(site, memento);

        S2ContainerUtil.injectDependency(this, activator.getS2Container());

        this.componentId = activator.getLocalId(getSite().getId());

        if (StringUtil.isNotBlank(componentId)) {
            activator.getS2Container().register(this, componentId);
        }

        Template template = templateManager.getTemplateById(componentId);
        UIComponentContainer root = template.getRootComponent();
        if (root instanceof ViewPartComponent) {
            this.viewPart = (ViewPartComponent) root;

            this.partContext = createPartContext(componentId);

            this.partAction = ComponentUtil.setupPartAction(partContext,
                    componentId);

            if (partAction != null) {
                ComponentUtil.setupFormComponent(partContext, componentId);

                // @Initialize メソッドの呼び出し
                ComponentUtil.invokeInitMethodOnAction(partAction, partContext);
            }
        } else {
            throw new RenderException(
                    UrumaMessageCodes.REQUIRED_VIEWPART_ERROR, template
                            .getPath());
        }
    }

    /**
     * {@link GenericViewPart} を構築します。<br />
     */
    public GenericViewPart() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        WidgetHandle parentHandle = ContextFactory.createWidgetHandle(parent);
        parentHandle.setUiComponent(activator.getWorkbenchComponent());

        viewPart.render(parentHandle, partContext);

        prepareSelectionProvider(partContext);

        setupSelectionListeners();

        MethodBindingSupport.createListeners(partContext);

        // 画面初期表示時の、フォームから画面へのエクスポート処理を実施
        ValueBindingSupport.exportValue(partContext);
        ValueBindingSupport.exportSelection(partContext);
    }

    /*
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        // Do nothing.
    }

    /*
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        IWorkbenchPage page = getSite().getPage();
        for (ISelectionListener listener : listeners) {
            page.removeSelectionListener(listener);
        }
        super.dispose();
    }

    protected PartContext createPartContext(final String id) {
        WindowContext windowContext = applicationContext
                .getWindowContext(UrumaConstants.WORKBENCH_WINDOW_CONTEXT_ID);
        return ContextFactory.createPartContext(windowContext, id);
    }

    protected void prepareSelectionProvider(final PartContext context) {
        List<WidgetHandle> viewers = context.getWidgetHandles(Viewer.class);
        if (viewers.size() == 1) {
            Viewer viewer = viewers.get(0).<Viewer> getCastWidget();
            getSite().setSelectionProvider(viewer);
        }
    }

    protected void setupSelectionListeners() {
        if (partAction == null) {
            return;
        }

        List<Method> listenerMethods = AnnotationUtil.getAnnotatedMethods(
                partAction.getClass(), SelectionListener.class);

        for (Method method : listenerMethods) {
            if (Modifier.isPublic(method.getModifiers())) {
                SelectionListener anno = method
                        .getAnnotation(SelectionListener.class);
                boolean nullSelection = anno.nullSelection();
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length <= 1) {
                    SingleParamTypeMethodBinding methodBinding = new SingleParamTypeMethodBinding(
                            partAction, method);

                    GenericSelectionListener listener;
                    if (nullSelection) {
                        listener = new NullGenericSelectionListener(
                                partContext, methodBinding);
                    } else {
                        listener = new GenericSelectionListener(partContext,
                                methodBinding);
                    }

                    String partId = anno.partId();

                    if (StringUtil.isEmpty(partId)) {
                        getSite().getPage().addSelectionListener(listener);
                    } else {
                        partId = UrumaAppActivator.getInstance().createRcpId(
                                partId);
                        getSite().getPage().addSelectionListener(partId,
                                listener);
                    }

                    logger.log(
                            UrumaMessageCodes.ISELECTION_LISTENER_REGISTERED,
                            getViewSite().getId(), methodBinding, partId);
                    listeners.add(listener);
                }
            }
        }
    }
}
