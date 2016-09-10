package com.fr.plugin.db.ots.ui.speed;

import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.primary.OTSRowPrimaryKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by richie on 16/9/5.
 */
public class RowPrimaryKeyPane extends BasicPane {

    private UILabel countLabel;
    private OTSRowPrimaryKey rowPrimaryKey;

    public RowPrimaryKeyPane() {
        setLayout(new BorderLayout());

        countLabel = new UILabel();
        add(countLabel, BorderLayout.CENTER);

        UIButton button = new UIButton(Inter.getLocText("Plugin-OTS_Edit"));
        add(button, BorderLayout.EAST);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final RowPrimaryKeyEditPane editPane = new RowPrimaryKeyEditPane();
                editPane.populate(rowPrimaryKey);
                BasicDialog dialog = editPane.showWindow(SwingUtilities.getWindowAncestor(RowPrimaryKeyPane.this), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        rowPrimaryKey = editPane.update();
                        updateRowPrimaryKeyCount();
                    }
                });
                dialog.setVisible(true);
            }
        });
        updateRowPrimaryKeyCount();
    }

    private void updateRowPrimaryKeyCount() {
        countLabel.setText((rowPrimaryKey == null ? 0 : rowPrimaryKey.getKeyValueCount()) + Inter.getLocText("Plugin-OTS_Primary_Key_Count"));
    }

    public OTSRowPrimaryKey update() {
        return rowPrimaryKey;
    }

    public void populate(OTSRowPrimaryKey rowPrimaryKey) {
        this.rowPrimaryKey = rowPrimaryKey;
        updateRowPrimaryKeyCount();
    }

    @Override
    protected String title4PopupWindow() {
        return "PrimaryKey";
    }
}
