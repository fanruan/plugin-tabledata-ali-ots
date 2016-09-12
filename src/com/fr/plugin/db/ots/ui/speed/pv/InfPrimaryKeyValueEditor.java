package com.fr.plugin.db.ots.ui.speed.pv;

import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.primary.OTSPrimaryKeyValue;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class InfPrimaryKeyValueEditor extends Editor<OTSPrimaryKeyValue> {

    private UIDictionaryComboBox<String> comboBox;

    public InfPrimaryKeyValueEditor() {
        setLayout(new BorderLayout());
        comboBox = new UIDictionaryComboBox<String>(
                new String[]{"__InfMax__", "__InfMin__"},
                new String[]{"最大值", "最小值"}
                );
        add(comboBox, BorderLayout.CENTER);
        setName(Inter.getLocText("Plugin-OTS_Infinity"));
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/infinity.png");
    }

    @Override
    public OTSPrimaryKeyValue getValue() {
        return new OTSPrimaryKeyValue("Infinity", comboBox.getSelectedItem());
    }

    @Override
    public void setValue(OTSPrimaryKeyValue value) {
        if (value != null) {
            comboBox.setSelectedItem(value.getValue());
        }
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSPrimaryKeyValue
                && ((OTSPrimaryKeyValue) object).getType().equals("Infinity");
    }
}
