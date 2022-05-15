package util.impl;

import exception.ObjException;
import util.BaseType;

import java.math.BigDecimal;

/**
 * @author cmc
 */
public class ObjectUtil implements BaseType {

    private Object boj;

    public ObjectUtil(Object num) {
       boj = num;
    }

    @Override
    public Object add(Object obj) throws ObjException {

        switch (boj.getClass().getSimpleName()) {
            case INT_TYPE:
                boj = (int) boj + (int) obj;
                break;
            case DOUBLE_TYPE:
                boj = (double) boj + (double) obj;
                break;
            case FLOAT_TYPE:
                boj = (float) boj + (float) obj;
                break;
            case BIG_DECIMAL_TYPE:
                boj = ((BigDecimal) boj).add((BigDecimal) obj);
                break;
            case STRING_TYPE:
                boj = ((StringBuffer) boj).append((StringBuffer) obj);
                break;
            case INTEGER_TYPE:
                boj = (Integer) boj + (Integer) obj;
                break;
            case D_DOUBLE_TYPE:
                boj = (Double) boj + (Double) obj;
                break;
            case F_FLOAT_TYPE:
                boj = (Float) boj + (Float) obj;
                break;
            default:
                throw new ObjException("暂不支持的类型");
        }

        return getValue();

    }

    @Override
    public boolean compare(Object obj) throws ObjException {

        return false;
    }

    @Override
    public Object getValue(){
        return boj;
    }

    @Override
    public int intValue() throws ObjException {
        if(int.class.equals(boj.getClass()) ||
                Integer.class.equals(boj.getClass())){

            return (int) boj;
        }else{
            throw new ObjException("类型格式不匹配");
        }
    }

    @Override
    public double doubleValue() throws ObjException {
        if(double.class.equals(boj.getClass()) ||
                Double.class.equals(boj.getClass())){

            return (double)boj;
        }else{
            throw new ObjException("类型格式不匹配");
        }
    }

    @Override
    public float floatValue() throws ObjException {
        if(float.class.equals(boj.getClass()) ||
                Float.class.equals(boj.getClass())){

            return (float)boj;
        }else{
            throw new ObjException("类型格式不匹配");
        }
    }

}
