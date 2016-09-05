package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.ClientException;
import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.ServiceException;
import com.aliyun.openservices.ots.model.*;
import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.aliyun.openservices.ots.model.condition.RelationalCondition;

import java.util.Iterator;

/**
 * Created by richie on 16/9/5.
 */
public class OTSHelper {
    private static final String COLUMN_GID_NAME = "gid";
    private static final String COLUMN_UID_NAME = "uid";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_ADDRESS_NAME = "address";
    private static final String COLUMN_AGE_NAME = "age";
    private static final String COLUMN_IS_STUDENT_NAME = "isstudent";

    private static void getRowWithFilter(OTSClient client, String tableName)
            throws ServiceException, ClientException {

        SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(tableName);
        RowPrimaryKey primaryKeys = new RowPrimaryKey();
        primaryKeys.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(1));
        primaryKeys.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.fromLong(2));
        criteria.setPrimaryKey(primaryKeys);

        // 增加一个查询条件，只有当满足条件 name == 'lilei' 时才返回数据。
        RelationalCondition filter = new RelationalCondition(COLUMN_NAME_NAME, RelationalCondition.CompareOperator.EQUAL, ColumnValue.fromString("lilei"));
        criteria.setFilter(filter);

        GetRowRequest request = new GetRowRequest();
        request.setRowQueryCriteria(criteria);
        GetRowResult result = client.getRow(request);
        Row row = result.getRow();
        System.out.println("Row returned (name == 'lilei'): " + row.getColumns());

        // 更改查询条件，只有当满足条件(name == 'lilei' and isstudent == true)时才返回数据。
        RelationalCondition anotherFilter = new RelationalCondition(COLUMN_IS_STUDENT_NAME, RelationalCondition.CompareOperator.EQUAL, ColumnValue.fromBoolean(true));
        CompositeCondition cc = new CompositeCondition(CompositeCondition.LogicOperator.AND);
        cc.addCondition(filter);
        cc.addCondition(anotherFilter);

        criteria.setFilter(cc);
        // 而目前表中的该行数据不满足该条件，所以不会返回。
        result = client.getRow(request);
        row = result.getRow();
        System.out.println("Row returned (name == 'lilei' and isstudent == true): " + row.getColumns());
    }


    private static void getRangeWithFilter(OTSClient client, String tableName)
            throws ServiceException, ClientException {
        // 查询范围为：
        //      start primary key = (gid=0, INF_MIN)
        //      end primary key   = (gid=100, INF_MAX)
        // 且满足条件为：
        //      isstudent == true
        // 的所有行。
        RangeIteratorParameter param = new RangeIteratorParameter(tableName);
        RowPrimaryKey startPk = new RowPrimaryKey();
        startPk.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(0));
        startPk.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.INF_MIN);
        RowPrimaryKey endPk = new RowPrimaryKey();
        endPk.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(100));
        endPk.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.INF_MAX);

        param.setInclusiveStartPrimaryKey(startPk);
        param.setExclusiveEndPrimaryKey(endPk);

        RelationalCondition filter = new RelationalCondition(COLUMN_IS_STUDENT_NAME, RelationalCondition.CompareOperator.EQUAL, ColumnValue.fromBoolean(true));
        param.setFilter(filter);

        Iterator<Row> rowIter = client.createRangeIterator(param);
        int totalRows = 0;
        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            totalRows++;
            System.out.println(row);
        }

        System.out.println("Total rows read: " + totalRows);
    }
}
