package com.fr.plugin.db.ots.ui.speed.core;

import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.editor.Editor;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class FormulaEditor<T> extends Editor<T> {
    private Formula formula = new Formula(StringUtils.EMPTY);
    private UITextField currentTextField;
    private FormulaEditor.ShowPaneListener listener = new ShowPaneListener();


    public FormulaEditor(String name) {
        this(name, null);
    }

    public FormulaEditor(String name, Formula formula) {
        if (formula != null) {
            this.formula = formula;
        }
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel editPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        currentTextField = new UITextField(28);
        currentTextField.setText(this.formula.getContent());

        editPane.add(currentTextField, BorderLayout.CENTER);
        currentTextField.setEditable(false);
        currentTextField.addMouseListener(listener);
        this.add(editPane, BorderLayout.CENTER);
        this.setName(name);
    }

    private class ShowPaneListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (currentTextField.isEnabled()) {
                showFormulaPane();
            }
        }
    }


    /**
     * 选中时弹出公式编辑框
     */
    public void selected() {
        showFormulaPane();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        currentTextField.setEnabled(enabled);
    }


    protected void showFormulaPane() {
        final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
        formulaPane.populate(formula);
        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(FormulaEditor.this), new DialogActionAdapter() {

            @Override
            public void doOk() {
                formula = formulaPane.update();
                setValue(createDataObject("Formula", formula));
                fireStateChanged();
            }
        }).setVisible(true);
    }

    public abstract T createDataObject(String type, Formula formula);

    public abstract Formula getFormula(T v);

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public T getValue() {
        if (formula != null && "=".equals(formula.getContent())) {
            return null;
        }
        return createDataObject("Formula", formula);
    }

    /**
     * Set the value to the CellEditor.
     */
    @Override
    public void setValue(T value) {
        if (value != null)  {
            this.formula = getFormula(value);
            currentTextField.setText(GeneralUtils.objectToString(formula));
        }
    }

    public String getIconName() {
        return "type_formula";
    }


    /**
     * 重置
     */
    public void reset() {
        currentTextField.setText("=");
        formula = new Formula(StringUtils.EMPTY);
    }

    /**
     * 清楚数据
     */
    public void clearData() {
        reset();
    }

}

