package com.fr.plugin.db.ots;

import com.fr.data.impl.Connection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractConnectionProvider;
import com.fr.plugin.db.ots.core.OTSDatabaseConnection;
import com.fr.plugin.db.ots.ui.OTSConnectionPane;

/**
 * Created by richie on 16/1/22.
 */
public class OTSConnectionImpl extends AbstractConnectionProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String nameForConnection() {
        return "OTS";
    }

    @Override
    public String iconPathForConnection() {
        return "/com/fr/plugin/db/ots/images/ots.png";
    }

    @Override
    public Class<? extends Connection> classForConnection() {
        return OTSDatabaseConnection.class;
    }

    @Override
    public Class<? extends BasicBeanPane<? extends Connection>> appearanceForConnection() {
        return OTSConnectionPane.class;
    }
}