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
package org.seasar.uruma.renderer.impl;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.seasar.uruma.component.jface.TreeItemComponent;

/**
 * {@link TreeItem} のレンダリングを行うクラスです。<br />
 * 
 * @author y-komori
 * @author $Author$
 * @version $Revision$
 */
public class TreeItemRenderer extends AbstractWidgetRenderer<TreeItemComponent, TreeItem> {

    /*
     * @see org.seasar.uruma.renderer.impl.AbstractWidgetRenderer#doRender(org.seasar.uruma.component.UIComponent,
     *      org.eclipse.swt.widgets.Widget)
     */
    @Override
    protected void doRender(final TreeItemComponent uiComponent, final TreeItem widget) {
        renderText(uiComponent, widget);
        renderImage(uiComponent, widget);
    }

    /*
     * @see org.seasar.uruma.renderer.impl.AbstractWidgetRenderer#getWidgetType()
     */
    @Override
    protected Class<TreeItem> getWidgetType() {
        return TreeItem.class;
    }

    protected void renderText(final TreeItemComponent component, final TreeItem widget) {
        String text = component.text;
        if (text != null) {
            widget.setText(text);
        }
    }

    protected void renderImage(final TreeItemComponent component, final TreeItem widget) {
        String value = component.image;

        if (value != null) {
            Image image = getResourceRegistry().getImage(value, component.getParentURL());
            widget.setImage(image);
        }
    }
}
