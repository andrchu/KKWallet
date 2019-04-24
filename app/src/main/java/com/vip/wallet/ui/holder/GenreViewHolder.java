package com.vip.wallet.ui.holder;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.vip.wallet.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class GenreViewHolder extends GroupViewHolder {

    private TextView genreName;
    private ImageView arrow;
    private ImageView icon;

    public GenreViewHolder(View itemView) {
        super(itemView);
        genreName = itemView.findViewById(R.id.lig_chain_name);
        arrow = itemView.findViewById(R.id.lig_arrow);
        icon = itemView.findViewById(R.id.lig_chain_icon);
    }

    public void setGenreTitle(String title) {
        genreName.setText(title);
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    public void setChainIcon(int iconResId) {
        icon.setImageResource(iconResId);
    }
}
