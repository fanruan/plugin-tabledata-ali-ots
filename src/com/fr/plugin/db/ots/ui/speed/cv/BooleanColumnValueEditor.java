package com.fr.plugin.db.ots.ui.speed.cv;

import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.IOUtils;
import com.fr.plugin.db.ots.core.condition.OTSColumnValue;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class BooleanColumnValueEditor extends Editor<OTSColumnValue> {

    private UICheckBox checkBox;

    public BooleanColumnValueEditor() {
        setLayout(new BorderLayout());
        checkBox = new UICheckBox();
        add(checkBox, BorderLayout.CENTER);
        setName("布尔值");
    }

    @Override
    public OTSColumnValue getValue() {
        return new OTSColumnValue("Boolean", checkBox.isSelected());
    }

    @Override
    public void setValue(OTSColumnValue value) {
        if (value != null) {
            checkBox.setSelected((Boolean) value.getValue());
        }
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_bool.png");
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSColumnValue && ((OTSColumnValue) object).getType().equals("Boolean");
    }
}
