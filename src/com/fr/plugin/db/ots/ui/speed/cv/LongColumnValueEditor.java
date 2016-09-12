package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.db.ots.core.condition.OTSColumnValue;

import javax.swing.*;

/**
 * Created by richie on 16/9/5.
 */
public class LongColumnValueEditor extends NumberEditor<OTSColumnValue> {

    public LongColumnValueEditor() {
        super(new OTSColumnValue("Long", 0L), "整数");
    }

    @Override
    public OTSColumnValue getValue() {
        return new OTSColumnValue("Long", (long) (int)numberField.getValue());
    }

    @Override
    public void setValue(OTSColumnValue value) {
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
        return object instanceof OTSColumnValue && ((OTSColumnValue) object).getType().equals("Long");
    }
}
