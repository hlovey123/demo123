
package com.vanzo.manual;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TitleBarUtils {
    public TitleBarUtils(final Activity activity) {
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        Button leftbutton = (Button) activity.findViewById(R.id.title_back);
        leftbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        leftbutton.setText(R.string.leftbutton_text);
        TextView tx = (TextView) activity.findViewById(R.id.title_text);
        tx.setText(activity.getTitle());
    }
}
