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
package org.seasar.uruma.renderer.impl;

import org.eclipse.swt.widgets.Link;
import org.seasar.uruma.annotation.RenderingPolicy.ConversionType;
import org.seasar.uruma.component.impl.LinkComponent;
import org.seasar.uruma.renderer.RendererSupportUtil;

/**
 * {@link Link} のレンダリングを行うクラスです。<br />
 * 
 * @author bskuroneko
 */
public class LinkRenderer extends AbstractControlRenderer<LinkComponent, Link> {

    /*
     * @see org.seasar.uruma.renderer.impl.AbstractControlRenderer#doRenderControl(org.seasar.uruma.component.impl.ControlComponent,
     *      org.eclipse.swt.widgets.Control)
     */
    @Override
    protected void doRenderControl(final LinkComponent controlComponent,
            final Link control) {
        setText(controlComponent, control);
    }

    private void setText(final LinkComponent controlComponent,
            final Link control) {
        String value = controlComponent.getText();
        String text = (String) RendererSupportUtil.convertValue(
                controlComponent, value, ConversionType.TEXT);
        if (text.indexOf("<a") == -1 && text.indexOf("</a") == -1) {
            text = "<a>" + text + "</a>";
        }
        control.setText(text);
    }

    /*
     * @see org.seasar.uruma.renderer.impl.AbstractWidgetRenderer#getWidgetType()
     */
    @Override
    protected Class<Link> getWidgetType() {
        return Link.class;
    }
}