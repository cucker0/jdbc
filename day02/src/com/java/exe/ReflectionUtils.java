package com.java.exe;

import java.lang.reflect.*;

/**
 * 反射工具
 *
 * 可获取私有变量、泛型类型的Class、属性等
 *
 */
public class ReflectionUtils {
    /**
     * 获取在指定Class对应类的声明父类中，指定索引的泛型参数的类型
     *
     * @param clazz: Class对象
     * @param index: 泛型参数的索引
     * @return: 获取的Class类型
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index < 0 || index >= params.length) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取 指定的Class 定义中声明的父类的索引为0的泛型参数类型
     * 如：public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param clazz: Class
     * @param <T>: 泛型类型
     * @return: 获取的Class类型
     */
    @SuppressWarnings("unchecked")
    public static<T> Class<T> getSuperGenericType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object:
     * @param methodName:
     * @param parameterTypes:
     * @return:
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // 方法methodName不在当前定义的类中，继续向上转型
//                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 把非public的filed 变为可访问
     *
     * @param field:
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 从指定的对象的中获取名为属性名的DeclaredField
     *
     * @param object: 指定的对象
     * @param fieldName: 属性名
     * @return: 获取到的fieldName对应的DeclaredField
     *      如无，则返回null*/
    public static Field getDeclaredField(Object object, String fieldName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // 属性fieldName不在当前定义的类中，继续向上转型
//                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     *
     * @param object: Object对象
     * @param methodName: 方法名
     * @param parameterTypes: 参数类型Class数组
     * @param parameters: 参数数组
     * @return: 调用方法methodName后的返回结果
     * @throws IllegalArgumentException: 找不到名为methodName的方法
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(
            Object object,
            String methodName,
            Class<?>[] parameterTypes,
            Object[] parameters
    ) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object: Object对象
     * @param fieldName: field名
     * @param value: 要给fieldName设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object: 指定的对象
     * @param fieldName: 属性名
     * @return: 获取到的属性对象
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Object result = null;
        Field field =  getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

}
