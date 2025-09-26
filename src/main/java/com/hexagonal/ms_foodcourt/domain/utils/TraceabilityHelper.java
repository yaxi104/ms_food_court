package com.hexagonal.ms_foodcourt.domain.utils;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.Traceability;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;

import java.util.Objects;

public class TraceabilityHelper {

    private TraceabilityHelper() {
    }

    public static Traceability createTrace(Order order, String statusPrevious, IUserFeignPort userFeignPort) {
        String emailEmployee = null;
        if (!Objects.isNull(userFeignPort)) {
            emailEmployee = userFeignPort
                    .getUserByid(order.getIdChef())
                    .map(User::getEmail)
                    .orElse(null);
        }

        return new Traceability.Builder()
                .orderId(order.getId())
                .clientId(order.getIdClient())
                .date(order.getDate())
                .previousStatus(statusPrevious)
                .newStatus(order.getStatus())
                .employeeId(order.getIdChef())
                .employeeEmail(emailEmployee)
                .build();
    }
}
