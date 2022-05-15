package exception;

import entity.Operator;
import service.impl.BaseService;

/**
 * @author cmc
 */
public class EntityFindException extends BaseException {

    public EntityFindException(String message) {
        super(message);
        BaseService<Operator> bs = new BaseService<>();

    }

    public EntityFindException(String message, String otherInfo){
        super(message, otherInfo);
    }
}
