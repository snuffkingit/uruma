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
package org.seasar.uruma.rcp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IExtension;
import org.seasar.uruma.util.AssertionUtil;

/**
 * <code>extension</code> 要素を表すクラスです。<br />
 * 
 * @see IExtension
 * @author y-komori
 */
public class Extension {

    private String point;

    private List<ConfigurationElement> elements = new ArrayList<ConfigurationElement>();

    /**
     * {@link Extension} を構築します。<br />
     * 
     * @param point
     *            <code>point</code> 属性
     */
    public Extension(final String point) {
        AssertionUtil.assertNotEmpty("point", point);
        this.point = point;
    }

    /**
     * <code>point</code> 属性を返します。<br />
     * 
     * @return <code>point</code> 属性
     */
    public String getPoint() {
        return this.point;
    }

    /**
     * {@link ConfigurationElement} のリストを返します。<br />
     * 
     * @return {@link ConfigurationElement} のリスト
     */
    public List<ConfigurationElement> getConfigurationElements() {
        return elements;
    }

    /**
     * {@link ConfigurationElement} を追加します。<br />
     * 
     * @param configurationElement
     *            {@link ConfigurationElement}
     */
    public void addConfigurationElement(
            final ConfigurationElement configurationElement) {
        AssertionUtil.assertNotNull("configurationElement",
                configurationElement);
        this.elements.add(configurationElement);
    }
}