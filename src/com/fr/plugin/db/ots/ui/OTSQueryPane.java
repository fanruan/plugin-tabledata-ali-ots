package com.fr.plugin.db.ots.ui;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.plugin.db.ots.core.OTSTableData;
import com.fr.plugin.db.ots.ui.speed.OTSConditionPane;
import com.fr.plugin.db.ots.ui.speed.RowPrimaryKeyPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

/**
 * Created by richie on 16/1/22.
 */
public class OTSQueryPane extends BasicPane {

    private UITextField tableNameTextField;
    private RowPrimaryKeyPane startRowPrimaryKeyPane;
    private RowPrimaryKeyPane endRowPrimaryKeyPane;
    private OTSConditionPane conditionPane;
    private UICheckBox rangeCheckBox;

    public OTSQueryPane() {
        setLayout(new BorderLayout());

        tableNameTextField = new UITextField();

        ActionLabel helpLabel = new ActionLabel(Inter.getLocText("Plugin-OTS_Help"));
        helpLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(URI.create(SiteCenter.getInstance().acquireUrlByKind("help.db.ots")));
                } catch (IOException e1) {
                    FRLogger.getLogger().error(e1.getMessage(), e1);
                }
            }
        });
        rangeCheckBox = new UICheckBox();
        startRowPrimaryKeyPane = new RowPrimaryKeyPane();
        endRowPrimaryKeyPane = new RowPrimaryKeyPane();
        conditionPane = new OTSConditionPane();

        Component[][] coms = new Component[][]{
                {new UILabel(Inter.getLocText("Plugin-OTS_Table_Name") + ":"), tableNameTextField},
                {new UILabel(Inter.getLocText("Plugin-OTS_Range_Query") + ":"), rangeCheckBox},
                {new UILabel(Inter.getLocText("Plugin-OTS_Start_PrimaryKey") + ":"), startRowPrimaryKeyPane},
                {new UILabel(Inter.getLocText("Plugin-OTS_End_PrimaryKey") + ":"), endRowPrimaryKeyPane},
                {new UILabel(Inter.getLocText("Plugin-OTS_Filter_Condition") + ":"), conditionPane}
        };

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};

        JPanel panel = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);

        add(panel, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return "Query";
    }

    public void loadDBNames(String[] names) {

    }

    public void update(OTSTableData tableData) {
        if (tableData != null) {
            tableData.setStartRowPrimaryKey(startRowPrimaryKeyPane.update());
            tableData.setEndRowPrimaryKey(endRowPrimaryKeyPane.update());
            tableData.setTableName(tableNameTextField.getText());
            tableData.setCondition(conditionPane.update());
        }
    }

    public void populate(OTSTableData tableData) {
        if (tableData != null) {
            tableNameTextField.setText(tableData.getTableName());
            startRowPrimaryKeyPane.populate(tableData.getStartRowPrimaryKey());
            endRowPrimaryKeyPane.populate(tableData.getEndRowPrimaryKey());
            conditionPane.populate(tableData.getCondition());
        }
    }
}