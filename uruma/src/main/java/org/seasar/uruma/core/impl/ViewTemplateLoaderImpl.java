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
package org.seasar.uruma.core.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.uruma.core.TemplateManager;
import org.seasar.uruma.core.UrumaConstants;
import org.seasar.uruma.core.UrumaMessageCodes;
import org.seasar.uruma.core.ViewTemplateLoader;
import org.seasar.uruma.core.io.ExtFileFilter;
import org.seasar.uruma.log.UrumaLogger;
import org.seasar.uruma.rcp.util.RcpResourceUtil;
import org.seasar.uruma.util.PathUtil;

/**
 * {@link ViewTemplateLoader} の実装クラスです。<br />
 * 
 * @author y-komori
 */
public class ViewTemplateLoaderImpl implements ViewTemplateLoader,
        UrumaConstants, UrumaMessageCodes {
    private static final UrumaLogger logger = UrumaLogger
            .getLogger(ViewTemplateLoader.class);

    private TemplateManager templateManager;

    private static final FileFilter filter = new ExtFileFilter("xml");

    /*
     * @see org.seasar.uruma.core.ViewTemplateLoader#loadViewTemplates()
     */
    public void loadViewTemplates(final URL resourceUrl) throws IOException {

        // TODO プロトコル毎にクラスを分けて整理する
        if (UrumaConstants.PROTCOL_FILE.equals(resourceUrl.getProtocol())) {
            File localFile = new File(resourceUrl.getPath());
            File baseDir = new File(localFile.getParent() + SLASH
                    + DEFAULT_VIEWS_PATH);

            logger.log(UrumaMessageCodes.FINDING_XML_START, baseDir
                    .getAbsolutePath());

            List<File> pathList = RcpResourceUtil.findFileResources(baseDir,
                    filter);

            String localBasePath = PathUtil.getParent(localFile
                    .getAbsolutePath());
            List<String> viewFilePaths = new ArrayList<String>(pathList.size());
            for (File file : pathList) {
                String path = PathUtil.getRelativePath(localBasePath, file
                        .getAbsolutePath());
                viewFilePaths.add(PathUtil.replaceSeparator(path));
            }

            templateManager.loadTemplates(viewFilePaths);
        } else if (PROTCOL_JAR.equals(resourceUrl.getProtocol())) {
            String jarFilePath = StringUtil.substringFromLast(resourceUrl
                    .getPath(), EXCLAMATION_MARK);
            String jarLocalPath = StringUtil.substringToLast(
                    resourceUrl.getPath(), EXCLAMATION_MARK).substring(1);
            // workebnch.xml の親ディレクトリをクラスパスルートとみなす
            String classPathRoot = PathUtil.getParent(jarLocalPath) + SLASH;
            JarFile jarFile = JarFileUtil.create((new URL(jarFilePath))
                    .getFile());

            logger.log(FINDING_XML_START, jarFilePath + EXCLAMATION_MARK
                    + classPathRoot);

            // TODO SUSIE
            String basePath = DEFAULT_VIEWS_PATH + SLASH;

            List<String> viewFilePaths = new ArrayList<String>();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                String entryPath = entry.getName();
                if (entryPath.startsWith(basePath)
                        && entryPath.endsWith(".xml")) {
                    viewFilePaths.add(PathUtil.replaceSeparator(entryPath));
                }
            }

            templateManager.loadTemplates(viewFilePaths);
        }
    }

    /**
     * {@link TemplateManager} を設定します。<br />
     * 
     * @param templateManager
     *      {@link TemplateManager}
     */
    @Binding(bindingType = BindingType.MUST)
    public void setTemplateManager(final TemplateManager templateManager) {
        this.templateManager = templateManager;
    }
}
