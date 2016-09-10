package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;

import javax.swing.*;

/**
 * Created by richie on 16/9/5.
 */
public class LongColumnValueEditor extends NumberEditor<ColumnValue> {

    public LongColumnValueEditor() {
        super(ColumnValue.fromLong(0), "整数");
    }

    @Override
    public ColumnValue getValue() {
        return ColumnValue.fromLong((long) (int)numberField.getValue());
    }

    @Override
    public void setValue(ColumnValue value) {
        if (value != null) {
            numberField.setText(GeneralUtils.objectToString(value.asLong()));
        }
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_int.png");
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof ColumnValue && ((ColumnValue) object).getType() == ColumnType.INTEGER;
    }
}
