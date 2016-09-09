package com.fr.plugin.db.ots.ui.speed;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.condition.OTSCompositeCondition;
import com.fr.plugin.db.ots.core.condition.OTSCondition;
import com.fr.plugin.db.ots.core.condition.OTSRelationCondition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by richie on 16/9/5.
 */
public class OTSConditionEditPane extends BasicPane {

    private JPanel contentPane;
    private java.util.List<OTSConditionItemPane> paneList = new ArrayList<OTSConditionItemPane>();

    public OTSConditionEditPane() {
        setLayout(new BorderLayout());

        JPanel toolbarPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(toolbarPane, BorderLayout.NORTH);
        UIButton addButton = new UIButton(Inter.getLocText("Plugin-OTS_Add"));
        toolbarPane.add(addButton);

        UIButton removeButton = new UIButton(Inter.getLocText("Plugin-OTS_Remove"));
        toolbarPane.add(removeButton);


        JPanel centerPane = new JPanel(new BorderLayout());
        add(centerPane, BorderLayout.CENTER);

        JPanel northPane = new JPanel(new BorderLayout());
        centerPane.add(northPane, BorderLayout.NORTH);
        UILabel selectLabel = new UILabel(Inter.getLocText("Plugin-OTS_Select"), SwingConstants.CENTER);
        northPane.add(selectLabel, BorderLayout.WEST);
        JPanel titlePane = new JPanel(new GridLayout(1, 4));
        northPane.add(titlePane, BorderLayout.CENTER);
        titlePane.add(new UILabel("逻辑符", SwingConstants.CENTER));
        titlePane.add(new UILabel("列名", SwingConstants.CENTER));
        titlePane.add(new UILabel("比较符", SwingConstants.CENTER));
        titlePane.add(new UILabel("值", SwingConstants.CENTER));

        contentPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPane.add(contentPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OTSConditionItemPane itemPane = new OTSConditionItemPane();
                addItemPane(itemPane);
                LayoutUtils.layoutContainer(contentPane);
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<OTSConditionItemPane> iterator = paneList.iterator();
                while (iterator.hasNext()) {
                    OTSConditionItemPane itemPane = iterator.next();
                    if (itemPane.isSelected()) {
                        iterator.remove();
                        contentPane.remove(itemPane);
                    }
                }
                contentPane.revalidate();
                contentPane.repaint();
            }
        });


    }

    private void addItemPane(OTSConditionItemPane itemPane) {
        contentPane.add(itemPane);
        paneList.add(itemPane);
    }

    public OTSCondition update() {
        int count = paneList.size();
        if (count == 0) {
            return null;
        } else if (count == 1) {
            return paneList.get(0).update();
        } else {
            OTSCompositeCondition cc = new OTSCompositeCondition();
            for (OTSConditionItemPane itemPane : paneList) {
                cc.addCondition(itemPane.update());
            }
            return cc;
        }
    }

    public void populate(OTSCondition condition) {
        if (condition instanceof OTSRelationCondition) {
            OTSConditionItemPane itemPane = new OTSConditionItemPane();
            itemPane.populate((OTSRelationCondition) condition);
            addItemPane(itemPane);
        } else if (condition instanceof OTSCompositeCondition) {
            OTSCompositeCondition cc = (OTSCompositeCondition)condition;
            for (int i = 0, len = cc.getConditionSize(); i < len; i ++) {
                OTSConditionItemPane itemPane = new OTSConditionItemPane();
                if (i == 0) {
                    itemPane.setHead(true);
                }
                itemPane.populate((OTSRelationCondition) cc.getCondition(i));
                addItemPane(itemPane);
            }
        }
        LayoutUtils.layoutContainer(contentPane);
    }

    @Override
    protected String title4PopupWindow() {
        return "Edit";
    }
}
