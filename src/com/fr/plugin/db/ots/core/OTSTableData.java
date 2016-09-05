package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.model.PrimaryKeyType;
import com.aliyun.openservices.ots.model.PrimaryKeyValue;
import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.base.TemplateUtils;
import com.fr.data.AbstractParameterTableData;
import com.fr.data.core.DataCoreXmlUtils;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.file.DatasourceManager;
import com.fr.general.Inter;
import com.fr.general.data.DataModel;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.plugin.ExtraClassManager;
import com.fr.plugin.db.ots.core.condition.OTSCondition;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richie on 16/1/22.
 */
public class OTSTableData extends AbstractParameterTableData {

    private static final FunctionProcessor OTS_DB = new AbstractFunctionProcessor() {

        @Override
        public int getId() {
            return FunctionHelper.generateFunctionID(OTSConstants.PLUGIN_ID);
        }

        @Override
        public String getLocaleKey() {
            return "Plugin-OTS_DB";
        }

        @Override
        public String toString() {
            return Inter.getLocText("Plugin-OTS_DB");
        }
    };

    private static final String ATTR_TAG = "OTSTableDataAttr";

    private Connection database;

    private String dbName;

    private String tableName;

    private RowPrimaryKey startRowPrimaryKey;
    private RowPrimaryKey endRowPrimaryKey;

    private OTSCondition condition;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public RowPrimaryKey getStartRowPrimaryKey() {
        return startRowPrimaryKey;
    }

    public void setStartRowPrimaryKey(RowPrimaryKey startRowPrimaryKey) {
        this.startRowPrimaryKey = startRowPrimaryKey;
    }

    public RowPrimaryKey getEndRowPrimaryKey() {
        return endRowPrimaryKey;
    }

    public void setEndRowPrimaryKey(RowPrimaryKey endRowPrimaryKey) {
        this.endRowPrimaryKey = endRowPrimaryKey;
    }

    public void setCondition(OTSCondition condition) {
        this.condition = condition;
    }

    public OTSCondition getCondition() {
        return condition;
    }

    public Connection getDatabase() {
        return database;
    }

    public void setDatabase(Connection database) {
        this.database = database;
    }

    public void setParameters(ParameterProvider[] providers) {
        super.setDefaultParameters(providers);
    }

    @Override
    public DataModel createDataModel(Calculator calculator) {
        return createDataModel(calculator, TableData.RESULT_ALL);
    }

    @Override
    public DataModel createDataModel(Calculator calculator, int rowCount) {
        FunctionProcessor processor = ExtraClassManager.getInstance().getFunctionProcessor();
        if (processor != null) {
            processor.recordFunction(OTS_DB);
        }
        Parameter[] ps = Parameter.providers2Parameter(Calculator.processParameters(calculator, parameters));
        if (database instanceof NameDatabaseConnection) {
            String name = ((NameDatabaseConnection) database).getName();
            OTSDatabaseConnection mc = DatasourceManager.getProviderInstance().getConnection(name, OTSDatabaseConnection.class);
            if (mc != null) {
                return new OTSTableDataModel(mc,
                        tableName,
                        startRowPrimaryKey,
                        endRowPrimaryKey,
                        condition,
                        rowCount);
            }
        }
        return null;
    }

    private String calculateQuery(String query, Parameter[] ps) {
        if (ArrayUtils.isEmpty(ps)) {
            return query;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (Parameter p : ps) {
            map.put(p.getName(), p.getValue());
        }
        try {
            return TemplateUtils.renderParameter4Tpl(query, map);
        } catch (Exception e) {
            return query;
        }
    }

    public void readXML(XMLableReader reader) {
        super.readXML(reader);

        if (reader.isChildNode()) {
            String tagName = reader.getTagName();

            if (com.fr.data.impl.Connection.XML_TAG.equals(tagName)) {
                if (reader.getAttrAsString("class", null) != null) {
                    com.fr.data.impl.Connection con = DataCoreXmlUtils.readXMLConnection(reader);
                    this.setDatabase(con);
                }
            } else if (ATTR_TAG.equals(tagName)) {
                tableName = reader.getAttrAsString("tableName", StringUtils.EMPTY);
            } else if ("StartRowPrimaryKey".equals(tagName)) {
                startRowPrimaryKey = readRowPrimaryKey(reader);
            } else if ("EndRowPrimaryKey".equals(tagName)) {
                endRowPrimaryKey = readRowPrimaryKey(reader);
            } else if (OTSCondition.XML_TAG.equals(tagName)) {
                condition = (OTSCondition) GeneralXMLTools.readXMLable(reader);
            }
        }
    }

    private RowPrimaryKey readRowPrimaryKey(final XMLableReader reader) {
        final RowPrimaryKey rowPrimaryKey = new RowPrimaryKey();
        reader.readXMLObject(new XMLReadable() {
            @Override
            public void readXML(XMLableReader xmLableReader) {
                if (xmLableReader.isChildNode()) {
                    String tagName = reader.getTagName();
                    if ("PrimaryKey".equals(tagName)) {
                        String name = xmLableReader.getAttrAsString("name", StringUtils.EMPTY);
                        String type = xmLableReader.getAttrAsString("type", "string");
                        PrimaryKeyValue keyValue = null;
                        if ("string".equals(type)) {
                            keyValue = PrimaryKeyValue.fromString(xmLableReader.getAttrAsString("value", null));
                        } else if ("long".equals(type)) {
                            keyValue = PrimaryKeyValue.fromLong(xmLableReader.getAttrAsLong("value", 0));
                        } else if ("type".equals("isInfMin")) {
                            keyValue = PrimaryKeyValue.INF_MIN;
                        } else if ("type".equals("isInfMax")) {
                            keyValue = PrimaryKeyValue.INF_MAX;
                        }
                        if (keyValue != null) {
                            rowPrimaryKey.addPrimaryKeyColumn(name, keyValue);
                        }
                    }
                }
            }
        });
        return rowPrimaryKey;
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        if (this.database != null) {
            DataCoreXmlUtils.writeXMLConnection(writer, this.database);
        }
        writer.startTAG(ATTR_TAG);
        writer.attr("tableName", tableName);
        writer.end();
        if (startRowPrimaryKey != null) {
            writer.startTAG("StartRowPrimaryKey");
            writeRowPrimaryKey(writer, startRowPrimaryKey);
            writer.end();
        }
        if (endRowPrimaryKey != null) {
            writer.startTAG("EndRowPrimaryKey");
            writeRowPrimaryKey(writer, endRowPrimaryKey);
            writer.end();
        }
        if (condition != null) {
            GeneralXMLTools.writeXMLable(writer, condition, OTSCondition.XML_TAG);
        }
    }

    private void writeRowPrimaryKey(XMLPrintWriter writer, RowPrimaryKey rowPrimaryKey) {
        for (Map.Entry<String, PrimaryKeyValue> entry : rowPrimaryKey.getPrimaryKey().entrySet()) {
            writer.startTAG("PrimaryKey");
            writer.attr("name", entry.getKey());
            PrimaryKeyValue keyValue = entry.getValue();
            if (keyValue.isInfMin()) {
                writer.attr("type","isInfMin");
            } else if (keyValue.isInfMax()) {
                writer.attr("type", "isInfMax");
            } else if (keyValue.getType() == PrimaryKeyType.STRING) {
                writer.attr("type", "string").attr("value", keyValue.asString());
            } else if (keyValue.getType() == PrimaryKeyType.INTEGER) {
                writer.attr("type", "long").attr("value", keyValue.asLong());
            }
            writer.end();
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OTSTableData cloned = (OTSTableData) super.clone();
        cloned.database = database;
        cloned.dbName = dbName;
        cloned.tableName = tableName;
        // richie:克隆暂时懒得弄
        return cloned;
    }
}