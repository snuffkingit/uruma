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
package org.seasar.uruma.rcp.core;

import org.seasar.uruma.exception.UrumaAppInitException;
import org.seasar.uruma.rcp.UrumaService;

/**
 * テスト用のモック {@link UrumaService} です。<br />
 * 
 * @author y-komori
 */
public class MockUrumaService extends UrumaServiceImpl {

    /**
     * {@link MockUrumaService} を構築します。<br />
     */
    public MockUrumaService() {
        super(new MockBundle());
    }

    /*
     * @see org.seasar.uruma.rcp.core.UrumaServiceImpl#initialize()
     */
    @Override
    protected void initialize() {
        switchToAppClassLoader();
        try {
            initS2Container();
            prepareS2Components();

        } catch (Exception ex) {
            throw new UrumaAppInitException(targetBundle, ex, ex.getMessage());
        } finally {
            restoreClassLoader();
        }
    }
}
