package util.impl;

import com.esotericsoftware.reflectasm.MethodAccess;
import entity.BaseEntity;
import exception.EntityFindException;
import exception.ObjException;
import exception.RepErrorException;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author admin
 */
public abstract class ObjUtil {

    private final static String NO_DEFAULT_FORMAT = "不符合所有默认日期格式";

    private final static String PARAM_CANT_EMPTY = "转换对象不能为空";

    private final static int LEVEL = 3;

    public final static String GET = "get";

    public final static String SET = "set";

    private final static String START_WITH = "startWith";

    private final static String PROJECT = "project";

    private final static String BASE_ENTITY = "BaseEntity";

    private final static int DATE_AMOUNT = 2;

    private final static String DATE_SIGN = "-";

    private final static Pattern INT_REG = Pattern.compile("^[+-]?[0-9]+$");

    private final static Pattern DOUBLE_REG = Pattern.compile("^[-\\+]?[.\\d]*$");

    private static LogUtilImpl logger = new LogUtilImpl();

    /*
        首字母转换为大写
     */
    public static String firstUpper(String str){
        str = str.replaceFirst(str.substring(0,1),str.substring(0,1).toUpperCase());
        return str;
    }

    /*
        首字母转换为小写
     */
    public static String firstLower(String str){
        str = str.replaceFirst(str.substring(0,1),str.substring(0,1).toLowerCase());
        return str;
    }

    /*
        获得类的所有GET方法
     */
    public static List<Method> classToGetMethodList(Class cs) {
        return classToMethodCollection(cs,GET);
    }

    /*
        获得类的所有SET方法
     */
    public static List<Method> classToSetMethodList(Class cs) {
        return classToMethodCollection(cs,SET);
    }

/*    public static List<Method> classToStartWithMethodList(Class cs, String beginSeq){
        return classToMethodList(cs, START_WITH , beginSeq);
    }*/

    /*
        获取类中的指定方法
     */
    public static List<Method> classToMethodList(Class cs,String type) {
        return classToMethodCollection(cs,type);
    }

    private static List<Method> classToMethodCollection(Class cs , String type){

        List<Method> methods = new ArrayList<>();
        logger.loggerInfo(String.format("start translate class %s to method list", cs.getName()));

        while (!Object.class.equals(cs)){
            List<Method> methodT = Arrays.asList(cs.getDeclaredMethods());
            methodT = methodT.stream().filter(method -> method.getName().startsWith(type)).collect(Collectors.toList());
            methods.addAll(methodT);
            cs = cs.getSuperclass();
        }

        return methods;

    }

    private static List<Field> classToFieldCollection(Class cs){

        List<Field> fields = new ArrayList<>();
        logger.loggerInfo(String.format("start translate class %s to field list", cs.getName()));

        while(!Object.class.equals(cs)){
            fields.addAll(Arrays.asList(cs.getDeclaredFields()));
            cs = cs.getSuperclass();
        }

        return fields;
    }

    public static Map<String,Method> classToMethodMap(Class cs, String type){

        List<Method> methods = classToMethodCollection(cs,type);
        Map<String, Method> map = methods.stream().
                collect(Collectors.toMap(key->firstLower(key.getName().replaceFirst(type,"")),value->value));

        return map;
    }

    public static Map<String,Field> classToFieldMap(Class cs){
        return classToFieldCollection(cs).stream().collect(Collectors.toMap(Field::getName, field->field));
    }

    public static List<Field> classToFieldList(Class cs){
        return classToFieldCollection(cs);
    }


    public static Map<String,String> GetColumnNameMap(Class cs){

        Map<String,String> map = new HashMap<>();

        while(!Object.class.equals(cs)) {
            Field[] fields = cs.getDeclaredFields();
            map.putAll(Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Column.class)).
                    collect(Collectors.toMap(Field::getName, value->value.getAnnotation(Column.class).name())));
            cs = cs.getSuperclass();
        }
        return map;
    }

    public static String[] splitAllSymbol(String str){
        String[] chars = new String[]{",", "[+]", "，"};
        for (String b : chars) {
            str = str.replaceAll(b, "%");
        }
        return str.split("%");
    }

    @SuppressWarnings("unchecked")
    public static <T> T objectArrayToObject(Object[] array,String[] arrayName,Class cs){

        Map<String,Method> setMethod = classToMethodMap(cs,SET);
        T entity = null;

        try {
            entity = (T) cs.newInstance();
            int length = array.length;
            for(int i = 0; length > i; i ++) {
                setMethod.get(arrayName[i]).invoke(entity,array[i]);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return entity;
    }

//
//    public static ProjectQuery getProjectQuery(JSONObject proJson){
//        ProjectQuery query = new ProjectQuery();
//        query.setTableList(proJson.getString("tableList"));
//        query.setReturnSql((Boolean) proJson.get("returnSql"));
//        query.setLastDates( (List<String>)proJson.get("lastDates"));
//        return query;
//    }

    /*
        将JSON数组转化为若干个实例
     */
    public static <T> List<T> getTableQueryParam(T entity, JSONArray array) throws EntityFindException {
        JSONObject item;
        T obj;
        Map<String,Method> setMethod = classToMethodMap(entity.getClass(),SET);
        Map<String,Field> fieldMap = classToFieldMap(entity.getClass());
        List<T> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for(int i = 0;array.size() > i;i++){
            item = (JSONObject) array.get(i);
            Set<String> keySet = item.keySet();
            try {
                obj = (T) entity.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new EntityFindException(e.getMessage(), "新建实例出错");
            }
            for(String key:keySet){
                if("Version__".equals(key)) {
                    continue;
                }
                try {
                    if(item.get(key) == null || (item.get(key) instanceof JSONNull))
                        continue;
                    if(fieldMap.get(firstLower(key)).getType() == BigDecimal.class)
                        setMethod.get(firstLower(key)).invoke(obj,new BigDecimal((Double)item.get(key)));
                    else if(fieldMap.get(firstLower(key)).getType() == Date.class)
                        setMethod.get(firstLower(key)).invoke(obj,sdf.parse(item.getString(key)));
                    else
                        setMethod.get(firstLower(key)).invoke(obj,item.get(key));
                } catch (Exception e) {
                    throw new EntityFindException(String.format("字段%s解析出错",key));
                }
            }
            list.add(obj);
        }
        return list;
    }

    public static String getFullAttr(String text,int index){
        int next = text.indexOf("\"",index);
        char[] chars = text.toCharArray();
        int pre = 0;
        for(int i = index; i > -1;i--){
            if(chars[i] == '\"'){
                pre = i;
                break;
            }
        }
        return text.substring(pre,next);
    }

    public static Date parseDate(String dateStr) throws ObjException {

        dateStr = dateStr.replaceAll("T"," ").replaceAll("/",DATE_SIGN).replaceAll("'","").trim();

        String sdfStr = "yyyy-MM-dd";

        SimpleDateFormat sdf;

        if(countOfSubStr(dateStr,DATE_SIGN) != DATE_AMOUNT){
            throw new ObjException(NO_DEFAULT_FORMAT);
        }

        switch (countOfSubStr(dateStr,":")){
            case 0:sdf = new SimpleDateFormat(sdfStr);break;
            case 1:sdf = new SimpleDateFormat(sdfStr + " hh:mm");break;
            case 2:sdf = new SimpleDateFormat(sdfStr + " hh:mm:ss");break;
            default:sdf = new SimpleDateFormat(sdfStr + " hh:mm:ss.Zsss");break;
        }

        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new ObjException( dateStr + NO_DEFAULT_FORMAT);
        }

    }

    /*
        统计子字符串个数
     */
    public static int countOfSubStr(String str,String subStr){

        int count = 0;
        int mark;

        while((mark = str.indexOf(subStr)) != -1) {
            str = str.substring(mark + subStr.length());
            count++;
        }
        return count;
    }

    /*
        类型转化(特定条件)
     */
    public static <T,E> T repTo(Class ncs,E from) throws RepErrorException {
        return repTo(ncs,from,new RepConfig());
    }

    @SuppressWarnings("unchecked")
    public static <T,E> T repTo(Class ncs,E from,RepConfig config) throws RepErrorException {
        List<E> fromList = new ArrayList<>();
        fromList.add(from);
        List<T> tpList = repTo(fromList,ncs,config);

        if(tpList == null || tpList.size() == 0){
            try {
                return (T) ncs.newInstance();
            } catch (IllegalAccessException | InstantiationException e){
                throw new RepErrorException(e.getMessage(),
                        String.format("!------%s转化失败",from.getClass().getName()));
            }
        }

        return tpList.get(0);
    }

    @SuppressWarnings("unchecked")
    public static <T,E> List<T> repTo(List<E> from,Class ncs,RepConfig config) throws RepErrorException{

        if(ncs == null || from == null || from.size() == 0){
            return new ArrayList<>();
        }

        MethodAccess access = MethodAccess.get(from.get(0).getClass());
        MethodAccess setAcc = MethodAccess.get(ncs);

        List<String> setNames = Arrays.stream(ncs.getDeclaredFields()).
                map(Field::getName).collect(Collectors.toList());

        Map<String,Class> aimClass = classToFieldList(ncs).stream()
                .collect(Collectors.toMap(Field::getName, Field::getType));

        Map<String,Class> map = classToFieldList(from.get(0).getClass()).stream().
                filter(item -> setNames.contains(item.getName())).
                collect(Collectors.toMap(item->firstUpper(item.getName()), Field::getType));


        List<T> lists = new ArrayList<>();

        for(E item : from){
            try {
                T entity = (T) ncs.newInstance();
                map.forEach((key,value) -> {
                    try {
                        if (BigDecimal.class.equals(value)) {
                            BigDecimal dec = (BigDecimal) access.invoke(item, GET + key);
                            setAcc.invoke(entity, SET + key, decimalObject(dec, aimClass.get(firstLower(key)), config));
                        } else if (Date.class.equals(value)) {
                            Date date = (Date) access.invoke(item, GET + key);
                            setAcc.invoke(entity, SET + key, dateObject(date,aimClass.get(firstLower(key)), config));
                        } else if (Timestamp.class.equals(value)) {
                            Timestamp dateTime = (Timestamp) access.invoke(item, GET + key);
                            setAcc.invoke(entity, SET + key, config.getFormatDate().format(dateTime));
                        } else if(String.class.equals(value)){
                            String str = (String) access.invoke(item, GET + key);
                            setAcc.invoke(entity, SET + key, stringObject(str, aimClass.get(firstLower(key)),config));
                        } else {
                            setAcc.invoke(entity, SET + key, access.invoke(item, GET + key));
                        }
                    } catch (ObjException e) {
                        e.printInfo(key,value);
                    }

                });
                lists.add(entity);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RepErrorException(e.getMessage(),
                        String.format("%s 创建实例失败", item.getClass().getName()));
            }
        }

        return lists;
    }

    public static <T,E> List<T> repTo(List<E> from,Class ncs,Map<String,String> entMap){

        return null;
    }

    public static <T,E> List<T> repTo(List<E> from,Class ncs) throws RepErrorException {
        return repTo(from,ncs,new RepConfig());
    }

    private static void baseCheck(Class cs) throws ObjException {
        if(cs == null){
            throw new ObjException("转换至的类型不能为Null");
        }
    }

    /*
        日期类型转换
     */
    private static Object dateObject(Date date,Class toc,RepConfig config) throws ObjException {

        if(date == null && !Date.class.equals(toc)){
            throw new ObjException("数据源日期为Null");
        }
        baseCheck(toc);

        if(Date.class.equals(toc)){
            return date;
        } else if(long.class.equals(toc) || Long.class.equals(toc)){
            return date.getTime();
        } else if(Timestamp.class.equals(toc)){
            return new Timestamp(date.getTime());
        } else if(String.class.equals(toc)){
            return config.getFormatDate().format(date);
        } else if(Calendar.class.equals(toc)){
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } else {
            throw new ObjException(toc.getSimpleName() + "是Date不能转换为的类型");
        }
    }

    /*
        大数类型转换
     */
    private static Object decimalObject(BigDecimal dec,Class toc,RepConfig config) throws ObjException {

        dec = (dec == null || config.getDefaultDecimal().compareTo(dec) == 0)?
                config.getDefaultDecimal():dec;
        baseCheck(toc);

        if(BigDecimal.class.equals(toc)){
            return dec.setScale(config.getAccuracy(),config.getScaleType());
        } else if(int.class.equals(toc) || Integer.class.equals(toc)) {
            return dec.intValue();
        } else if (long.class.equals(toc) || Long.class.equals(toc)) {
            return dec.longValue();
        } else if (double.class.equals(toc) || Double.class.equals(toc)) {
            return dec.doubleValue();
        } else if (float.class.equals(toc) || Float.class.equals(toc)) {
            return dec.floatValue();
        } else if (String.class.equals(toc)) {
            return dec.setScale(config.getAccuracy(), config.getScaleType()).toString();
        } else if (boolean.class.equals(toc) || Boolean.class.equals(toc)) {
            return RepConfig.zero.compareTo(dec) <= 0;
        } else {
            throw new ObjException(toc.getSimpleName() + "是Decimal不能转换为的类型");
        }
    }

    /*
        字符串类型转换
     */
    private static Object stringObject(String str, Class toc, RepConfig config) throws ObjException {

        if(str == null){
            str = "";
        }

        baseCheck(toc);

        if(String.class.equals(toc)){
            return str;
        }else if(int.class.equals(toc) || Integer.class.equals(toc)){
            if(isInteger(str)) {
                return Integer.parseInt(str);
            }else {
                throw new ObjException( str + "不符合整形格式,不能转换为整数");
            }
        } else if(double.class.equals(toc) || Double.class.equals(toc)) {
            if(isDouble(str)) {
                return Double.parseDouble(str);
            } else {
                throw new ObjException( str + "不符合实数格式,不能转换为double型");
            }
        } else if(float.class.equals(toc) || Float.class.equals(toc)) {
            if(isDouble(str)) {
                return Float.parseFloat(str);
            } else {
                throw new ObjException( str + "不符合实数格式,不能转换为int型");
            }
        } else if(BigDecimal.class.equals(toc)){
            if(isDouble(str)) {
                return new BigDecimal(str);
            } else {
                throw new ObjException( str + "不符合实数格式,不能转换为BigDecimal型");
            }
        } else if(Date.class.equals(toc)){
            return parseDate(str);
        } else if(Timestamp.class.equals(toc)){
            Date date = parseDate(str);
            return new Timestamp(date.getTime());
        } else if(JSONObject.class.equals(toc)){
            return JSONObject.fromObject(str);
        } else if(JSONArray.class.equals(toc)){
            return JSONArray.fromObject(str);
        } else if(boolean.class.equals(toc) || Boolean.class.equals(toc)){
            return Boolean.valueOf(str);
        } else{
            throw new ObjException("无法为" + toc.getSimpleName() + "找到默认的转换类型");
        }
    }

    /**
     * its an empty function with nothing work
     */
    private static void doNothing(){
        /* // */

        /**
         * there is nothing happened here
         * just for an empty function
         * work in an exception or
         * an unFinished function
         */
    }

    public static <T extends BaseEntity> Map<String,Object> entityToMap(T entity) {

        Map<String,Method> getMethod = classToMethodMap(entity.getClass(),GET);
        List<Field> fields = classToFieldList(entity.getClass());

        Map<String,Object> map =  fields.stream().filter(field -> {
            Object obj;
            try {
                obj = getMethod.get(field.getName()).invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return false;
            }
            return !(obj == null || obj.equals(0) || obj.equals(false));
        }).collect(Collectors.toMap(Field::getName, field -> {
            try {
                return getMethod.get(field.getName()).invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }));

        return map;

    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToObject(Class cs,String jsonStr) throws ObjException {
        try {
            JSONObject json = JSONObject.fromObject(jsonStr);
            return (T) JSONObject.toBean(json,cs);
        } catch (Exception e) {
            throw new ObjException("字符串转换为对象失败:" + e.getMessage());
        }
    }

    public static boolean isInteger(String input){
        Matcher mer = INT_REG.matcher(input);
        return mer.find();
    }

    public static boolean isDouble(String str){
        Matcher mer = DOUBLE_REG.matcher(str);
        return mer.find();
    }

    public static boolean isListEmpty(List list){

        if(list == null || list.isEmpty()){
            return true;
        }

        boolean boo = true;
        for (Object aList : list) {
            if (aList != null) {
                boo = false;
            }
        }

        return boo;

    }
}
