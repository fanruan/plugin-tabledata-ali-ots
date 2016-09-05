package com.fr.plugin.db.ots.ui;

import com.fr.design.border.UITitledBorder;
import com.fr.design.data.datapane.connect.DatabaseConnectionPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.OTSDatabaseConnection;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/1/22.
 */
public class OTSConnectionPane extends DatabaseConnectionPane<OTSDatabaseConnection> {

    private UITextField endPointTextField;
    private UITextField accessIdTextField;
    private UITextField accessKeyTextField;
    private UITextField instanceNameTextField;
    private UITextField optionsTextField;


    @Override
    protected JPanel mainPanel() {
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        endPointTextField = new UITextField();
        accessIdTextField = new UITextField();
        accessKeyTextField = new UITextField();
        instanceNameTextField = new UITextField();
        optionsTextField = new UITextField();

        Component[][] components = new Component[][]{
                {new UILabel(Inter.getLocText("Plugin-OTS_EndPoint") + ":"), endPointTextField},
                {new UILabel(Inter.getLocText("Plugin-OTS_AccessId") + ":"), accessIdTextField},
                {new UILabel(Inter.getLocText("Plugin-OTS_AccessKey") + ":"), accessKeyTextField},
                {new UILabel(Inter.getLocText("Plugin-OTS_InstanceName") + ":"), instanceNameTextField},
                {new UILabel(Inter.getLocText("Plugin-OTS_Options") + ":"), optionsTextField}
        };
        double p = TableLayout.PREFERRED;

        double[] rowSize = new double[]{p, p, p, p, p};
        double[] columnSize = new double[]{p, 400};

        JPanel settingsUI = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        settingsUI.setBorder(UITitledBorder.createBorderWithTitle("OTS"));

        JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();

        centerPane.add(settingsUI);

        pane.add(centerPane, BorderLayout.CENTER);
        return pane;
    }

    @Override
    protected boolean isFineBI() {
        return false;
    }

    @Override
    protected void populateSubDatabaseConnectionBean(OTSDatabaseConnection ob) {
        endPointTextField.setText(ob.getEndPoint());
        accessIdTextField.setText(ob.getAccessId());
        accessKeyTextField.setText(ob.getAccessKey());
        instanceNameTextField.setText(ob.getInstanceName());
        optionsTextField.setText(ob.getOptions());
    }

    @Override
    protected OTSDatabaseConnection updateSubDatabaseConnectionBean() {
        OTSDatabaseConnection connection = new OTSDatabaseConnection();
        connection.setEndPoint(endPointTextField.getText());
        connection.setAccessId(accessIdTextField.getText());
        connection.setAccessKey(accessKeyTextField.getText());
        connection.setInstanceName(instanceNameTextField.getText());
        connection.setOptions(optionsTextField.getText());
        return connection;
    }

    @Override
    protected String title4PopupWindow() {
        return "OTS";
    }
}