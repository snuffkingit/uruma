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
package org.seasar.uruma.component.jface;

import org.eclipse.swt.widgets.ProgressBar;
import org.seasar.uruma.annotation.FieldDescription;
import org.seasar.uruma.annotation.RenderingPolicy;
import org.seasar.uruma.annotation.RenderingPolicy.ConversionType;

/**
 * {@link ProgressBar} に対応するコンポーネントです。<br />
 * 
 * @author bskuroneko
 */
public class ProgressBarComponent extends ControlComponent {
    @RenderingPolicy(conversionType = ConversionType.INT)
    @FieldDescription("最大値")
    private String maximum;

    @RenderingPolicy(conversionType = ConversionType.INT)
    @FieldDescription("最小値")
    private String minimum;

    @RenderingPolicy(conversionType = ConversionType.INT)
    @FieldDescription("現在値")
    private String selection;

    /**
     * 最大値を取得します。<br />
     * 
     * @return 最大値
     */
    public String getMaximum() {
        return this.maximum;
    }

    /**
     * 最大値を設定します。<br />
     * 
     * @param maximum 最大値
     */
    public void setMaximum(final String maximum) {
        this.maximum = maximum;
    }

    /**
     * 最小値を取得します。<br />
     * 
     * @return 最小値
     */
    public String getMinimum() {
        return this.minimum;
    }

    /**
     * 最小値を設定します。<br />
     * 
     * @param minimum 最小値
     */
    public void setMinimum(final String minimum) {
        this.minimum = minimum;
    }

    /**
     * 現在値を取得します。<br />
     * 
     * @return 現在値
     */
    public String getSelection() {
        return this.selection;
    }

    /**
     * 現在値を設定します。<br />
     * 
     * @param selection 現在値
     */
    public void setSelection(final String selection) {
        this.selection = selection;
    }

}
