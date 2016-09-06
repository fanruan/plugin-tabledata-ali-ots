package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.OTSException;
import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.data.impl.Connection;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.fun.OTSConfigMangerProvider;
import com.fr.stable.CodeUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.List;

/**
 * Created by richie on 16/1/22.
 */
public class OTSDatabaseConnection extends AbstractDatabaseConnection {


    private String endPoint;
    private String accessId;
    private String accessKey;
    private String instanceName;
    private String options;

    public OTSDatabaseConnection() {

    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }


    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public OTSClient createOTSClient() {

        OTSConfigMangerProvider config = OTSConfigManager.getProviderInstance();

        return new OTSClient(endPoint, accessId, accessKey, instanceName, config.createConfiguration());
    }

    @Override
    public void testConnection() throws Exception {
        OTSClient client = createOTSClient();
        try {
            client.listTable();
        } catch (Exception e) {
            throw new Exception("Connect Failed");
        } finally {
            client.shutdown();
        }
    }

    @Override
    public java.sql.Connection createConnection() throws Exception {
        return null;
    }

    @Override
    public String connectMessage(boolean status) {
        if (status) {
            return Inter.getLocText("Datasource-Connection_successfully") + "!";
        } else {
            return Inter.getLocText("Datasource-Connection_failed") + "!";
        }
    }

    @Override
    public void addConnection(List<String> list, String connectionName, Class<? extends Connection>[] acceptTypes) {
        for (Class<? extends com.fr.data.impl.Connection> accept : acceptTypes) {
            if (StableUtils.classInstanceOf(getClass(), accept)) {
                list.add(connectionName);
                break;
            }
        }
    }

    @Override
    public String getDriver() {
        return null;
    }

    @Override
    public String getOriginalCharsetName() {
        return null;
    }

    @Override
    public void setOriginalCharsetName(String s) {

    }

    @Override
    public String getNewCharsetName() {
        return null;
    }

    @Override
    public void setNewCharsetName(String s) {

    }

    @Override
    public void readXML(XMLableReader reader) {
        super.readXML(reader);
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("Attr".equals(tagName)) {
                endPoint = reader.getAttrAsString("endPoint", StringUtils.EMPTY);
                accessId = reader.getAttrAsString("accessId", StringUtils.EMPTY);
                accessKey = reader.getAttrAsString("accessKey", StringUtils.EMPTY);
                instanceName = reader.getAttrAsString("instanceName", StringUtils.EMPTY);
                options = reader.getAttrAsString("options", StringUtils.EMPTY);
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        writer.startTAG("Attr");
        writer.attr("endPoint", endPoint);
        writer.attr("accessId", accessId);
        writer.attr("accessKey", accessKey);
        writer.attr("instanceName", instanceName);
        if (StringUtils.isNotEmpty(options)) {
            writer.attr("options", options);
        }
        writer.end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OTSDatabaseConnection cloned = (OTSDatabaseConnection) super.clone();
        cloned.endPoint = endPoint;
        cloned.accessId = accessId;
        cloned.accessKey = accessKey;
        cloned.instanceName = instanceName;
        cloned.options = options;
        return cloned;
    }
}