package util;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cmc
 */

public interface HttpUtil{

    /**
     * 返回数据
     * @param response response信息
     */
    void writeBack(HttpServletResponse response);
}
