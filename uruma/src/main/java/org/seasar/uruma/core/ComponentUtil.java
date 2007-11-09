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
package org.seasar.uruma.core;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.StringUtil;
import org.seasar.uruma.annotation.Form;
import org.seasar.uruma.binding.context.ApplicationContextBinder;
import org.seasar.uruma.binding.widget.WidgetBinder;
import org.seasar.uruma.context.PartContext;
import org.seasar.uruma.desc.PartActionDesc;
import org.seasar.uruma.desc.PartActionDescFactory;
import org.seasar.uruma.exception.RenderException;
import org.seasar.uruma.log.UrumaLogger;
import org.seasar.uruma.util.AssertionUtil;
import org.seasar.uruma.util.S2ContainerUtil;

/**
 * パートアクションクラスに関するユーティリティクラスです。<br />
 * 
 * @author y-komori
 */
public class ComponentUtil {
    private static final UrumaLogger logger = UrumaLogger
            .getLogger(ComponentUtil.class);

    private static S2Container defaultContainer;

    private ComponentUtil() {
    }

    /**
     * 各種コンポーネントを検索する際の {@link S2Container} を設定します。<br />
     * 本クラスの他のメソッドを使用する前に呼び出してください。<br />
     * 
     * @param container
     *            {@link S2Container} オブジェクト
     */
    public static void setS2Container(final S2Container container) {
        AssertionUtil.assertNotNull("container", container);

        defaultContainer = container;
    }

    /**
     * パートアクションクラスを準備します。<br />
     * 
     * @param context
     *            {@link PartContext} オブジェクト
     * @param id
     *            対応するパートの ID
     * @return パートアクションクラスが見つかった場合、そのオブジェクト。<br />
     *         見つからなかった場合は <code>null</code>
     */
    public static Object setupPartAction(final PartContext context,
            final String id) {
        String actionComponentName = StringUtil.decapitalize(id)
                + UrumaConstants.PART_ACTION_SUFFIX;

        Object partActionComponent = S2ContainerUtil.getComponentNoException(
                actionComponentName, defaultContainer);
        if (partActionComponent != null) {
            context.setPartActionObject(partActionComponent);
            PartActionDesc desc = PartActionDescFactory
                    .getPartActionDesc(partActionComponent.getClass());
            context.setPartActionDesc(desc);

            logger.log(UrumaMessageCodes.PART_ACTION_CLASS_FOUND, id,
                    actionComponentName, partActionComponent.getClass()
                            .getName());

            return partActionComponent;
        } else {
            return null;
        }
    }

    /**
     * フォームクラスを準備します。<br />
     * また、パートアクションクラスにフォームクラスのプロパティが存在する場合、 そのプロパティにフォームオブジェクトをインジェクションします。<br />
     * 
     * @param context
     *            {@link PartContext} オブジェクト
     * @param id
     *            対応するパートの ID
     * @return フォームクラスが見つかった場合、そのオブジェクト。<br />
     *         見つからなかった場合は <code>null</code>
     */
    public static Object setupFormComponent(final PartContext context,
            final String id) {
        Object formObject = null;
        Object actionObject = context.getPartActionObject();
        if (actionObject != null) {
            Form formAnnotation = actionObject.getClass().getAnnotation(
                    Form.class);
            if (formAnnotation != null) {
                Class<?> formClass = formAnnotation.value();
                if (formClass == actionObject.getClass()) {
                    formObject = actionObject;
                } else {
                    formObject = S2ContainerUtil.getComponent(formClass,
                            defaultContainer);
                }
            }
        }

        if (formObject == null) {
            String formComponentName = StringUtil.decapitalize(id)
                    + UrumaConstants.FORM_SUFFIX;
            formObject = S2ContainerUtil.getComponentNoException(
                    formComponentName, defaultContainer);
        }

        if (formObject != null) {
            logger.log(UrumaMessageCodes.FORM_CLASS_FOUND, id, formObject
                    .getClass().getName());

            context.setFormObject(formObject);
            injectFormToAction(context);
            return formObject;
        } else {
            return null;
        }
    }

    /**
     * パートアクションオブジェクトにフォームオブジェクトのプロパティが存在する場合、 {@link PartContext}
     * が保持するフォームオブジェクトをインジェクションします。
     */
    protected static void injectFormToAction(final PartContext context) {
        Object partActionObject = context.getPartActionObject();
        Object formObject = context.getFormObject();

        String formObjectName = formObject.getClass().getSimpleName();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(partActionObject
                .getClass());
        if (beanDesc.hasPropertyDesc(formObjectName)) {
            PropertyDesc pd = beanDesc.getPropertyDesc(formObjectName);
            pd.setValue(partActionObject, formObject);
        }
    }

    /**
     * <code>viewer</code> に対して {@link IContentProvider} を設定します。<br />
     * <p>
     * 本メソッドでは以下の動作を行います。<br />
     * <ol>
     * <li>S2Container 上に、&lt;コンポーネントのID&gt;ContentProvider という名称で S2
     * コンポーネントが登録されているか確認する。
     * <li>登録されていれば、その S2 コンポーネントが {@link IContentProvider} の実装クラスであるかどうかを確認する。
     * <li>サブクラスであれば、その S2 コンポーネントをコンテントプロバイダとして <code>viewer</code> へ設定する。
     * <li>S2 コンポーネントが見つからない場合、 <code>defaultProvider</code> で指定された
     * オブジェクトをコンテントプロバイダとして <code>viewer</code> へ設定する。
     * </ol>
     * </p>
     * 
     * @param viewer
     *            設定対象のビューア
     * @param id
     *            ビューアのコンポーネントID
     * @param defaultProvider
     *            デフォルトで指定するコンテントプロバイダ
     * @throws RenderException
     *             該当する名称の S2 コンポーネントが {@link IContentProvider} の実装クラスでない場合
     * @see ContentViewer#setContentProvider(IContentProvider)
     */
    public static void setupContentProvider(final ContentViewer viewer,
            final String id, final IContentProvider defaultProvider) {
        IContentProvider provider = null;
        if (!StringUtil.isEmpty(id)) {
            Object defined = S2ContainerUtil.getComponentNoException(id
                    + UrumaConstants.CONTENT_PROVIDER_SUFFIX, defaultContainer);
            if (defined != null) {
                if (defined instanceof IContentProvider) {
                    provider = IContentProvider.class.cast(defined);
                } else {
                    throw new RenderException(
                            UrumaMessageCodes.UNSUPPORTED_TYPE_ERROR, provider,
                            IContentProvider.class.getName());
                }
            }
        }

        if (provider == null) {
            provider = defaultProvider;
        }

        if (provider != null) {
            viewer.setContentProvider(provider);

            logger.log(UrumaMessageCodes.CONTENT_PROVIDER_FOUND, id, provider
                    .getClass());
        }
    }

    /**
     * <code>viewer</code> に対して {@link ILabelProvider} を設定します。<br />
     * <p>
     * 本メソッドでは以下の動作を行います。<br />
     * <ol>
     * <li>S2Container 上に、&lt;コンポーネントのID&gt;LabelProvider という名称で S2
     * コンポーネントが登録されているか確認する。
     * <li>登録されていれば、その S2 コンポーネントが <code>providerClass</code>
     * のサブクラスであるかどうかを確認する。
     * <li>サブクラスであれば、その S2 コンポーネントをラベルプロバイダとして <code>viewer</code> へ設定する。
     * <li>S2 コンポーネントが見つからない場合、 <code>defaultProvider</code> をラベルプロバイダとして
     * <code>viewer</code> へ設定する。
     * </ol>
     * </p>
     * 
     * @param viewer
     *            設定対象のビューア
     * @param id
     *            ビューアのコンポーネントID
     * @throws RenderException
     *             該当する名称の S2 コンポーネントが <code>providerClass</code> のサブクラスでない場合
     * @see StructuredViewer#setLabelProvider(IBaseLabelProvider)
     */
    public static void setupLabelProvider(final StructuredViewer viewer,
            final String id, final IBaseLabelProvider defaultProvider,
            final Class<? extends IBaseLabelProvider> providerClass) {
        IBaseLabelProvider provider = null;
        if (!StringUtil.isEmpty(id)) {
            Object defined = S2ContainerUtil.getComponentNoException(id
                    + UrumaConstants.LABEL_PROVIDER_SUFFIX, defaultContainer);
            if (defined != null) {
                if (providerClass.isAssignableFrom(defined.getClass())) {
                    provider = providerClass.cast(defined);
                } else {
                    throw new RenderException(
                            UrumaMessageCodes.UNSUPPORTED_TYPE_ERROR, defined
                                    .getClass().getName(), providerClass
                                    .getName());
                }
            }
        }

        if (provider == null) {
            provider = defaultProvider;
        }

        if (provider != null) {
            viewer.setLabelProvider(provider);

            logger.log(UrumaMessageCodes.LABEL_PROVIDER_FOUND, id, provider
                    .getClass());
        }
    }

    /**
     * <code>viewer</code> に対して {@link ViewerComparator} を設定します。<br />
     * <p>
     * 本メソッドでは以下の動作を行います。<br />
     * <ol>
     * <li>S2Container 上に、&lt;コンポーネントのID&gt;Comparator という名称で S2
     * コンポーネントが登録されているか確認する。
     * <li>登録されていれば、その S2 コンポーネントが {@link ViewerComparator} のサブクラスであるかどうかを確認する。
     * <li>サブクラスであれば、その S2 コンポーネントをコンパレータとして <code>viewer</code> へ設定する。
     * <li>S2 コンポーネントが見つからない場合、 <code>defaultComparator</code> をコンパレータとして
     * <code>viewer</code> へ設定する。
     * </ol>
     * </p>
     * 
     * @param viewer
     *            設定対象のビューア
     * @param id
     *            ビューアのコンポーネントID
     * @throws RenderException
     *             該当する名称の S2 コンポーネントが {@link ViewerComparator} のサブクラスでない場合
     * @see StructuredViewer#setComparator(ViewerComparator)
     */
    public static void setupComparator(final StructuredViewer viewer,
            final String id, final ViewerComparator defaultComparator) {
        if (!StringUtil.isEmpty(id)) {
            Object comparator = S2ContainerUtil
                    .getComponentNoException(id
                            + UrumaConstants.SORTER_SUFFIX,
                            defaultContainer);
            if (comparator != null) {
                if (comparator instanceof ViewerComparator) {
                    viewer.setComparator(ViewerComparator.class
                            .cast(comparator));

                    logger.log(UrumaMessageCodes.COMPARATOR_FOUND, id,
                            comparator.getClass());
                } else {
                    throw new RenderException(
                            UrumaMessageCodes.UNSUPPORTED_TYPE_ERROR,
                            comparator, ViewerComparator.class.getName());
                }
            } else {
                viewer.setComparator(defaultComparator);

                String className = "(null)";
                if (defaultComparator != null) {
                    className = defaultComparator.getClass().getName();
                    logger.log(UrumaMessageCodes.COMPARATOR_FOUND, id,
                            className);
                }
            }
        }
    }

    /**
     * パートアクションクラスの初期化メソッドを呼び出します。<br />
     * 
     * @param partAction
     *            パートアクションオブジェクト
     * @param context
     *            {@link PartContext}
     */
    public static void invokeInitMethodOnAction(final Object partAction,
            final PartContext context) {
        if (partAction != null) {
            WidgetBinder.bindWidgets(partAction, context);

            // ApplicationContext からのインポート処理
            PartActionDesc desc = PartActionDescFactory
                    .getPartActionDesc(partAction.getClass());
            ApplicationContextBinder.importObjects(partAction, desc
                    .getApplicationContextDefList(), context.getWindowContext()
                    .getApplicationContext());

            desc.invokeInitializeMethod(partAction);
        }
    }
}