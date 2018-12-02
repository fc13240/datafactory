package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.T799Task;
import com.moonkin.task.TaskManager;
import com.moonkin.task.TaskScheduleBuilder;
import com.moonkin.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * T799模式数据规整窗体事件监控
 */
public class T799Listener {
    private static final Log logger = LogFactory.get();
    private static T799Task t799Task;

    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getT799SourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getT799SourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getT799Panel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getT799SourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        //目标目录
        MainWindow.mainWindow.getT799DestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getT799DestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getT799Panel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getT799DestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //启动
        MainWindow.mainWindow.getT799StartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getT799HourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getT79910minRadio().isSelected();
            if(hourSelected) {
                if(t799Task != null) {
                    TaskManager.removeTask(t799Task.getTaskId());
                }
                t799Task = new T799Task();
                t799Task.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(t799Task);
            }
            if(minSelected) {
                if(t799Task != null) {
                    TaskManager.removeTask(t799Task.getTaskId());
                }
                t799Task = new T799Task();
                t799Task.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(t799Task);
            }
        });

        //停止
        MainWindow.mainWindow.getT799StopButton().addActionListener(e -> {
            TaskManager.pauseTask(t799Task.getTaskId());
        });

    }
}
