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
package org.netbeans.android.file.action;

import org.netbeans.android.file.parser.FXMLGenerator;
import org.netbeans.android.layout.generator.AndroidLayoutGenerator;
import org.netbeans.android.layout.wrapper.AndroidWrapperComponent;
import org.netbeans.jfluidon.file.IFluidonFile;
import org.netbeans.jfluidon.file.IFluidonFileDataObject;
import org.netbeans.jfluidon.file.action.FluidonFileActionListener;
import org.netbeans.jfluidon.specification.annotaton.FluidonConfig;
import org.netbeans.jfluidon.specification.annotaton.LibraryConfig;
import org.netbeans.jfluidon.specification.annotaton.Model;
import org.netbeans.jfluidon.specification.annotaton.NavigatorConfig;
import org.netbeans.jfluidon.specification.annotaton.PropertyConfig;
import org.netbeans.jfluidon.specification.annotaton.Vendor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Edit", id = "AndroidLayoutFileActionListener")
@ActionRegistration(displayName = "#CTL_AndroidLayoutFileActionListener")
@Messages("CTL_AndroidLayoutFileActionListener=Design")
@ActionReference(path = "Loaders/text/android+xml/Actions", position = 0, separatorAfter = 50)
@Vendor(id = "ANDROID", version = 1.0F, name = "Android", displayName = "Android",
        model = @Model(id = "ANDROID_LAYOUT_DESIGNER", name = "Android Layout Designer")
)
@FluidonConfig(library = @LibraryConfig(component = "LibraryWindow", config = "org/netbeans/android/resource/config/LibraryConfig.xml"),
        property = @PropertyConfig(component = "PropertyWindow", config = "org/netbeans/android/resource/config/PropertyConfig.xml"),
        navigator = @NavigatorConfig(component = "NavigatorWindow"),
        preferences = "org/netbeans/android/resource/config/PreferenceConfig.xml",
        generator = AndroidLayoutGenerator.class
)
public final class AndroidLayoutFileActionListener extends FluidonFileActionListener {

    public AndroidLayoutFileActionListener(IFluidonFileDataObject context) {
        super(context);
    }

    @Override
    public void initSpecification(IFluidonFile fluidonFile) {
        fluidonFile.setWrapperComponent(AndroidWrapperComponent.class);
        fluidonFile.setFxmlFile(FXMLGenerator.generate(fluidonFile));
    }

}
