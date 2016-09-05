package com.fr.plugin.db.ots.ui.speed.cv;

import com.aliyun.openservices.ots.model.ColumnValue;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public abstract class NumberEditor extends Editor<ColumnValue> {
    private static final long serialVersionUID = 1L;
    protected UINumberField numberField;

    protected String oldValue = StringUtils.EMPTY;


    public NumberEditor() {
        this(null, StringUtils.EMPTY);
    }


    public NumberEditor(ColumnValue value, String name) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        numberField = new UINumberField();
        this.add(numberField, BorderLayout.CENTER);
        this.numberField.addKeyListener(textKeyListener);
        this.numberField.setHorizontalAlignment(UITextField.RIGHT);
        this.setValue(value);
        this.setName(name);
    }

    /**
     * 给numberField加键盘事件
     *
     * @param keylistener
     */
    public void addKeyListner2EditingComp(KeyListener keylistener) {
        numberField.addKeyListener(keylistener);
    }

    public void setColumns(int columns) {
        this.numberField.setColumns(columns);
    }

    /**
     * Returns the horizontal alignment of the CellEditor. Valid keys are:
     * <ul>
     * <li><code>UITextField.LEFT</code>
     * <li><code>UITextField.CENTER</code>
     * <li><code>UITextField.RIGHT</code>
     * <li><code>UITextField.LEADING</code>
     * <li><code>UITextField.TRAILING</code>
     * </ul>
     */
    public int getHorizontalAlignment() {
        return this.numberField.getHorizontalAlignment();
    }

    /**
     * Sets the horizontal alignment of the CellEditor. Valid keys are:
     * <ul>
     * <li><code>UITextField.LEFT</code>
     * <li><code>UITextField.CENTER</code>
     * <li><code>UITextField.RIGHT</code>
     * <li><code>UITextField.LEADING</code>
     * <li><code>UITextField.TRAILING</code>
     * </ul>
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.numberField.setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Sets whether or not this component is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.numberField.setEnabled(enabled);
    }

    public void selected() {
        this.requestFocus();
    }

    public String getIconName() {
        return "type_double";
    }

    /**
     * Request focus
     */
    public void requestFocus() {
        this.numberField.requestFocus();
    }

    KeyListener textKeyListener = new KeyAdapter() {

        public void keyReleased(KeyEvent evt) {
            int code = evt.getKeyCode();

            if (code == KeyEvent.VK_ESCAPE) {
                numberField.setText(oldValue);
            }
            if (code == KeyEvent.VK_ENTER) {
                fireEditingStopped();
            } else {
                fireStateChanged();
            }
        }
    };

}