package com.fr.plugin.db.ots.ui.speed;

import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.aliyun.openservices.ots.model.condition.RelationalCondition;
import com.fr.data.DataConstants;
import com.fr.data.condition.JoinCondition;
import com.fr.data.condition.ListCondition;
import com.fr.design.condition.ContinuousTreeSelectionModel;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.general.data.Condition;
import com.fr.plugin.db.ots.core.condition.OTSCompositeCondition;
import com.fr.plugin.db.ots.core.condition.OTSCondition;
import com.fr.plugin.db.ots.ui.speed.cv.BooleanColumnValueEditor;
import com.fr.plugin.db.ots.ui.speed.cv.DoubleColumnValueEditor;
import com.fr.plugin.db.ots.ui.speed.cv.LongColumnValueEditor;
import com.fr.plugin.db.ots.ui.speed.cv.StringColumnValueEditor;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Created by richie on 16/9/6.
 */
public class OTSConditionEditTreePane extends BasicPane {
    private UITextField columnNameTextField;
    private UIDictionaryComboBox<RelationalCondition.CompareOperator> compareComboBox;
    private ValueEditorPane valueEditorPane;
    private UIRadioButton andRadioButton;
    private UIRadioButton orRadioButton;
    private UIRadioButton notRadioButton;
    private JTree conditionsTree;

    @Override
    protected String title4PopupWindow() {
        return "TreePane";
    }

    public OTSConditionEditTreePane() {
        setLayout(new BorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, p, f};
        double[] rowSize = {p, p};
        columnNameTextField = new UITextField(12);
        compareComboBox = new UIDictionaryComboBox<RelationalCondition.CompareOperator>(
                RelationalCondition.CompareOperator.values(),
                new String[]{
                        "等于", "不等于", "大于", "大于等于", "小于", "小于等于"
                });
        valueEditorPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{
                new StringColumnValueEditor(),
                new DoubleColumnValueEditor(),
                new LongColumnValueEditor(),
                new BooleanColumnValueEditor()
        });
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                {new UILabel("列名"), new UILabel("操作符"), new UILabel("值")},
                {columnNameTextField, compareComboBox, valueEditorPane}
        }, rowSize, columnSize);
        add(northPane, BorderLayout.NORTH);

        JPanel centerPane = new JPanel(new BorderLayout());
        add(centerPane, BorderLayout.CENTER);
        JPanel operatorPane = new JPanel(new BorderLayout());
        centerPane.add(operatorPane, BorderLayout.NORTH);
        operatorPane.add(createLogicPane(), BorderLayout.CENTER);
        operatorPane.add(createModifyPane(), BorderLayout.EAST);

        centerPane.add(createTreePane(), BorderLayout.CENTER);
    }

    private JComponent createLogicPane() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ButtonGroup group = new ButtonGroup();
        andRadioButton = new UIRadioButton(Inter.getLocText("Plugin-OTS_Logic_And"), true);
        panel.add(andRadioButton);
        group.add(andRadioButton);

        orRadioButton = new UIRadioButton(Inter.getLocText("Plugin-OTS_Logic_Or"));
        panel.add(orRadioButton);
        group.add(orRadioButton);

        notRadioButton = new UIRadioButton(Inter.getLocText("Plugin-OTS_Logic_Not"));
        panel.add(notRadioButton);
        group.add(notRadioButton);
        return panel;
    }

    private JComponent createModifyPane() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UIButton addButton = new UIButton(Inter.getLocText("Plugin-OTS_Add"));
        panel.add(addButton);
        UIButton modifyButton = new UIButton(Inter.getLocText("Plugin-OTS_Modify"));
        panel.add(modifyButton);
        return panel;
    }

    private JComponent createTreePane() {
        conditionsTree = new JTree(new DefaultTreeModel(new ExpandMutableTreeNode(new JoinCondition(DataConstants.AND, new ListCondition()))));
        conditionsTree.setRootVisible(false);
        conditionsTree.setCellRenderer(conditionsTreeCellRenderer);
        conditionsTree.setSelectionModel(new ContinuousTreeSelectionModel());
        conditionsTree.addTreeExpansionListener(treeExpansionListener);
        conditionsTree.setShowsRootHandles(true);
        return new JScrollPane(conditionsTree);
    }

    private void adjustParentListCondition(DefaultMutableTreeNode node) {

    }

    private DefaultTreeCellRenderer conditionsTreeCellRenderer = new DefaultTreeCellRenderer() {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) value;
            adjustParentListCondition(currentTreeNode);
            DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) currentTreeNode.getParent();

            this.setIcon(null);
            OTSCompositeCondition condition = (OTSCompositeCondition) currentTreeNode.getUserObject();
            StringBuilder sBuf = new StringBuilder();

            if (parentTreeNode != null && parentTreeNode.getFirstChild() != currentTreeNode) {
                if (condition.getLogicOperator() == CompositeCondition.LogicOperator.AND) {
                    sBuf.append("and ");
                } else if (condition.getLogicOperator() == CompositeCondition.LogicOperator.OR) {
                    sBuf.append("or  ");
                } else if (condition.getLogicOperator() == CompositeCondition.LogicOperator.NOT) {
                    sBuf.append("not ");
                }
            }

            if (condition != null) {
                sBuf.append(condition.toString());
            }
            this.setText(sBuf.toString());

            return this;
        }
    };

    TreeExpansionListener treeExpansionListener = new TreeExpansionListener() {

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath selectedTreePath = event.getPath();
            if (selectedTreePath == null) {
                return;
            }

            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            selectedTreeNode.setExpanded(true);
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            TreePath selectedTreePath = event.getPath();
            if (selectedTreePath == null) {
                return;
            }

            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            selectedTreeNode.setExpanded(false);
        }
    };

    public OTSCondition update() {
        return null;
    }

    public void populate(OTSCondition condition) {

    }

}
