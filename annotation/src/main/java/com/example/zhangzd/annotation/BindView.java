package com.example.zhangzd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 控件绑定注解
 * @Author: zhangzd
 * @CreateDate: 2019-12-04 10:37
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value();   //绑定控件的id
}
