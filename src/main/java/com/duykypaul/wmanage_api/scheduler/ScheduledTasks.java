package com.duykypaul.wmanage_api.scheduler;

import com.duykypaul.wmanage_api.common.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Scheduled(cron = "0 30 9 15 * ?")
    public void backupDB() throws IOException {
        log.info("The time is now {}", dateFormat.format(new Date()));
        Date backupDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy");
        String backupDateStr = format.format(backupDate);
        String dbName = "wmanage_api"; // default file name

        Path path = Paths.get(CommonConst.BACKUP_DB);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String saveFileName = dbName + CommonConst.UNDERSCORE + backupDateStr + CommonConst.UNDERSCORE + new Date().getTime() + ".sql";
        String savePath = path.toAbsolutePath() + File.separator + saveFileName;

        String executeCmd = "mysqldump -u " + dbUsername + " -p" + dbPassword + " -P " + CommonConst.DB_PORT + " " + dbName + " -r " + savePath;

        Process runtimeProcess = null;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int processComplete = 0;
        try {
            assert runtimeProcess != null;
            processComplete = runtimeProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (processComplete == 0) {
            System.out.println("Backup Complete at " + new Date());
        } else {
            System.out.println("Backup Failure");
        }
    }
}
