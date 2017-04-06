# FileProvider

Show how to use **[FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider.html)** ( android.support.v4.content.FileProvider ).

**[Lessons](https://developer.android.com/training/secure-file-sharing/index.html)**

FileProvider is a special subclass of ContentProvider that facilitates secure sharing of files associated with an app by creating a **content://** Uri for a file instead of a **file:///** Uri.

## Usage

- Sure you need sharing of files to other Apps.
- Defining **provider** at manifest.
- Specifying files folder what you want at a **?.xml**.
- Create Content Uri for a file use **FileProvider.getUriForFile**.
- Granting permission to a Uri such as **intent.addflags()**.
- Serving a Content Uri to another App.

### Provider

```
 </application>
     ...
     <provider
         android:name="android.support.v4.content.FileProvider"
         android:authorities="${applicationId}.fileProvider"
         android:exported="false"
         android:grantUriPermissions="true">
         <meta-data
             android:name="android.support.FILE_PROVIDER_PATHS"
             android:resource="@xml/provider_path" />
     </provider>
     ...
 </application>
```

**android:name** always static -> **android.support.v4.content.FileProvider**

**android:authorities**  attribute to a URI authority based on a domain you control.

**android:exported** Must be false.

**android:grantUriPermissions** Should be true, allow you to grant temporary access to files.

### ?.xml

Set the <meta-data> element's "android:name" attribute to android.support.FILE_PROVIDER_PATHS. Set the element's "android:resource" attribute to @xml/provider_path (notice that you don't specify the .xml extension)

provider_path.xml

```
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path
        name="file_img"
        path="Pictures/x/d/" />
    <external-cache-path
        name="cache_img"
        path="x/d/" />
    <external-path
        name="public_img"
        path="Pictures/providerTest/" />
</paths>
```

**<files-path name="name" path="path" /> == Context.getFilesDir( ).**

**<cache-path name="name" path="path" /> ==  Context.getCacheDir().**

**<external-path name="name" path="path" /> == Environment.getExternalStorageDirectory().**

**<external-files-path name="name" path="path" /> == Context.getExternalFilesDir(String).**

**<external-cache-path name="name" path="path" /> == Context.getExternalCacheDir().**

**name** A URI path segment. Should be show in content Uri.

**path** is the name attribute is a URI path segment,

### Uri

```
   File file = new File(folder, fileName);
   Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", file);
```

As a result of the previous snippet, getUriForFile() returns the content Uri: **content://com.jax.fileprovider.fileProvider/public_img/providerTest.png.**

The authority of getUriForFile param defined in a <provider> authorities element in manifest.
     
### flag

To grant an access permission to a content URI returned from getUriForFile(), do one of the following:
* Call the method Context.grantUriPermission(package, Uri, mode_flags) for the content:// Uri, using the desired mode flags. This grants temporary access permission for the content URI to the specified package, according to the value of the the mode_flags parameter, which you can set to FLAG_GRANT_READ_URI_PERMISSION, FLAG_GRANT_WRITE_URI_PERMISSION or both. The permission remains in effect until you revoke it by calling revokeUriPermission() or until the device reboots.
+ Put the content URI in an Intent by calling setData().
+ Next, call the method Intent.setFlags() with either FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION or both.
+ Finally, send the Intent to another app. Most often, you do this by calling setResult().

Permissions granted in an Intent remain in effect while the stack of the receiving Activity is active. When the stack finishes, the permissions are automatically removed. Permissions granted to one Activity in a client app are automatically extended to other components of that app.

```
if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
    // not work in android 4.2/4.3 ,may be all not work below android 6.0, so,that is ok.
    // -> http://stackoverflow.com/questions/33650632/fileprovider-not-working-with-camera
    //---------------------↓
    List<ResolveInfo> resInfoList =
            getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    for (ResolveInfo resolveInfo : resInfoList) {
        String packageName = resolveInfo.activityInfo.packageName;
        grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
    //---------------------↑
} else {
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
}
```

### Intent

 startActivityForResult(intent, 1);
 
 intent.setDataAndType(uri, "image/*");
 
 intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
 
 etc...
 
## Wrapping up

+ Defining xml element **path** must show at file path.

+ If **path** is public storage area. When create File must be create folder at first. otherwise, error.

+ Must check Intent resolveAble.( intent.resolveActivity(getPackageManager() )

+ When create Intent add intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), it may be invalid on different OS. You can like this giving all resolved Apps Permission.
```
if (intent.resolveActivity(getPackageManager()) != null) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          } else {
              List<ResolveInfo> resolveInfos
                      = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
              for (ResolveInfo info : resolveInfos) {
                  String packName = info.activityInfo.packageName;
                  Log.d(TAG, "openFile: " + packName);
                  grantUriPermission(packName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
              }
          }
          startActivity(intent);
      }
```

