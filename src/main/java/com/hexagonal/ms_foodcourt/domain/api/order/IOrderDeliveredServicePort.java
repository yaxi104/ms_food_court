package com.hexagonal.ms_foodcourt.domain.api.order;

import com.hexagonal.ms_foodcourt.domain.model.DeliverOrder;
import com.hexagonal.ms_foodcourt.domain.model.response.MessageResult;

public interface IOrderDeliveredServicePort {

    MessageResult markOrderAsDelivered(DeliverOrder deliverOrder);

}
