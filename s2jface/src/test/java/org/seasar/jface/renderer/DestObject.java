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

import org.eclipse.swt.graphics.Color;

/**
 * @author y-komori
 */
public class DestObject {
    public String nonTargetField;

    public String stringField;

    public String textField;

    public int intField;

    public boolean booleanField;

    public Color colorField;

    public int swtConstField;

    private String stringProperty;

    private String textProperty;

    private int intProperty;

    private boolean booleanProperty;

    private Color colorProperty;

    private int swtConstProperty;

    public boolean getBooleanProperty() {
        return this.booleanProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public Color getColorProperty() {
        return this.colorProperty;
    }

    public void setColorProperty(Color colorProperty) {
        this.colorProperty = colorProperty;
    }

    public int getIntProperty() {
        return this.intProperty;
    }

    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }

    public String getStringProperty() {
        return this.stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public int getSwtConstProperty() {
        return this.swtConstProperty;
    }

    public void setSwtConstProperty(int swtConstProperty) {
        this.swtConstProperty = swtConstProperty;
    }

    public String getTextProperty() {
        return this.textProperty;
    }

    public void setTextProperty(String textProperty) {
        this.textProperty = textProperty;
    }
}