package util;

import exception.ObjException;

/**
 * @author cmc
 */
public interface BaseType {

    String INT_TYPE = "int";

    String DOUBLE_TYPE = "double";

    String FLOAT_TYPE = "float";

    String BIG_DECIMAL_TYPE = "BigDecimal";

    String STRING_TYPE = "String";

    String DATE_TYPE = "Date";

    String INTEGER_TYPE = "Integer";

    String D_DOUBLE_TYPE = "Double";

    String F_FLOAT_TYPE = "Float";

    /**
     * 两个类型相加
     * @param obj 对比数据
     * @return 相加结果
     * @throws ObjException 相加时产生的异常
     */
    Object add(Object obj) throws ObjException;

    /**
     * 两个数据对比
     * @param obj 对比数据
     * @return 对比结果
     * @throws ObjException 对比时产生的异常
     */
    boolean compare(Object obj) throws ObjException;

    /**
     * 获取数据
     * @return 获取数据实际值
     */
    Object getValue();

    /**
     * 返回整形数据
     * @return 整形数据
     * @throws ObjException
     */
    int intValue() throws ObjException;

    /**
     * 返回双精度浮点型数据
     * @return
     * @throws ObjException
     */
    double doubleValue() throws ObjException;

    /**
     * 返回浮点型数据
     * @return
     * @throws ObjException
     */
    float floatValue() throws ObjException;
}
