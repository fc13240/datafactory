package com.moonkin.ui;

import com.moonkin.config.ConfigPathLoader;
import com.moonkin.config.TaskProperties;
import com.moonkin.ui.listener.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 入口类
 *
 * @author xuduo
 * @since 2018-09-25
 */
public class MainWindow {
    //主面板
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    //结果输出框
    private JScrollPane outputCenterPanel;
    private JTextArea outputTextArea;
    //地面面板
    private JPanel groundPanel;
    private JLabel groundSourceLabel;
    private JTextField groundSourceTextField;
    private JButton groundSourceButton;
    private JLabel groundDestLabel;
    private JTextField groundDestTextField;
    private JButton groundDestButton;
    private JLabel groundCronLabel;
    private JRadioButton groundHourRadio;
    private JLabel groundHourLabel;
    private JRadioButton ground10minRadio;
    private JLabel ground10minLabel;
    private JButton groundStartButton;
    private JButton groundStopButton;
    private JLabel groundDelLabel;
    private JComboBox groundDelTime;

    //高空面板
    private JPanel highPanel;
    private JLabel highSourceLabel;
    private JTextField highSourceTextField;
    private JButton highSourceButton;
    private JLabel highDestLabel;
    private JTextField highDestTextField;
    private JButton highDestButton;
    private JLabel highCronLabel;
    private JTextField highCronTextField;
    private JRadioButton highHourRadio;
    private JLabel highHourLabel;
    private JRadioButton high10minRadio;
    private JLabel high10minLabel;
    private JButton highStartButton;
    private JButton highStopButton;
    private JLabel highDelLabel;
    private JComboBox highDelTime;
    //EC数据
    private JPanel ecPanel;
    private JLabel ecSourceLabel;
    private JTextField ecSourceTextField;
    private JButton ecSourceButton;
    private JLabel ecDestLabel;
    private JTextField ecDestTextField;
    private JButton ecDestButton;
    private JLabel ecCronLabel;
    private JTextField ecCronTextField;
    private JRadioButton ecHourRadio;
    private JLabel ecHourLabel;
    private JRadioButton ec10minRadio;
    private JLabel ec10minLabel;
    private JButton ecStartButton;
    private JButton ecStopButton;
    private JLabel ecDelLabel;
    private JComboBox ecDelTime;
    //雷达数据
    private JPanel radarPanel;
    private JLabel radarSourceLabel;
    private JTextField radarSourceTextField;
    private JButton radarSourceButton;
    private JLabel radarDestLabel;
    private JTextField radarDestTextField;
    private JButton radarDestButton;
    private JLabel radarCronLabel;
    private JTextField radarCronTextField;
    private JRadioButton radarHourRadio;
    private JLabel radarHourLabel;
    private JRadioButton radar10minRadio;
    private JLabel radar10minLabel;
    private JButton radarStartButton;
    private JButton radarStopButton;
    private JLabel radarDelLabel;
    private JComboBox radarDelTime;
    //T799数据
    private JPanel t799Panel;
    private JLabel t799SourceLabel;
    private JTextField t799SourceTextField;
    private JButton t799SourceButton;
    private JLabel t799DestLabel;
    private JTextField t799DestTextField;
    private JButton t799DestButton;
    private JLabel t799CronLabel;
    private JTextField t799CronTextField;
    private JRadioButton t799HourRadio;
    private JLabel t799HourLabel;
    private JRadioButton t79910minRadio;
    private JLabel t79910minLabel;
    private JButton t799StartButton;
    private JButton t799StopButton;
    private JLabel t799DelLabel;
    private JComboBox t799DelTime;
    //日本传真图
    private JPanel japanPanel;
    private JLabel japanSourceLabel;
    private JTextField japanSourceTextField;
    private JButton japanSourceButton;
    private JLabel japanDestLabel;
    private JTextField japanDestTextField;
    private JButton japanDestButton;
    private JLabel japanCronLabel;
    private JTextField japanCronTextField;
    private JRadioButton japanHourRadio;
    private JLabel japanHourLabel;
    private JRadioButton japan10minRadio;
    private JLabel japan10minLabel;
    private JButton japanStartButton;
    private JButton japanStopButton;
    private JLabel japanDelLabel;
    private JComboBox japanDelTime;
    //FY2D数据
    private JPanel fy2dPanel;
    private JLabel fy2dSourceLabel;
    private JTextField fy2dSourceTextField;
    private JButton fy2dSourceButton;
    private JLabel fy2dDestLabel;
    private JTextField fy2dDestTextField;
    private JButton fy2dDestButton;
    private JLabel fy2dCronLabel;
    private JTextField fy2dCronTextField;
    private JRadioButton fy2dHourRadio;
    private JLabel fy2dHourLabel;
    private JRadioButton fy2d10minRadio;
    private JLabel fy2d10minLabel;
    private JButton fy2dStartButton;
    private JButton fy2dStopButton;
    private JLabel fy2dDelLabel;
    private JComboBox fy2dDelTime;
    //FY2E数据
    private JPanel fy2ePanel;
    private JLabel fy2eSourceLabel;
    private JTextField fy2eSourceTextField;
    private JButton fy2eSourceButton;
    private JLabel fy2eDestLabel;
    private JTextField fy2eDestTextField;
    private JButton fy2eDestButton;
    private JLabel fy2eCronLabel;
    private JTextField fy2eCronTextField;
    private JRadioButton fy2eHourRadio;
    private JLabel fy2eHourLabel;
    private JRadioButton fy2e10minRadio;
    private JLabel fy2e10minLabel;
    private JButton fy2eStartButton;
    private JButton fy2eStopButton;
    private JLabel fy2eDelLabel;
    private JComboBox fy2eDelTime;

    public static JFrame frame;

    public static MainWindow mainWindow;

    public MainWindow() {

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // 初始化主题
            Init.initTheme();
            // 统一设置字体
            Init.initGlobalFont();
            // Windows系统状态栏图标
            frame = new JFrame(ConstantsUI.APP_NAME);
            frame.setIconImage(ConstantsUI.IMAGE_ICON);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
            frame.setBounds((int) (screenSize.width * 0.1), (int) (screenSize.height * 0.08), (int) (screenSize.width * 0.8),
                    (int) (screenSize.height * 0.8));

            Dimension preferSize = new Dimension((int) (screenSize.width * 0.8),
                    (int) (screenSize.height * 0.8));
            frame.setPreferredSize(preferSize);

            mainWindow = new MainWindow();

            frame.setContentPane(mainWindow.mainPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            // 添加事件监听
            GroundListener.addListeners();
            HighListener.addListeners();
            ECListener.addListeners();
            RadarListener.addListeners();
            T799Listener.addListeners();
            JapanListener.addListeners();
            TabListener.addListeners();
            FramListener.addListeners();
            Fy2EListener.addListeners();
        });
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getGroundPanel() {
        return groundPanel;
    }

    public JTextField getGroundSourceTextField() {
        return groundSourceTextField;
    }

    public JButton getGroundSourceButton() {
        return groundSourceButton;
    }

    public JTextField getGroundDestTextField() {
        return groundDestTextField;
    }

    public JButton getGroundDestButton() {
        return groundDestButton;
    }

    public JRadioButton getGroundHourRadio() {
        return groundHourRadio;
    }

    public JRadioButton getGround10minRadio() {
        return ground10minRadio;
    }

    public JButton getGroundStartButton() {
        return groundStartButton;
    }

    public JButton getGroundStopButton() {
        return groundStopButton;
    }

    public JComboBox getGroundDelTime() {
        return groundDelTime;
    }

    public JTextArea getOutputTextArea() {
        return outputTextArea;
    }

    public JPanel getHighPanel() {
        return highPanel;
    }

    public JTextField getHighSourceTextField() {
        return highSourceTextField;
    }

    public JButton getHighSourceButton() {
        return highSourceButton;
    }

    public JTextField getHighDestTextField() {
        return highDestTextField;
    }

    public JButton getHighDestButton() {
        return highDestButton;
    }

    public JRadioButton getHighHourRadio() {
        return highHourRadio;
    }

    public JRadioButton getHigh10minRadio() {
        return high10minRadio;
    }

    public JComboBox getHighDelTime() {
        return highDelTime;
    }

    public JComboBox getEcDelTime() {
        return ecDelTime;
    }

    public JComboBox getRadarDelTime() {
        return radarDelTime;
    }

    public JComboBox getT799DelTime() {
        return t799DelTime;
    }

    public JComboBox getJapanDelTime() {
        return japanDelTime;
    }

    public JComboBox getFy2dDelTime() {
        return fy2dDelTime;
    }

    public JComboBox getFy2eDelTime() {
        return fy2eDelTime;
    }

    public JRadioButton getEcHourRadio() {
        return ecHourRadio;
    }

    public JRadioButton getEc10minRadio() {
        return ec10minRadio;
    }

    public JRadioButton getRadarHourRadio() {
        return radarHourRadio;
    }

    public JRadioButton getRadar10minRadio() {
        return radar10minRadio;
    }

    public JRadioButton getT799HourRadio() {
        return t799HourRadio;
    }

    public JRadioButton getT79910minRadio() {
        return t79910minRadio;
    }

    public JRadioButton getJapanHourRadio() {
        return japanHourRadio;
    }

    public JRadioButton getJapan10minRadio() {
        return japan10minRadio;
    }

    public JRadioButton getFy2dHourRadio() {
        return fy2dHourRadio;
    }

    public JRadioButton getFy2d10minRadio() {
        return fy2d10minRadio;
    }

    public JRadioButton getFy2eHourRadio() {
        return fy2eHourRadio;
    }

    public JRadioButton getFy2e10minRadio() {
        return fy2e10minRadio;
    }

    public JButton getHighStartButton() {
        return highStartButton;
    }

    public JButton getHighStopButton() {
        return highStopButton;
    }

    public JPanel getEcPanel() {
        return ecPanel;
    }

    public JTextField getEcSourceTextField() {
        return ecSourceTextField;
    }

    public JButton getEcSourceButton() {
        return ecSourceButton;
    }

    public JTextField getEcDestTextField() {
        return ecDestTextField;
    }

    public JButton getEcDestButton() {
        return ecDestButton;
    }

    public JButton getEcStartButton() {
        return ecStartButton;
    }

    public JButton getEcStopButton() {
        return ecStopButton;
    }

    public JPanel getRadarPanel() {
        return radarPanel;
    }

    public JTextField getRadarSourceTextField() {
        return radarSourceTextField;
    }

    public JButton getRadarSourceButton() {
        return radarSourceButton;
    }

    public JTextField getRadarDestTextField() {
        return radarDestTextField;
    }

    public JButton getRadarDestButton() {
        return radarDestButton;
    }

    public JButton getRadarStartButton() {
        return radarStartButton;
    }

    public JButton getRadarStopButton() {
        return radarStopButton;
    }

    public JPanel getT799Panel() {
        return t799Panel;
    }

    public JTextField getT799SourceTextField() {
        return t799SourceTextField;
    }

    public JButton getT799SourceButton() {
        return t799SourceButton;
    }

    public JTextField getT799DestTextField() {
        return t799DestTextField;
    }

    public JButton getT799DestButton() {
        return t799DestButton;
    }

    public JButton getT799StartButton() {
        return t799StartButton;
    }

    public JButton getT799StopButton() {
        return t799StopButton;
    }

    public JPanel getJapanPanel() {
        return japanPanel;
    }

    public JTextField getJapanSourceTextField() {
        return japanSourceTextField;
    }

    public JButton getJapanSourceButton() {
        return japanSourceButton;
    }

    public JTextField getJapanDestTextField() {
        return japanDestTextField;
    }

    public JButton getJapanDestButton() {
        return japanDestButton;
    }

    public JButton getJapanStartButton() {
        return japanStartButton;
    }

    public JButton getJapanStopButton() {
        return japanStopButton;
    }

    public JPanel getFy2dPanel() {
        return fy2dPanel;
    }

    public JTextField getFy2dSourceTextField() {
        return fy2dSourceTextField;
    }

    public JButton getFy2dSourceButton() {
        return fy2dSourceButton;
    }

    public JTextField getFy2dDestTextField() {
        return fy2dDestTextField;
    }

    public JButton getFy2dDestButton() {
        return fy2dDestButton;
    }

    public JButton getFy2dStartButton() {
        return fy2dStartButton;
    }

    public JButton getFy2dStopButton() {
        return fy2dStopButton;
    }

    public JPanel getFy2ePanel() {
        return fy2ePanel;
    }

    public JTextField getFy2eSourceTextField() {
        return fy2eSourceTextField;
    }

    public JButton getFy2eSourceButton() {
        return fy2eSourceButton;
    }

    public JTextField getFy2eDestTextField() {
        return fy2eDestTextField;
    }

    public JButton getFy2eDestButton() {
        return fy2eDestButton;
    }

    public JButton getFy2eStartButton() {
        return fy2eStartButton;
    }

    public JButton getFy2eStopButton() {
        return fy2eStopButton;
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    public static void setMainWindow(MainWindow mainWindow) {
        MainWindow.mainWindow = mainWindow;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        //主面板
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(8, 0, 0, 0), -1, -1));
        //tab标签页
        tabbedPane = new JTabbedPane();
        Font tabbedPaneFont = this.$$$getFont$$$(null, -1, -1, tabbedPane.getFont());
        if (tabbedPaneFont != null) tabbedPane.setFont(tabbedPaneFont);
        mainPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 100), null, 0, false));
        //输出日志框
        outputCenterPanel = new JScrollPane();
        mainPanel.add(outputCenterPanel, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(1000, 450), null, null, 0, false));
        outputCenterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "执行情况", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, outputCenterPanel.getFont())));
        outputTextArea = new JTextArea();
        outputTextArea.setText("");
        outputTextArea.setEditable(false);
        outputCenterPanel.setViewportView(outputTextArea);

        //读任务配置信息
        TaskProperties groundTaskPropertie = ConfigPathLoader.getTaskProperties("ground");
        TaskProperties highTaskPropertie = ConfigPathLoader.getTaskProperties("high");
        TaskProperties ecTaskPropertie = ConfigPathLoader.getTaskProperties("ec");
        TaskProperties t799TaskPropertie = ConfigPathLoader.getTaskProperties("t799");
        TaskProperties japanTaskPropertie = ConfigPathLoader.getTaskProperties("japan");
        TaskProperties radarTaskPropertie = ConfigPathLoader.getTaskProperties("radar");
        TaskProperties fydTaskPropertie = ConfigPathLoader.getTaskProperties("fyd");
        TaskProperties fyeTaskPropertie = ConfigPathLoader.getTaskProperties("fye");

        //地面数据
        groundPanel = new JPanel();
        groundPanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("地面绘图报", groundPanel);
        groundSourceLabel = new JLabel();
        groundSourceLabel.setText("原始目录");
        groundPanel.add(groundSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groundSourceTextField = new JTextField();
        groundSourceTextField.setText(groundTaskPropertie.GetPathIn());
        groundPanel.add(groundSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        groundSourceButton = new JButton();
        groundSourceButton.setText("浏览");
        groundPanel.add(groundSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        groundDestLabel = new JLabel();
        groundDestLabel.setText("输出目录");
        groundPanel.add(groundDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groundDestTextField = new JTextField();
        groundDestTextField.setText(groundTaskPropertie.GetPathOut());
        groundPanel.add(groundDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        groundDestButton = new JButton();
        groundDestButton.setText("浏览");
        groundPanel.add(groundDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        groundCronLabel = new JLabel();
        groundCronLabel.setText("执行时间");
        groundPanel.add(groundCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groundHourRadio = new JRadioButton();
        if(groundTaskPropertie.GetRadioIndex() == 1)
            groundHourRadio.setSelected(true);
        groundPanel.add(groundHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groundHourLabel = new JLabel();
        groundHourLabel.setText("每小时执行一次");
        groundPanel.add(groundHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ground10minRadio = new JRadioButton();
        if(groundTaskPropertie.GetRadioIndex() == 2)
            ground10minRadio.setSelected(true);
        groundPanel.add(ground10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ground10minLabel = new JLabel();
        ground10minLabel.setText("每10分钟执行一次");
        groundPanel.add(ground10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        groundStartButton = new JButton();
        groundStartButton.setText("启动");
        groundPanel.add(groundStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        groundStopButton = new JButton();
        groundStopButton.setText("停止");
        groundPanel.add(groundStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        groundDelLabel = new JLabel();
        groundDelLabel.setText("数据保留天数:");
        groundPanel.add(groundDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        groundDelTime = new JComboBox();
        groundDelTime.addItem("3");
        groundDelTime.addItem("2");
        groundDelTime.addItem("1");
        groundDelTime.setSelectedItem(groundTaskPropertie.GetDataKeepTime());
        groundPanel.add(groundDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        //高空数据
        highPanel = new JPanel();
        highPanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("高空绘图报", highPanel);
        highSourceLabel = new JLabel();
        highSourceLabel.setText("原始目录");
        highPanel.add(highSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        highSourceTextField = new JTextField();
        highSourceTextField.setText(highTaskPropertie.GetPathIn());
        highPanel.add(highSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        highSourceButton = new JButton();
        highSourceButton.setText("浏览");
        highPanel.add(highSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        highDestLabel = new JLabel();
        highDestLabel.setText("输出目录");
        highPanel.add(highDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        highDestTextField = new JTextField();
        highDestTextField.setText(highTaskPropertie.GetPathOut());
        highPanel.add(highDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        highDestButton = new JButton();
        highDestButton.setText("浏览");
        highPanel.add(highDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        highCronLabel = new JLabel();
        highCronLabel.setText("执行时间");
        highPanel.add(highCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        highHourRadio = new JRadioButton();
        highPanel.add(highHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        highHourLabel = new JLabel();
        highHourLabel.setText("每小时执行一次");
        if(highTaskPropertie.GetRadioIndex() == 1)
            highHourRadio.setSelected(true);
        highPanel.add(highHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        high10minRadio = new JRadioButton();
        highPanel.add(high10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        high10minLabel = new JLabel();
        high10minLabel.setText("每10分钟执行一次");
        if(highTaskPropertie.GetRadioIndex() == 2)
            high10minRadio.setSelected(true);
        highPanel.add(high10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        
        highStartButton = new JButton();
        highStartButton.setText("启动");
        highPanel.add(highStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        highStopButton = new JButton();
        highStopButton.setText("停止");
        highPanel.add(highStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        highDelLabel = new JLabel();
        highDelLabel.setText("数据保留天数:");
        highPanel.add(highDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        highDelTime = new JComboBox();
        highDelTime.addItem("3");
        highDelTime.addItem("2");
        highDelTime.addItem("1");
        highDelTime.setSelectedItem(highTaskPropertie.GetDataKeepTime());
        highPanel.add(highDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


        //EC数据
        ecPanel = new JPanel();
        ecPanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("EC数据", ecPanel);
        ecSourceLabel = new JLabel();
        ecSourceLabel.setText("原始目录");
        ecPanel.add(ecSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ecSourceTextField = new JTextField();
        ecSourceTextField.setText(ecTaskPropertie.GetPathIn());
        ecPanel.add(ecSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ecSourceButton = new JButton();
        ecSourceButton.setText("浏览");
        ecPanel.add(ecSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        ecDestLabel = new JLabel();
        ecDestLabel.setText("输出目录");
        ecPanel.add(ecDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ecDestTextField = new JTextField();
        ecDestTextField.setText(ecTaskPropertie.GetPathOut());
        ecPanel.add(ecDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ecDestButton = new JButton();
        ecDestButton.setText("浏览");
        ecPanel.add(ecDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        ecCronLabel = new JLabel();
        ecCronLabel.setText("执行时间");
        ecPanel.add(ecCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        ecHourRadio = new JRadioButton();
        ecPanel.add(ecHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ecHourLabel = new JLabel();
        ecHourLabel.setText("每小时执行一次");
        if(ecTaskPropertie.GetRadioIndex() == 1)
            ecHourRadio.setSelected(true);
        ecPanel.add(ecHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ec10minRadio = new JRadioButton();
        ecPanel.add(ec10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ec10minLabel = new JLabel();
        ec10minLabel.setText("每10分钟执行一次");
        if(ecTaskPropertie.GetRadioIndex() == 2)
            ec10minRadio.setSelected(true);
        ecPanel.add(ec10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        ecStartButton = new JButton();
        ecStartButton.setText("启动");
        ecPanel.add(ecStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        ecStopButton = new JButton();
        ecStopButton.setText("停止");
        ecPanel.add(ecStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        ecDelLabel = new JLabel();
        ecDelLabel.setText("数据保留天数:");
        ecPanel.add(ecDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        ecDelTime = new JComboBox();
        ecDelTime.addItem("3");
        ecDelTime.addItem("2");
        ecDelTime.addItem("1");
        ecDelTime.setSelectedItem(ecTaskPropertie.GetDataKeepTime());
        ecPanel.add(ecDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


        //T799数据
        t799Panel = new JPanel();
        t799Panel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab(" T799 ", t799Panel);
        t799SourceLabel = new JLabel();
        t799SourceLabel.setText("原始目录");
        t799Panel.add(t799SourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        t799SourceTextField = new JTextField();
        t799SourceTextField.setText(t799TaskPropertie.GetPathIn());
        t799Panel.add(t799SourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        t799SourceButton = new JButton();
        t799SourceButton.setText("浏览");
        t799Panel.add(t799SourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        t799DestLabel = new JLabel();
        t799DestLabel.setText("输出目录");
        t799Panel.add(t799DestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        t799DestTextField = new JTextField();
        t799DestTextField.setText(t799TaskPropertie.GetPathOut());
        t799Panel.add(t799DestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        t799DestButton = new JButton();
        t799DestButton.setText("浏览");
        t799Panel.add(t799DestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        t799CronLabel = new JLabel();
        t799CronLabel.setText("执行时间");
        t799Panel.add(t799CronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        t799HourRadio = new JRadioButton();
        t799Panel.add(t799HourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        t799HourLabel = new JLabel();
        t799HourLabel.setText("每小时执行一次");
        if(t799TaskPropertie.GetRadioIndex() == 1)
            t799HourRadio.setSelected(true);
        t799Panel.add(t799HourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        t79910minRadio = new JRadioButton();
        t799Panel.add(t79910minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        t79910minLabel = new JLabel();
        t79910minLabel.setText("每10分钟执行一次");
        if(t799TaskPropertie.GetRadioIndex() == 2)
            t79910minRadio.setSelected(true);
        t799Panel.add(t79910minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        t799StartButton = new JButton();
        t799StartButton.setText("启动");
        t799Panel.add(t799StartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        t799StopButton = new JButton();
        t799StopButton.setText("停止");
        t799Panel.add(t799StopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        t799DelLabel = new JLabel();
        t799DelLabel.setText("数据保留天数:");
        t799Panel.add(t799DelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        t799DelTime = new JComboBox();
        t799DelTime.addItem("3");
        t799DelTime.addItem("2");
        t799DelTime.addItem("1");
        t799DelTime.setSelectedItem(t799TaskPropertie.GetDataKeepTime());
        t799Panel.add(t799DelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        
        //日本传真图
        japanPanel = new JPanel();
        japanPanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab(" 日本传真图 ", japanPanel);
        japanSourceLabel = new JLabel();
        japanSourceLabel.setText("原始目录");
        japanPanel.add(japanSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        japanSourceTextField = new JTextField();
        japanSourceTextField.setText(japanTaskPropertie.GetPathIn());
        japanPanel.add(japanSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        japanSourceButton = new JButton();
        japanSourceButton.setText("浏览");
        japanPanel.add(japanSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        japanDestLabel = new JLabel();
        japanDestLabel.setText("输出目录");
        japanPanel.add(japanDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        japanDestTextField = new JTextField();
        japanDestTextField.setText(japanTaskPropertie.GetPathOut());
        japanPanel.add(japanDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        japanDestButton = new JButton();
        japanDestButton.setText("浏览");
        japanPanel.add(japanDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        japanCronLabel = new JLabel();
        japanCronLabel.setText("执行时间");
        japanPanel.add(japanCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        japanHourRadio = new JRadioButton();
        japanPanel.add(japanHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        japanHourLabel = new JLabel();
        japanHourLabel.setText("每小时执行一次");
        if(japanTaskPropertie.GetRadioIndex() == 1)
            japanHourRadio.setSelected(true);
        japanPanel.add(japanHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        japan10minRadio = new JRadioButton();
        japanPanel.add(japan10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        japan10minLabel = new JLabel();
        japan10minLabel.setText("每10分钟执行一次");
        if(japanTaskPropertie.GetRadioIndex() == 2)
            japan10minRadio.setSelected(true);
        japanPanel.add(japan10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        japanStartButton = new JButton();
        japanStartButton.setText("启动");
        japanPanel.add(japanStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        japanStopButton = new JButton();
        japanStopButton.setText("停止");
        japanPanel.add(japanStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        japanDelLabel = new JLabel();
        japanDelLabel.setText("数据保留天数:");
        japanPanel.add(japanDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        japanDelTime = new JComboBox();
        japanDelTime.addItem("3");
        japanDelTime.addItem("2");
        japanDelTime.addItem("1");
        japanDelTime.setSelectedItem(japanTaskPropertie.GetDataKeepTime());
        japanPanel.add(japanDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        //雷达图
        radarPanel = new JPanel();
        radarPanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        Font settingPanelFont = this.$$$getFont$$$("Microsoft YaHei UI", -1, -1, radarPanel.getFont());
        if (settingPanelFont != null) radarPanel.setFont(settingPanelFont);
        tabbedPane.addTab(" 雷达图 ", radarPanel);
        radarSourceLabel = new JLabel();
        radarSourceLabel.setText("原始目录");
        radarPanel.add(radarSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radarSourceTextField = new JTextField();
        radarSourceTextField.setText(radarTaskPropertie.GetPathIn());
        radarPanel.add(radarSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        radarSourceButton = new JButton();
        radarSourceButton.setText("浏览");
        radarPanel.add(radarSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        radarDestLabel = new JLabel();
        radarDestLabel.setText("输出目录");
        radarPanel.add(radarDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radarDestTextField = new JTextField();
        radarDestTextField.setText(radarTaskPropertie.GetPathOut());
        radarPanel.add(radarDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        radarDestButton = new JButton();
        radarDestButton.setText("浏览");
        radarPanel.add(radarDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        radarCronLabel = new JLabel();
        radarCronLabel.setText("执行时间");
        radarPanel.add(radarCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        radarHourRadio = new JRadioButton();
        radarPanel.add(radarHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radarHourLabel = new JLabel();
        radarHourLabel.setText("每小时执行一次");
        if(radarTaskPropertie.GetRadioIndex() == 1)
            radarHourRadio.setSelected(true);
        radarPanel.add(radarHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        radar10minRadio = new JRadioButton();
        radarPanel.add(radar10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radar10minLabel = new JLabel();
        radar10minLabel.setText("每10分钟执行一次");
        if(radarTaskPropertie.GetRadioIndex() == 2)
            radar10minRadio.setSelected(true);
        radarPanel.add(radar10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        radarStartButton = new JButton();
        radarStartButton.setText("启动");
        radarPanel.add(radarStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        radarStopButton = new JButton();
        radarStopButton.setText("停止");
        radarPanel.add(radarStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        radarDelLabel = new JLabel();
        radarDelLabel.setText("数据保留天数:");
        radarPanel.add(radarDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        radarDelTime = new JComboBox();
        radarDelTime.addItem("3");
        radarDelTime.addItem("2");
        radarDelTime.addItem("1");
        radarDelTime.setSelectedItem(radarTaskPropertie.GetDataKeepTime());
        radarPanel.add(radarDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


        //风云2D卫星
        fy2dPanel = new JPanel();
        fy2dPanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        //tabbedPane.addTab("风云2D卫星", fy2dPanel);
        fy2dSourceLabel = new JLabel();
        fy2dSourceLabel.setText("原始目录");
        fy2dPanel.add(fy2dSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2dSourceTextField = new JTextField();
        fy2dSourceTextField.setText(fydTaskPropertie.GetPathIn());
        fy2dPanel.add(fy2dSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fy2dSourceButton = new JButton();
        fy2dSourceButton.setText("浏览");
        fy2dPanel.add(fy2dSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fy2dDestLabel = new JLabel();
        fy2dDestLabel.setText("输出目录");
        fy2dPanel.add(fy2dDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2dDestTextField = new JTextField();
        fy2dDestTextField.setText(fydTaskPropertie.GetPathOut());
        fy2dPanel.add(fy2dDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fy2dDestButton = new JButton();
        fy2dDestButton.setText("浏览");
        fy2dPanel.add(fy2dDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fy2dCronLabel = new JLabel();
        fy2dCronLabel.setText("执行时间");
        fy2dPanel.add(fy2dCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        fy2dHourRadio = new JRadioButton();
        fy2dPanel.add(fy2dHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2dHourLabel = new JLabel();
        fy2dHourLabel.setText("每小时执行一次");
        if(fydTaskPropertie.GetRadioIndex() == 1)
            fy2dHourRadio.setSelected(true);
        fy2dPanel.add(fy2dHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fy2d10minRadio = new JRadioButton();
        fy2dPanel.add(fy2d10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2d10minLabel = new JLabel();
        fy2d10minLabel.setText("每10分钟执行一次");
        if(fydTaskPropertie.GetRadioIndex() == 2)
            fy2d10minRadio.setSelected(true);
        fy2dPanel.add(fy2d10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        
        fy2dStartButton = new JButton();
        fy2dStartButton.setText("启动");
        fy2dPanel.add(fy2dStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fy2dStopButton = new JButton();
        fy2dStopButton.setText("停止");
        fy2dPanel.add(fy2dStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        fy2dDelLabel = new JLabel();
        fy2dDelLabel.setText("数据保留天数:");
        fy2dPanel.add(fy2dDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        fy2dDelTime = new JComboBox();
        fy2dDelTime.addItem("3");
        fy2dDelTime.addItem("2");
        fy2dDelTime.addItem("1");
        fy2dDelTime.setSelectedItem(fydTaskPropertie.GetDataKeepTime());
        fy2dPanel.add(fy2dDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        //风云2E卫星
        fy2ePanel = new JPanel();
        fy2ePanel.setLayout(new GridLayoutManager(6, 7, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("风云2E卫星", fy2ePanel);
        fy2eSourceLabel = new JLabel();
        fy2eSourceLabel.setText("原始目录");
        fy2ePanel.add(fy2eSourceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2eSourceTextField = new JTextField();
        fy2eSourceTextField.setText(fyeTaskPropertie.GetPathIn());
        fy2ePanel.add(fy2eSourceTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fy2eSourceButton = new JButton();
        fy2eSourceButton.setText("浏览");
        fy2ePanel.add(fy2eSourceButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fy2eDestLabel = new JLabel();
        fy2eDestLabel.setText("输出目录");
        fy2ePanel.add(fy2eDestLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2eDestTextField = new JTextField();
        fy2eDestTextField.setText(fyeTaskPropertie.GetPathOut());
        fy2ePanel.add(fy2eDestTextField, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fy2eDestButton = new JButton();
        fy2eDestButton.setText("浏览");
        fy2ePanel.add(fy2eDestButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fy2eCronLabel = new JLabel();
        fy2eCronLabel.setText("执行时间");
        fy2ePanel.add(fy2eCronLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        fy2eHourRadio = new JRadioButton();
        fy2ePanel.add(fy2eHourRadio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2eHourLabel = new JLabel();
        fy2eHourLabel.setText("每小时执行一次");
        if(fyeTaskPropertie.GetRadioIndex() == 1)
            fy2eHourRadio.setSelected(true);
        fy2ePanel.add(fy2eHourLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fy2e10minRadio = new JRadioButton();
        fy2ePanel.add(fy2e10minRadio, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fy2e10minLabel = new JLabel();
        fy2e10minLabel.setText("每10分钟执行一次");
        if(fyeTaskPropertie.GetRadioIndex() == 2)
            fy2e10minRadio.setSelected(true);
        fy2ePanel.add(fy2e10minLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        fy2eStartButton = new JButton();
        fy2eStartButton.setText("启动");
        fy2ePanel.add(fy2eStartButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fy2eStopButton = new JButton();
        fy2eStopButton.setText("停止");
        fy2ePanel.add(fy2eStopButton, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));

        fy2eDelLabel = new JLabel();
        fy2eDelLabel.setText("数据保留天数:");
        fy2ePanel.add(fy2eDelLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        fy2eDelTime = new JComboBox();
        fy2eDelTime.addItem("3");
        fy2eDelTime.addItem("2");
        fy2eDelTime.addItem("1");
        fy2eDelTime.setSelectedItem(fyeTaskPropertie.GetDataKeepTime());
        fy2ePanel.add(fy2eDelTime, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
