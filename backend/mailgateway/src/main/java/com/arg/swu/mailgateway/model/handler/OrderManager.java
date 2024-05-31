package com.arg.swu.mailgateway.model.handler;

import com.arg.swu.mailgateway.order.RequestOrderMail;

public interface OrderManager {
    void placeOrder(RequestOrderMail order);
}
