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
package org.seasar.jface.viewer;

import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * {@link ViewerAdapter} のための基底クラスです。<br />
 * 
 * @author y-komori
 */
public abstract class AbstractViewerAdapter<VIEWER_TYPE extends Viewer>
        implements ViewerAdapter<VIEWER_TYPE> {

    protected StructuredViewer viewer;

    public AbstractViewerAdapter(StructuredViewer viewer) {
        this.viewer = viewer;
        viewer.setContentProvider(getContentProvider());
    }

    public void setContents(Object[] contents) {
        setContentsToProvider(contents);
    }

    public void setContents(List<?> contents) {
        Object[] contentsArray = (Object[]) contents
                .toArray(new Object[contents.size()]);
        setContentsToProvider(contentsArray);
    }

    public Object getSelectedElement() {
        ISelection selection = viewer.getSelection();
        return ((IStructuredSelection) selection).getFirstElement();
    }

    public Object[] getSelectedElements() {
        ISelection selection = viewer.getSelection();
        return ((IStructuredSelection) selection).toArray();
    }

    private void setContentsToProvider(Object[] contents) {
        IContentProvider provider = viewer.getContentProvider();
        if (provider instanceof ContentsSettable) {
            ContentsSettable holder = (ContentsSettable) provider;
            holder.setContents(contents);
        }
    }

    /**
     * サブクラスでデフォルトのコンテントプロバイダを返してください。<br />
     * 
     * @return コンテントプロバイダ
     */
    protected abstract IStructuredContentProvider getContentProvider();
}
