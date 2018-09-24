package com.moonkin.ui;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.util.Config;
import com.moonkin.util.SystemUtil;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.lang3.StringUtils;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * 初始化类
 * @author xuduo
 * @date 2018-09-23
 */
public class Init {

    private static final Log logger = LogFactory.get();

    /**
     * 配置文件管理器对象
     */
    public static Config configer = Config.getInstance();

    /**
     * 设置全局字体
     */
    public static void initGlobalFont() {

        // 低分辨率屏幕字号初始化
        String lowDpiKey = "lowDpiInit";
        // 得到屏幕的尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.width <= 1366 && StringUtils.isEmpty(configer.getProps(lowDpiKey))) {
            configer.setFontSize(15);
            configer.setProps(lowDpiKey, "true");
            configer.save();
        }

        Font fnt = new Font(configer.getFont(), Font.PLAIN, configer.getFontSize());
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    /**
     * 其他初始化
     */
    public static void initOthers() {
        // 设置滚动条速度
        MainWindow.mainWindow.getSettingScrollPane().getVerticalScrollBar().setUnitIncrement(15);
        MainWindow.mainWindow.getSettingScrollPane().getVerticalScrollBar().setDoubleBuffered(true);
    }

    /**
     * 初始化look and feel
     */
    public static void initTheme() {

        try {
            switch (configer.getTheme()) {
                case "系统默认":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case "Windows":
                    UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
                    break;
                case "Metal":
                    UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
                    break;
                case "Motif":
                    UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());
                    break;
                default:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    /**
     * 初始化消息tab
     */
    public static void initMsgTab(String selectedMsgName) {
        // 初始化，清空所有相关的输入框内容
        MainWindow.mainWindow.setMsgTypeComboBox("");
        MainWindow.mainWindow.setMsgTemplateIdTextField("");
        MainWindow.mainWindow.setMsgTemplateUrlTextField("");
        MainWindow.mainWindow.setMsgKefuMsgTypeComboBox("");
        MainWindow.mainWindow.setMsgKefuMsgTitleTextField("");
        MainWindow.mainWindow.setMsgKefuPicUrlTextField("");
        MainWindow.mainWindow.setMsgKefuDescTextField("");
        MainWindow.mainWindow.setMsgKefuUrlTextField("");
        MainWindow.mainWindow.setMsgTemplateMiniAppidTextField("");
        MainWindow.mainWindow.setMsgTemplateMiniPagePathTextField("");
        MainWindow.mainWindow.setMsgTemplateKeyWordTextField("");

        String msgName;
        if (StringUtils.isEmpty(selectedMsgName)) {
            msgName = configer.getMsgName();
        } else {
            msgName = selectedMsgName;
        }

        MainWindow.mainWindow.setMsgNameField(msgName);
        MainWindow.mainWindow.setPreviewUserField(configer.getPreviewUser());

        Map<String, String[]> msgMap = new HashMap<>(16);

        if (msgMap != null && msgMap.size() != 0) {
            if (msgMap.containsKey(msgName)) {
                String[] msgDataArray = msgMap.get(msgName);
                String msgType = msgDataArray[1];
                MainWindow.mainWindow.setMsgTypeComboBox(msgType);
                MainWindow.mainWindow.setMsgTemplateIdTextField(msgDataArray[2]);
                MainWindow.mainWindow.setMsgTemplateUrlTextField(msgDataArray[3]);
                String kefuMsgType = msgDataArray[4];
                MainWindow.mainWindow.setMsgKefuMsgTypeComboBox(kefuMsgType);
                MainWindow.mainWindow.setMsgKefuMsgTitleTextField(msgDataArray[5]);
                MainWindow.mainWindow.setMsgKefuPicUrlTextField(msgDataArray[6]);
                MainWindow.mainWindow.setMsgKefuDescTextField(msgDataArray[7]);
                MainWindow.mainWindow.setMsgKefuUrlTextField(msgDataArray[8]);
                if (msgDataArray.length > 10) {
                    MainWindow.mainWindow.setMsgTemplateKeyWordTextField(msgDataArray[11]);
                }
                if (msgDataArray.length > 9) {
                    MainWindow.mainWindow.setMsgTemplateMiniAppidTextField(msgDataArray[9]);
                    MainWindow.mainWindow.setMsgTemplateMiniPagePathTextField(msgDataArray[10]);
                }
                switchMsgType(msgType);
                switchKefuMsgType(kefuMsgType);

                // 模板消息Data表
                List<String[]> list = new ArrayList<>();
                String[] headerNames = {"Name", "Value", "Color", "操作"};
                Object[][] cellData = new String[list.size()][headerNames.length];
                for (int i = 0; i < list.size(); i++) {
                    cellData[i] = list.get(i);
                }
                DefaultTableModel model = new DefaultTableModel(cellData, headerNames);
                MainWindow.mainWindow.getTemplateMsgDataTable().setModel(model);
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().
                        getColumn(headerNames.length - 1).
                        setCellRenderer(new ButtonColumn(MainWindow.mainWindow.getTemplateMsgDataTable(), headerNames.length - 1));
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().
                        getColumn(headerNames.length - 1).
                        setCellEditor(new ButtonColumn(MainWindow.mainWindow.getTemplateMsgDataTable(), headerNames.length - 1));

                // 设置列宽
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(0).setPreferredWidth(150);
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(0).setMaxWidth(150);
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(2).setPreferredWidth(130);
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(2).setMaxWidth(130);
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(3).setPreferredWidth(130);
                MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(3).setMaxWidth(130);

                MainWindow.mainWindow.getTemplateMsgDataTable().updateUI();
            }
        } else {
            switchMsgType(MainWindow.mainWindow.getMsgTypeComboBox().getSelectedItem().toString());
        }
    }

    /**
     * 根据消息类型转换界面显示
     *
     * @param msgType
     */
    public static void switchMsgType(String msgType) {
        switch (msgType) {
            case "模板消息":
                MainWindow.mainWindow.getKefuMsgPanel().setVisible(false);
                MainWindow.mainWindow.getTemplateMsgPanel().setVisible(true);
                MainWindow.mainWindow.getTemplateUrlLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateUrlTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramAppidLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateMiniAppidTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramPagePathLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateMiniPagePathTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel1().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel2().setVisible(true);
                MainWindow.mainWindow.getTemplateMsgColorLabel().setVisible(true);
                MainWindow.mainWindow.getTemplateDataColorTextField().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateKeyWordTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateKeyWordLabel().setVisible(false);
                MainWindow.mainWindow.getPreviewMemberLabel().setText("预览消息用户openid（以半角分号分隔）");

                break;
            case "模板消息-小程序":
                MainWindow.mainWindow.getKefuMsgPanel().setVisible(false);
                MainWindow.mainWindow.getTemplateMsgPanel().setVisible(true);
                MainWindow.mainWindow.getTemplateUrlLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateUrlTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramAppidLabel().setVisible(false);
                MainWindow.mainWindow.getMsgTemplateMiniAppidTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramPagePathLabel().setVisible(false);
                MainWindow.mainWindow.getMsgTemplateMiniPagePathTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel1().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel2().setVisible(false);
                MainWindow.mainWindow.getTemplateMsgColorLabel().setVisible(true);
                MainWindow.mainWindow.getTemplateDataColorTextField().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateKeyWordTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateKeyWordLabel().setVisible(true);
                MainWindow.mainWindow.getPreviewMemberLabel().setText("预览消息用户openid（以半角分号分隔）");

                break;
            case "客服消息":
                MainWindow.mainWindow.getKefuMsgPanel().setVisible(true);
                MainWindow.mainWindow.getTemplateMsgPanel().setVisible(false);
                break;
            case "客服消息优先":
                MainWindow.mainWindow.getKefuMsgPanel().setVisible(true);
                MainWindow.mainWindow.getTemplateMsgPanel().setVisible(true);
                MainWindow.mainWindow.getTemplateUrlLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateUrlTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramAppidLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateMiniAppidTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramPagePathLabel().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateMiniPagePathTextField().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel1().setVisible(true);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel2().setVisible(true);
                MainWindow.mainWindow.getTemplateMsgColorLabel().setVisible(true);
                MainWindow.mainWindow.getTemplateDataColorTextField().setVisible(true);
                MainWindow.mainWindow.getMsgTemplateKeyWordTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateKeyWordLabel().setVisible(false);
                MainWindow.mainWindow.getPreviewMemberLabel().setText("预览消息用户openid（以半角分号分隔）");
                break;
            case "阿里云短信":
            case "腾讯云短信":
            case "阿里大于模板短信":
                MainWindow.mainWindow.getKefuMsgPanel().setVisible(false);
                MainWindow.mainWindow.getTemplateMsgPanel().setVisible(true);
                MainWindow.mainWindow.getTemplateUrlLabel().setVisible(false);
                MainWindow.mainWindow.getMsgTemplateUrlTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramAppidLabel().setVisible(false);
                MainWindow.mainWindow.getMsgTemplateMiniAppidTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramPagePathLabel().setVisible(false);
                MainWindow.mainWindow.getMsgTemplateMiniPagePathTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel1().setVisible(false);
                MainWindow.mainWindow.getTemplateMiniProgramOptionalLabel2().setVisible(false);
                MainWindow.mainWindow.getTemplateMsgColorLabel().setVisible(false);
                MainWindow.mainWindow.getTemplateDataColorTextField().setVisible(false);
                MainWindow.mainWindow.getMsgTemplateKeyWordTextField().setVisible(false);
                MainWindow.mainWindow.getTemplateKeyWordLabel().setVisible(false);
                MainWindow.mainWindow.getPreviewMemberLabel().setText("预览消息用户手机号（以半角分号分隔）");
                break;
            default:
                break;
        }
    }

    /**
     * 根据客服消息类型转换界面显示
     *
     * @param msgType
     */
    public static void switchKefuMsgType(String msgType) {
        switch (msgType) {
            case "文本消息":
                MainWindow.mainWindow.getKefuMsgTitleLabel().setText("内容");
                MainWindow.mainWindow.getKefuMsgDescLabel().setVisible(false);
                MainWindow.mainWindow.getMsgKefuDescTextField().setVisible(false);
                MainWindow.mainWindow.getKefuMsgPicUrlLabel().setVisible(false);
                MainWindow.mainWindow.getMsgKefuPicUrlTextField().setVisible(false);
                MainWindow.mainWindow.getMsgKefuDescTextField().setVisible(false);
                MainWindow.mainWindow.getKefuMsgUrlLabel().setVisible(false);
                MainWindow.mainWindow.getMsgKefuUrlTextField().setVisible(false);
                break;
            case "图文消息":
                MainWindow.mainWindow.getKefuMsgTitleLabel().setText("标题");
                MainWindow.mainWindow.getKefuMsgDescLabel().setVisible(true);
                MainWindow.mainWindow.getMsgKefuDescTextField().setVisible(true);
                MainWindow.mainWindow.getKefuMsgPicUrlLabel().setVisible(true);
                MainWindow.mainWindow.getMsgKefuPicUrlTextField().setVisible(true);
                MainWindow.mainWindow.getMsgKefuDescTextField().setVisible(true);
                MainWindow.mainWindow.getKefuMsgUrlLabel().setVisible(true);
                MainWindow.mainWindow.getMsgKefuUrlTextField().setVisible(true);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化导入用户tab
     */
    public static void initMemberTab() {
        MainWindow.mainWindow.setImportFromSqlTextArea(configer.getMemberSql());
        MainWindow.mainWindow.setMemberFilePathField(configer.getMemberFilePath());

        MainWindow.mainWindow.getMemberHisComboBox().removeAllItems();

        File pushHisDir = new File(SystemUtil.configHome + "data" + File.separator + "push_his");
        if (!pushHisDir.exists()) {
            pushHisDir.mkdirs();
        }

        File[] files = pushHisDir.listFiles();
        if (files.length > 0) {
            for (File file : files) {
                MainWindow.mainWindow.getMemberHisComboBox().addItem(file.getName());
            }
        }
    }

    /**
     * 初始化推送tab
     */
    public static void initPushTab() {
        MainWindow.mainWindow.setPushMsgName(configer.getMsgName());
        MainWindow.mainWindow.setPushPageSizeTextField(configer.getRecordPerPage());
        MainWindow.mainWindow.setPushPagePerThreadTextField(configer.getPagePerThread());
        MainWindow.mainWindow.setDryRunCheckBox(configer.isDryRun());
    }

    /**
     * 初始化计划任务tab
     */
    public static void initScheduleTab() {
        // 开始
        MainWindow.mainWindow.setRunAtThisTimeRadioButton(configer.isRadioStartAt());
        MainWindow.mainWindow.setStartAtThisTimeTextField(configer.getTextStartAt());

        //每天
        MainWindow.mainWindow.setRunPerDayRadioButton(configer.isRadioPerDay());
        MainWindow.mainWindow.setStartPerDayTextField(configer.getTextPerDay());

        // 每周
        MainWindow.mainWindow.setRunPerWeekRadioButton(configer.isRadioPerWeek());
        MainWindow.mainWindow.setSchedulePerWeekComboBox(configer.getTextPerWeekWeek());
        MainWindow.mainWindow.setStartPerWeekTextField(configer.getTextPerWeekTime());
    }


    /**
     * 初始化设置tab
     */
    public static void initSettingTab() {
        // 常规
        MainWindow.mainWindow.setAutoCheckUpdateCheckBox(configer.isAutoCheckUpdate());

        // 微信公众号
        MainWindow.mainWindow.setWechatAppIdTextField(configer.getWechatAppId());
        MainWindow.mainWindow.setWechatAppSecretPasswordField(configer.getWechatAppSecret());
        MainWindow.mainWindow.setWechatTokenPasswordField(configer.getWechatToken());
        MainWindow.mainWindow.setWechatAesKeyPasswordField(configer.getWechatAesKey());

        // 微信小程序
        MainWindow.mainWindow.setMiniAppAppIdTextField(configer.getMiniAppAppId());
        MainWindow.mainWindow.setMiniAppAppSecretPasswordField(configer.getMiniAppAppSecret());
        MainWindow.mainWindow.setMiniAppTokenPasswordField(configer.getMiniAppToken());
        MainWindow.mainWindow.setMiniAppAesKeyPasswordField(configer.getMiniAppAesKey());

        // 阿里云短信
        MainWindow.mainWindow.setAliyunAccessKeyIdTextField(configer.getAliyunAccessKeyId());
        MainWindow.mainWindow.setAliyunAccessKeySecretTextField(configer.getAliyunAccessKeySecret());
        MainWindow.mainWindow.setAliyunSignTextField(configer.getAliyunSign());

        // 阿里大于
        MainWindow.mainWindow.setAliServerUrlTextField(configer.getAliServerUrl());
        MainWindow.mainWindow.setAliAppKeyPasswordField(configer.getAliAppKey());
        MainWindow.mainWindow.setAliAppSecretPasswordField(configer.getAliAppSecret());
        MainWindow.mainWindow.setAliSignTextField(configer.getAliSign());

        // 腾讯云短信
        MainWindow.mainWindow.setTxyunAppIdTextField(configer.getTxyunAppId());
        MainWindow.mainWindow.setTxyunAppKeyTextField(configer.getTxyunAppKey());
        MainWindow.mainWindow.setTxyunSignTextField(configer.getTxyunSign());

        // MySQL
        MainWindow.mainWindow.setMysqlUrlTextField(configer.getMysqlUrl());
        MainWindow.mainWindow.setMysqlDatabaseTextField(configer.getMysqlDatabase());
        MainWindow.mainWindow.setMysqlUserTextField(configer.getMysqlUser());
        MainWindow.mainWindow.setMysqlPasswordField(configer.getMysqlPassword());

        // 外观
        MainWindow.mainWindow.setSettingThemeComboBox(configer.getTheme());
        MainWindow.mainWindow.setSettingFontNameComboBox(configer.getFont());
        MainWindow.mainWindow.setSettingFontSizeComboBox(configer.getFontSize());

        // 历史消息管理
        String[] headerNames = {"选择", "消息名称"};
        DefaultTableModel model = new DefaultTableModel(null, headerNames);
        MainWindow.mainWindow.getMsgHistable().setModel(model);
        // 隐藏表头
        MainWindow.mainWindow.getMsgHistable().getTableHeader().setVisible(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setPreferredSize(new Dimension(0, 0));
        MainWindow.mainWindow.getMsgHistable().getTableHeader().setDefaultRenderer(renderer);

        Map<String, String[]> msgMap = new HashMap<>(16);
        Object[] data;
        for (String msgName : msgMap.keySet()) {
            data = new Object[2];
            data[0] = false;
            data[1] = msgName;
            model.addRow(data);
        }
        MainWindow.mainWindow.getMsgHistable().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        MainWindow.mainWindow.getMsgHistable().getColumnModel().getColumn(0).setCellRenderer(new MyCheckBoxRenderer());
        // 设置列宽
        MainWindow.mainWindow.getMsgHistable().getColumnModel().getColumn(0).setPreferredWidth(50);
        MainWindow.mainWindow.getMsgHistable().getColumnModel().getColumn(0).setMaxWidth(50);
    }

    /**
     * 初始化模板消息数据table
     */
    public static void initTemplateDataTable() {
        String[] headerNames = {"Name", "Value", "Color", "操作"};
        DefaultTableModel model = new DefaultTableModel(null, headerNames);
        MainWindow.mainWindow.getTemplateMsgDataTable().setModel(model);
        MainWindow.mainWindow.getTemplateMsgDataTable().updateUI();
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().
                getColumn(headerNames.length - 1).
                setCellRenderer(new ButtonColumn(MainWindow.mainWindow.getTemplateMsgDataTable(), headerNames.length - 1));
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().
                getColumn(headerNames.length - 1).
                setCellEditor(new ButtonColumn(MainWindow.mainWindow.getTemplateMsgDataTable(), headerNames.length - 1));

        // 设置列宽
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(0).setPreferredWidth(150);
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(0).setMaxWidth(150);
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(2).setPreferredWidth(130);
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(2).setMaxWidth(130);
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(3).setPreferredWidth(130);
        MainWindow.mainWindow.getTemplateMsgDataTable().getColumnModel().getColumn(3).setMaxWidth(130);
    }

    /**
     * 初始化所有tab
     */
    public static void initAllTab() {
        initMsgTab(null);
        initMemberTab();
        initPushTab();
        initScheduleTab();
        // 初始化后置，切换tab时再触发
        initSettingTab();
    }

    /**
     * 自定义单元格按钮渲染器
     */
    public static class ButtonColumn extends AbstractCellEditor implements
            TableCellRenderer, TableCellEditor, ActionListener {
        JTable table;
        JButton renderButton;
        JButton editButton;

        public ButtonColumn(JTable table, int column) {
            super();
            this.table = table;
            renderButton = new JButton();
            editButton = new JButton();
            editButton.setFocusPainted(false);
            editButton.addActionListener(this);

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer(this);
            columnModel.getColumn(column).setCellEditor(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (hasFocus) {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            } else if (isSelected) {
                renderButton.setForeground(table.getSelectionForeground());
                renderButton.setBackground(table.getSelectionBackground());
            } else {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }

            renderButton.setText("移除");
            renderButton.setIcon(new ImageIcon(getClass().getResource("/icon/remove.png")));
            return renderButton;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            editButton.setText("移除");
            editButton.setIcon(new ImageIcon(getClass().getResource("/icon/remove.png")));
            return editButton;
        }

        @Override
        public Object getCellEditorValue() {
            return "移除";
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int isDelete = JOptionPane.showConfirmDialog(MainWindow.mainWindow.getMessagePanel(), "确认移除？", "确认",
                    JOptionPane.INFORMATION_MESSAGE);
            if (isDelete == JOptionPane.YES_OPTION) {
                fireEditingStopped();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(table.getSelectedRow());
            }
        }
    }

    /**
     * 自定义单元格单选框渲染器
     */
    public static class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            //这一列必须都是integer类型(0-100)
            Boolean b = (Boolean) value;
            setSelected(b);
            return this;
        }
    }

}
