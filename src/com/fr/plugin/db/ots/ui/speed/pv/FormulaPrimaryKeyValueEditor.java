package com.fr.plugin.db.ots.ui.speed.pv;

import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.editor.Editor;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralUtils;
import com.fr.plugin.db.ots.core.primary.OTSPrimaryKeyValue;
import com.fr.plugin.db.ots.ui.speed.core.FormulaEditor;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormulaPrimaryKeyValueEditor extends FormulaEditor<OTSPrimaryKeyValue> {

    public FormulaPrimaryKeyValueEditor(String name) {
        this(name, null);
    }

    public FormulaPrimaryKeyValueEditor(String name, Formula formula) {
        super(name, formula);
    }

    @Override
    public OTSPrimaryKeyValue createDataObject(String type, Formula formula) {
        return new OTSPrimaryKeyValue("Formula", formula);
    }

    @Override
    public Formula getFormula(OTSPrimaryKeyValue key) {
        return (Formula) key.getValue();
    }


    /**
     * object是否是公司类型对象
     *
     * @param object 需判断的对象
     * @return 是公式类型则返回true
     */
    public boolean accept(Object object) {
        return object instanceof OTSPrimaryKeyValue && ((OTSPrimaryKeyValue)object).getType().equals("Formula");
    }


}
