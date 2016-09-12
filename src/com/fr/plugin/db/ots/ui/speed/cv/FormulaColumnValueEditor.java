package com.fr.plugin.db.ots.ui.speed.cv;

import com.fr.base.Formula;
import com.fr.plugin.db.ots.core.condition.OTSColumnValue;
import com.fr.plugin.db.ots.ui.speed.core.FormulaEditor;

/**
 * Created by richie on 16/9/12.
 */
public class FormulaColumnValueEditor extends FormulaEditor<OTSColumnValue> {

    public FormulaColumnValueEditor(String name) {
        super(name);
    }

    @Override
    public OTSColumnValue createDataObject(String type, Formula formula) {
        return new OTSColumnValue("Formula", formula);
    }

    @Override
    public Formula getFormula(OTSColumnValue v) {
        return (Formula) v.getValue();
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof OTSColumnValue && ((OTSColumnValue)object).getType().equals("Formula");
    }
}
