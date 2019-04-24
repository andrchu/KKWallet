package com.jgd.eoslibrary.ese;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/7 0007 09:56
 * 描述	      ${TODO}
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanField {
    int order();
}
