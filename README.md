# ZLSqlDatabase-Android
安卓操作数据库读写，以及增删改查。

### 支持implementation导入

```
implementation 'com.github.czl0325:zlsqldatabase:1.0.0'
```

### 说明

该类库用于一行代码操作数据库

该类库会在你的app文件夹下创建一个以你工程名命名的db文件
创建表名会以你model的类名来作为表名，如果你需要别的表名，可以在model类写注解
表里面的字段名根据Model类的成员变量来命名，也可以通过注解自己命名。
支持类型有： String , float, int, long, double, byte[]
String需要指定在数据库类的占用长度，如果没指定，默认是100.

### 初始化数据库变量
```
private IBaseBean userBean;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    userBean = (IBaseBean) ZLSqlDatabase.load(this).getDataHelper(User.class);
}
```


### 增删改查操作一句代码完成
```
public void insert(View view) {
  for (int i=0; i<10; i++) {
      User user = new User("陈昭良","123456");
      userBean.insert(user);
  }
  Log.e("czl","插入完成");
}

public void deleteUser(View view) {
  User user = new User();
  user.setName("陈昭良");
  int result= userBean.delete(user);
  Log.e("czl",""+result);
}

public void updateUser(View view) {
  User user1 = new User();
  user1.setPassword("123456");
  User user2 = new User();
  user2.setName("czl");
  user2.setPassword("123");
  int result = userBean.update(user2, user1);
  Log.e("czl",""+result);
}

public void getCount(View view) {

}

public void queryUser(View view) {
  User user = new User();
  user.setPassword("1");
  List<User> list = userBean.query(user);
  Log.e("czl","共查询到 "+list.size()+" 条数据!");
  for (User u:list) {
      Log.e("czl",u.toString());
  }
}
```
