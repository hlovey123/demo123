package com.vanzo.manual;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarUtils {
    private ActionBar mTitleBar;
    public ActionBarUtils(Context context, final Activity activity, CharSequence charSequence) {
        mTitleBar = activity.getActionBar();
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.title_bar, null);
        Button leftbutton = (Button) view.findViewById(R.id.title_back);
        leftbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        leftbutton.setText(R.string.leftbutton_text);
        TextView tx = (TextView) view.findViewById(R.id.title_text);

        tx.setText(charSequence);
        Button rightbutton = (Button) view.findViewById(R.id.title_menu);
        mTitleBar.setCustomView(view);
        mTitleBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE);
    }
}
