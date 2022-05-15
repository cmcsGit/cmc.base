package exception;

/**
 * @author cmc
 */
public class MyDocumentException extends BaseException{

    public MyDocumentException(String message) {
        super(message);
        setErrorMessage("xml转换出现错误");
    }
}
