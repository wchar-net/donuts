package net.wchar.donuts.sys.exception;


/**
 * 权限校验异常
 * @author Elijah
 */
public class PermissionException extends BusException{
    public PermissionException(String msg) {
        super(msg);
    }
    public PermissionException(String msg,boolean internalServerErrorFlag) {
        super(msg);
        this.internalServerErrorFlag = internalServerErrorFlag;
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    public PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
