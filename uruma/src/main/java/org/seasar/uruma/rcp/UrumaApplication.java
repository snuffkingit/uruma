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
package org.seasar.uruma.rcp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.seasar.uruma.core.UrumaMessageCodes;
import org.seasar.uruma.log.UrumaLogger;
import org.seasar.uruma.rcp.ui.UrumaWorkbenchAdvisor;

/**
 * Uruma RCP のブートストラップクラスです。<br />
 * 
 * @author y-komori
 */
public class UrumaApplication implements IApplication {
    private static final UrumaLogger logger = UrumaLogger
            .getLogger(UrumaApplication.class);

    /*
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
     */
    public Object start(final IApplicationContext context) throws Exception {
        logger.log(UrumaMessageCodes.URUMA_RCP_START);

        Display display = PlatformUI.createDisplay();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display,
                    new UrumaWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART)
                return IApplication.EXIT_RESTART;
            else
                return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }
    }

    /*
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    public void stop() {
        logger.log(UrumaMessageCodes.URUMA_RCP_STOP);

        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null)
            return;
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            public void run() {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }
}