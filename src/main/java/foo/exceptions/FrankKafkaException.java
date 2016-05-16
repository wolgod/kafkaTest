package foo.exceptions;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:34
 */
public class FrankKafkaException extends  RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private Object[] values;

    /**
     * 错误消息
     */
    private String errorMsg;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getValues() {
        return this.values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public FrankKafkaException() {
        super();
    }

    public FrankKafkaException(String message) {
        super(message);
    }

    public FrankKafkaException(Throwable cause) {
        super(cause);
    }

    public FrankKafkaException(String message, Throwable cause) {
        super(message, cause);
    }


    public FrankKafkaException(String message, Throwable cause, String code,
                               Object[] values) {
        super(message, cause);
        this.code = code;
        this.values = values;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
