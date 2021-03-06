/**
 * Copyright [2015] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.android.component;

import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.jfluidon.component.library.LibraryTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

@ConvertAsProperties(
        dtd = "-//org.netbeans.android.component//LibraryWindow//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "LibraryWindow",
        iconBase = "org/netbeans/android/resource/component/icon/LIBRARY_ICON.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "org.netbeans.android.component.LibraryWindow")
@ActionReference(path = "Menu/Window/Android/Layout", position = 333)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_LibraryAction",
        preferredID = "LibraryWindow"
)
@NbBundle.Messages({
    "CTL_LibraryAction=Library",
    "CTL_LibraryWindow=Library",
    "HINT_LibraryWindow=Library window"
})
public class LibraryWindow extends LibraryTopComponent {

    public LibraryWindow() {
        super("Android Pallete", "Android toolkit");
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
