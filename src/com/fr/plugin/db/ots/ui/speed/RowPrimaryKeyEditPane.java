package com.fr.plugin.db.ots.ui.speed;

import com.aliyun.openservices.ots.model.PrimaryKeyValue;
import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by richie on 16/9/5.
 */
public class RowPrimaryKeyEditPane extends BasicPane {

    private java.util.List<RowPrimaryKeyItemPane> listPane;
    private JPanel contentPane;

    public RowPrimaryKeyEditPane() {
        setLayout(new BorderLayout());

        JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(northPane, BorderLayout.NORTH);

        UIButton addButton = new UIButton(Inter.getLocText("Plugin-OTS_Add"));
        northPane.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RowPrimaryKeyItemPane itemPane = new RowPrimaryKeyItemPane();
                addItemPane(itemPane);
                LayoutUtils.layoutContainer(contentPane);
            }
        });


        UIButton removeButton = new UIButton(Inter.getLocText("Plugin-OTS_Remove"));
        northPane.add(removeButton);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<RowPrimaryKeyItemPane> iterator = listPane.iterator();
                while (iterator.hasNext()) {
                    RowPrimaryKeyItemPane itemPane = iterator.next();
                    if (itemPane.isSelected()) {
                        iterator.remove();
                        contentPane.remove(itemPane);
                    }
                }
                LayoutUtils.layoutContainer(contentPane);
            }
        });

        contentPane = new JPanel();
        add(contentPane, BorderLayout.CENTER);
        contentPane.setLayout(new FlowLayout());

        listPane = new ArrayList<RowPrimaryKeyItemPane>();
    }

    public RowPrimaryKey update() {
        RowPrimaryKey rowPrimaryKey = new RowPrimaryKey();
        for (RowPrimaryKeyItemPane itemPane : listPane) {
            rowPrimaryKey.addPrimaryKeyColumn(itemPane.getKeyName(), itemPane.getPrimaryKeyValue());
        }
        return rowPrimaryKey;
    }

    public void populate(RowPrimaryKey rowPrimaryKey) {
        contentPane.removeAll();
        listPane.clear();
        if (rowPrimaryKey != null) {
            for (Map.Entry<String, PrimaryKeyValue> entry : rowPrimaryKey.getPrimaryKey().entrySet()) {
                RowPrimaryKeyItemPane itemPane = new RowPrimaryKeyItemPane(entry.getKey(), entry.getValue());
                addItemPane(itemPane);
            }
            LayoutUtils.layoutContainer(this);
        }
    }

    private void addItemPane(RowPrimaryKeyItemPane itemPane) {
        contentPane.add(itemPane);
        listPane.add(itemPane);
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Plugin-OTS_Edit");
    }
}
