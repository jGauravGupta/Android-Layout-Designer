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
package org.netbeans.android.layout.generator;

import com.oracle.javafx.scenebuilder.kit.fxom.FXOMDocument;
import java.io.File;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.apache.commons.io.FileUtils;
import org.netbeans.android.file.parser.XmlFormatter;
import org.netbeans.editor.BaseDocument;
import org.netbeans.android.layout.serializer.AndroidLayoutSerializer;
import org.netbeans.jfluidon.file.IFluidonFile;
import org.netbeans.jfluidon.source.generator.ISourceGenerator;
import org.netbeans.jfluidon.source.serializer.SourceSerializer;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 *
 * @author Gaurav Gupta
 */
public class AndroidLayoutGenerator implements ISourceGenerator<FXOMDocument> {

    @Override
    public void generate(IFluidonFile fluidonFile, FXOMDocument document) {
        try {
            File generatedFile = fluidonFile.getFile();//new File(generatedFilePath);
            if (!generatedFile.exists()) {
                generatedFile.createNewFile();
            }
            SourceSerializer serializer = new AndroidLayoutSerializer();
            FileUtils.writeStringToFile(generatedFile, XmlFormatter.format(document.getSource(serializer)));
            formatFile(generatedFile);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void formatFile(File file) {
        final FileObject fo = FileUtil.toFileObject(file);
        try {
            DataObject dobj = DataObject.find(fo);
            EditorCookie ec = dobj.getLookup().lookup(EditorCookie.class);
            if (ec == null) {
                return;
            }
            ec.close();
            StyledDocument document = ec.getDocument();
            if (document instanceof BaseDocument) {
                final BaseDocument doc = (BaseDocument) document;
                final Reformat f = Reformat.get(doc);
                f.lock();
                try {
                    doc.runAtomic(new Runnable() {
                        public void run() {
                            try {
                                f.reformat(0, doc.getLength());
                            } catch (BadLocationException ex) {
                                Exceptions.attachMessage(ex, "Failure while formatting " + FileUtil.getFileDisplayName(fo));
                                Exceptions.printStackTrace(ex);
                            }

                        }
                    });
                } finally {
                    f.unlock();
                }
                try {
                    ec.saveDocument();
//                    SaveCookie save = dobj.getLookup().lookup(SaveCookie.class);
//                    if (save != null) {
//                        save.save();
//                    }
                } catch (IOException ex) {
                    Exceptions.attachMessage(ex, "Failure while formatting and saving " + FileUtil.getFileDisplayName(fo));
                    Exceptions.printStackTrace(ex);
                }
            }
        } catch (DataObjectNotFoundException ex) {
            Exceptions.attachMessage(ex, "Failure while formatting " + FileUtil.getFileDisplayName(fo));
            Exceptions.printStackTrace(ex);
        } 
    }

}
