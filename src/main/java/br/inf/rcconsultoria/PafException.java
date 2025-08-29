package br.inf.rcconsultoria;

@SuppressWarnings("unused")
public class PafException extends Exception {

    public PafException(String message) {
        super(message);
    }

    public PafException(String message, Throwable cause) {
        super(message, cause);
    }

    public PafException(Throwable cause) {
        super(cause);
    }

    public PafException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
