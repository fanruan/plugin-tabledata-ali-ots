package com.fr.plugin.db.ots.core.condition;

import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by richie on 16/9/5.
 */
public class AbstractOTSCondtion implements OTSCondition {
    @Override
    public void readXML(XMLableReader xmLableReader) {

    }

    @Override
    public void writeXML(XMLPrintWriter xmlPrintWriter) {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
