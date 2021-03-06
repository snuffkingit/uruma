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

import org.seasar.uruma.component.UIComponent;
import org.seasar.uruma.context.PartContext;
import org.seasar.uruma.context.WidgetHandle;
import org.seasar.uruma.context.WindowContext;
import org.seasar.uruma.renderer.Renderer;

/**
 * 何も処理を行わないレンダラです。<br />
 * 
 * @author y-komori
 */
public class NullRenderer implements Renderer {

    /*
     * @see org.seasar.uruma.renderer.Renderer#preRender(org.seasar.uruma.component.UIComponent,
     *      org.seasar.uruma.context.WidgetHandle,
     *      org.seasar.uruma.context.WindowContext)
     */
    public WidgetHandle preRender(final UIComponent uiComponent, final WidgetHandle parent,
            final WindowContext context) {
        // Do nothing.
        return null;
    }

    /*
     * @see org.seasar.uruma.renderer.Renderer#render(org.seasar.uruma.component.UIComponent,
     *      org.seasar.uruma.context.WidgetHandle,
     *      org.seasar.uruma.context.PartContext)
     */
    public WidgetHandle render(final UIComponent uiComponent, final WidgetHandle parent,
            final PartContext context) {
        // Do nothing.
        return null;
    }

    /*
     * @see org.seasar.uruma.renderer.Renderer#renderAfter(org.seasar.uruma.context.WidgetHandle,
     *      org.seasar.uruma.component.UIComponent,
     *      org.seasar.uruma.context.WidgetHandle,
     *      org.seasar.uruma.context.PartContext)
     */
    public void renderAfter(final WidgetHandle widget, final UIComponent uiComponent,
            final WidgetHandle parent, final PartContext context) {
        // Do nothing.
    }

    /*
     * @see org.seasar.uruma.renderer.Renderer#reRender(org.seasar.uruma.context.WidgetHandle, org.seasar.uruma.context.PartContext)
     */
    public void reRender(final WidgetHandle widget, final PartContext context) {
        // Do nothing.
    }
}
