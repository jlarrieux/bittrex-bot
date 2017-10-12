package com.jlarrieux.bittrexbot.simulation.TO;

public class SellTO {
    private Boolean success;
    private String message;
    private Result result;

    public SellTO(){
        success = true;
        message = "";
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result createResult() {
        return new Result();
    }

    /*****************************
     * Class Result
     *****************************/

    public class Result {

        private String uuid;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

    }
}
