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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.seasar.framework.util.StringUtil;
import org.seasar.uruma.component.rcp.WorkbenchComponent;
import org.seasar.uruma.context.ContextFactory;
import org.seasar.uruma.context.WidgetHandle;
import org.seasar.uruma.context.WindowContext;
import org.seasar.uruma.core.UrumaConstants;
import org.seasar.uruma.core.UrumaMessageCodes;
import org.seasar.uruma.exception.NotFoundException;
import org.seasar.uruma.exception.RenderException;
import org.seasar.uruma.rcp.UrumaAppActivator;

/**
 * {@link WorkbenchComponent} の内容からメニューバーを構築するクラスです。<br />
 * 
 * @author y-komori
 */
public class UrumaActionBarAdvisor extends ActionBarAdvisor {
    private IActionBarConfigurer configurer;

    /**
     * {@link UrumaActionBarAdvisor} を構築します。<br />
     * 
     * @param configurer
     *            {@link IActionBarConfigurer} オブジェクト
     */
    public UrumaActionBarAdvisor(final IActionBarConfigurer configurer) {
        super(configurer);
        this.configurer = configurer;
    }

    /*
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    protected void fillMenuBar(final IMenuManager menuBar) {
        WindowContext context = UrumaAppActivator.getInstance()
                .getWorkbenchWindowContext();

        WorkbenchComponent workbench = UrumaAppActivator.getInstance()
                .getWorkbenchComponent();
        WidgetHandle handle;
        if (StringUtil.isNotBlank(workbench.menu)) {
            handle = context.getWidgetHandle(workbench.menu);
        } else {
            return;
        }

        if (handle != null) {
            if (handle.instanceOf(MenuManager.class)) {
                MenuManager menuManager = handle.<MenuManager> getCastWidget();
                IContributionItem[] items = menuManager.getItems();
                for (int i = 0; i < items.length; i++) {
                    menuBar.add(items[i]);
                }

                // ビューの表示アクションの作成
                // MenuManager showViewMenu = new MenuManager("ビューの表示(&V)");
                // showViewMenu.add(ContributionItemFactory.VIEWS_SHORTLIST
                // .create(configurer.getWindowConfigurer().getWindow()));
                // menuBar.add(showViewMenu);
                // 複数のウィンドウが開かれている場合にのみ表示され、
                // ウィンドウの切り替えを行うアクション
                // menu.add(ContributionItemFactory.OPEN_WINDOWS.create(window));
            } else {
                throw new RenderException(
                        UrumaMessageCodes.UNSUPPORTED_TYPE_ERROR,
                        workbench.menu, MenuManager.class.getName());
            }
        } else {
            throw new NotFoundException(
                    UrumaMessageCodes.UICOMPONENT_NOT_FOUND, workbench.menu);
        }

    }

    @Override
    protected void fillStatusLine(final IStatusLineManager statusLine) {
        // IStatusLineManager を WindowContext へ登録しておく
        WorkbenchComponent workbench = UrumaAppActivator.getInstance()
                .getWorkbenchComponent();

        if (Boolean.parseBoolean(workbench.statusLine)) {
            WidgetHandle handle = ContextFactory.createWidgetHandle(statusLine);
            handle.setId(UrumaConstants.STATUS_LINE_MANAGER_CID);

            UrumaAppActivator.getInstance().getWorkbenchWindowContext()
                    .putWidgetHandle(handle);
        }
    }

    /*
     * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
     */
    @Override
    protected void makeActions(final IWorkbenchWindow window) {
        // do nothing
    }
}
