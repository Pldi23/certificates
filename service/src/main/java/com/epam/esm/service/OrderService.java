package com.epam.esm.service;


import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;

public interface OrderService extends SaveService<OrderDTO>, UpdateService<OrderDTO>,
        FindOneService<OrderDTO>, FindAllService<OrderDTO>, DeleteService {

    PageableList<OrderDTO> findByCriteria(OrderSearchCriteriaDTO criteriaDTO, PageAndSortDTO pageAndSortDTO);
}
