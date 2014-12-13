package com.visenze.visearch.android;

/**
 * Created by visenze on 10/17/14.
 */
public class VisenzeError extends Throwable {

    private int errorCode = 0;

    public VisenzeError(String message, int code) {
        super(message);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public static final class Code {
        public static final int SUCCESS = 0;
        public static final int ERROR = 1;
    }

}
