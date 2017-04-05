# DownloadManager

A demo for downloadManger usage. ( Api 9 + )

## Reference

**https://blog.stylingandroid.com/downloadmanager-part-1/**

## Summary

- First clear what you want download.
- Create DownloadManager for **SystemService ( Context.DOWNLOAD_SERVICE )**.
- New a **DownloadManager.Request** for request resources.
- Add request to downloadManager through **DownloadManager.enqueue( Request request )**
- Register **BroadCastReceiver** for filter **DownloadManager.ACTION_DOWNLOAD_COMPLETE**
- Query downloaded file by id through **DownloadManager.Query**
- Do what you want.

### DownloadManager

You can get DownloadManager obj from

**context.getSystemService( Context.DOWNLOAD_SERVICE )**;

```
 DownloadManager downloadManager = (DownloadManager) listener.getContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);
```

### DownloadManager.Request

Construct a download request by DownloadManager.Request .
https://developer.android.com/reference/android/app/DownloadManager.Request.html

```
 DownloadManager.Request request = new DownloadManager.Request(uri);
 request.setTitle("TestDownload.pdf");
 request.setDescription("I am download description. ");
 request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
 request.allowScanningByMediaScanner();
 request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
```

#### Explanation

##### setTitle
 
Set the title of this download, to be displayed in notifications (if enabled). If no title is given, a default one will be assigned based on the download filename, once the download starts.

##### setDescription

Set a description of this download, to be displayed in notifications (if enabled)

##### setAllowedNetworkTypes

Restrict the types of networks over which this download may proceed. By default, all network types are allowed. Consider using setAllowedOverMetered(boolean) instead, since it's more flexible.

##### allowScanningByMediaScanner （ Api 11 ） 

If the file to be downloaded is to be scanned by MediaScanner, this method should be called before enqueue(Request) is called.

##### setNotificationVisibility （ Api 11 ）

Control whether a system notification is posted by the download manager while this download is running or when it is completed. If enabled, the download manager posts notifications about downloads through the system NotificationManager. By default, a notification is shown only when the download is in progress.

It can take the following values: VISIBILITY_HIDDEN, VISIBILITY_VISIBLE, VISIBILITY_VISIBLE_NOTIFY_COMPLETED.

If set to VISIBILITY_HIDDEN, this requires the permission android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.

### enqueue

Enqueue a new download. 

```
 downloadId = downloadManager.enqueue(request);
```
Return an ID for the download, 

### Register BroadCastReceiver

 Register **BroadCastReceiver** for filter **DownloadManager.ACTION_DOWNLOAD_COMPLETE**
 
```
   IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
   context.registerReceiver(this, filter);
   
   @Override
   public void onReceive(Context context, Intent intent) {
       long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
       listener.downloadComplete(downloadId);
   }
```

### Query

Query data by downloadId. New a Query obj for downloadManager.

```
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(this.downloadId);
    Cursor cursor = downloadManager.query(query);
    
     int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
     if (status == DownloadManager.STATUS_SUCCESSFUL) {
         long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
         Uri uri = downloadManager.getUriForDownloadedFile(id);
         String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
         listener.fileDownloaded(uri, mimeType);
     }
```

### Do what you want 

Open file by other App.

```
 Intent intent = new Intent(Intent.ACTION_VIEW);
 intent.setDataAndType(uri, mimeType);
 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//android 7.0 + should add this flag.  
```

**intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)** 

Allows other Apps to grant read and write access using temporary access permissions
