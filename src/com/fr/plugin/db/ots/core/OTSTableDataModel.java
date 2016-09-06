package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.aliyun.openservices.ots.model.RangeIteratorParameter;
import com.aliyun.openservices.ots.model.Row;
import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.fr.general.ModuleContext;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.plugin.PluginLicense;
import com.fr.plugin.PluginLicenseManager;
import com.fr.plugin.db.ots.core.condition.OTSCondition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by richie on 16/1/22.
 */
public class OTSTableDataModel implements DataModel {


    private List<String> columnNames;
    private List<List<Object>> data;


    public OTSTableDataModel(OTSDatabaseConnection mc,
                             String tableName,
                             RowPrimaryKey startRowPrimaryKey,
                             RowPrimaryKey endRowPrimaryKey,
                             OTSCondition condition,
                             int rowCount) {
        PluginLicense pluginLicense = PluginLicenseManager.getInstance().getPluginLicenseByID(OTSConstants.PLUGIN_ID);
        if (pluginLicense.isAvailable() || isDesign()) {
            initData(mc, tableName, startRowPrimaryKey, endRowPrimaryKey, condition, rowCount);
        } else {
            throw new RuntimeException("OTS Database Plugin License Expired!");
        }
    }

    private boolean isDesign() {
        return ModuleContext.isModuleStarted("com.fr.design.module.DesignerModule");
    }

    private synchronized void initData(OTSDatabaseConnection mc,
                                       String tableName,
                                       RowPrimaryKey startRowPrimaryKey,
                                       RowPrimaryKey endRowPrimaryKey,
                                       OTSCondition condition,
                                       int rowCount) {
        RangeIteratorParameter param = new RangeIteratorParameter(tableName);
        if (startRowPrimaryKey != null) {
            param.setInclusiveStartPrimaryKey(startRowPrimaryKey);
        }
        if (endRowPrimaryKey != null) {
            param.setExclusiveEndPrimaryKey(endRowPrimaryKey);
        }
        if (condition != null) {
            param.setFilter(condition.createColumnCondition());
        }
        OTSClient otsClient = mc.createOTSClient();

        Iterator<Row> rowIt = otsClient.createRangeIterator(param);
        int totalRows = 0;
        columnNames = new ArrayList<String>();
        data = new ArrayList<List<Object>>();
        while (rowIt.hasNext()) {
            if (rowCount != -1 && totalRows > rowCount) {
                break;
            }
            Row row = rowIt.next();
            Map<String, ColumnValue> item = row.getColumns();
            List<Object> rowData = new ArrayList<Object>();
            for (Map.Entry<String, ColumnValue> entry : item.entrySet()) {
                columnNames.add(entry.getKey());
                rowData.add(OTSHelper.convertColumnValueToObject(entry.getValue()));
            }
            data.add(rowData);
            totalRows++;
        }
        otsClient.shutdown();
    }


    @Override
    public int getColumnCount() throws TableDataException {
        return columnNames == null ? 0 : columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) throws TableDataException {
        return columnNames == null ? null : columnNames.get(columnIndex);
    }

    @Override
    public boolean hasRow(int rowIndex) throws TableDataException {
        return data != null && data.size() > rowIndex;
    }

    @Override
    public int getRowCount() throws TableDataException {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws TableDataException {
        if (data != null && data.size() > rowIndex) {
            List<Object> rowData = data.get(rowIndex);
            if (rowData != null && rowData.size() > columnIndex) {
                return rowData.get(columnIndex);
            }
        }
        return null;
    }

    @Override
    public void release() throws Exception {

    }
}