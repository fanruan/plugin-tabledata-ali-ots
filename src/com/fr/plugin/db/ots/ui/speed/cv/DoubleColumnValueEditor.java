package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnType;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;

import javax.swing.*;

/**
 * Created by richie on 16/9/5.
 */
public class DoubleColumnValueEditor extends NumberEditor<ColumnValue> {

    public DoubleColumnValueEditor() {
        super(ColumnValue.fromDouble(0), "小数");
    }

    @Override
    public ColumnValue getValue() {
        return ColumnValue.fromDouble(numberField.getValue());
    }

    @Override
    public void setValue(ColumnValue value) {
        if (value != null) {
            numberField.setText(GeneralUtils.objectToString(value.asDouble()));
        }
    }

    @Override
    public Icon getIcon() {
        return IOUtils.readIcon("com/fr/plugin/db/ots/images/type_double.png");
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof ColumnValue && ((ColumnValue) object).getType() == ColumnType.DOUBLE;
    }
}
