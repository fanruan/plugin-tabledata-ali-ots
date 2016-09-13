package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.aliyun.openservices.ots.model.RangeIteratorParameter;
import com.aliyun.openservices.ots.model.Row;
import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.aliyun.openservices.ots.model.condition.ColumnCondition;
import com.fr.general.ModuleContext;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.plugin.PluginLicense;
import com.fr.plugin.PluginLicenseManager;

import java.util.*;

/**
 * Created by richie on 16/1/22.
 */
public class OTSTableDataModel implements DataModel {


    private List<String> columnNames;
    private List<List<Object>> data;


    public OTSTableDataModel(OTSDatabaseConnection mc,
                             String tableName,
                             boolean rangeQuery,
                             RowPrimaryKey startRowPrimaryKey,
                             RowPrimaryKey endRowPrimaryKey,
                             ColumnCondition condition,
                             int rowCount) {
        PluginLicense pluginLicense = PluginLicenseManager.getInstance().getPluginLicenseByID(OTSConstants.PLUGIN_ID);
        if (pluginLicense.isAvailable() || isDesign()) {
            initData(mc, tableName, rangeQuery, startRowPrimaryKey, endRowPrimaryKey, condition, rowCount);
        } else {
            throw new RuntimeException("OTS Database Plugin License Expired!");
        }
    }

    private boolean isDesign() {
        return ModuleContext.isModuleStarted("com.fr.design.module.DesignerModule");
    }

    private synchronized void initData(OTSDatabaseConnection mc,
                                       String tableName,
                                       boolean rangeQuery,
                                       RowPrimaryKey startRowPrimaryKey,
                                       RowPrimaryKey endRowPrimaryKey,
                                       ColumnCondition condition,
                                       int rowCount) {
        if (rangeQuery) {
            loadDataByRange(mc, tableName, startRowPrimaryKey, endRowPrimaryKey, condition, rowCount);
        } else {
            loadBatch(mc, tableName, startRowPrimaryKey, endRowPrimaryKey, condition, rowCount);
        }
    }

    private void loadBatch(OTSDatabaseConnection mc,
                           String tableName,
                           RowPrimaryKey startRowPrimaryKey,
                           RowPrimaryKey endRowPrimaryKey,
                           ColumnCondition condition,
                           int rowCount) {
        OTSClient client = mc.createOTSClient();
        List<Row> rows = OTSHelper.batchGetRow(client, tableName, startRowPrimaryKey, endRowPrimaryKey, condition);
        Iterator<Row> rowIt = rows.iterator();
        fillRowData(rowIt, rowCount);
        client.shutdown();
    }


    private void loadDataByRange(OTSDatabaseConnection mc,
                                 String tableName,
                                 RowPrimaryKey startRowPrimaryKey,
                                 RowPrimaryKey endRowPrimaryKey,
                                 ColumnCondition condition,
                                 int rowCount) {
        RangeIteratorParameter param = new RangeIteratorParameter(tableName);
        if (startRowPrimaryKey != null) {
            param.setInclusiveStartPrimaryKey(startRowPrimaryKey);
        }
        if (endRowPrimaryKey != null) {
            param.setExclusiveEndPrimaryKey(endRowPrimaryKey);
        }

        if (condition != null) {
            param.setFilter(condition);
        }
        OTSClient otsClient = mc.createOTSClient();

        Iterator<Row> rowIt = otsClient.createRangeIterator(param);
        fillRowData(rowIt, rowCount);
        otsClient.shutdown();
    }

    private void fillRowData(Iterator<Row> rowIt, int rowCount) {
        int totalRows = 0;

        Map<String, List<Object>> columnDataMap = new HashMap<String, List<Object>>();
        while (rowIt.hasNext()) {
            if (rowCount != -1 && totalRows > rowCount) {
                break;
            }
            Row row = rowIt.next();
            Map<String, ColumnValue> item = row.getColumns();

            for (Map.Entry<String, ColumnValue> entry : item.entrySet()) {
                String columnName = entry.getKey();
                List<Object> columnData = columnDataMap.get(columnName);
                if (columnData == null) {
                    columnData = new ArrayList<Object>();
                    columnDataMap.put(columnName, columnData);
                }
                columnData.add(OTSHelper.convertColumnValueToObject(entry.getValue()));
            }
            totalRows++;
        }
        columnNames = new ArrayList<String>();
        data = new ArrayList<List<Object>>();
        for (Map.Entry<String, List<Object>> entry : columnDataMap.entrySet()) {
            columnNames.add(entry.getKey());
            data.add(entry.getValue());
        }
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
        return data != null && data.get(0).size() > rowIndex;
    }

    @Override
    public int getRowCount() throws TableDataException {
        return data == null ? 0 : data.get(0).size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws TableDataException {
        if (data != null && data.size() > columnIndex) {
            List<Object> columnData = data.get(columnIndex);
            if (columnData != null && columnData.size() > rowIndex) {
                return columnData.get(rowIndex);
            }
        }
        return null;
    }

    @Override
    public void release() throws Exception {

    }
}