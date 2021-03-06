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
package org.seasar.uruma.core.io;

import java.io.File;

import junit.framework.TestCase;

/**
 * {@link ExtFileFilter} のためのテストクラスです。<br />
 * 
 * @author y-komori
 */
public class ExtFileFilterTest extends TestCase {
    /**
     * {@link ExtFileFilter#accept(java.io.File)} メソッドのテストです。<br />
     */
    public void testAccept() {
        ExtFileFilter filter1 = new ExtFileFilter("xml");

        assertTrue("1", filter1.accept(new File("test.xml")));

        assertFalse("2", filter1.accept(new File("test.java")));

        assertFalse("3", filter1.accept(new File("")));

        ExtFileFilter filter2 = new ExtFileFilter("View", "xml");

        assertTrue("4", filter2.accept(new File("testView.xml")));

        assertFalse("5", filter2.accept(new File("test.xml")));

        assertFalse("6", filter2.accept(new File("testView.java")));

        assertFalse("7", filter2.accept(new File("test.")));

        assertFalse("8", filter2.accept(new File("")));
    }
}
