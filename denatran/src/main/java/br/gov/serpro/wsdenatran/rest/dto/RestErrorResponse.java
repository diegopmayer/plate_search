package br.gov.serpro.wsdenatran.rest.dto;

public class RestErrorResponse {

    private int returnCode = 0;
    private String message = "";

    public RestErrorResponse() {

    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RestErrorResponse [returnCode=");
        builder.append(returnCode);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }

}