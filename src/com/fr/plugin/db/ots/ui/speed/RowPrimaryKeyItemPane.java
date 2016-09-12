package com.fr.plugin.db.ots.ui.speed;

import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.LongEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.primary.OTSPrimaryKeyValue;
import com.fr.plugin.db.ots.ui.speed.pv.FormulaPrimaryKeyValueEditor;
import com.fr.plugin.db.ots.ui.speed.pv.InfPrimaryKeyValueEditor;
import com.fr.plugin.db.ots.ui.speed.pv.LongPrimaryKeyValueEditor;
import com.fr.plugin.db.ots.ui.speed.pv.StringPrimaryKeyValueEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class RowPrimaryKeyItemPane extends BasicPane {

    private UITextField columnNameTextField;
    private ValueEditorPane valueEditorPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{
            new StringPrimaryKeyValueEditor(),
            new LongPrimaryKeyValueEditor(),
            new InfPrimaryKeyValueEditor(),
            new FormulaPrimaryKeyValueEditor(Inter.getLocText("Plugin-OTS_Formula"))
    });
    private UICheckBox checkBox;


    public RowPrimaryKeyItemPane() {
        this(null, null);
    }

    public RowPrimaryKeyItemPane(String name, OTSPrimaryKeyValue primaryKeyValue) {
        setLayout(new BorderLayout());
        JPanel firstItemPane = new JPanel(new GridLayout(1, 2));
        add(firstItemPane, BorderLayout.CENTER);
        columnNameTextField = new UITextField();
        columnNameTextField.setText(name);
        firstItemPane.add(GUICoreUtils.createBorderLayoutPane(
                new UILabel(Inter.getLocText("Plugin-OTS_Column_Name")), BorderLayout.WEST,
                columnNameTextField, BorderLayout.CENTER
        ));

        firstItemPane.add(GUICoreUtils.createBorderLayoutPane(
                new UILabel(Inter.getLocText("Plugin-OTS_Column_Value")), BorderLayout.WEST,
                valueEditorPane, BorderLayout.CENTER
        ));
        valueEditorPane.populate(primaryKeyValue);
        checkBox = new UICheckBox();
        add(checkBox, BorderLayout.WEST);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 24);
    }

    public String getKeyName() {
        return columnNameTextField.getText();
    }

    public OTSPrimaryKeyValue getPrimaryKeyValue() {
        return (OTSPrimaryKeyValue) valueEditorPane.update();
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    @Override
    protected String title4PopupWindow() {
        return "KeyItem";
    }
}
