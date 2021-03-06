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
package org.seasar.uruma.rcp.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.AssertionUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.uruma.util.PathUtil;

/**
 * Eclipse / RCP 環境でのリソースを扱うためのユーティリティクラスです。<br />
 * 
 * @author y-komori
 */
public class RcpResourceUtil {
    private static final String SLASH = "/";

    /**
     * 指定されたプラグインの実行時パスを返します。<br />
     * 
     * @param uiPlugin
     *            実行時パスを調べる {@link Plugin}
     * @return 実行時パスを表す {@link URL} オブジェクト
     * @throws IOException
     *             パスの変換に失敗した場合
     * @see FileLocator#resolve(URL)
     */
    public static URL getNativePluginPath(final Plugin uiPlugin)
            throws IOException {
        URL pluginUrl = uiPlugin.getBundle().getEntry(SLASH);
        return FileLocator.resolve(pluginUrl);
    }

    /**
     * {@link Bundle}の絶対パスを返します。<br />
     * 
     * @param bundle
     *            {@link Bundle}
     * @return {@link Bundle}の絶対パス
     */
    public static String getBunldePath(final Bundle bundle) {
        URL entry = bundle.getEntry(SLASH);
        String pluginDirPath = null;
        try {
            pluginDirPath = FileLocator.resolve(entry).getPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return removeHeadChar(pluginDirPath);
    }

    /**
     * 指定されたパスにおけるリソースのローカルシステム上の {@link URL} を返します。<br />
     * 
     * @param path
     *            リソースのパス
     * @return ローカルシステム上の {@link URL}。パスが存在しない場合は <code>null</code>。
     * @throws IOException
     *             パスの変換に失敗した場合
     * @see FileLocator#resolve(URL)
     */
    public static URL getLocalResourceUrl(final String path) throws IOException {
        URL url = ResourceUtil.getClassLoader().getResource(path);
        if (url != null) {
            return FileLocator.resolve(url);
        } else {
            return null;
        }
    }

    /**
     * 指定されたパスにおけるリソースのローカルシステム上の {@link URL} を返します。<br />
     * 
     * @param path
     *            リソースのパス
     * @return ローカルシステム上の {@link URL} <br /> リソースが存在しない場合は、NULLを返却。
     * @see FileLocator#resolve(URL)
     */
    public static URL getLocalResourceUrlNoException(final String path) {
        URL url = ResourceUtil.getClassLoader().getResource(path);
        URL fileLocatorUrl = null;
        try {
            fileLocatorUrl = FileLocator.resolve(url);
        } catch (Exception ignore) {
        }
        return fileLocatorUrl;
    }

    /**
     * 指定されたパスのバンドルルートパスからの相対パスを返します。<br />
     * 
     * @param bundle
     *            バンドル
     * @param path
     *            パス
     * @return 相対パス。相対パスが得られなかった場合、 <code>null</code>。
     */
    public static String getBundleRelativePath(final Bundle bundle,
            final String path) {
        AssertionUtil.assertNotNull("bundle", bundle);
        AssertionUtil.assertNotNull("path", path);
        try {
            URL url = getLocalResourceUrl(path);
            if (url != null) {
                URL bundlePath = FileLocator.resolve(bundle.getEntry(SLASH));
                return PathUtil.getRelativePath(bundlePath.getPath(), url
                        .getPath());
            } else {
                return null;
            }
        } catch (IOException ignore) {
            return null;
        }

    }

    /**
     * 指定されたパスを起点として、ファイルシステムからリソースを再帰的に検索して返します。<br />
     * 
     * @param baseDir
     *            起点となるパス
     * @param filter
     *            条件を指定する {@link FileFilter}
     * @return 検索結果
     */
    public static List<File> findFileResources(final File baseDir,
            final FileFilter filter) {
        List<File> results = new ArrayList<File>();

        if (baseDir.isDirectory()) {
            findFileResources(baseDir, filter, results);
        }

        return results;
    }

    private static void findFileResources(final File parentDir,
            final FileFilter filter, final List<File> results) {
        File[] files = parentDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                findFileResources(file, filter, results);
            } else if (file.isFile() && filter.accept(file)) {
                results.add(file);
            }
        }
    }

    /**
     * アプリケーションの絶対パスを返します。 <br />
     * 
     * @return アプリケーションの絶対パス
     */
    public static String getInstallPath() {
        Location installLocation = Platform.getInstallLocation();
        String path = installLocation.getURL().getFile();
        return removeHeadChar(path);
    }

    private static String removeHeadChar(final String path) {
        AssertionUtil.assertNotEmpty("path", path);
        return path.substring(1, path.length() - 1);
    }
}
