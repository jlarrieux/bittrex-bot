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
        private String Type;
        private Double Quantity;
        private Double Price;
        private String Opened;
        private Object Closed;
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
            return Type;
        }

        public void setType(String type) {
            this.Type = type;
        }

        public Double getQuantity() {
            return Quantity;
        }

        public void setQuantity(Double quantity) {
            this.Quantity = quantity;
        }


        public Double getPrice() {
            return Price;
        }

        public void setPrice(Double price) {
            this.Price = price;
        }

        public String getOpened() {
            return Opened;
        }

        public void setOpened(String opened) {
            this.Opened = opened;
        }

        public Object getClosed() {
            return Closed;
        }

        public void setClosed(Object closed) {
            this.Closed = closed;
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
