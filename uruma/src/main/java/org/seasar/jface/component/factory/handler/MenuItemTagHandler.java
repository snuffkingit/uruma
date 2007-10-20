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
package org.seasar.jface.component.factory.handler;

import org.seasar.framework.xml.TagHandlerContext;
import org.seasar.jface.component.Menu;
import org.seasar.jface.component.UIElement;
import org.seasar.jface.component.impl.MenuItemComponent;

/**
 * <code>menuItem</code> 要素に対するタグハンドラです。<br />
 * 
 * @author y-komori
 */
public class MenuItemTagHandler extends S2JFaceGenericTagHandler {

    private static final long serialVersionUID = -5380844837424058319L;

    /**
     * {@link MenuItemTagHandler} を構築します。
     */
    public MenuItemTagHandler() {
        super(MenuItemComponent.class);
    }

    @Override
    public String getElementPath() {
        return "menuItem";
    }

    @Override
    protected void setParent(UIElement uiElement, TagHandlerContext context) {
        MenuItemComponent menuItem = (MenuItemComponent) uiElement;
        Menu parent = (Menu) context.peek();
        parent.addMenuItem(menuItem);
        menuItem.setParentMenu(parent);
    }
}