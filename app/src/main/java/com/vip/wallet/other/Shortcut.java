package com.vip.wallet.other;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/11 0011 14:30
 * 描述	      ${TODO}
 */

public class Shortcut {
    private ShortcutListener mShortcutListener;
    private int count = 0;
    private long preTime;

    public Shortcut setShortcutListener(ShortcutListener shortcutListener) {
        mShortcutListener = shortcutListener;
        return this;
    }

    public Shortcut() {
    }

    public Shortcut(ShortcutListener shortcutListener) {
        mShortcutListener = shortcutListener;
    }

    public void click() {
        count++;
        if (count == 5) {
            preTime = System.currentTimeMillis();
        } else if (count == 6) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - preTime < 1000) {
                count = 0;
            }
        } else if (count >= 10) {
            count = 0;
            if (mShortcutListener != null) {
                mShortcutListener.onNext();
            }
        }
    }

    public interface ShortcutListener {
        void onNext();
    }

}
