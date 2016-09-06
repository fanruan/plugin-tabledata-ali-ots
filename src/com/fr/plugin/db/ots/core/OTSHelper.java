package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;

/**
 * Created by richie on 16/9/5.
 */
public class OTSHelper {

    /**
     * 将OTS数据库的列值转换为一般对象
     * @param value OTS列值
     * @return 一般对象
     */
    public static Object convertColumnValueToObject(ColumnValue value) {
        if (value == null) {
            return null;
        }
        if (value.getType() == ColumnType.STRING) {
            return value.asString();
        } else if (value.getType() == ColumnType.DOUBLE) {
            return value.asBoolean();
        } else if (value.getType() == ColumnType.BINARY) {
            return value.asLong();
        } else if (value.getType() == ColumnType.DOUBLE) {
            return value.asDouble();
        }
        return value;
    }
}
