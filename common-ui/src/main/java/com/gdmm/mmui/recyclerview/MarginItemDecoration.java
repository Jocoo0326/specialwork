package com.gdmm.mmui.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public MarginItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = spacing;
        outRect.bottom = spacing;
    }
}
