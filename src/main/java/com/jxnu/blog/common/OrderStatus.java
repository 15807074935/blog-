package com.jxnu.blog.common;

public enum  OrderStatus {
    CANCELED(0,"订单取消"),
    NoPAY(10,"未付款"),
    PAID(20,"已支付"),
    ORDER_SUCCESS(50,"订单完成"),
    ORDER_CLOSE(60,"订单关闭");
    int code;
    String mes;
    OrderStatus(int code,String msg){
        this.code = code;
        this.mes = msg;
    }
    public int getCode() {
        return code;
    }

    public String getMes() {
        return mes;
    }

    public static OrderStatus getOrderStatus(int code){
        for(OrderStatus orderStatus:values()){
            if(orderStatus.getCode()==code)
                return orderStatus;
        }
        throw new RuntimeException("没有找到对应的枚举");
    }
}
