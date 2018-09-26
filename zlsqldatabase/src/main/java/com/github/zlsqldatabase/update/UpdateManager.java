package com.github.zlsqldatabase.update;

import android.content.Context;

import java.io.File;

public class UpdateManager {
    private File parentFile ;
    private File bakFile;
    private Context context;

    public UpdateManager(Context context) {
        super();
        this.context = context;
        parentFile = new File(context.getFilesDir().getAbsolutePath(),"update");
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        bakFile = new File(context.getFilesDir().getAbsolutePath(),"bakDb");
        if (!bakFile.exists()) {
            bakFile.mkdir();
        }
    }

    public void checkThisVersionTable() {

    }
}
