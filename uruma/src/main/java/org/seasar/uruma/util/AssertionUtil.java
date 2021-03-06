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
package org.seasar.uruma.util;

import org.seasar.framework.exception.SIllegalArgumentException;
import org.seasar.framework.util.StringUtil;
import org.seasar.uruma.core.UrumaMessageCodes;

/**
 * アサーションを行うためのユーティリティクラスです。<br />
 * 
 * @author y-komori
 */
public class AssertionUtil {
    private AssertionUtil() {

    }

    /**
     * <code>arg</code> が <code>null</code> でないことをチェックします。<br />
     * 
     * @param name
     *            オブジェクト名称
     * @param arg
     *            チェック対象オブジェクト
     */
    public static void assertNotNull(final String name, final Object arg) {
        if (arg == null) {
            throw new SIllegalArgumentException(UrumaMessageCodes.CANT_BE_NULL,
                    new Object[] { name });
        }
    }

    /**
     * <code>arg</code> が <code>null</code> または空文字列ではないことをチェックします。<br />
     * 
     * @param name
     *            オブジェクト名称
     * @param arg
     *            チェック対象文字列
     */
    public static void assertNotEmpty(final String name, final String arg) {
        if (StringUtil.isEmpty(arg)) {
            throw new SIllegalArgumentException(
                    UrumaMessageCodes.CANT_BE_EMPTY_STRING,
                    new Object[] { name });
        }
    }

    /**
     * <code>arg</code> が <code>clazz</code> のサブクラスであることをチェックします。<br />
     * 
     * @param name
     *            オブジェクト名称
     * @param clazz
     *            クラス
     * @param arg
     *            チェック対象オブジェクト
     */
    public static void assertInstanceOf(final String name,
            final Class<?> clazz, final Object arg) {
        if (!clazz.isAssignableFrom(arg.getClass())) {
            throw new SIllegalArgumentException(
                    UrumaMessageCodes.TYPE_MISS_MATCH, new Object[] { name,
                            clazz.getClass().getName() });
        }
    }
}
