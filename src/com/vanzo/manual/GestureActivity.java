package com.vanzo.manual;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class GestureActivity extends Activity implements OnGestureListener,
        OnClickListener {

    ViewFlipper flipper;

    GestureDetector detector;

    Animation[] animations = new Animation[4];
    ArrayList<Integer> mTempList = new ArrayList<Integer>();
    final int FLIP_DISTANCE = 50;

    TextView mCurrentPage;
    ImageButton mPrev;
    ImageButton mNext;
    Button mFirst;
    Button mLast;
    Button mBack;
    int mIndex = 0;
    static float xS = 1.0f;
    static float yS = 1.0f;

    ArrayList<Integer> mImageList = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture);

        detector = new GestureDetector(this);
        mCurrentPage = (TextView) findViewById(R.id.current_page_title);
        mBack = (Button) findViewById(R.id.title_back);
        mBack.setOnClickListener(this);
        mPrev = (ImageButton) findViewById(R.id.show_prev);
        mPrev.setOnClickListener(this);
        mNext = (ImageButton) findViewById(R.id.show_next);
        mNext.setOnClickListener(this);
        mFirst = (Button) findViewById(R.id.first_button);
        mFirst.setOnClickListener(this);
        mLast = (Button) findViewById(R.id.last_button);
        mLast.setOnClickListener(this);

        Intent intent = getIntent();
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        mCurrentPage.setText(String.valueOf(mIndex + 1));
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        String country = l.getCountry().toLowerCase();
        final String[] extras;
        if("id".equals(country)){
            extras = getResources().getStringArray(R.array.image_indonesian);
        } else {
        	extras = getResources().getStringArray(R.array.image_english);
        }
        for (String extra : extras) {
            int res = getResources().getIdentifier(extra, "drawable",
                    getApplication().getPackageName());
            mTempList.add(res);
        }
        flipper.addView(addImageView(mTempList.get(0)));
        flipper.setDisplayedChild(mIndex - 1);

        animations[0] = AnimationUtils.loadAnimation(this, R.anim.right_in);
        animations[1] = AnimationUtils.loadAnimation(this, R.anim.right_out);
        animations[2] = AnimationUtils.loadAnimation(this, R.anim.left_in);
        animations[3] = AnimationUtils.loadAnimation(this, R.anim.left_out);
    }

    private View addImageView(int resId) {
        ScaleImageView imageView = new ScaleImageView(this);
        imageView.setImageResource(resId);
        imageView.setLayoutParams(new ViewFlipper.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void showNextPage() {
        if (flipper.getChildCount() >= 2) {
            flipper.removeViewAt(0);
        }
        if (mIndex < mTempList.size() - 1) {
            ++mIndex;
        }
        flipper.addView(addImageView(mTempList.get(mIndex)));
    }

    public void showPrevPage() {
        if (flipper.getChildCount() >= 2) {
            flipper.removeViewAt(0);
        }
        if (mIndex > 0) {
            mIndex -= 1;
        }
        flipper.addView(addImageView(mTempList.get(mIndex)));

    }
    
    public void showLastPage() {
        if (flipper.getChildCount() >= 2) {
            flipper.removeViewAt(0);
        }
        if (mIndex < mTempList.size()) {
            mIndex = mTempList.size() - 1;
        }
        Log.i("hewei", "addImageView>>>" + mIndex);
        flipper.addView(addImageView(mTempList.get(mTempList.size() - 1)));
    }

    public void showFirstPage() {
        if (flipper.getChildCount() >= 2) {
            flipper.removeViewAt(0);
        }
        Log.i("hewei", "??????????" + mIndex);
        if (mIndex > 0) {
            mIndex = 0;
        }
        Log.i("hewei", "addImageView???" + mIndex);
        flipper.addView(addImageView(mTempList.get(0)));
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
            float velocityX, float velocityY) {
//        if (event1.getX() - event2.getX() > FLIP_DISTANCE) {
//            showPrevPage();
//            flipper.setInAnimation(animations[0]);
//            flipper.setOutAnimation(animations[1]);
//            flipper.showPrevious();
//            mCurrentPage.setText(String.valueOf(flipper.getDisplayedChild()));
//            return true;
//        } else if (event2.getX() - event1.getX() > FLIP_DISTANCE) {
//            showNextPage();
//            flipper.setInAnimation(animations[2]);
//            flipper.setOutAnimation(animations[3]);
//            flipper.showNext();
//            mCurrentPage.setText(String.valueOf(flipper.getDisplayedChild()));
//            return true;
//        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
            float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mPrev.getId()) {
            showPrevPage();
            flipper.setInAnimation(animations[0]);
            flipper.setOutAnimation(animations[1]);
            flipper.showPrevious();
        } else if (v.getId() == mNext.getId()) {
            showNextPage();
            flipper.setInAnimation(animations[2]);
            flipper.setOutAnimation(animations[3]);
            flipper.showNext();
        } else if (v.getId() == mFirst.getId()) {
            showFirstPage();
            flipper.setInAnimation(animations[2]);
            flipper.setOutAnimation(animations[3]);
            flipper.setDisplayedChild(1);
        } else if (v.getId() == mLast.getId()) {
            showLastPage();
            flipper.setInAnimation(animations[2]);
            flipper.setOutAnimation(animations[3]);
            flipper.setDisplayedChild(1);
        } else if (v.getId() == mBack.getId()) {
            finish();
        }
        mCurrentPage.setText(String.valueOf(mIndex + 1));
    }
}