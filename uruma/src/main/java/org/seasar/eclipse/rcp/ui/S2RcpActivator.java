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
package org.seasar.eclipse.rcp.ui;

import java.io.ByteArrayInputStream;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.jface.S2JFaceTemplateManager;
import org.seasar.jface.WindowContext;
import org.seasar.jface.component.Template;
import org.seasar.jface.impl.S2JFaceTemplateManagerImpl;
import org.seasar.jface.impl.WindowContextImpl;

/**
 * S2RCP アプリケーションのための基底アクティベータです。<br />
 * <p>
 * S2RCPを利用するアプリケーションは、本クラスを継承したアクティベータを作成してください。<br />
 * </p>
 * 
 * @author y-komori
 */
public abstract class S2RcpActivator extends AbstractUIPlugin {

    protected S2RcpActivator plugin;

    private S2Container container;

    private S2JFaceTemplateManager templateManager = new S2JFaceTemplateManagerImpl();

    private Logger logger = Logger.getLogger(getClass());

    /**
     * {@link S2Container} へ本クラスを登録する際のコンポーネント名です。<br />
     * 値：{@value}
     */
    public static final String PLUGIN = "plugin";

    /**
     * {@link S2Container} へ {@link WindowContext} オブジェクトを登録する際のコンポーネント名です。<br />
     * 値：{@value}
     */
    public static final String WINDOW_CONTEXT = "windowContext";

    /**
     * {@link S2RcpActivator} を構築します。<br />
     */
    public S2RcpActivator() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public final void start(final BundleContext context) throws Exception {
        super.start(context);

        Thread currentThread = Thread.currentThread();
        ClassLoader originalLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(getClass().getClassLoader());

        try {
            SingletonS2ContainerFactory.init();
            container = SingletonS2ContainerFactory.getContainer();

            registComponentsToS2Container();
        } catch (ResourceNotFoundRuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            currentThread.setContextClassLoader(originalLoader);
            throw ex;
        }
        currentThread.setContextClassLoader(originalLoader);

        // ここからテスト
        IExtensionRegistry registry = Platform.getExtensionRegistry();

        StringBuffer buf = new StringBuffer(2048);
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buf.append("<?eclipse version=\"3.2\"?>");
        buf.append("<plugin>");
        buf.append("<extension point=\"org.eclipse.ui.views\">");
        buf
                .append("<view class=\"org.seasar.rcp.example.fileman.FolderViewPart\" id=\"org.seasar.rcp.example.fileman.FolderView\" name=\"フォルダ・ツリー\" />");
        buf
                .append("<view class=\"org.seasar.rcp.example.fileman.FileViewPart\" id=\"org.seasar.rcp.example.fileman.FileView\" name=\"ファイル・ビュー\" />");
        buf.append("</extension>");
        buf.append("</plugin>");
        ByteArrayInputStream is = new ByteArrayInputStream(buf.toString()
                .getBytes("UTF-8"));

        Object token = ((ExtensionRegistry) registry).getTemporaryUserToken();

        // System.out.println("ExtensionPointContributor = "
        // + extensionPoint.getContributor());
        //
        // IExtension viewExtension = extensionPoint
        // .getExtension("org.seasar.rcp.example.fileman.views");
        // if (viewExtension != null) {
        // RegistryContributor viewContributor = (RegistryContributor)
        // viewExtension
        // .getContributor();
        // System.out.println("ExtensionContributor = " + viewContributor);
        // } else {
        // System.out.println("ExtensionContributor = NULL");
        // }

        IContributor contributorOsgi = ContributorFactoryOSGi
                .createContributor(getBundle());
        System.out.println("ContributorByOSGI = " + contributorOsgi);

        registry.addContribution(is, contributorOsgi, false, null, null, token);

        // ExtensionRegistry registry = (ExtensionRegistry) RegistryFactory
        // .getRegistry();

        // IExtension viewExtension = extensionPoint
        // .getExtension("org.seasar.rcp.example.fileman.views");
        // System.out.println(viewExtension);
        // RegistryContributor viewContributor = (RegistryContributor)
        // viewExtension
        // .getContributor();
        //
        // RegistryObjectFactory factory = registry.getElementFactory();
        // String[] props = new String[] { "class",
        // "org.seasar.rcp.example.fileman.FileViewPart", "id",
        // "org.seasar.rcp.example.fileman.FileView", "name", "ファイル・ビュー" };

        // ConfigurationElement element = factory
        // .createConfigurationElement(-1, viewContributor.getActualId(),
        // "view", props ,new int[0], 0x80000000, );

        IExtensionPoint extensionPoint = registry
                .getExtensionPoint("org.eclipse.ui.views");
        IExtension[] extensions = extensionPoint.getExtensions();
        for (IExtension extension : extensions) {
            System.out.println("\nNamespaceIdentifier="
                    + extension.getNamespaceIdentifier());
            System.out.println("ExtensionPointUniqueIdentifier="
                    + extension.getExtensionPointUniqueIdentifier());
            System.out.println("Label=" + extension.getLabel());
            System.out.println("SimpleIdentifier="
                    + extension.getSimpleIdentifier());
            System.out.println("UniqueIdentifier="
                    + extension.getUniqueIdentifier());

            IConfigurationElement[] configurationElements = extension
                    .getConfigurationElements();
            for (IConfigurationElement configurationElement : configurationElements) {
                IContributor contributor = configurationElement
                        .getContributor();
                System.out.println("ContributorClass = "
                        + contributor.getClass().getName());
                System.out
                        .println("ContributorName = " + contributor.getName());
                System.out.println("ConfigurationElementClass = "
                        + configurationElement.getClass().getName());

                String[] attrs = configurationElement.getAttributeNames();
                for (String string : attrs) {
                    System.out.println(string + "="
                            + configurationElement.getAttribute(string));
                }
            }
        }

        // ここまで

        s2RcpStart(context);
    }

    protected void registComponentsToS2Container() {
        container.register(this, PLUGIN);

        WindowContext windowContext = new WindowContextImpl();
        container.register(windowContext, WINDOW_CONTEXT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public final void stop(final BundleContext context) throws Exception {
        plugin = null;
        s2RcpStop(context);

        container.destroy();

        super.stop(context);
    }

    /**
     * 指定されたパスの画面定義XMLを読み込み、{@link Template} オブジェクトを生成します。<br />
     * 
     * @param path
     *            画面定義XMLのパス
     * @return {@link Template} オブジェクト
     */
    public Template getTemplate(final String path) {
        try {
            Template template = templateManager.getTemplate(path);
            return template;
        } catch (ResourceNotFoundRuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * {@link S2Container} のインスタンスを取得します。<br />
     * 
     * @return {@link S2Container} のインスタンス
     */
    protected S2Container getContainer() {
        return container;
    }

    /**
     * プラグイン初期化時に呼び出されるメソッドです。<br />
     * <p>
     * プラグイン初期化時の処理は、本メソッドをオーバーライドして記述してください。
     * </p>
     * 
     * @param context
     *            {@link BundleContext} オブジェクト
     * @throws Exception
     */
    protected abstract void s2RcpStart(BundleContext context) throws Exception;

    /**
     * プラグイン終了時に呼び出されるメソッドです。<br />
     * <p>
     * プラグイン終了時の処理は、本メソッドをオーバーライドして記述してください。
     * </p>
     * 
     * @param context
     *            {@link BundleContext} オブジェクト
     * @throws Exception
     */
    protected abstract void s2RcpStop(BundleContext context) throws Exception;
}