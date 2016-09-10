package com.fr.plugin.db.ots.ui.speed.pv;

import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.db.ots.core.primary.OTSPrimaryKeyValue;
import com.fr.plugin.db.ots.ui.speed.cv.NumberEditor;

import javax.swing.*;

/**
 * Created by richie on 16/9/5.
 */
public class LongPrimaryKeyValueEditor extends NumberEditor<OTSPrimaryKeyValue> {

    public LongPrimaryKeyValueEditor() {
        super(new OTSPrimaryKeyValue("Long", 0L), "整数");
    }

    @Override
    public OTSPrimaryKeyValue getValue() {
        return new OTSPrimaryKeyValue("Long", (long) (int)numberField.getValue());
    }

    @Override
    public void setValue(OTSPrimaryKeyValue value) {
        if (value != null) {
            numberField.setText(GeneralUtils.objectToString(value.getValue()));
        }
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_int.png");
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSPrimaryKeyValue && ((OTSPrimaryKeyValue) object).getType().equals("Long");
    }
}
