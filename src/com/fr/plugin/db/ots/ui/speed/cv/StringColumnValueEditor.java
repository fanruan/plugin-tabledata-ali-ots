package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.db.ots.core.condition.OTSColumnValue;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class StringColumnValueEditor extends Editor<OTSColumnValue> {

    private UITextField textField;

    public StringColumnValueEditor() {
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
    public OTSColumnValue getValue() {
        return new OTSColumnValue("String", textField.getText());
    }

    @Override
    public void setValue(OTSColumnValue value) {
        if (value != null) {
            textField.setText(GeneralUtils.objectToString(value.getValue()));
        }
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSColumnValue && ((OTSColumnValue)object).getType().equals("String");
    }
}
