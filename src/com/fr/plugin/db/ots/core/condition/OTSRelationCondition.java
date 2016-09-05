package com.fr.plugin.db.ots.core.condition;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.aliyun.openservices.ots.model.condition.ColumnCondition;
import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.aliyun.openservices.ots.model.condition.RelationalCondition;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/9/5.
 */
public class OTSRelationCondition extends AbstractOTSCondtion {
    private CompositeCondition.LogicOperator logicOperator;
    private String columnName;
    private RelationalCondition.CompareOperator compareOperator;
    private ColumnValue columnValue;

    public OTSRelationCondition() {

    }

    @Override
    public int getConditionCount() {
        return 1;
    }

    @Override
    public ColumnCondition createColumnCondition() {
        return new RelationalCondition(columnName, compareOperator, columnValue);
    }

    public CompositeCondition.LogicOperator getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(CompositeCondition.LogicOperator logicOperator) {
        this.logicOperator = logicOperator;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public RelationalCondition.CompareOperator getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(RelationalCondition.CompareOperator compareOperator) {
        this.compareOperator = compareOperator;
    }

    public ColumnValue getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(ColumnValue columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Attr".equals(tagName)) {
                logicOperator = CompositeCondition.LogicOperator.valueOf(reader.getAttrAsString("logic", StringUtils.EMPTY));
                columnName = reader.getAttrAsString("name", StringUtils.EMPTY);
                compareOperator = RelationalCondition.CompareOperator.valueOf(reader.getAttrAsString("compare", StringUtils.EMPTY));
                ColumnType type = ColumnType.valueOf(reader.getAttrAsString("type", StringUtils.EMPTY));
                if (type == ColumnType.STRING) {
                    columnValue = ColumnValue.fromString(reader.getAttrAsString("value", StringUtils.EMPTY));
                } else if (type == ColumnType.INTEGER) {
                    columnValue = ColumnValue.fromLong(reader.getAttrAsLong("value", 0L));
                } if (type == ColumnType.DOUBLE) {
                    columnValue = ColumnValue.fromDouble(reader.getAttrAsDouble("value", 0));
                } if (type == ColumnType.BOOLEAN) {
                    columnValue = ColumnValue.fromBoolean(reader.getAttrAsBoolean("value", false));
                }
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("Attr");
        writer.attr("logic", logicOperator.name());
        writer.attr("name", columnName);
        writer.attr("compare", compareOperator.name());
        if (columnValue.getType() == ColumnType.STRING) {
            writer.attr("type", ColumnType.STRING.name()).attr("value", columnValue.asString());
        } else if (columnValue.getType() == ColumnType.INTEGER) {
            writer.attr("type", ColumnType.INTEGER.name()).attr("value", columnValue.asLong());
        } else if (columnValue.getType() == ColumnType.DOUBLE) {
            writer.attr("type", ColumnType.DOUBLE.name()).attr("value", columnValue.asDouble());
        } else if (columnValue.getType() == ColumnType.BOOLEAN) {
            writer.attr("type", ColumnType.BOOLEAN.name()).attr("value", columnValue.asBoolean());
        }

        writer.end();
    }

    public Object clone() throws CloneNotSupportedException {
        OTSRelationCondition cloned = (OTSRelationCondition)super.clone();
        return cloned;
    }
}
