package com.example.movieticketproject.Decorators;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomDecorator extends RecyclerView.ItemDecoration {
    int top,left,right,bottom;

    public CustomDecorator(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = top;
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
    }
}
