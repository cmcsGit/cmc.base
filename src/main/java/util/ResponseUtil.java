package util;

/**
 * @author cmc
 */
public interface ResponseUtil {

    /**
     * 获取是否成功信息
     * @return
     */
    Boolean getSuccess();

    /**
     * 设置请求是否成功
     * @param success
     */
    void setSuccess(Boolean success);

    /**
     * 获取额外信息
     * @return
     */
    String getMessage();

    /**
     * 设置额外信息
     * @param message
     */
    void setMessage(String message);

    /**
     * 获取数据
     * @return
     */
    String getRetDate();

    /**
     * 设置获取到的数据
     * @param retDate
     */
    void setRetDate(String retDate);

    /**
     * 获取请求经过的时间
     * @return
     */
    Long getPassedTime();

    /**
     * 设置请求经过的时间
     * @param passedTime
     */
    void setPassedTime(Long passedTime);
}
