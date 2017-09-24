package com.jlarrieux.bittrexbot.simulation.TO;

import lombok.Data;

@Data
public class OrderTO {

    private Boolean success = true;
    private String message = "";
    private Result result;

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


    public class Result {

        private String OrderUuid;
        private String type;
        private Double quantity;
        private Double price;
        private String opened;
        private Object closed;
        private Boolean IsOpen = false;
        private Boolean CancelInitiated = false;
        private String Exchange;


        public String getOrderUuid() {
            return OrderUuid;
        }

        public void setOrderUuid(String orderUuid) {
            this.OrderUuid = orderUuid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }


        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getOpened() {
            return opened;
        }

        public void setOpened(String opened) {
            this.opened = opened;
        }

        public Object getClosed() {
            return closed;
        }

        public void setClosed(Object closed) {
            this.closed = closed;
        }

        public Boolean getIsOpen() {
            return IsOpen;
        }

        public void setIsOpen(Boolean isOpen) {
            this.IsOpen = isOpen;
        }

        public String getExchange() {
            return Exchange;
        }

        public void setExchange(String exchange) {
            Exchange = exchange;
        }
    }
}
