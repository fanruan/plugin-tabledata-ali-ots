package com.fr.plugin.db.ots;

import com.fr.plugin.db.ots.core.OTSConstants;
import com.fr.stable.fun.Authorize;
import com.fr.stable.fun.impl.AbstractLocaleFinder;

/**
 * Created by richie on 16/1/22.
 */
@Authorize(callSignKey = OTSConstants.PLUGIN_ID)
public class OTSLocaleFinder extends AbstractLocaleFinder {
    @Override
    public String find() {
        return "com/fr/plugin/db/ots/locale/ots";
    }
}