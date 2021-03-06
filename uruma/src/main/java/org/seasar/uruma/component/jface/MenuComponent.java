/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.uruma.component.jface;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.util.StringUtil;
import org.seasar.uruma.annotation.ComponentAttribute;
import org.seasar.uruma.annotation.ComponentElement;
import org.seasar.uruma.annotation.FieldDescription;
import org.seasar.uruma.annotation.RenderingPolicy;
import org.seasar.uruma.annotation.RenderingPolicy.TargetType;
import org.seasar.uruma.component.UIComponent;
import org.seasar.uruma.component.UIComponentContainer;
import org.seasar.uruma.component.UIElement;
import org.seasar.uruma.component.UIElementVisitor;
import org.seasar.uruma.component.rcp.ViewPartComponent;
import org.seasar.uruma.context.WidgetHandle;
import org.seasar.uruma.context.WindowContext;
import org.seasar.uruma.util.AssertionUtil;

/**
 * メニュー情報を保持するためのコンポーネントです。<br />
 * 
 * @author bskuroneko
 * @author y-komori
 */
@ComponentElement
public class MenuComponent extends MenuItemComponent implements
        UIComponentContainer {
    /**
     * デフォルトアイテムIDです。<br />
     */
    @RenderingPolicy(targetType = TargetType.NONE)
    @ComponentAttribute
    @FieldDescription("デフォルトアイテムID")
    public String defaultItemId;

    /**
     * 可視状態です。<br />
     */
    @RenderingPolicy(targetType = TargetType.NONE)
    @ComponentAttribute
    @FieldDescription("可視状態")
    public String visible;

    /**
     * メニューの表示 X座標です。<br />
     */
    @RenderingPolicy(targetType = TargetType.NONE)
    @ComponentAttribute
    @FieldDescription("メニューの表示 X 座標")
    public String x;

    /**
     * メニューの表示 Y 座標です。<br />
     */
    @RenderingPolicy(targetType = TargetType.NONE)
    @ComponentAttribute
    @FieldDescription("メニューの表示 Y 座標")
    public String y;

    private List<UIElement> children = new ArrayList<UIElement>();

    /*
     * @see
     * org.seasar.uruma.component.UIElementContainer#addChild(org.seasar.uruma
     * .component.UIElement)
     */
    public void addChild(final UIElement child) {
        AssertionUtil.assertNotNull("child", child);
        AssertionUtil.assertInstanceOf("child", MenuItemComponent.class, child);

        this.children.add(child);
    }

    /*
     * @see org.seasar.uruma.component.UIElementContainer#getChildren()
     */
    public List<UIElement> getChildren() {
        return this.children;
    }

    /*
     * @see
     * org.seasar.uruma.component.jface.AbstractUIComponent#doPreRender(org.
     * seasar.uruma.context.WidgetHandle,
     * org.seasar.uruma.context.WindowContext)
     */
    @Override
    protected void doPreRender(final WidgetHandle parent,
            final WindowContext context) {
        UIComponentContainer parentComponent = getParent();

        if (parentComponent instanceof WindowComponent) {
            WindowComponent windowComponent = (WindowComponent) parentComponent;
            if (StringUtil.isEmpty(windowComponent.menu)) {
                windowComponent.menu = getId();
            }
        } else if (parentComponent instanceof ViewPartComponent) {
            // RCP版の場合は、メニューをレンダリングしない
            return;
        }

        for (UIElement child : children) {
            if (child instanceof UIComponent) {
                ((UIComponent) child).preRender(context
                        .getWidgetHandle(getId()), context);
            }
        }
    }

    /*
     * @see
     * org.seasar.uruma.component.base.AbstractUIElement#accept(org.seasar.uruma
     * .component.UIElementVisitor)
     */
    @Override
    public void accept(final UIElementVisitor visitor) {
        visitor.visit(this);
    }
}
