package com.jax.shortcutdemo;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.util.Collection;
import java.util.Collections;

/**
 * https://www.novoda.com/blog/exploring-android-nougat-7-1-app-shortcuts/
 */
public class MainActivity extends AppCompatActivity {
    ShortcutManager shortcutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortcutManager = getSystemService(ShortcutManager.class);

    }

    public void addShortcut3(View v) {
        /*
         * dynamic shortcut .
         */

        ShortcutInfo shortcutInfo3 = new ShortcutInfo
                .Builder(this, "shortcut3")
                .setIcon(Icon.createWithResource(this, R.drawable.circle_shape_read))
                .setDisabledMessage(getString(R.string.shortcutDisabledMessage3))
                .setLongLabel(getString(R.string.static_shortcut_long_label3))
                .setShortLabel(getString(R.string.static_shortcut_short_label3))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")))
                .setRank(3)
                .build();
        shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcutInfo3));
    }

    public void addShortcut4(View v) {
        /*
         * dynamic shortcut .
         */

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
    }
}
