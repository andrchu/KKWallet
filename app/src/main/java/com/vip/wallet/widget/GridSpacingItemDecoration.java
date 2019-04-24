package com.vip.wallet.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.R.attr.spacing;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/12/20 18:30   <br/><br/>
 * 描述:	      ${TODO}
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int vertical;
    private int horizontal;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int vertical, int horizontal, boolean includeEdge) {
        this.spanCount = spanCount;
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * horizontal / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * horizontal / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = vertical;
            }
            outRect.bottom = horizontal; // item bottom
        } else {
            outRect.left = column * horizontal / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = horizontal - (column + 1) * horizontal / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = vertical; // item top
            }
        }
    }
}
