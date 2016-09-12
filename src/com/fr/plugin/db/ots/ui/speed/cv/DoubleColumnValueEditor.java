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
public class DoubleColumnValueEditor extends NumberEditor<OTSColumnValue> {

    public DoubleColumnValueEditor() {
        super(new OTSColumnValue("Double", 0), "小数");
    }

    @Override
    public OTSColumnValue getValue() {
        return new OTSColumnValue("Double", numberField.getValue());
    }

    @Override
    public void setValue(OTSColumnValue value) {
        if (value != null) {
            numberField.setText(GeneralUtils.objectToString(value.getValue()));
        }
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_double.png");
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSColumnValue && ((OTSColumnValue) object).getType().equals("Double");
    }
}
