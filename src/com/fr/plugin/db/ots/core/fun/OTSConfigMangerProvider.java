package com.fr.plugin.db.ots.core.fun;

import com.aliyun.openservices.ots.ClientConfiguration;
import com.fr.stable.FCloneable;
import com.fr.stable.file.RemoteXMLFileManagerProvider;

/**
 * Created by richie on 16/9/6.
 */
public interface OTSConfigMangerProvider extends RemoteXMLFileManagerProvider, java.io.Serializable, FCloneable {

    ClientConfiguration createConfiguration();
}
