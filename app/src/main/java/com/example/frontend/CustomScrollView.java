package com.example.frontend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private float startX;
    private float startY;
    private boolean isScrolling;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                isScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX() - startX);
                float dy = Math.abs(ev.getY() - startY);
                if (dx > dy) {
                    // Horizontal scroll
                    isScrolling = true;
                    return false; // Let child handle
                } else {
                    // Vertical scroll
                    isScrolling = true;
                    return super.onInterceptTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                isScrolling = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrolling) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
