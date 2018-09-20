package com.github.zlsqldatabase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.security.PrivilegedAction;

public class ZLSqlDatabase {
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    private static ZLSqlDatabase instance = null;

    private ZLSqlDatabase(Context context) {
        this.context = context;
        openDatabase();
    }

    public static ZLSqlDatabase load(Context context) {
        if (instance == null) {
            synchronized (ZLSqlDatabase.class) {
                if (instance == null) {
                    instance = new ZLSqlDatabase(context);
                }
            }
        }
        return instance;
    }

    public synchronized <T> T getDataHelper(Class<T> clazz) {
        BaseBean baseBean = new BaseBean();
        baseBean.init(clazz, sqLiteDatabase);
        return (T) baseBean;
    }

    private void openDatabase() {
        String path = context.getFilesDir()+File.separator+"db";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        path += (File.separator
                +context.getPackageName().substring(context.getPackageName().lastIndexOf(".")+1)
                +".db");
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path,null);
    }
}
