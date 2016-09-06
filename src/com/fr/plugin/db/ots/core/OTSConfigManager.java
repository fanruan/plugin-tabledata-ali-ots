package com.fr.plugin.db.ots.core;

import com.aliyun.openservices.ots.ClientConfiguration;
import com.fr.base.ConfigManager;
import com.fr.cluster.rpc.RPC;
import com.fr.file.BaseClusterHelper;
import com.fr.file.XMLFileManager;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.plugin.db.ots.core.fun.OTSConfigMangerProvider;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;

import java.io.InputStream;

/**
 * Created by richie on 16/9/6.
 */
public class OTSConfigManager extends XMLFileManager implements OTSConfigMangerProvider {

    private static OTSConfigMangerProvider configManager = null;

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            public void envChanged() {
                OTSConfigManager.envChanged();
            }
        });
    }

    private synchronized static void envChanged() {
        configManager = null;
    }

    public synchronized static OTSConfigManager getInstance() {
        return (OTSConfigManager) getProviderInstance();
    }

    public synchronized static OTSConfigMangerProvider getProviderInstance() {
        if (configManager == null) {
            if (isClusterMember()) {
                return configManager;
            }
            configManager.readXMLFile();
        }
        return configManager;
    }

    private synchronized static boolean isClusterMember() {
        switch (BaseClusterHelper.getClusterState()) {
            case LEADER:
                configManager = new OTSConfigManager();
                RPC.registerSkeleton(configManager);
                return false;
            case MEMBER:
                String ip = BaseClusterHelper.getMainServiceIP();
                configManager = (OTSConfigMangerProvider) RPC.getProxy(ConfigManager.class, ip);
                return true;
            default:
                configManager = new OTSConfigManager();
                break;
        }
        return false;
    }

    @Override
    public void readFromInputStream(InputStream input) throws Exception {
        // 服务器端新建一个对象
        OTSConfigManager manager = new OTSConfigManager();
        // 从客户端传过来的inputstream中读取对象属性
        XMLTools.readInputStreamXML(manager, input);
        // 赋值给当前服务器端对象
        configManager = manager;
        // 服务器端保存到本地xml中
        GeneralContext.getEnvProvider().writeResource(configManager);
    }

    private boolean enableConfiguration = false;

    private int proxyPort;
    private String proxyHost;
    private String proxyUsername;
    private String proxyPassword;
    private String proxyDomain;
    private String proxyWorkstation;
    private String userAgent;

    public boolean shouldUseConfiguration() {
        return false;
    }

    public ClientConfiguration createConfiguration() {
        if (enableConfiguration) {
            return new ClientConfiguration();
        } else {
            return null;
        }
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyDomain() {
        return proxyDomain;
    }

    public void setProxyDomain(String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }

    public String getProxyWorkstation() {
        return proxyWorkstation;
    }

    public void setProxyWorkstation(String proxyWorkstation) {
        this.proxyWorkstation = proxyWorkstation;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean writeResource() throws Exception {
        return false;
    }

    @Override
    public String fileName() {
        return "ots.xml";
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            enableConfiguration = reader.getAttrAsBoolean("enable", false);
        } else if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if ("ProxyPort".equals(tagName)) {
                proxyPort = GeneralUtils.objectToNumber(reader.getElementValue()).intValue();
            } else if ("ProxyHost".equals(tagName)) {
                proxyHost = reader.getElementValue();
            } else if ("ProxyUsername".equals(tagName)) {
                proxyUsername = reader.getElementValue();
            } else if ("ProxyPassword".equals(tagName)) {
                proxyPassword = reader.getElementValue();
            } else if ("ProxyDomain".equals(tagName)) {
                proxyDomain = reader.getElementValue();
            } else if ("ProxyWorkstation".equals(tagName)) {
                proxyWorkstation = reader.getElementValue();
            } else if ("UserAgent".equals(tagName)) {
                userAgent = reader.getElementValue();
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("Config");
        writer.attr("enable", enableConfiguration);
        writer.startTAG("ProxyPort").textNode(proxyPort + "").end();
        writer.startTAG("ProxyHost").textNode(proxyHost).end();
        writer.startTAG("ProxyUsername").textNode(proxyUsername).end();
        writer.startTAG("ProxyPassword").textNode(proxyPassword).end();
        writer.startTAG("ProxyDomain").textNode(proxyDomain).end();
        writer.startTAG("ProxyWorkstation").textNode(proxyWorkstation).end();
        writer.startTAG("UserAgent").textNode(userAgent).end();
        writer.end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
