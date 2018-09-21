# ZLSqlDatabase-Android
安卓操作数据库读写，以及增删改查。


### 说明



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
