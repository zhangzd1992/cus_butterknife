package com.example.zhangzd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 控件点击事件的注解
 * @Author: zhangzd
 * @CreateDate: 2019-12-04 10:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface OnClick {
    int value();   //需要添加点击事件的控件的id值
}
