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

import static org.seasar.jface.annotation.component.ComponentAttribute.ConversionType.BOOLEAN;
import static org.seasar.jface.annotation.component.ComponentAttribute.ConversionType.COLOR;
import static org.seasar.jface.annotation.component.ComponentAttribute.ConversionType.INT;
import static org.seasar.jface.annotation.component.ComponentAttribute.ConversionType.STRING;
import static org.seasar.jface.annotation.component.ComponentAttribute.ConversionType.SWT_CONST;
import static org.seasar.jface.annotation.component.ComponentAttribute.ConversionType.TEXT;
import static org.seasar.jface.annotation.component.ComponentAttribute.TargetType.FIELD;
import static org.seasar.jface.annotation.component.ComponentAttribute.TargetType.NONE;

import org.seasar.jface.annotation.component.ComponentAttribute;

/**
 * @author y-komori
 */
public class SrcObject {
    @ComponentAttribute(targetType = NONE)
    private String nonTargetField = "NonTarget";

    @ComponentAttribute(targetType = FIELD, conversionType = STRING)
    private String stringField = "StringField1";

    @ComponentAttribute(targetType = FIELD, conversionType = TEXT)
    private String textField = "Text\\tField1\\nText\\tField1\\n";

    @ComponentAttribute(targetType = FIELD, conversionType = INT)
    private String intField = "123";

    @ComponentAttribute(targetType = FIELD, conversionType = BOOLEAN)
    private String booleanField = "true";

    @ComponentAttribute(targetType = FIELD, conversionType = COLOR)
    private String colorField = "#FFFFFF";

    @ComponentAttribute(targetType = FIELD, conversionType = SWT_CONST)
    private String swtConstField = "YES";

    @ComponentAttribute(conversionType = STRING)
    private String stringProperty = "StringField2";

    @ComponentAttribute(conversionType = TEXT)
    private String textProperty = "Text\\tField2\\nText\\tField2\\n";

    @ComponentAttribute(conversionType = INT)
    private String intProperty = "456";

    @ComponentAttribute(conversionType = BOOLEAN)
    private String booleanProperty = "false";

    @ComponentAttribute(conversionType = COLOR)
    private String colorProperty = "#000000";

    @ComponentAttribute(conversionType = SWT_CONST)
    private String swtConstProperty = "NO";

    public String getNonTargetField() {
        return this.nonTargetField;
    }

    public String getBooleanField() {
        return this.booleanField;
    }

    public String getBooleanProperty() {
        return this.booleanProperty;
    }

    public String getColorField() {
        return this.colorField;
    }

    public String getColorProperty() {
        return this.colorProperty;
    }

    public String getIntField() {
        return this.intField;
    }

    public String getIntProperty() {
        return this.intProperty;
    }

    public String getStringField() {
        return this.stringField;
    }

    public String getStringProperty() {
        return this.stringProperty;
    }

    public String getSwtConstField() {
        return this.swtConstField;
    }

    public String getSwtConstProperty() {
        return this.swtConstProperty;
    }

    public String getTextField() {
        return this.textField;
    }

    public String getTextProperty() {
        return this.textProperty;
    }
}