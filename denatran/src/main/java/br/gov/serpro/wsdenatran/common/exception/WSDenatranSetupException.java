package br.gov.serpro.wsdenatran.common.exception;

public class WSDenatranSetupException extends Exception {

    private static final long serialVersionUID = 6889707281755435477L;

    public WSDenatranSetupException(String message) {
        super(message);
    }

    public WSDenatranSetupException(String message, Throwable e) {
        super(message, e);
    }

}
