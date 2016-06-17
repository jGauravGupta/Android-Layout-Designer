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
package org.netbeans.android.file;

import java.io.IOException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.jfluidon.file.FluidonFileDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_ANDROID_LOADER=Files of Android Layout"
})
//@MIMEResolver.NamespaceRegistration(
//        displayName = "#LBL_ANDROID_LOADER",
//        mimeType = "text/android+xml",
//        elementNS = {"http://schemas.android.com/apk/res/android"}
//)
//@MIMEResolver.ExtensionRegistration(
//        displayName = "#LBL_ANDROID_LOADER",
//        mimeType = "text/android+xml",
//        extension = {"and"}
//)
@MIMEResolver.Registration(
        displayName = "#LBL_ANDROID_LOADER",
        resource = "/org/netbeans/android/file/AndroidLayoutResolver.xml"
)
@DataObject.Registration(
        mimeType = "text/android+xml",
        iconBase = "org/netbeans/android/resource/container/icon/ICON.png",
        displayName = "#LBL_ANDROID_LOADER",
        position = 300
)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/android+xml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200
    ),
//    @ActionReference(
//            path = "Loaders/text/android+xml/Actions",
//            id = @ActionID(category = "Edit", id = "AndroidLayoutFileActionListener"),
//            position = 100,
//            separatorAfter = 200
//    ),
    @ActionReference(
            path = "Loaders/text/android+xml/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300
    //            separatorBefore = 200
    ),
    @ActionReference(
            path = "Loaders/text/android+xml/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500
    ),
    @ActionReference(
            path = "Loaders/text/android+xml/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
    ),
    @ActionReference(
            path = "Loaders/text/android+xml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800
    ),
//    @ActionReference(
//            path = "Loaders/text/android+xml/Actions",
//            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
//            position = 900,
//            separatorAfter = 1000
//    ),
//    @ActionReference(
//            path = "Loaders/text/android+xml/Actions",
//            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
//            position = 1100,
//            separatorAfter = 1200
//    ),
//    @ActionReference(
//            path = "Loaders/text/android+xml/Actions",
//            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
//            position = 1300
//    ),
    @ActionReference(
            path = "Loaders/text/android+xml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400
    )
})
public class AndroidLayoutDataObject extends FluidonFileDataObject {

    public AndroidLayoutDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/android+xml", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(
            displayName = "#LBL_JSFDefault_EDITOR",
            iconBase = "org/netbeans/android/resource/container/icon/ICON.png",
            mimeType = "text/android+xml",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "JSFDefault",
            position = 1000
    )
    @Messages("LBL_JSFDefault_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
}
