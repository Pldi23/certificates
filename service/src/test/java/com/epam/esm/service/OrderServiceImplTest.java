package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractOrderCertificateRepository;
import com.epam.esm.repository.AbstractOrderRepository;
import com.epam.esm.repository.AbstractUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-23.
 * @version 0.0.1
 */
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private AbstractOrderRepository orderRepository;
    @Mock
    private AbstractCertificateRepository certificateRepository;

    @Mock
    private AbstractUserRepository userRepository;

    @Mock
    private AbstractOrderCertificateRepository orderCertificateRepository;

    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        CertificateConverter certificateConverter = new CertificateConverter();
        OrderConverter orderConverter = new OrderConverter(certificateConverter);
        orderService = new OrderServiceImpl(orderRepository, certificateRepository, userRepository, orderConverter, orderCertificateRepository);

        User user = User.builder()
                .id(1L)
                .email("e")
                .role(Role.builder().id(1L).value("ROLE_USER").build())
                .build();

        order = Order.builder()
                .id(1L)
                .user(user)
                .orderCertificates(new ArrayList<>())
                .createdAt(LocalDateTime.of(2010,1,1,1,1))
                .activeStatus(null)
                .build();

        orderDTO = OrderDTO.builder()
                .id(1L)
                .userId(user.getId())
                .userEmail(user.getEmail())
                .price(new BigDecimal(0))
                .giftCertificates(new ArrayList<>())
                .createdAt(LocalDateTime.of(2010,1,1,1,1))
                .build();
    }


    @Test
    void delete() {
        orderService.delete(order.getId());
        Mockito.verify(orderRepository, times(1)).deleteById(order.getId());
        Mockito.verify(orderCertificateRepository, times(1)).deleteByOrderId(order.getId());

    }

    @Test
    void findAll() {

        List<Order> orders = List.of(order);
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(orderRepository
                .findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()))
                .thenReturn(orders);
        Mockito.when(orderCertificateRepository.calculateOrderFixedPrice(orderDTO.getId())).thenReturn(new BigDecimal(0));

        List<OrderDTO> expected = List.of(orderDTO);
        List<OrderDTO> actual = orderService.findAll(pageAndSortDTO);
        assertEquals(expected, actual);
    }

    @Test
    void findOne() {

        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(orderCertificateRepository.calculateOrderFixedPrice(orderDTO.getId())).thenReturn(new BigDecimal(0));


        OrderDTO expected = orderDTO;
        OrderDTO actual = orderService.findOne(order.getId());
        Mockito.verify(orderRepository, times(1)).findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void save() {

        Mockito.when(userRepository.findByEmail(orderDTO.getUserEmail())).thenReturn(Optional.of(order.getUser()));
        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(orderCertificateRepository.findAllByOrderId(order.getId())).thenReturn(new ArrayList<>());
        Mockito.when(orderCertificateRepository.calculateOrderFixedPrice(order.getId())).thenReturn(new BigDecimal(0));

        OrderDTO actual = orderService.save(orderDTO);

        assertEquals(orderDTO, actual);
    }

    @Test
    void update() {
        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(orderCertificateRepository.calculateOrderFixedPrice(order.getId())).thenReturn(new BigDecimal(0));
        OrderDTO actual = orderService.update(orderDTO, order.getId());

        assertEquals(orderDTO, actual);


    }

    @Test
    void findByCriteria() {
        List<Order> orders = List.of(order);
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();
        OrderSearchCriteriaDTO orderSearchCriteriaDTO = OrderSearchCriteriaDTO.builder().build();

        Mockito.when(orderRepository
                .findByCriteria(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize(),
                        orderSearchCriteriaDTO.getEmail(), orderSearchCriteriaDTO.getUserId(),
                        orderSearchCriteriaDTO.getCertificatesNames(), orderSearchCriteriaDTO.getCertificatesIds()))
                .thenReturn(orders);
        Mockito.when(orderCertificateRepository.calculateOrderFixedPrice(orderDTO.getId())).thenReturn(new BigDecimal(0));

        List<OrderDTO> expected = List.of(orderDTO);
        List<OrderDTO> actual = orderService.findByCriteria(orderSearchCriteriaDTO, pageAndSortDTO);
        assertEquals(expected, actual);

    }
}