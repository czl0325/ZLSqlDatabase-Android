package com.github.zlsqldatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private IBaseBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userBean = (IBaseBean) ZLSqlDatabase.load(this).getDataHelper(User.class);
    }

    public void insert(View view) {
        for (int i=0; i<10; i++) {
            User user = new User("陈昭良","123456");
            userBean.insert(user);
        }
    }

    public void deleteUser(View view) {
        User user = new User();
        user.setName("陈昭良");
        userBean.delete(user);
    }

    public void getCount(View view) {
        Toast.makeText(this, "当前数据库总数="+userBean.getTotalCount(),
                Toast.LENGTH_LONG).show();
    }
}
