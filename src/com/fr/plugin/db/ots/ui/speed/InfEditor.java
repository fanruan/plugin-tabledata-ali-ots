package com.fr.plugin.db.ots.ui.speed;

import com.aliyun.openservices.ots.model.PrimaryKeyValue;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class InfEditor extends Editor<PrimaryKeyValue> {

    private UIDictionaryComboBox<PrimaryKeyValue> comboBox;

    public InfEditor() {
        setLayout(new BorderLayout());
        comboBox = new UIDictionaryComboBox<PrimaryKeyValue>(
                new PrimaryKeyValue[]{PrimaryKeyValue.INF_MAX, PrimaryKeyValue.INF_MIN},
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
    public PrimaryKeyValue getValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public void setValue(PrimaryKeyValue value) {
        comboBox.setSelectedItem(value);
    }

    @Override
    public boolean accept(Object object) {
        return object == PrimaryKeyValue.INF_MAX || object == PrimaryKeyValue.INF_MIN;
    }
}
