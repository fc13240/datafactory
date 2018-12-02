package com.moonkin.task;

import com.moonkin.decode.HighObservationToMicaps2;
import com.moonkin.ui.MainWindow;
import org.quartz.JobDataMap;

/**
 * 高空探空报解析任务
 * @author xuduo
 * @date 2018/9/27
 */
public class HighTask extends BaseTask {

    @Override
    protected void config() {
        setTaskId("tk_task");
        setTaskName("高空探空报报转格式");
    }

    @Override
    protected void doWork(JobDataMap dataMap) {
        String sourceFile = MainWindow.mainWindow.getHighSourceTextField().getText().trim();
        String destFile = MainWindow.mainWindow.getHighDestTextField().getText().trim();
        HighObservationToMicaps2.writeToMicap2(sourceFile, destFile);
    }
}
