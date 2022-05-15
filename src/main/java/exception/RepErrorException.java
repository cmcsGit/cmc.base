package exception;

/*
 * @author by cmc
 * @date 2019/3/20 19:43
 */
public class RepErrorException extends BaseException {

    public RepErrorException(String message){
        super(message);
    }

    public RepErrorException(String message, String otherInfo){
        super(message, otherInfo);
    }

}
