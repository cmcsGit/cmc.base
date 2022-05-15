package util;

/**
 * @author cmc
 */
public interface JsonUtil {

    /**
     * 将实体转换为json字符串
     * @param entity 实体
     * @return json字符串
     */
    String toStr(Object entity);

    /**
     * 转换为json字符串
     * @return
     */
    String toStr();

}
