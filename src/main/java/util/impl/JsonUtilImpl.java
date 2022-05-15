package util.impl;

import net.sf.json.JSONObject;
import util.JsonUtil;

/**
 * @author cmc
 */
public class JsonUtilImpl implements JsonUtil {

    @Override
    public String toStr(Object entity) {
        return JSONObject.fromObject(entity).toString();
    }

    @Override
    public String toStr() {
        return null;
    }
}
