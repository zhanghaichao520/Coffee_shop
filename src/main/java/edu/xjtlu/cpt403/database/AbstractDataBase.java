package edu.xjtlu.cpt403.database;

import edu.xjtlu.cpt403.util.DateUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractDataBase<T> {
    final String path = "src/main/resources/Database.xlsx";
    abstract List<T> selectAll() throws Exception;

    abstract boolean insert(T object, boolean idIncrAuto) throws Exception;

    RowData convert(T object) throws Exception {
        RowData rowData = new RowData();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field :fields) {
            field.setAccessible(true);
            rowData.put(field.getName(),String.valueOf(field.get((object))));
        }

        Field[] superFields =  object.getClass().getSuperclass().getDeclaredFields();
        for (Field field :superFields) {
            field.setAccessible(true);
            rowData.put(field.getName(),String.valueOf(field.get((object))));
        }
        return rowData;
    }


    T convert(RowData rowData, T instance) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        for(Map.Entry<String, String> entry : rowData.entrySet()){
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            if (StringUtils.isEmpty(mapKey) || mapValue == null || StringUtils.isEmpty(mapValue.toString())) {
                continue;
            }
            // 这里使用暴力访问方式也行
            PropertyDescriptor propertyDescriptor = null;
            propertyDescriptor = new PropertyDescriptor(mapKey, instance.getClass());
            Method setMethod = propertyDescriptor.getWriteMethod();
            // 已知咱们都是调用set方法  且都只有一个参数 所以获取set方法的第一个参数的参数类型
            setMethod.invoke(instance, AbstractDataBase.getValue(mapValue,setMethod.getParameterTypes()[0]));
        }
        return instance;
    }


    static Object getValue(Object value,Class type){
        if (value != null){
            if (type.isAssignableFrom(String[].class))
                return toStringArray(value);
            if (type.isAssignableFrom(Integer[].class))
                return toIntegerArray(value);
            else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class) )
                return toInteger(value);
            if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class))
                return toDouble(value);
            else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class))
                return toBoolean(value);
            else if (type.isAssignableFrom(String.class))
                return toString(value);
            else if (type.isAssignableFrom(Date.class))
                return toDate(value);
        }
        return null;
    }

    private static String[] toStringArray(Object value){
        return value.toString().split(",");
    }

    private static Integer[] toIntegerArray(Object value){
        String[] stringArray = toStringArray(value);
        Integer[] intArray = new Integer[stringArray.length];
        for (int i = 0; i < stringArray.length; i++){
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }

    private static Integer toInteger(Object value){
        return Integer.parseInt(value.toString());
    }

    private static Double toDouble(Object value){
        return Double.parseDouble(value.toString());
    }

    private static String toString(Object value){
        return value.toString();
    }

    private static Date toDate(Object value){
        return DateUtils.parseDate(value.toString());
    }

    private static Boolean toBoolean(Object value){
        return Boolean.parseBoolean(value.toString());
    }
}
