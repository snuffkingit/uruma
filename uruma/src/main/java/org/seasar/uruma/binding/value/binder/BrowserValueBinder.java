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
package org.seasar.uruma.binding.value.binder;

import org.eclipse.swt.browser.Browser;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.uruma.binding.value.ValueBinder;
import org.seasar.uruma.component.UIComponent;

/**
 * {@link Browser} のための {@link ValueBinder} です。<br />
 * 
 * @author y.sugigami
 */
public class BrowserValueBinder extends AbstractValueBinder<Browser> {

    /**
     * {@link BrowserValueBinder} を構築します。<br />
     * 
     */
    public BrowserValueBinder() {
        super(Browser.class);
    }

    /*
     * @see org.seasar.uruma.binding.value.binder.AbstractValueBinder#doImportValue(java.lang.Object,
     *      java.lang.Object, org.seasar.framework.beans.PropertyDesc)
     */
    @Override
    public void doImportValue(final Browser widget, final Object formObj,
            final PropertyDesc propDesc, final UIComponent uiComp) {
        Object value = propDesc.getValue(widget);

        // 空文字列の入力は null として扱う
        if (value instanceof String && ((String) value).length() == 0) {
            value = null;
        }

        logBinding(IMPORT_VALUE, widget, propDesc, formObj, propDesc, value);

        propDesc.setValue(formObj, value);
    }

    /*
     * @see org.seasar.uruma.binding.value.binder.AbstractValueBinder#doExportValue(java.lang.Object,
     *      java.lang.Object, org.seasar.framework.beans.PropertyDesc)
     */
    @Override
    protected void doExportValue(final Browser widget, final Object formObj,
            final PropertyDesc propDesc, final UIComponent uiComp) {

        Object value = propDesc.getValue(formObj);

        if (value == null) {
            value = "";
        }

        logBinding(EXPORT_VALUE, formObj, propDesc, widget, propDesc, value);

        widget.setText((String) value);

    }
}
