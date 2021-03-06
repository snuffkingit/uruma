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
package org.seasar.uruma.component.factory;

import org.seasar.framework.xml.TagHandler;
import org.seasar.framework.xml.TagHandlerContext;

/**
 * XMLタグを処理するための基底クラスです。<br />
 * 
 * @author y-komori
 * @author $Author$
 * @version $Revision$ $Date$
 */
public abstract class UrumaTagHandler extends TagHandler {
    private static final long serialVersionUID = -3260173885181997297L;

    /**
     * 画面定義テンプレートファイルの URL を表す {@link TagHandlerContext} のパラメータ名です。<br />
     */
    public static final String PARAM_URL = "url";

    /**
     * 画面定義テンプレートファイルの親 URL を表す {@link TagHandlerContext} のパラメータ名です。<br />
     */
    public static final String PARAM_PARENT_URL = "parentUrl";

    /**
     * 要素のパスを返します。<br />
     * 
     * @return 要素のパス
     */
    public abstract String getElementPath();
}
