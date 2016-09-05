package com.fr.plugin.db.ots.ui.speed;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.condition.OTSCondition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by richie on 16/9/5.
 */
public class OTSConditionPane extends BasicPane {

    private UILabel conditionCountLabel;
    private OTSCondition condition;

    public OTSConditionPane() {
        setLayout(new BorderLayout());
        conditionCountLabel = new UILabel();
        add(conditionCountLabel, BorderLayout.CENTER);
        UIButton editButton = new UIButton(Inter.getLocText("Plugin-OTS_Edit"));
        add(editButton, BorderLayout.EAST);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final OTSConditionEditPane editPane = new OTSConditionEditPane();
                editPane.populate(condition);
                BasicDialog dialog = editPane.showWindow(SwingUtilities.getWindowAncestor(OTSConditionPane.this), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        condition = editPane.update();
                        updateConditionCountLabel();
                    }
                });
                dialog.setVisible(true);
            }
        });
        updateConditionCountLabel();
    }

    private void updateConditionCountLabel() {
        conditionCountLabel.setText("有N个过滤条件");
    }

    public OTSCondition update() {
        return condition;
    }

    public void populate(OTSCondition condition) {
        this.condition = condition;
    }

    @Override
    protected String title4PopupWindow() {
        return "Condition";
    }
}
