package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cmc
 */
public class BaseException extends Exception{

    private String errorMessage;

    private static Logger log = LoggerFactory.getLogger(BaseException.class);

    public BaseException(){
        super();
    }

    public BaseException(String message){
        super(message);
    }

    public BaseException(String message, String otherInfo){
        super(message);
        log.error("!------------发生异常-------------!");
        log.error(String.format("造成此处异常可能的原因是:%s",otherInfo));
        log.error("!--------------------------------!");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void info(String message){
    }
}
