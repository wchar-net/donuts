package net.wchar.donuts.sys.exception;


/**
 * 抛出统一ajax异常
 * @author Elijah
 */
public class BusException extends RuntimeException {

    //是否抛出 http 500状态码
    protected boolean internalServerErrorFlag = false;

    public boolean getInternalServerErrorFlag() {
        return internalServerErrorFlag;
    }

    private BusException() {
    }

    public BusException(String msg,boolean internalServerErrorFlag) {
        super(msg);
        this.internalServerErrorFlag = internalServerErrorFlag;
    }

    public BusException(String msg) {
        super(msg);
    }

    public BusException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusException(Throwable cause) {
        super(cause);
    }

    public BusException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(msg, cause, enableSuppression, writableStackTrace);
    }
}
