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
package org.seasar.jface.exception;

import java.lang.reflect.Field;

/**
 * @author y-komori
 */
public class ValueBindingException extends S2JFaceRuntimeException {

    private static final long serialVersionUID = 4452747426693887995L;

    public static final String WIDGET_NOT_SUPPORTED = "EJFC0207";

    public static final String IMPORT_SOURCE_NOT_FOUND = "EJFC0208";

    public static final String EXPORT_COMPONENT_NOT_FOUND = "EJFC0210";

    public ValueBindingException(String messageCode, String id, Class clazz,
            Field field) {
        super(messageCode,
                new Object[] { id, clazz.getName(), field.getName() });
    }
}