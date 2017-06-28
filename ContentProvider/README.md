# ContentProvider

ContentProvider （内容提供者）用于提供应用间数据共享

## 基础知识

### Uri
　URI（Uniform Resource Identifier）——统一资源定位符，URI在ContentProvider中代表了要操做的数据。

    在Android系统中通常的URI格式为：content://com.example.provider.Users/User/21
- content://——schema，这个是Android中已经定义好的一个标准。我个人一直认为这和我们的http://有异曲同工之妙，都是代表的协议。ContentProvider（内容提供者）的scheme已经由Android所规定为：content://

- com.example.provider.Users 
authority（主机名），用于唯一标识这个ContentProvider，外部调用者通过这个authority来找到它。相当于www.XXXX.com代表的是我们ContentProvider所在的”域名”，这个”域名”在我们Android中一定要是唯一的，否则系统怎么能知道该找哪一个Provider呢?所以一般情况下，建议采用完整的包名加类名来标识这个ContentProvider的authority。

- /User/21——路径，用来标识我们要操作的数据。/user/21表示的意思是——找到User中id为21的记录。其实这个相当于/AAA/123。

综上所述，content://com.example.provider.Users/User/21 所代表的URI的意思为：标识LiB.cprovider.myprovider中Users表中_ID为21的User项。

### UriMatcher
    UriMatcher用于操作Uri，用于匹配Uri
    
#### 用法：
+ 注册需要匹配的Uri路径
+ 使用uriMather.mather(Uri uri)对输入的uri进行匹配，如果匹配就返回匹配码。

##### 注册
     //常量 UriMatcher.NO_MATCH 表示不匹配任何路径的返回码(-1)。
初始化UriMather
```
 UriMatcher  uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
 //如果match()方法匹配content://com.example.provider.contactprovider/contact路径，返回匹配码为1
 uriMatcher.addURI(“com.example.provider.contactprovider”, “contact”, 1);    //添加需要匹配uri，如果匹配就会返回匹配码
 //如果match()方法匹配   content://com.example.provider.contactprovider/contact/230路径，返回匹配码为2
 uriMatcher.addURI(“com.example.provider.contactprovider”, “contact/#”, 2);    //#号为通配符
```
##### 匹配
注册完需要匹配的Uri后，就可以使用uriMatcher.match(uri)方法对输入的Uri进行匹配，
如果匹配就返回匹配码，匹配码是调用addURI()方法传入的第三个参数，假设匹配
content://com.example.provider.contactprovider/contact 路径，则返回的匹配码为1。
### ContentUris

用于获取Uri路径后面的ID部分
它有两个比较实用的方法：
- withAppendedId(uri, id)用于为路径加上ID部分
- parseId(uri)方法用于从路径中获取ID部分
```
Uri uri = Uri.parse("content://com.ljq.provider.personprovider/person")
Uri resultUri = ContentUris.withAppendedId(uri, 10); 
//生成后的Uri为：content://com.ljq.provider.personprovider/person/10
Uri uri = Uri.parse("content://com.ljq.provider.personprovider/person/10")
long personid = ContentUris.parseId(uri);//获取的结果为:10

```
### ContentResolver

当外部应用需要对ContentProvider中的数据进行添加、删除、修改和查询操作时，可以使用ContentResolver 类来完成，要获取ContentResolver 对象，可以使用Activity提供的getContentResolver()方法。 ContentResolver使用insert、delete、update、query方法，来操作数据。
```
 getContext().getContentResolver()
```

具有以下几个常用的方法
```
public Uri insert(Uri uri, ContentValues values)：
该方法用于往ContentProvider添加数据。
public int delete(Uri uri, String selection, String[] selectionArgs)：
该方法用于从ContentProvider删除数据。
public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)：
该方法用于更新ContentProvider中的数据。
public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)：
该方法用于从ContentProvider中获取数据。
```

### ContentProvider

ContentProvider是一个abstract类，继承他需要实现以下几个方法：
```
public boolean onCreate()：
该方法在ContentProvider创建后就会被调用，Android开机后，ContentProvider在其它应用第一次访问它时才会被创建。
public Uri insert(Uri uri, ContentValues values)：
该方法用于供外部应用往ContentProvider添加数据。
public int delete(Uri uri, String selection, String[] selectionArgs)：
该方法用于供外部应用从ContentProvider删除数据。
public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)：
该方法用于供外部应用更新ContentProvider中的数据。
public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)：
该方法用于供外部应用从ContentProvider中获取数据。
public String getType(Uri uri)：
该方法用于返回当前Url所代表数据的MIME类型。
```
如果操作的数据属于集合类型，那么MIME类型字符串应该以vnd.android.cursor.dir/开头，
如果要操作的数据属于非集合类型数据，那么MIME类型字符串应该以vnd.android.cursor.item/开头，

## 实现步骤

#### 首先生成一个集成ContentProvider的类
```
public class JaxProvider extends ContentProvider {

    DBHelper dbHelper;
    SQLiteDatabase database;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Profile.AUTHORITIES, Profile.TABLE_NAME, Profile.ITEM);
        uriMatcher.addURI(Profile.AUTHORITIES, Profile.TABLE_NAME + "/#", Profile.ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Profile.ITEM:
                return Profile.CONTENT_TYPE;
            case Profile.ITEM_ID:
                return Profile.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
       retrun null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

```

#### 在androidManifest.xml里面添加一个provider的标签
```
  <provider
            android:name="com.ytxx.contentprovider.provider.JaxProvider"
            android:authorities="${applicationId}.JaxProvider"
            android:exported="false" />
```

#### 通过从ContentResolver间接的对ContentProvider进行数据的增删改查
```
 //测试更改第二个数据
                Cursor update = contentResolver.query(Profile.CONNECT_URI, null, null, null, null);
                assert update != null;
                int id2;
                String name2;
                if (update.getCount() <= 0) {
                    Log.d(TAG, "delete: data size is 0. ");
                    return;
                }
                if (update.getCount() > 1) {
                    update.moveToPosition(1);
                    id2 = update.getInt(update.getColumnIndex(Profile.COLUMN_ID));
                    name2 = update.getString(update.getColumnIndex(Profile.COLUMN_NAME));
                } else {
                    update.moveToFirst();
                    id2 = update.getInt(update.getColumnIndex(Profile.COLUMN_ID));
                    name2 = update.getString(update.getColumnIndex(Profile.COLUMN_NAME));
                }
                update.close();
                Log.d(TAG, "update: " + id2 + " name->" + name2);
                ContentValues cv1 = new ContentValues();
                cv1.put(Profile.COLUMN_NAME, "update" + new Random().nextInt(100));
                int sss = contentResolver.update(Uri.withAppendedPath(Profile.CONNECT_URI, "/" + id2), cv1, null, null);
                Log.d(TAG, "update success : " + sss);
```