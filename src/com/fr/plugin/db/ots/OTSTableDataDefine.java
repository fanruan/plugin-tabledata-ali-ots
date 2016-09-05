package com.fr.plugin.db.ots;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.fun.ServerTableDataDefineProvider;
import com.fr.design.fun.impl.AbstractTableDataDefineProvider;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.OTSTableData;
import com.fr.plugin.db.ots.ui.OTSTableDataPane;

/**
 * Created by richie on 16/1/22.
 */
public class OTSTableDataDefine extends AbstractTableDataDefineProvider implements ServerTableDataDefineProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public Class<? extends TableData> classForTableData() {
        return OTSTableData.class;
    }

    @Override
    public Class<? extends TableData> classForInitTableData() {
        return OTSTableData.class;
    }

    @Override
    public Class<? extends AbstractTableDataPane> appearanceForTableData() {
        return OTSTableDataPane.class;
    }

    @Override
    public String nameForTableData() {
        return Inter.getLocText("Plugin-OTS_Table_Data");
    }

    @Override
    public String prefixForTableData() {
        return "ots";
    }

    @Override
    public String iconPathForTableData() {
        return "/com/fr/plugin/db/ots/images/ots.png";
    }
}