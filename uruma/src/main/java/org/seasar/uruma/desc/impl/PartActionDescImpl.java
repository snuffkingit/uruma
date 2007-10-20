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
package org.seasar.uruma.desc.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;
import org.seasar.uruma.annotation.ArgumentValue;
import org.seasar.uruma.annotation.EventListener;
import org.seasar.uruma.annotation.InitializeMethod;
import org.seasar.uruma.annotation.ReturnValue;
import org.seasar.uruma.binding.method.EventListenerDef;
import org.seasar.uruma.desc.PartActionDesc;
import org.seasar.uruma.exception.ArgumentFieldException;
import org.seasar.uruma.exception.InitializeMethodException;
import org.seasar.uruma.exception.ReturnFieldException;
import org.seasar.uruma.util.AssertionUtil;

/**
 * {@link PartActionDesc} の実装クラスです。<br />
 * 
 * @author y-komori
 */
public class PartActionDescImpl implements PartActionDesc {

    private Class<?> partActionClass;

    private Method initializeMethod;

    private Map<String, List<Method>> methodsCache = new HashMap<String, List<Method>>();

    private Map<String, Field> fieldsCache = new HashMap<String, Field>();

    private Field argumentField = null;

    private Field returnField = null;

    private List<EventListenerDef> eventListenerDefs = new ArrayList<EventListenerDef>();

    /**
     * {@link PartActionDescImpl} を構築します。<br />
     * 
     * @param partActionClass
     *            対応するクラスオブジェクト
     */
    public PartActionDescImpl(final Class<?> partActionClass) {
        if (partActionClass == null) {
            throw new EmptyRuntimeException("partActionClass");
        }

        this.partActionClass = partActionClass;

        setupMethods();
        setupFields();
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#getInitializeMethod()
     */
    public Method getInitializeMethod() {
        return this.initializeMethod;
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#invokeInitializeMethod(java.lang.Object)
     */
    public void invokeInitializeMethod(final Object target) {
        if (initializeMethod != null) {
            AssertionUtil.assertNotNull("target", target);
            try {
                initializeMethod.invoke(target, (Object[]) null);
            } catch (Throwable ex) {
                throw new InitializeMethodException(ex, partActionClass,
                        initializeMethod, target);
            }
        }
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#getArgumentField()
     */
    public Field getArgumentField() {
        return this.argumentField;
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#getReturnField()
     */
    public Field getReturnField() {
        return this.returnField;
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#getReturnValue(java.lang.Object)
     */
    public Object getReturnValue(final Object target) {
        if (returnField != null) {
            return FieldUtil.get(returnField, target);
        } else {
            return null;
        }
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#setArgumentValue(java.lang.Object,
     *      java.lang.Object)
     */
    public void setArgumentValue(final Object target, final Object value) {
        if (argumentField != null) {
            FieldUtil.set(argumentField, target, value);
        }
    }

    /*
     * @see org.seasar.uruma.desc.PartActionDesc#getEventListenerDefList()
     */
    public List<EventListenerDef> getEventListenerDefList() {
        return Collections.unmodifiableList(eventListenerDefs);
    }

    protected void setupMethods() {
        Map<String, List<Method>> methodListMap = new HashMap<String, List<Method>>();
        Method[] methods = partActionClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            List<Method> methodList = methodListMap.get(methods[i].getName());
            if (methodList == null) {
                methodList = new ArrayList<Method>();
                methodListMap.put(methods[i].getName(), methodList);
            }
            methodList.add(methods[i]);

            setupInitializeMethod(methods[i]);
            setupEventListenerMethod(methods[i]);
        }

        for (Entry<String, List<Method>> entry : methodListMap.entrySet()) {
            String methodName = entry.getKey();
            List<Method> methodList = entry.getValue();
            methodsCache.put(methodName, methodList);
        }
    }

    protected void setupInitializeMethod(final Method method) {
        if (method.isAnnotationPresent(InitializeMethod.class)) {
            if ((method.getReturnType() == Void.TYPE)
                    && (method.getParameterTypes().length == 0)) {
                if (initializeMethod == null) {
                    initializeMethod = method;
                } else {
                    throw new InitializeMethodException(
                            InitializeMethodException.DUPLICATE,
                            partActionClass, method);
                }
            } else {
                throw new InitializeMethodException(
                        InitializeMethodException.INVALID, partActionClass,
                        method);
            }
        }
    }

    protected void setupEventListenerMethod(final Method method) {
        EventListener eventListener = method.getAnnotation(EventListener.class);
        if (eventListener != null) {
            EventListenerDef eventListenerDef = new EventListenerDef(method,
                    eventListener);
            eventListenerDefs.add(eventListenerDef);
        }
    }

    protected void setupFields() {
        setupFieldsByClass(partActionClass);
        Class<?> superClass = partActionClass.getSuperclass();
        while (superClass != Object.class && superClass != null) {
            setupFieldsByClass(superClass);
            superClass = superClass.getSuperclass();
        }
    }

    protected void setupFieldsByClass(final Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (!fieldsCache.containsKey(fieldName)) {
                field.setAccessible(true);
                fieldsCache.put(fieldName, field);

                setupArgumentField(field);
                setupReturnField(field);
            }
        }
    }

    protected void setupArgumentField(final Field field) {
        if (field.isAnnotationPresent(ArgumentValue.class)) {
            if (argumentField != null) {
                throw new ArgumentFieldException(
                        ArgumentFieldException.DUPLICATE, partActionClass,
                        field);
            }
            argumentField = field;
        }
    }

    protected void setupReturnField(final Field field) {
        if (field.isAnnotationPresent(ReturnValue.class)) {
            if (returnField != null) {
                throw new ReturnFieldException(ReturnFieldException.DUPLICATE,
                        partActionClass, field);
            }
            returnField = field;
        }
    }
}