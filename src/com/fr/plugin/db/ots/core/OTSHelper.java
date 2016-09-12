package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.ClientException;
import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.OTSException;
import com.aliyun.openservices.ots.ServiceException;
import com.aliyun.openservices.ots.model.*;
import com.aliyun.openservices.ots.model.condition.ColumnCondition;
import com.fr.plugin.db.ots.core.condition.OTSCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by richie on 16/9/5.
 */
public class OTSHelper {

    /**
     * 将OTS数据库的列值转换为一般对象
     *
     * @param value OTS列值
     * @return 一般对象
     */
    public static Object convertColumnValueToObject(ColumnValue value) {
        if (value == null) {
            return null;
        }
        if (value.getType() == ColumnType.STRING) {
            return value.asString();
        } else if (value.getType() == ColumnType.INTEGER) {
            return value.asLong();
        } else if (value.getType() == ColumnType.DOUBLE) {
            return value.asDouble();
        } else if (value.getType() == ColumnType.BOOLEAN) {
            return value.asBoolean();
        }
        return value;
    }

    public static List<Row> batchGetRow(OTSClient client,
                                        String tableName,
                                        RowPrimaryKey startRowPrimaryKey,
                                        RowPrimaryKey endRowPrimaryKey,
                                        ColumnCondition condition
    )
            throws OTSException, ClientException {
        BatchGetRowRequest request = new BatchGetRowRequest();

        MultiRowQueryCriteria tableRows = new MultiRowQueryCriteria(tableName);
        if (startRowPrimaryKey != null) {
            tableRows.addRow(startRowPrimaryKey);
        }
        if (endRowPrimaryKey != null) {
            tableRows.addRow(endRowPrimaryKey);
        }

        if (condition != null) {
            tableRows.setFilter(condition);
        }

        request.addMultiRowQueryCriteria(tableRows);

        BatchGetRowResult result = client.batchGetRow(request);
        BatchGetRowRequest failedOperations = null;

        List<Row> succeedRows = new ArrayList<Row>();

        int retryCount = 0;
        do {
            failedOperations = new BatchGetRowRequest();

            Map<String, List<com.aliyun.openservices.ots.model.BatchGetRowResult.RowStatus>> status = result
                    .getTableToRowsStatus();
            for (Map.Entry<String, List<BatchGetRowResult.RowStatus>> entry : status
                    .entrySet()) {
                tableName = entry.getKey();
                tableRows = new MultiRowQueryCriteria(tableName);
                List<com.aliyun.openservices.ots.model.BatchGetRowResult.RowStatus> statuses = entry
                        .getValue();
                for (int index = 0; index < statuses.size(); index++) {
                    com.aliyun.openservices.ots.model.BatchGetRowResult.RowStatus rowStatus = statuses
                            .get(index);
                    if (!rowStatus.isSucceed()) {
                        // 操作失败， 需要放到重试列表中再次重试
                        // 需要重试的操作可以从request中获取参数
                        tableRows.addRow(request
                                .getPrimaryKey(tableName, index));
                    } else {
                        succeedRows.add(rowStatus.getRow());
                    }
                }

                if (!tableRows.getRowKeys().isEmpty()) {
                    failedOperations.addMultiRowQueryCriteria(tableRows);
                }
            }

            if (failedOperations.isEmpty() || ++retryCount > 3) {
                break; // 如果所有操作都成功了或者重试次数达到上线， 则不再需要重试。
            }

            // 如果有需要重试的操作， 则稍微等待一会后再次重试， 否则继续出错的概率很高。
            try {
                Thread.sleep(100); // 100ms后继续重试
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            request = failedOperations;
            result = client.batchGetRow(request);
        } while (true);

        System.out.println(String.format("查询成功%d行数据。", succeedRows.size()));
        return succeedRows;
    }
}
