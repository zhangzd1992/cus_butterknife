package com.example.zhangzd.cusbutterkife;

import android.view.View;
import com.example.zhangzd.cusbutterknife_library.DebouncingOnClickListener;
import com.example.zhangzd.cusbutterknife_library.ViewBinder;
import java.lang.Override;

public class MainActivity$ViewBinder implements ViewBinder<MainActivity> {
  @Override
  public void bind(final MainActivity target) {
    target.tv = target.findViewById(2131165326);
    target.findViewById(2131165326).setOnClickListener(new DebouncingOnClickListener() {
      public void doClick(View v)  {
        target.tvClick(v);
      }
    } );
    target.findViewById(2131165218).setOnClickListener(new DebouncingOnClickListener() {
      public void doClick(View v)  {
        target.btClick(v);
      }
    } );
  }
}
