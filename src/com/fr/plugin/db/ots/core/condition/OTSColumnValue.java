package com.fr.plugin.db.ots.core.condition;

import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.base.Formula;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/9/12.
 */
public class OTSColumnValue implements XMLable {

    public static final String XML_TAG = "ColumnValue";

    private String type;
    private Object value;

    public OTSColumnValue() {

    }

    public OTSColumnValue(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public ColumnValue createColumnValue(Calculator c) {
        if ("Long".equals(type)) {
            return ColumnValue.fromLong(GeneralUtils.objectToNumber(value).longValue());
        } else if ("Double".equals(type)) {
            return ColumnValue.fromDouble(GeneralUtils.objectToNumber(value).doubleValue());
        } else if ("Boolean".equals(type)) {
            return ColumnValue.fromBoolean(Boolean.parseBoolean(GeneralUtils.objectToString(value)));
        } else if ("Formula".equals(type)) {
            Formula formula = (Formula)value;
            try {
                Object result = c.evalValue(formula);
                if (result instanceof Long || result instanceof Integer) {
                    return ColumnValue.fromLong(GeneralUtils.objectToNumber(result).longValue());
                } else if (result instanceof Double) {
                    return ColumnValue.fromDouble(GeneralUtils.objectToNumber(result).doubleValue());
                } else if (result instanceof Boolean) {
                    return ColumnValue.fromBoolean((Boolean)result);
                } else {
                    return ColumnValue.fromString(GeneralUtils.objectToString(value));
                }
            } catch (UtilEvalError u) {
                FRLogger.getLogger().error(u.getMessage(), u);
            }
        }
        return ColumnValue.fromString(GeneralUtils.objectToString(value));
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Attr".equals(tagName)) {
                type = reader.getAttrAsString("type", "String");
                if ("Long".equals(type)) {
                    value = reader.getAttrAsLong("value", 0L);
                } else if ("Double".equals(type)) {
                    value = reader.getAttrAsDouble("value", 0);
                } else if ("Boolean".equals(type)) {
                    value = reader.getAttrAsBoolean("value", false);
                } else if ("Formula".equals(type)) {
                    value = new Formula(reader.getAttrAsString("value", StringUtils.EMPTY));
                } else {
                    value = reader.getAttrAsString("value", StringUtils.EMPTY);
                }
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("Attr");
        if (value instanceof Long) {
            writer.attr("type", "Long").attr("value", (Long) value);
        } else if (value instanceof Double) {
            writer.attr("type", "Double").attr("value", (Double)value);
        } else if (value instanceof Boolean) {
            writer.attr("type", "Boolean").attr("value", (Boolean)value);
        } else if (value instanceof Formula) {
            writer.attr("type", "Formula").attr("value", ((Formula)value).getContent());
        } else {
            writer.attr("type", "String").attr("value", GeneralUtils.objectToString(value));
        }

        writer.end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
