package exception;

/**
 * @author cmc
 */
public class ObjException extends BaseException{
    public ObjException(String message) {
        super(message);
    }

    public void printInfo(String key,Class cs){
        super.info("跳过字段" + key + ",原类型为" + cs.getName());
    }
}
