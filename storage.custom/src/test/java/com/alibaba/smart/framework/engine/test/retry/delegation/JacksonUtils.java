//package com.alibaba.smart.framework.engine.test.retry.delegation;
//
//import com.alibaba.smart.framework.engine.common.util.StringUtil;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.type.TypeFactory;
//import com.google.common.collect.Sets;
//
//import java.util.List;
//import java.util.Set;
//
///**
// * Created with IntelliJ IDEA.
// * User: wuqi.zlx
// * Date: 2017/10/24
// * Time: 下午2:21
// */
//@SuppressWarnings("unchecked")
//public class JacksonUtils {
//
//    private static Set<Class> polymorphicClassSet = Sets.newHashSet();
//
//    /**
//     * simplify Mapper
//     */
//    private static final ObjectMapper simplifyObjectMapperUseField = new ObjectMapper();
//
//    private static void attachSimplify(ObjectMapper attached) {
//        attached.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        /**
//         * NON_EMPTY can't setting, or else Long = 0L will be filtered
//         */
//        attached.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        attached.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
//    }
//
//    static {
//        simplifyObjectMapperUseField.setVisibility(
//                simplifyObjectMapperUseField.getSerializationConfig().
//                        getDefaultVisibilityChecker().
//                        withFieldVisibility(JsonAutoDetect.Visibility.ANY).
//                        withGetterVisibility(JsonAutoDetect.Visibility.NONE).
//                        withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
//        );
//        /**
//         * simplify Mapper
//         */
//        attachSimplify(simplifyObjectMapperUseField);
//    }
//
//
//    /**
//     * exception throw to the upper level
//     */
//    public static String serialize(Object serialized) {
//        if (serialized == null) {
//            return null;
//        }
//        try {
//            return simplifyObjectMapperUseField.writeValueAsString(serialized);
//        } catch (Exception e) {
//            throw new RuntimeException( e);
//        }
//    }
//
//    public static <T> T deserialize(String jsonStr, Class<T> transferClass) {
//
//        try {
//            return simplifyObjectMapperUseField.readValue(jsonStr, transferClass);
//        } catch (Exception e) {
//            throw new RuntimeException( e);
//        }
//    }
//
//
//    public static <T> List<T> deserializeList(String jsonStr, TypeReference reference) {
//
//        try {
//            return simplifyObjectMapperUseField.readValue(jsonStr, reference);
//        } catch (Exception e) {
//            throw new RuntimeException( e);
//        }
//    }
//
//    ///**
//    // * JacksonUtils.deserializeList(
//    // * value,
//    // * TypeFactory.defaultInstance().constructParametrizedType(
//    // * List.class, List.class, collectionElementType));
//    // */
//    //public static <T> List<T> deserializeList(String jsonStr, Class<T> elementClass) {
//    //    if (StringUtils.isBlank(jsonStr)) {
//    //        return null;
//    //    }
//    //    return deserializeList(
//    //            jsonStr,
//    //            TypeFactory.defaultInstance().constructParametricType(
//    //                    List.class, List.class, elementClass)
//    //    );
//    //}
//    //
//    //public static <T> List<T> deserializeList(String jsonStr, JavaType javaType) {
//    //    if (StringUtils.isBlank(jsonStr)) {
//    //        return null;
//    //    }
//    //    try {
//    //        return simplifyObjectMapperUseField.readValue(jsonStr, javaType);
//    //    } catch (Exception e) {
//    //        throw new DeSerializeException(
//    //                "deserializeList error, type is " + javaType.getTypeName(), e);
//    //    }
//    //}
//}