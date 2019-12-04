package com.example.zhangzd.cusbutterkife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zhangzd.annotation.BindView;
import com.example.zhangzd.annotation.OnClick;
import com.example.zhangzd.cusbutterknife_library.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tv.setText("我是更新后的我文字");
    }

    @OnClick(R.id.tv)
    public void tvClick(View view) {
        Log.e("aaa" ,"aaaaaaaaaaaaaaaaa");
    }


    @OnClick(R.id.bt)
    public void btClick(View view) {

        Log.e("aaa" ,"===================");
    }
}
