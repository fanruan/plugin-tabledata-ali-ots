package com.fr.plugin.db.ots.ui.speed;

import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.aliyun.openservices.ots.model.condition.RelationalCondition;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.condition.OTSColumnValue;
import com.fr.plugin.db.ots.core.condition.OTSCondition;
import com.fr.plugin.db.ots.core.condition.OTSRelationCondition;
import com.fr.plugin.db.ots.ui.speed.cv.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/9/5.
 */
public class OTSConditionItemPane extends BasicPane {

    private UICheckBox checkBox;
    private UIDictionaryComboBox<CompositeCondition.LogicOperator> logicComboBox;
    private UITextField columnTextField;
    private UIDictionaryComboBox<RelationalCondition.CompareOperator> compareComboBox;
    private ValueEditorPane valueEditorPane;
    private boolean isHead;


    public OTSConditionItemPane() {
        setLayout(new BorderLayout());
        checkBox = new UICheckBox();
        add(checkBox, BorderLayout.WEST);

        JPanel centerPane = new JPanel(new GridLayout(1, 4));
        add(centerPane, BorderLayout.CENTER);
        logicComboBox = new UIDictionaryComboBox<CompositeCondition.LogicOperator>(
                CompositeCondition.LogicOperator.values(),
                new String[]{
                        "NOT", "AND", "OR"
                });
        // 默认选择AND
        logicComboBox.setSelectedIndex(1);
        if (isHead()) {
            logicComboBox.setEditable(false);
        }
        centerPane.add(logicComboBox);
        columnTextField = new UITextField();
        centerPane.add(columnTextField);
        compareComboBox = new UIDictionaryComboBox<RelationalCondition.CompareOperator>(
                RelationalCondition.CompareOperator.values(),
                new String[]{
                        "等于", "不等于", "大于", "大于等于", "小于", "小于等于"
                }
        );
        centerPane.add(compareComboBox);
        valueEditorPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{
                new StringColumnValueEditor(),
                new DoubleColumnValueEditor(),
                new LongColumnValueEditor(),
                new BooleanColumnValueEditor(),
                new FormulaColumnValueEditor(Inter.getLocText("Plugin-OTS_Formula"))
        });
        centerPane.add(valueEditorPane);
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public boolean isHead() {
        return isHead;
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 24);
    }

    @Override
    protected String title4PopupWindow() {
        return "Item";
    }

    public OTSCondition update() {
        OTSRelationCondition condition = new OTSRelationCondition();
        condition.setLogicOperator(logicComboBox.getSelectedItem());
        condition.setColumnName(columnTextField.getText());
        condition.setCompareOperator(compareComboBox.getSelectedItem());
        Object data = valueEditorPane.update();
        condition.setColumnValue((OTSColumnValue)data);
        return condition;
    }

    public void populate(OTSRelationCondition condition) {
        logicComboBox.setSelectedItem(condition.getLogicOperator());
        columnTextField.setText(condition.getColumnName());
        compareComboBox.setSelectedItem(condition.getCompareOperator());
        valueEditorPane.populate(condition.getColumnValue());
    }
}
