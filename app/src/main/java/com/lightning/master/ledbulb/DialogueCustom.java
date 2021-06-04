package com.lightning.master.ledbulb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Geeta on 20-Mar-18.
 */

public class DialogueCustom {
    // **************************************Connection Dialog**************************************

    public static void dialogue_custom(final Context c, final String title, final String subtitle, final String desc,
                                       final String actiontitle1,
                                       final String actiontitle2,
                                       final boolean action_two_visible,
                                       final int img_src, final String event_action1, final String event_action2,
                                       final @ColorInt int colorId_1, final @ColorInt int colorId_2) {
        // Create custom dialog object
        SharedPreferences sharedPref;
        final String app_url;

        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.dialogue_custom);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_dial_title = (TextView) dialog.findViewById(R.id.tv_dial_title);
        TextView tv_dial_subtitle = (TextView) dialog.findViewById(R.id.tv_dial_subtitle);
        TextView tv_dial_desc = (TextView) dialog.findViewById(R.id.tv_dial_desc);
        android.widget.ImageView img_dial = (android.widget.ImageView) dialog.findViewById(R.id.img_dial);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(c); // Preference manager
        app_url = sharedPref.getString("app_url", "");


        /******************  Custom Dialogue ***********/
        TextView tv_action1 = (TextView) dialog.findViewById(R.id.tv_action1);
        TextView tv_action2 = (TextView) dialog.findViewById(R.id.tv_action2);


        tv_dial_title.setText("" + title);
        tv_dial_subtitle.setText("" + subtitle);
        tv_dial_desc.setText("" + desc);
        tv_action1.setText("" + actiontitle1);
        tv_action2.setText("" + actiontitle2);
        img_dial.setImageResource(img_src);

        tv_action1.setBackgroundColor(colorId_1);
        tv_action2.setBackgroundColor(colorId_2);

        if (action_two_visible) {
            tv_action2.setVisibility(View.VISIBLE);
        } else {
            tv_action2.setVisibility(View.GONE);
        }

        tv_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event_action1.equalsIgnoreCase("exit")) {
                    ((Splash) c).finish();
                } else if (event_action1.equalsIgnoreCase("update")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(app_url));
                    c.startActivity(browserIntent);
                    dialog.cancel();
                    ((Splash) c).finish();

                } else if (event_action1.equalsIgnoreCase("gotointernetsettings")) {
                    c.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    ((Splash) c).finish();
                }else {

                }
            }
        });

        tv_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event_action2.equalsIgnoreCase("exit")) {
                    ((Splash) c).finish();
                } else if (event_action2.equalsIgnoreCase("update")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(app_url));
                    ((Splash) c). startActivity(browserIntent);
                    dialog.cancel();
                } else if (event_action2.equalsIgnoreCase("gotointernetsettings")) {
                    c.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    ((Splash) c).finish();
                }else {

                }
            }
        });
    }
}
