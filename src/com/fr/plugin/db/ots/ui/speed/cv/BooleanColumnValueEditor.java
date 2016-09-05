package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class BooleanColumnValueEditor extends Editor<ColumnValue> {

    private UICheckBox checkBox;

    public BooleanColumnValueEditor() {
        setLayout(new BorderLayout());
        checkBox = new UICheckBox();
        add(checkBox, BorderLayout.CENTER);
        setName("布尔值");
    }

    @Override
    public ColumnValue getValue() {
        return ColumnValue.fromBoolean(checkBox.isSelected());
    }

    @Override
    public void setValue(ColumnValue value) {
        if (value != null) {
            checkBox.setSelected(value.asBoolean());
        }
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_bool.png");
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof ColumnValue && ((ColumnValue) object).getType() == ColumnType.BOOLEAN;
    }
}
