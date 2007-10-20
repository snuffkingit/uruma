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
package org.seasar.jface.component;

import java.util.List;

/**
 * メニューに関する情報を保持するためのインターフェースです。<br />
 * 
 * @author y-komori
 */
public interface Menu extends MenuItem {

    /**
     * メニュー項目のリストを取得します。<br />
     * 
     * @return メニュー項目のリスト
     */
    public List<MenuItem> getMenuItemList();

    /**
     * メニュー項目を追加します。<br />
     * 
     * @param menuItem
     *            メニュー項目オブジェクト
     */
    public void addMenuItem(MenuItem menuItem);

    /**
     * メニューを保持する親コンポーネントを設定します。
     * 
     * @param uiComponent
     *            親コンポーネント
     */
    public void setMenuHolder(UIComponent uiComponent);

    /**
     * メニューを保持する親コンポーネントを取得します。
     * 
     * @return 親コンポーネント
     */
    public UIComponent getMenuHolder();
}