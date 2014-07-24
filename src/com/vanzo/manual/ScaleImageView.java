package com.vanzo.manual;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class ScaleImageView extends ImageView {
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int SMALLER = 3;
    static final int BIGGER = 4;
    static final int OPENSCALE = 1;
    static final int OPENTRANS = 2;
    private int mode = NONE;
    private float beforeLength;
    private float afterLength;
    private float scale = 0.05f;
    private int screenW;
    private int screenH;
    private int start_x;
    private int start_y;
    private int stop_x;
    private int stop_y;
    private TranslateAnimation trans;
    private int bmWidth;
    private int bmHeight;
    private Bitmap bitmap;
    private float maxScale = 4.0f;
    private float minScale = 1.0f;
    private int startWidth = 0;
    private float piovtX = 0.5f;
    private float piovtY = 0.5f;
    private int animSwicth = OPENSCALE | OPENTRANS;
    private float[] center;

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, int x, int y) {
        super(context);
        this.setPadding(0, 0, 0, 0);
        screenW = x;
        screenH = y;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        startWidth = 0;
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
        bitmap = bm;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (startWidth == 0)
            startWidth = right - left;
        setRect();
        animSwicth = 0;
        onRebound();
        animSwicth = OPENSCALE | OPENTRANS;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            mode = DRAG;
            stop_x = (int) event.getRawX();
            stop_y = (int) event.getRawY();
            start_x = (int) event.getX();
            start_y = stop_y - getTop();
            if (event.getPointerCount() == 2)
                beforeLength = spacing(event);
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            center = centerPoint(event);
            piovtX = center[0] / getWidth();
            piovtY = center[1] / getHeight();

            center[0] = (center[0] / getWidth()) * scale;
            center[1] = (center[1] / getHeight()) * scale;
            if (spacing(event) > 10f) {
                mode = ZOOM;
                beforeLength = spacing(event);
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_UP:
            mode = NONE;
            setRect();
            if (!onReScale())
                //onRebound();
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                this.setPosition(stop_x - start_x, stop_y - start_y, stop_x
                        + this.getWidth() - start_x,
                        stop_y - start_y + this.getHeight());
                stop_x = (int) event.getRawX();
                stop_y = (int) event.getRawY();
            }
            if (mode == ZOOM) {
                if (spacing(event) > 10f) {
                    afterLength = spacing(event);
                    float gapLenght = afterLength - beforeLength;
                    if (gapLenght == 0) {
                        break;
                    } else if (Math.abs(gapLenght) > 5f) {
                        if (gapLenght > 0) {
                            this.setScale(scale, BIGGER);
                        } else {
                            this.setScale(scale, SMALLER);
                        }
                        beforeLength = afterLength;
                    }
                }
            }
            break;
        default:
            break;
        }
        return true;
    }

    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }

    private void setRect() {
        float scale = Math.min((float) getWidth() / (float) bmWidth,
                (float) getHeight() / (float) bmHeight);
        int w = (int) ((float) bmWidth * scale) + 1;
        int h = (int) ((float) bmHeight * scale) + 1;

        int l = getLeft();
        int t = getTop();
        int r = l + w;
        int b = t + h;
    }

    public boolean onReScale() {
        float scaleX = 1f;
        float scaleY = 1f;
        int width = getWidth();
        int height = getHeight();
        int l, t, r, b;
        if (center == null)
            return false;
        if (getWidth() > startWidth * maxScale) {
            while (getWidth() > startWidth * maxScale) {
                l = this.getLeft() + (int) (center[0] * this.getWidth());
                t = this.getTop() + (int) (center[1] * this.getHeight());
                r = this.getRight()
                        - (int) ((scale - center[0]) * this.getWidth());
                b = this.getBottom()
                        - (int) ((scale - center[1]) * this.getHeight());
                this.setFrame(l, t, r, b);
            }
            scaleX = (float) width / (float) getWidth();
            scaleY = (float) height / (float) getHeight();
        }
        if (getWidth() < startWidth * minScale) {
            while (getWidth() < startWidth * minScale) {
                l = this.getLeft() - (int) (center[0] * this.getWidth());
                t = this.getTop() - (int) (center[1] * this.getHeight());
                r = this.getRight()
                        + (int) ((scale - center[0]) * this.getWidth());
                b = this.getBottom()
                        + (int) ((scale - center[1]) * this.getHeight());
                this.setFrame(l, t, r, b);
            }
            scaleX = (float) width / (float) getWidth();
            scaleY = (float) height / (float) getHeight();
        }

        if (scaleX == 1f && scaleY == 1f)
            return false;
        if ((animSwicth & OPENSCALE) == 0) {
            setRect();
            onRebound();
            return true;
        }
        ScaleAnimation scaleanim = new ScaleAnimation(scaleX, 1f, scaleY, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, piovtX,
                ScaleAnimation.RELATIVE_TO_SELF, piovtY);
        scaleanim.setDuration(300);
        scaleanim.setInterpolator(new AccelerateInterpolator());
        scaleanim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation paramAnimation) {
            }

            @Override
            public void onAnimationRepeat(Animation paramAnimation) {
            }

            @Override
            public void onAnimationEnd(Animation paramAnimation) {
                setRect();
                onRebound();
            }
        });
        this.startAnimation(scaleanim);
        return true;
    }

    public void onRebound() {
        int disX = 0, disY = 0;
        if (getHeight() < screenH) {
            disY = (screenH - getHeight()) / 2 - getTop();
        }
        if (getWidth() < screenW) {
            disX = (screenW - getWidth()) / 2 - getLeft();
        }
        if (getHeight() >= screenH) {
            if (getTop() > 0)
                disY = -getTop();
            if (getBottom() < screenH)
                disY = screenH - getBottom();
        }
        if (getWidth() >= screenW) {
            if (getLeft() > 0)
                disX = -getLeft();
            if (getRight() < screenW)
                disX = screenW - getRight();
        }
        rebound(disX, disY);
    }

    public void rebound(int disX, int disY) {
        if ((animSwicth & OPENTRANS) == 0)
            return;
        trans = new TranslateAnimation(-disX, 0, -disY, 0);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setDuration(300);
        this.startAnimation(trans);
    }

    private void setScale(float temp, int flag) {
        int l = 0, t = 0, r = 0, b = 0;
        if (flag == BIGGER) {
            l = this.getLeft() - (int) (center[0] * this.getWidth());
            t = this.getTop() - (int) (center[1] * this.getHeight());
            r = this.getRight() + (int) ((scale - center[0]) * this.getWidth());
            b = this.getBottom()
                    + (int) ((scale - center[1]) * this.getHeight());
        } else if (flag == SMALLER) {
            l = this.getLeft() + (int) (center[0] * this.getWidth());
            t = this.getTop() + (int) (center[1] * this.getHeight());
            r = this.getRight() - (int) ((scale - center[0]) * this.getWidth());
            b = this.getBottom()
                    - (int) ((scale - center[1]) * this.getHeight());
        }
        this.setFrame(l, t, r, b);
    }

    public void recycle() {
        setImageBitmap(null);
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private float[] centerPoint(MotionEvent event) {
        float[] center = new float[2];
        float x = event.getX(0);
        float y = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);
        center[0] = (x + x1) / 2;
        center[1] = (y + y1) / 2;
        return center;
    }
}