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
package org.seasar.jface.component;

/**
 * @author y-komori
 * 
 */
public interface Property extends Cloneable {

    public static final String INHERITANCE_NONE = "none";

    public static final String INHERITANCE_CHILD = "child";

    public static final String INHERITANCE_CHILD_ONLY = "childOnly";

    public static final String INHERITANCE_DESCENDANT = "descendant";

    public static final String INHERITANCE_DESCENDANT_ONLY = "descendantOnly";

    public Inheritance getInheritance();

    public void setInheritance(Inheritance inheritance);

    public String getName();

    public void setName(String name);

    public String getValue();

    public void setValue(String value);

    public int getIntValue();

    public boolean getBooleanValue();

    public boolean isValueExist();

    public Property cloneProperty(Inheritance inheritance);
}