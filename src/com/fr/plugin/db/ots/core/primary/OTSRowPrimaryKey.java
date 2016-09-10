package com.fr.plugin.db.ots.core.primary;

import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by richie on 16/9/9.
 */
public class OTSRowPrimaryKey implements XMLable {

    public static final String XML_TAG = "RowPrimaryKey";

    private Map<String, OTSPrimaryKeyValue> data;

    public OTSRowPrimaryKey() {

    }

    public void addPrimaryKeyColumn(String columnName, OTSPrimaryKeyValue value) {
        if (data == null) {
           data = new HashMap<String, OTSPrimaryKeyValue>();
        }
        data.put(columnName, value);
    }

    public int getKeyValueCount() {
        return data == null ? 0 : data.size();
    }

    public Set<Map.Entry<String, OTSPrimaryKeyValue>> getEntrySet() {
        if (data == null) {
            return null;
        } else {
            return data.entrySet();
        }
    }


    public RowPrimaryKey createRowPrimaryKey(Calculator calculator) {
        if (data == null) {
            return null;
        }
        RowPrimaryKey rowPrimaryKey = new RowPrimaryKey();
        for (Map.Entry<String, OTSPrimaryKeyValue> entry : data.entrySet()) {
            rowPrimaryKey.addPrimaryKeyColumn(entry.getKey(), entry.getValue().createPrimaryKeyValue(calculator));
        }
        return rowPrimaryKey;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (tagName.equals(OTSPrimaryKeyValue.XML_TAG)) {
                String name = reader.getAttrAsString("name", StringUtils.EMPTY);
                OTSPrimaryKeyValue keyValue = new OTSPrimaryKeyValue();
                reader.readXMLObject(keyValue);
                addPrimaryKeyColumn(name, keyValue);
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        if (data != null) {
            for (Map.Entry<String, OTSPrimaryKeyValue> entry : data.entrySet()) {
                writer.startTAG(OTSPrimaryKeyValue.XML_TAG);
                writer.attr("name", entry.getKey());
                entry.getValue().writeXML(writer);
                writer.end();
            }
        }
        writer.end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
