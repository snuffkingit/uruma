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

import org.eclipse.swt.widgets.Composite;
import org.seasar.uruma.annotation.ComponentElement;
import org.seasar.uruma.component.CommonAttributes;
import org.seasar.uruma.component.LayoutDataInfo;
import org.seasar.uruma.component.LayoutInfo;
import org.seasar.uruma.component.UIComponent;
import org.seasar.uruma.component.UICompositeComponent;
import org.seasar.uruma.component.UIElement;
import org.seasar.uruma.component.UIElementVisitor;
import org.seasar.uruma.context.PartContext;
import org.seasar.uruma.context.WidgetHandle;
import org.seasar.uruma.context.WindowContext;

/**
 * {@link Composite} に対応するコンポーネントです。<br />
 * 
 * @author y-komori
 */
@ComponentElement
public class CompositeComponent extends ControlComponent implements
        UICompositeComponent {
    private LayoutInfo<?> layoutInfo;

    private LayoutDataInfo childLayoutDataInfo;

    private CommonAttributes commonAttributes;

    private List<UIElement> children = new ArrayList<UIElement>();

    /*
     * @see org.seasar.uruma.component.UICompositeComponent#getLayoutInfo()
     */
    public LayoutInfo<?> getLayoutInfo() {
        return this.layoutInfo;
    }

    /*
     * @see org.seasar.uruma.component.UICompositeComponent#setLayoutInfo(org.seasar.uruma.component.LayoutInfo)
     */
    public void setLayoutInfo(final LayoutInfo<?> layoutInfo) {
        this.layoutInfo = layoutInfo;
    }

    /**
     * 子コンポーネントの {@link LayoutDataInfo} を取得します。<br />
     * 
     * @return 子コンポーネントの {@link LayoutDataInfo}
     */
    public LayoutDataInfo getChildLayoutDataInfo() {
        return this.childLayoutDataInfo;
    }

    /**
     * 子コンポーネントの {@link LayoutDataInfo} を設定します。<br />
     * 
     * @param childLayoutDataInfo
     *            子コンポーネントの {@link LayoutDataInfo}
     */
    public void setChildLayoutDataInfo(final LayoutDataInfo childLayoutDataInfo) {
        this.childLayoutDataInfo = childLayoutDataInfo;
    }

    /*
     * @see org.seasar.uruma.component.UICompositeComponent#getCommonAttributes()
     */
    public CommonAttributes getCommonAttributes() {
        return commonAttributes;
    }

    /*
     * @see org.seasar.uruma.component.UICompositeComponent#setCommonAttributes(org.seasar.uruma.component.CommonAttributes)
     */
    public void setCommonAttributes(final CommonAttributes commonAttributes) {
        this.commonAttributes = commonAttributes;
    }

    /*
     * @see org.seasar.uruma.component.UIElementContainer#addChild(org.seasar.uruma.component.UIElement)
     */
    public void addChild(final UIElement child) {
        this.children.add(child);

        if (child instanceof UIComponent) {
            ((UIComponent) child).setParent(this);
        }
    }

    /*
     * @see org.seasar.uruma.component.UIElementContainer#getChildren()
     */
    public List<UIElement> getChildren() {
        return children;
    }

    /*
     * @see org.seasar.uruma.component.jface.AbstractUIComponent#doPreRender(org.seasar.uruma.context.WidgetHandle,
     *      org.seasar.uruma.context.WindowContext)
     */
    @Override
    protected void doPreRender(final WidgetHandle parent,
            final WindowContext context) {
        preRenderChild(context.getWidgetHandle(getId()), context);
    }

    /*
     * @see org.seasar.uruma.component.jface.AbstractUIComponent#doRender(org.seasar.uruma.context.WidgetHandle,
     *      org.seasar.uruma.context.PartContext)
     */
    @Override
    protected void doRender(final WidgetHandle parent, final PartContext context) {
        renderChild(context.getWidgetHandle(getId()), context);
    }

    /*
     * @see org.seasar.uruma.component.base.AbstractUIElement#accept(org.seasar.uruma.component.UIElementVisitor)
     */
    @Override
    public void accept(final UIElementVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * 子コンポーネントのプリレンダリングを行います。<br />
     * 
     * @param parent
     *            親 {@link WidgetHandle}
     * @param context
     *            {@link WindowContext} オブジェクト
     */
    protected void preRenderChild(final WidgetHandle parent,
            final WindowContext context) {
        for (UIElement child : children) {
            if (child instanceof UIComponent) {
                ((UIComponent) child).preRender(parent, context);
            }
        }
    }

    /**
     * 子コンポーネントのレンダリングを行います。<br />
     * 
     * @param parent
     *            親 {@link WidgetHandle}
     * @param context
     *            {@link PartContext} オブジェクト
     */
    protected void renderChild(final WidgetHandle parent,
            final PartContext context) {
        for (UIElement child : children) {
            if (child instanceof UIComponent) {
                ((UIComponent) child).render(parent, context);
            }
        }
    }
}
