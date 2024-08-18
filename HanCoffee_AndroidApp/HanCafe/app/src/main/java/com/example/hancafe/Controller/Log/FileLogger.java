package com.example.hancafe.Controller.Log;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    private static FileLogger logger;
    private Context context;
    private FileLogger(Context context) {
        this.context = context;
    }
    public static FileLogger getFileLogger(Context context) {
        if(logger == null){
            logger = new FileLogger(context);
        }
        return logger;
    }
    public synchronized void log(String msg) {
        // Lấy thư mục lưu trữ trong bộ nhớ nội bộ của ứng dụng
        File internalStorageDir = context.getExternalFilesDir(null);
        // Tạo đường dẫn đầy đủ tới file log.txt trong thư mục lưu trữ
        File logFile = new File(internalStorageDir, "log.txt");

        try (FileWriter fileWriter = new FileWriter(logFile, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            printWriter.println(timeStamp + " - " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
