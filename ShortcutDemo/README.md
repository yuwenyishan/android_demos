# Shortcut Demo In Android . (Api 25)

**this module will performance how to use Shortcut in Android 7.1.1**

**https://www.novoda.com/blog/exploring-android-nougat-7-1-app-shortcuts/**

## Part1 ( Static Shortcuts )

#### Use
Navigate to your AndroidManifest.xml and add the following meta-data tag to your main activity:

```
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    <activity android:name=".MainActivity">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
        <category android:name="android.intent.category.DEFAULT" />
      </intent-filter>

      <meta-data
        android:name="android.app.shortcuts"
        android:resource="@xml/shortcuts" />
    </activity>
  </application>
```

In the meta-data tag, the android:resource key corresponds to a resource defined in your res/xml/shortcuts.xml. Here you need to define all of your static shortcuts

```
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android">

    <shortcut
        android:enabled="true"
        android:icon="@drawable/circle_shape"
        android:shortcutDisabledMessage="@string/shortcutDisabledMessage1"
        android:shortcutId="static1"
        android:shortcutLongLabel="@string/static_shortcut_long_label1"
        android:shortcutShortLabel="@string/static_shortcut_short_label1">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.jax.shortcutdemo.MainActivity"
            android:targetPackage="com.jax.shortcutdemo" />
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.jax.shortcutdemo.ShortCut1Activity"
            android:targetPackage="com.jax.shortcutdemo" />>
    </shortcut>
    
    ...
    
</shortcuts>

```
#### introduction
- enabled: As the name states, whether the shortcut is enabled or not. If you decide to disable your static shortcut you could either set this to false or simply remove it from the <shortcuts> set. One use-case where you might want to use this feature is to control which shortcut is disabled by build flavour.
- icon: The icon shown on the left hand side of the shortcut.
- shortcutDisabledMessage: this is the string shown to a user if they try to launch a disabled shortcut pinned to their home screen.
- shortcutLongLabel: This is a longer variant of the shortcut text, shown when the launcher has enough space.
- shortcutShortLabel: This is a concise description of the shortcut.This field is mandatory. This is probably the shortcut text that most users will see on their home screen.
- intent: here you define your intent (or more intents) that your shortcut will open upon being tapped

## Part 2 ( Dynamic Shortcuts )

#### Use

```
 ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
       String label = "shortcut4";
       SpannableStringBuilder colouredLabel = new SpannableStringBuilder(label);
       colouredLabel.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
       Intent main = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class)
               .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       Intent short4 = new Intent();
       short4.setAction(Shortcut4Activity.ACTION);
       ShortcutInfo shortcutInfo3 = new ShortcutInfo
               .Builder(this, "shortcut4")
               .setIcon(Icon.createWithResource(this, R.drawable.circle_shape_blue_deep))
               .setDisabledMessage(getString(R.string.shortcutDisabledMessage4))
               .setLongLabel(getString(R.string.static_shortcut_long_label4))
               .setShortLabel(colouredLabel)
               .setIntents(new Intent[]{main, short4})
               .setRank(4)
               .build();
       shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcutInfo3));
```


## Part 2 ( Wrapping up )

- App Shortcuts are great for exposing actions of your app and bringing back users into your flow
- They can be static or dynamic
- Static shortcuts are set in stone once you define them (you can only update them with an app redeploy)
- Dynamic shortcuts can be changed on the fly
- You can create a back stack of activities for each shortcut
- Shortcuts can be reordered, but only in their respective type
- Static shortcuts will come always at the bottom as they're added first (there's no rank property to be defined on them)
- The labels of the shortcuts contain a CharSequence so you can manipulate them through spans
- You can checkout this blog post's sample app on Github ( https://github.com/Electryc/AppShortcutsDemo )

