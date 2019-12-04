package com.example.zhangzd.cusbutterknife_library;

import android.view.View;

/**
 * @Description: 防抖动点击事件
 * @Author: zhangzd
 * @CreateDate: 2019-12-04 10:49
 */
public abstract class DebouncingOnClickListener implements View.OnClickListener {



    static boolean enabled = true;

    private static final Runnable ENABLE_AGAIN = new Runnable() {
        @Override
        public void run() {
            enabled = true;
        }
    };

    @Override public final void onClick(View v) {
        if (enabled) {
            enabled = false;
            v.post(ENABLE_AGAIN);
            doClick(v);
        }
    }

    protected abstract void doClick(View v);
}
