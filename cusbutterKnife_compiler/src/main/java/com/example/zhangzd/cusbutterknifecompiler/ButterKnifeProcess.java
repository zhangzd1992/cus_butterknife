package com.example.zhangzd.cusbutterknifecompiler;

import com.example.zhangzd.annotation.BindView;
import com.example.zhangzd.annotation.OnClick;
import com.example.zhangzd.cusbutterknifecompiler.utils.EmptyUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @Description: butterknife 注解处理器
 * @Author: zhangzd
 * @CreateDate: 2019-12-04 11:01
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({Constant.ANNOTATION_BINDVIEW, Constant.ANNOTATION_ONCLICK})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ButterKnifeProcess extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<TypeElement, List<VariableElement>> bindViewTempMaps = new HashMap<>();
    private Map<TypeElement, List<ExecutableElement>> onClickTempMaps = new HashMap<>();


    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        typeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!EmptyUtils.isEmpty(set)) {
            //获取带有BindView注解的所有元素
            Set<? extends Element> fieldElements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
            Set<? extends Element> methodElements = roundEnvironment.getElementsAnnotatedWith(OnClick.class);

            //获取注解值存放在临时集合中备用
            valueOfMap(fieldElements, methodElements);
            try {
                createJavaFile();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;
        }
        return false;
    }

    /**
     * 创建Java文件，绑定控件，设置点击事件
     */
    private void createJavaFile() throws Exception {
        if (EmptyUtils.isEmpty(bindViewTempMaps) && EmptyUtils.isEmpty(onClickTempMaps)) {
            return;
        }


        for (TypeElement typeElement : onClickTempMaps.keySet()) {
            if (bindViewTempMaps.get(typeElement) == null) {
                bindViewTempMaps.put(typeElement, new ArrayList<VariableElement>());
            }
        }


        Set<Map.Entry<TypeElement, List<VariableElement>>> entries = bindViewTempMaps.entrySet();
        TypeElement viewBinderTypeElement = elementUtils.getTypeElement(Constant.PATH_VIEWBINDER);
        TypeElement viewTypeElement = elementUtils.getTypeElement(Constant.PATH_VIEW);
        TypeElement clickListenerTypeElement = elementUtils.getTypeElement(Constant.PATH_ONCLICKLISTENER);
        for (Map.Entry<TypeElement, List<VariableElement>> entry : entries) {

            TypeElement keyElement = entry.getKey();
            ClassName className = ClassName.get(keyElement);

            MethodSpec.Builder methodBuild = MethodSpec.methodBuilder(Constant.METHOD_NAME)
                    .returns(void.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(ParameterSpec.builder(className, Constant.TARGET, Modifier.FINAL).build());


            List<VariableElement> fieldList = entry.getValue();
            //添加控件绑定
            for (VariableElement fieldElement : fieldList) {
                BindView bindView = fieldElement.getAnnotation(BindView.class);
                Name fieldName = fieldElement.getSimpleName();
                String methodContent = "$N." + fieldName + " = $N.findViewById($L)";
                //target.tv = target.findViewById(R.id.tv);
                methodBuild.addStatement(methodContent,
                        Constant.TARGET,
                        Constant.TARGET,
                        bindView.value());
            }

            // 添加控件点击事件
            if (onClickTempMaps.get(keyElement) != null) {
                List<ExecutableElement> executableElements = onClickTempMaps.get(keyElement);
                for (ExecutableElement executableElement : executableElements) {

                    OnClick annotation = executableElement.getAnnotation(OnClick.class);

                    /**
                     *  target.findViewById(R.id.tv).setOnClickListener(new DebouncingOnClickListener() {
                     *             @Override
                     *             protected void doClick(View v) {
                     *                 target.tvClick(v);
                     *             }
                     *         });
                     */
                    String content = "$N.findViewById($L).setOnClickListener(new $T()";
                    methodBuild.beginControlFlow(content,
                            Constant.TARGET,
                            annotation.value(),
                            ClassName.get(clickListenerTypeElement));
                    methodBuild.beginControlFlow("public void doClick($T v) ", ClassName.get(viewTypeElement));
                    methodBuild.addStatement("$N."+executableElement.getSimpleName().toString()+"(v)", Constant.TARGET);
                    methodBuild.endControlFlow();
                    methodBuild.endControlFlow(")");
                }
            }


            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get(viewBinderTypeElement), className);

            JavaFile build = JavaFile.builder(
                    className.packageName(),
                    TypeSpec.classBuilder(className.simpleName() + "$ViewBinder")
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(typeName)
                            .addMethod(methodBuild.build())
                            .build()
            ).build();
            build.writeTo(filer);
        }

    }

    private void valueOfMap(Set<? extends Element> fieldElements, Set<? extends Element> methodElements) {
        for (Element element : fieldElements) {
            VariableElement fieldElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) fieldElement.getEnclosingElement();
            if (bindViewTempMaps.get(typeElement) != null) {
                bindViewTempMaps.get(typeElement).add(fieldElement);
            } else {
                List<VariableElement> fieldList = new ArrayList<>();
                fieldList.add(fieldElement);
                bindViewTempMaps.put(typeElement, fieldList);
            }
        }

        for (Element element : methodElements) {
            ExecutableElement methodElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
            if (onClickTempMaps.get(typeElement) != null) {
                onClickTempMaps.get(typeElement).add(methodElement);
            } else {
                List<ExecutableElement> methodList = new ArrayList<>();
                methodList.add(methodElement);
                onClickTempMaps.put(typeElement, methodList);
            }
        }
    }
}
