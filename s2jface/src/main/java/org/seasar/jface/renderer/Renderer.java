/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.jface.renderer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.seasar.jface.WindowContext;
import org.seasar.jface.component.UIComponent;

/**
 * Widgetをレンダリングするためのインターフェースです。</br>
 * 
 * @author y-komori
 * 
 */
public interface Renderer {
    /**
     * レンダリングを行います。</br>
     * 
     * @param uiComponent
     *            レンダリング対象の情報を持つ <code>UIComponent</code> オブジェクト
     * @param parent
     *            親コンポジット
     * @param context
     *            画面情報を収めた <code>WindowContext</code> オブジェクト
     * @return
     * @see Widget
     * @see Composite
     */
    public Widget render(UIComponent uiComponent, Composite parent,
            WindowContext context);

    /**
     * レンダラの名称を返します。</br> レンダラ名称は、画面定義XML上で <code>type</code> 属性として指定されます。
     * 
     * @return レンダラの名称
     */
    public String getRendererName();
}
