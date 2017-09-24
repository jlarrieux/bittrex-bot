package com.jlarrieux.bittrexbot.simulation.TO;

public class BalanceTO {

    private Boolean success = true;
    private String message = "";
    private Result result;

    public Result createResult(){
        return new Result();
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

    /*****************************
     * Class Result
     *****************************/
    public class Result {

        private Double Available;

        public Double getAvailable() {
            return Available;
        }

        public void setAvailable(Double available) {
            this.Available = available;
        }
    }
}
