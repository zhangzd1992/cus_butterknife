package com.example.zhangzd.cusbutterknife_library;

import android.app.Activity;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-12-04 10:53
 */
public class ButterKnife {
    public static void bind(Activity target) {
        String activityName = target.getClass().getName();
        try {
            Class<?> bindClass = Class.forName(activityName + "$ViewBinder");

            ViewBinder viewBinder = (ViewBinder) bindClass.newInstance();
            viewBinder.bind(target);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
