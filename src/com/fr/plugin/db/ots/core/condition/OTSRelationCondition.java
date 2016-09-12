package com.fr.plugin.db.ots.core.condition;

import com.aliyun.openservices.ots.model.condition.ColumnCondition;
import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.aliyun.openservices.ots.model.condition.RelationalCondition;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/9/5.
 */
public class OTSRelationCondition extends AbstractOTSCondition {
    private CompositeCondition.LogicOperator logicOperator;
    private String columnName;
    private RelationalCondition.CompareOperator compareOperator;
    private OTSColumnValue columnValue;

    public OTSRelationCondition() {

    }

    @Override
    public int getConditionCount() {
        return 1;
    }

    @Override
    public ColumnCondition createColumnCondition(Calculator c) {
        return new RelationalCondition(columnName, compareOperator, columnValue.createColumnValue(c));
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

    public OTSColumnValue getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(OTSColumnValue columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("ConditionAttr".equals(tagName)) {
                logicOperator = CompositeCondition.LogicOperator.valueOf(reader.getAttrAsString("logic", StringUtils.EMPTY));
                columnName = reader.getAttrAsString("name", StringUtils.EMPTY);
                compareOperator = RelationalCondition.CompareOperator.valueOf(reader.getAttrAsString("compare", StringUtils.EMPTY));
            } else if (OTSColumnValue.XML_TAG.equals(tagName)) {
                this.columnValue = (OTSColumnValue) GeneralXMLTools.readXMLable(reader);
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("ConditionAttr");
        writer.attr("logic", logicOperator.name());
        writer.attr("name", columnName);
        writer.attr("compare", compareOperator.name());
        if (columnValue != null) {
            GeneralXMLTools.writeXMLable(writer, columnValue, OTSColumnValue.XML_TAG);
        }

        writer.end();
    }

    public Object clone() throws CloneNotSupportedException {
        OTSRelationCondition cloned = (OTSRelationCondition)super.clone();
        return cloned;
    }
}
