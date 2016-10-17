package com.hnsun.myaccount.util.data.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不序列化字段
 * @author hnsun
 * @date 2016/09/19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD }) //注解在字段上
public @interface NotSerialAnnotation {

}
