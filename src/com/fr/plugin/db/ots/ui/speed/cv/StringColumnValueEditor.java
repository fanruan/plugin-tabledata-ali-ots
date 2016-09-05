package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class StringColumnValueEditor extends Editor<ColumnValue> {

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
    public ColumnValue getValue() {
        return ColumnValue.fromString(textField.getText());
    }

    @Override
    public void setValue(ColumnValue value) {
        if (value != null) {
            textField.setText(value.asString());
        }
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof ColumnValue && ((ColumnValue)object).getType() == ColumnType.STRING;
    }
}
