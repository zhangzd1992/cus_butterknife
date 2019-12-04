//package com.example.zhangzd.cusbutterkife;
//
//import android.view.View;
//
//import com.example.zhangzd.cusbutterknife_library.DebouncingOnClickListener;
//import com.example.zhangzd.cusbutterknife_library.ViewBinder;
//
//import java.lang.reflect.Field;
//
///**
// * @Description:
// * @Author: zhangzd
// * @CreateDate: 2019-12-04 11:27
// */
//public class MainActivity$ViewBinder implements ViewBinder<MainActivity> {
//    @Override
//    public void bind(final MainActivity target) {
//        target.tv = target.findViewById(R.id.tv);
//        target.tv = target.findViewById(R.id.tv);
//        target.tv = target.findViewById(R.id.tv);
//        target.tv = target.findViewById(R.id.tv);
//
//
//        target.findViewById(R.id.tv).setOnClickListener(new DebouncingOnClickListener() {
//            @Override
//            protected void doClick(View v) {
//                target.tvClick(v);
//            }
//        });
//
//    }
//}
