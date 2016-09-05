package com.fr.plugin.db.ots.ui;

import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.model.ListTableResult;
import com.fr.base.FRContext;
import com.fr.data.impl.Connection;
import com.fr.design.data.datapane.connect.ConnectionComboBoxPanel;
import com.fr.design.dialog.BasicPane;
import com.fr.file.DatasourceManager;
import com.fr.plugin.db.ots.core.OTSDatabaseConnection;
import com.fr.plugin.db.ots.ui.event.DataLoadedListener;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by richie on 16/1/22.
 */
public class OTSConnectionChosePane extends BasicPane {

    private ConnectionComboBoxPanel connectionComboBoxPanel;
    private DefaultListModel listModel = new DefaultListModel();
    private List<DataLoadedListener> listeners = new ArrayList<DataLoadedListener>();

    public OTSConnectionChosePane() {
        setLayout(new BorderLayout(4, 4));
        connectionComboBoxPanel = new ConnectionComboBoxPanel(Connection.class) {

            protected void filterConnection(Connection connection, String conName, List<String> nameList) {
                connection.addConnection(nameList, conName, new Class[]{OTSDatabaseConnection.class});
            }
        };

        add(connectionComboBoxPanel, BorderLayout.NORTH);
        connectionComboBoxPanel.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = getSelectMongoConnectionName();
                if (StringUtils.isEmpty(name)) {
                    clearList();
                    fireDataLoaded(ArrayUtils.EMPTY_STRING_ARRAY);
                    return;
                }
                OTSDatabaseConnection connection = DatasourceManager.getProviderInstance().getConnection(name, OTSDatabaseConnection.class);
                if (connection != null) {
                    listAllDBNames(connection);
                }
            }
        });
        JList list = new JList(listModel);
        add(list, BorderLayout.CENTER);
    }

    private void clearList() {
        listModel.clear();
    }

    private void listAllDBNames(final OTSDatabaseConnection connection) {
        clearList();
        new SwingWorker<String[], Void>() {

            @Override
            protected String[] doInBackground() throws Exception {
                OTSClient client = connection.createOTSClient();
                ListTableResult result = client.listTable();
                List<String> names = result.getTableNames();
                client.shutdown();
                return names.toArray(new String[names.size()]);
            }

            @Override
            protected void done() {
                try {
                    String[] names = get();
                    for (String name : names) {
                        listModel.addElement(name);
                    }
                    fireDataLoaded(names);
                } catch (InterruptedException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                } catch (ExecutionException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }.execute();
    }

    public String getSelectMongoConnectionName() {
        return connectionComboBoxPanel.getSelectedItem();
    }

    public void populateConnection(Connection connection) {
        connectionComboBoxPanel.populate(connection);
    }

    public void addDataLoadedListener(DataLoadedListener listener) {
        listeners.add(listener);
    }

    private void fireDataLoaded(String[] data) {
        for (DataLoadedListener listener : listeners) {
            listener.fireEvent(data);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "Choose";
    }
}