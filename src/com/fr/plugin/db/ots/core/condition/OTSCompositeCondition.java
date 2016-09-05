package com.fr.plugin.db.ots.core.condition;

import com.aliyun.openservices.ots.model.condition.ColumnCondition;
import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richie on 16/9/5.
 */
public class OTSCompositeCondition extends AbstractOTSCondtion {

    private List<OTSCondition> conditionList;

    public OTSCompositeCondition() {

    }

    @Override
    public ColumnCondition createColumnCondition() {
        if (conditionList == null) {
            return null;
        }
        CompositeCondition andCondition = new CompositeCondition(CompositeCondition.LogicOperator.AND);
        CompositeCondition orCondition = new CompositeCondition(CompositeCondition.LogicOperator.OR);
        CompositeCondition notCondition = new CompositeCondition(CompositeCondition.LogicOperator.NOT);
        for (OTSCondition condition : conditionList) {
            if (condition.getLogicOperator() == CompositeCondition.LogicOperator.AND) {
                andCondition.addCondition(condition.createColumnCondition());
            } else if (condition.getLogicOperator() == CompositeCondition.LogicOperator.OR) {
                orCondition.addCondition(condition.createColumnCondition());
            } else if (condition.getLogicOperator() == CompositeCondition.LogicOperator.NOT) {
                notCondition.addCondition(condition.createColumnCondition());
            }
        }

        CompositeCondition result = new CompositeCondition(CompositeCondition.LogicOperator.AND);
        result.addCondition(andCondition);
        result.addCondition(orCondition);
        result.addCondition(notCondition);
        return result;
    }

    public int getConditionSize() {
        return conditionList == null ? 0 : conditionList.size();
    }

    public void addCondition(OTSCondition condition) {
        if (conditionList == null) {
            conditionList = new ArrayList<OTSCondition>();
        }
        conditionList.add(condition);
    }

    public OTSCondition getCondition(int index) {
        if (conditionList == null) {
            return null;
        }
        if (index > -1 && index < conditionList.size()) {
            return conditionList.get(index);
        }
        return null;
    }

    public void clearCondtions() {
        if (conditionList != null) {
            conditionList.clear();
        }
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            clearCondtions();
        } else if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("CompositeCondition".equals(tagName)) {
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader xmLableReader) {
                        if (xmLableReader.isChildNode()) {
                            String name = xmLableReader.getTagName();
                            if (OTSCondition.XML_TAG.equals(name)) {
                                OTSCondition condition = (OTSCondition) GeneralXMLTools.readXMLable(xmLableReader);
                                addCondition(condition);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("CompositeCondition");
        if (conditionList != null) {
            for (OTSCondition condition : conditionList) {
                GeneralXMLTools.writeXMLable(writer, condition, OTSCondition.XML_TAG);
            }
        }
        writer.end();
    }
}
