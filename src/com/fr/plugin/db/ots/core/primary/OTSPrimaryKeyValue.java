package com.fr.plugin.db.ots.core.primary;

import com.aliyun.openservices.ots.model.PrimaryKeyValue;
import com.fr.base.Formula;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/9/9.
 */
public class OTSPrimaryKeyValue implements XMLable {

    public static final String XML_TAG = "PrimaryKeyValue";

    private String type;
    private Object value;

    public OTSPrimaryKeyValue() {

    }

    public OTSPrimaryKeyValue(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public OTSPrimaryKeyValue(Object value) {
        this.value = value;
    }

    public PrimaryKeyValue createPrimaryKeyValue(Calculator calculator) {
        if ("Long".equals(type)) {
            return PrimaryKeyValue.fromLong(GeneralUtils.objectToNumber(value).longValue());
        } else if ("Formula".equals(type)) {
            Formula formula = (Formula)value;
            try {
                Object result = calculator.evalValue(formula);
                if (result instanceof Long || result instanceof Integer) {
                    return PrimaryKeyValue.fromLong(GeneralUtils.objectToNumber(result).longValue());
                } else {
                    return PrimaryKeyValue.fromString(GeneralUtils.objectToString(result));
                }
            } catch (UtilEvalError e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }
        }
        return PrimaryKeyValue.fromString(GeneralUtils.objectToString(value));
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (tagName.equals("Attr")) {
                type = reader.getAttrAsString("type", "String");
                if ("Long".equals(type)) {
                    value = reader.getAttrAsLong("value", 0L);
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
            writer.attr("type", "Long").attr("value", (Long)value);
        } else if (value instanceof Formula) {
            writer.attr("type", "Formula").attr("value", ((Formula) value).getContent());
        } else {
            writer.attr("type", "String").attr("value", GeneralUtils.objectToString(value));
        }
        writer.end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
