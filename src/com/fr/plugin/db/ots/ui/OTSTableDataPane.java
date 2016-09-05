package com.fr.plugin.db.ots.ui;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.ParameterHelper;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.menu.ToolBarDef;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.db.ots.core.OTSTableData;
import com.fr.plugin.db.ots.ui.event.DataLoadedListener;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class OTSTableDataPane extends AbstractTableDataPane<OTSTableData> {
    private static final String PREVIEW_BUTTON = Inter.getLocText("Preview");
    private static final String REFRESH_BUTTON = Inter.getLocText("Refresh");

    private OTSConnectionChosePane chosePane;

    private UITableEditorPane<ParameterProvider> editorPane;

    private OTSQueryPane queryPane;


    public OTSTableDataPane() {
        this.setLayout(new BorderLayout(4, 4));


        Box box = new Box(BoxLayout.Y_AXIS);


        queryPane = new OTSQueryPane();
        JPanel northPane = new JPanel(new BorderLayout(4, 4));
        JToolBar editToolBar = createToolBar();
        northPane.add(editToolBar, BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));


        ParameterTableModel model = new ParameterTableModel() {
            @Override
            public UITableEditAction[] createAction() {
                return ArrayUtils.add(super.createDBTableAction(), new RefreshAction());
            }
        };
        editorPane = new UITableEditorPane<ParameterProvider>(model);


        box.add(northPane);
        box.add(queryPane);

        box.add(editorPane);

        JPanel sqlSplitPane = new JPanel(new BorderLayout(4, 4));
        sqlSplitPane.add(box, BorderLayout.CENTER);

        chosePane = new OTSConnectionChosePane();
        chosePane.setPreferredSize(new Dimension(200, 200));
        sqlSplitPane.add(chosePane, BorderLayout.WEST);

        this.add(sqlSplitPane, BorderLayout.CENTER);

        chosePane.addDataLoadedListener(new DataLoadedListener() {
            @Override
            public void fireEvent(String[] data) {
                queryPane.loadDBNames(data);
            }
        });
    }

    private boolean isPreviewOrRefreshButton (FocusEvent e) {
        if (e.getOppositeComponent() != null) {
            String name = e.getOppositeComponent().getName();
            return ComparatorUtils.equals(name, PREVIEW_BUTTON) || ComparatorUtils.equals(name, REFRESH_BUTTON);
        }
        return false;
    }
    private JToolBar createToolBar() {
        ToolBarDef toolBarDef = new ToolBarDef();
        toolBarDef.addShortCut(new PreviewAction());
        UIToolbar editToolBar = ToolBarDef.createJToolBar();
        toolBarDef.updateToolBar(editToolBar);
        return editToolBar;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("DS-Database_Query");
    }

    private void refresh() {
//        String[] paramTexts = new String[]{queryPane.getQuery(), queryPane.getFilter(), queryPane.getSort()};
//
//        List<ParameterProvider> existParameterList = editorPane.update();
//        Parameter[] ps = existParameterList == null ? new Parameter[0] : existParameterList.toArray(new Parameter[existParameterList.size()]);
//
//        editorPane.populate(ParameterHelper.analyzeAndUnionSameParameters(paramTexts, ps));
    }


    private void checkParameter() {
//        String[] paramTexts = new String[]{queryPane.getQuery(), queryPane.getFilter(), queryPane.getSort()};

        Parameter[] parameters = ParameterHelper.analyze4Parameters("", false);

        if (parameters.length < 1 && editorPane.update().size() < 1) {
            return;
        }
        boolean isIn = true;
        List<ParameterProvider> list = editorPane.update();
        List<String> name = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            name.add(list.get(i).getName());
        }
        for (int i = 0; i < parameters.length; i++) {
            if (!name.contains(parameters[i].getName())) {
                isIn = false;
                break;
            }
        }
        if (list.size() == parameters.length && isIn) {
            return;
        }
        // bug:34175  删了是否刷新对话框， 均直接刷新
        refresh();
    }

    @Override
    public void populateBean(OTSTableData tableData) {
        if (tableData == null) {
            return;
        }
        Calculator c = Calculator.createCalculator();
        editorPane.populate(tableData.getParameters(c));

        chosePane.populateConnection(tableData.getDatabase());

        queryPane.populate(tableData);
    }


    @Override
    public OTSTableData updateBean() {
        OTSTableData tableData = new OTSTableData();

        String connectionName = chosePane.getSelectMongoConnectionName();
        if (StringUtils.isNotEmpty(connectionName)) {
            tableData.setDatabase(new NameDatabaseConnection(connectionName));
        }

        List<ParameterProvider> parameterList = editorPane.update();
        ParameterProvider[] parameters = parameterList.toArray(new ParameterProvider[parameterList.size()]);
        tableData.setParameters(parameters);

        queryPane.update(tableData);


        return  tableData;
    }

    private class PreviewAction extends UpdateAction {
        public PreviewAction() {
            this.setName(PREVIEW_BUTTON);
            this.setMnemonic('P');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/preview.png"));
        }

        public void actionPerformed(ActionEvent evt) {
            checkParameter();
            PreviewTablePane.previewTableData(OTSTableDataPane.this.updateBean());
        }
    }


    protected class RefreshAction extends UITableEditAction {
        public RefreshAction() {
            this.setName(REFRESH_BUTTON);
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        }

        public void actionPerformed(ActionEvent e) {
            refresh();
        }

        @Override
        public void checkEnabled() {
        }
    }
}