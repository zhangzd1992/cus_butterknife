# cus_butterknife
利用apt + javapoet 实现控件动态绑定
# 项目结构介绍
  annotation 定义注解 包含BindView（控件绑定注解）和 OnClick(点击事件注解)
  cusbutterKnife_compiler 注解处理器，包含注解处理逻辑，以及利用Javapoet穿件辅助文件
  cusbutterknife_library 帮助模块，对外提供Bind方法
