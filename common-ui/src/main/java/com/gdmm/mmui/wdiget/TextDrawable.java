package com.gdmm.mmui.wdiget;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.njgdmm.diesel.widget.R;

/**
 * @Description:
 * @author: Created by martin on 2017/4/1.
 */
@SuppressLint("AppCompatCustomView")
public class TextDrawable extends TextView {

    private final int LENGTH = 4;

    private Drawable[] drawables = new Drawable[LENGTH];

    //icon 宽高
    private int drawableW;
    private int drawableH;

    public TextDrawable(Context context) {
        this(context, null);
    }

    public TextDrawable(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextDrawable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextDrawable, defStyleAttr, 0);
        try {
            drawableW = a.getDimensionPixelSize(R.styleable.TextDrawable_icon_width, 0);
            drawableH = a.getDimensionPixelSize(R.styleable.TextDrawable_icon_height, 0);
        } finally {
            a.recycle();
        }

        initDrawable();

        setCompoundDrawables();
    }

    private void initDrawable() {
        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = getCompoundDrawables()[i];
        }
    }

    private void setCompoundDrawables() {
        for (int i = 0; i < drawables.length; i++) {

            if (null == drawables[i])
                continue;

            int w = drawableW == 0 ? drawables[i].getIntrinsicWidth() : drawableW;
            int h = drawableH == 0 ? drawables[i].getIntrinsicHeight() : drawableH;


            drawables[i].setBounds(0, 0, w, h);
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    public void setCompoundDrawables(Drawable[] drawables) {
        if (this.drawables.length != drawables.length)
            return;
        this.drawables = drawables;
        setCompoundDrawables();
    }

    public void setCompoundDrawables(@DrawableRes int resIds[]) {
        if (this.drawables.length != resIds.length)
            return;

        convertDrawables(resIds);
        setCompoundDrawables();
    }

    public void setLeftDrawable(@NonNull Drawable drawable) {
        drawables[0] = drawable;
        setCompoundDrawables();
    }

    public void setLeftDrawable(@NonNull Drawable drawable, int drawableW, int drawableH) {
        this.drawableH = drawableH;
        this.drawableW = drawableW;
        drawables[0] = drawable;
        setCompoundDrawables();
    }

    public void setTopDrawable(@NonNull Drawable drawable) {
        drawables[1] = drawable;
        setCompoundDrawables();
    }

    public void setRightDrawable(@NonNull Drawable drawable) {
        drawables[2] = drawable;
        setCompoundDrawables();
    }

    public void setBottomDrawable(@NonNull Drawable drawable) {
        drawables[3] = drawable;
        setCompoundDrawables();
    }

    /***
     * 设置icon是否显示
     * @param visible true 显示;false 不显示
     */
    public void setIconVisible(boolean visible) {

        Drawable[] tempDrawables = new Drawable[LENGTH];
        for (int i = 0; i < drawables.length; i++) {
            if (!visible) {
                tempDrawables[i] = visible ? drawables[i] : null;
            }
        }
        setCompoundDrawables();
    }

    private void convertDrawables(@DrawableRes int[] resIds) {
        for (int i = 0; i < resIds.length; i++) {
            this.drawables[i] = getDrawable(getContext(), resIds[i]);
        }
    }
}
