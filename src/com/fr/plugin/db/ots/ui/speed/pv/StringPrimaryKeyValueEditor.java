package com.fr.plugin.db.ots.ui.speed.pv;

import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.db.ots.core.primary.OTSPrimaryKeyValue;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class StringPrimaryKeyValueEditor extends Editor<OTSPrimaryKeyValue> {

    private UITextField textField;

    public StringPrimaryKeyValueEditor() {
        setLayout(new BorderLayout());
        textField = new UITextField();
        add(textField, BorderLayout.CENTER);
        setName("文本");
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_string.png");
    }

    @Override
    public OTSPrimaryKeyValue getValue() {
        return new OTSPrimaryKeyValue("String", textField.getText());
    }

    @Override
    public void setValue(OTSPrimaryKeyValue value) {
        if (value != null) {
            textField.setText(GeneralUtils.objectToString(value.getValue()));
        }
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSPrimaryKeyValue && ((OTSPrimaryKeyValue)object).getType().equals("String");
    }
}
