package com.epam.esm.service;


import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderPatchDTO;

public interface OrderService extends SaveService<OrderDTO>, UpdateService<OrderDTO>,
        FindOneService<OrderDTO>, FindAllService<OrderDTO>, DeleteService {

}
