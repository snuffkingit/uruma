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
package org.seasar.uruma.container.cooldeploy;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.PathResolver;
import org.seasar.framework.container.factory.S2ContainerFactory.DefaultConfigurator;
import org.seasar.framework.container.factory.S2ContainerFactory.Provider;

/**
 * Uruma COOL deploy用の
 * {@link org.seasar.framework.container.factory.S2ContainerFactory.Configurator}
 * です。<br />
 * 
 * @author y.sugigami
 * @see org.seasar.framework.container.factory.S2ContainerFactory.DefaultConfigurator
 * 
 */
public class UrumaS2ContainerFactoryCoolConfigurator extends
        DefaultConfigurator {
    /*
     * @see org.seasar.framework.container.factory.S2ContainerFactory$DefaultConfigurator#createProvider(org.seasar.framework.container.S2Container)
     */
    @Override
    protected Provider createProvider(final S2Container configurationContainer) {
        UrumaS2ContainerFactoryCoolProvider provider = new UrumaS2ContainerFactoryCoolProvider();
        if (configurationContainer.hasComponentDef(PathResolver.class)) {
            provider.setPathResolver((PathResolver) configurationContainer
                    .getComponent(PathResolver.class));
        }
        if (configurationContainer.hasComponentDef(ExternalContext.class)) {
            provider
                    .setExternalContext((ExternalContext) configurationContainer
                            .getComponent(ExternalContext.class));
        }
        if (configurationContainer
                .hasComponentDef(ExternalContextComponentDefRegister.class)) {
            provider
                    .setExternalContextComponentDefRegister((ExternalContextComponentDefRegister) configurationContainer
                            .getComponent(ExternalContextComponentDefRegister.class));
        }
        return provider;
    }

}
