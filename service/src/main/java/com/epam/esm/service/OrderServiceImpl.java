package com.epam.esm.service;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractOrderCertificateRepository;
import com.epam.esm.repository.AbstractOrderRepository;
import com.epam.esm.repository.AbstractUserRepository;
import com.epam.esm.repository.page.PageSizeData;
import com.epam.esm.repository.predicate.OrderHasCertificateByIdSpecification;
import com.epam.esm.repository.predicate.OrderHasCertificateByNameSpecification;
import com.epam.esm.repository.predicate.OrderHasUserEmailSpecification;
import com.epam.esm.repository.predicate.OrderHasUserIdSpecification;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.OrderSortData;
import com.epam.esm.util.Translator;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_NOT_FOUND_MESSAGE = "entity.order.not.found";

    private AbstractOrderRepository orderRepository;
    private AbstractCertificateRepository certificateRepository;
    private AbstractUserRepository userRepository;
    private OrderConverter orderConverter;
    private AbstractOrderCertificateRepository orderCertificateRepository;

    public OrderServiceImpl(AbstractOrderRepository orderRepository, AbstractCertificateRepository certificateRepository,
                            AbstractUserRepository userRepository, OrderConverter orderConverter,
                            AbstractOrderCertificateRepository orderCertificateRepository) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.orderConverter = orderConverter;
        this.orderCertificateRepository = orderCertificateRepository;
    }

    @Transactional
    @Override
    public void delete(long id) {
        try {
            orderCertificateRepository.deleteByOrderId(id);
            orderRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(Translator.toLocale(ORDER_NOT_FOUND_MESSAGE), id));
        }
    }

    @Override
    public PageableList<OrderDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return PageableList.<OrderDTO>builder().list(orderRepository.findAllSpecified(null,
                pageAndSortDTO.getSortParameter() != null ? new OrderSortData(pageAndSortDTO.getSortParameter()) : null,
                new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                .map(order -> {
                    OrderDTO dto = orderConverter.convert(order);
                    BigDecimal price = orderCertificateRepository.calculateOrderFixedPrice(dto.getId());
                    dto.setPrice(price);
                    return dto;
                })
                .collect(Collectors.toList()))
                .lastPage(orderRepository.countLastPage(null, pageAndSortDTO.getSize()))
                .build();
    }

    @Override
    public OrderDTO findOne(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale(ORDER_NOT_FOUND_MESSAGE), id)));
        OrderDTO orderDTO = orderConverter.convert(order);
        orderDTO.setPrice(orderCertificateRepository.calculateOrderFixedPrice(id));
        return orderDTO;
    }

    @Transactional
    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        Order saved = orderRepository.save(Order.builder()
                .user(userRepository.findByEmail(orderDTO.getUserEmail()).orElseThrow(() ->
                        new EntityNotFoundException(String.format(Translator.toLocale("entity.user.not.found"),
                                orderDTO.getUserEmail()))))
                .build());
        saveOrderCertificates(saved, orderDTO.getGiftCertificates());

        saved.setOrderCertificates(orderCertificateRepository.findAllByOrderId(saved.getId()));

        OrderDTO dto = orderConverter.convert(saved);
        dto.setPrice(orderCertificateRepository.calculateOrderFixedPrice(saved.getId()));
        return dto;
    }

    @Transactional
    @Override
    public OrderDTO update(OrderDTO orderDTO, long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderCertificateRepository.deleteByOrderId(id);
            saveOrderCertificates(order, orderDTO.getGiftCertificates());
            Order updated = orderRepository.save(order);
            updated.setOrderCertificates(orderCertificateRepository.findAllByOrderId(updated.getId()));
            OrderDTO dto = orderConverter.convert(updated);
            dto.setPrice(orderCertificateRepository.calculateOrderFixedPrice(updated.getId()));
            return dto;
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale(ORDER_NOT_FOUND_MESSAGE), id));
        }
    }

    @Override
    public PageableList<OrderDTO> findByCriteria(OrderSearchCriteriaDTO criteriaDTO, PageAndSortDTO pageAndSortDTO) {
        List<Specification<Order>> specifications = constructSpecifications(criteriaDTO);
        return PageableList.<OrderDTO>builder()
                .list(orderRepository.findAllSpecified(specifications,
                        pageAndSortDTO.getSortParameter() != null ? new OrderSortData(pageAndSortDTO.getSortParameter()) : null,
                        new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                        .map(order -> {
                            OrderDTO dto = orderConverter.convert(order);
                            BigDecimal price = orderCertificateRepository.calculateOrderFixedPrice(dto.getId());
                            dto.setPrice(price != null ? price : new BigDecimal(0));
                            return dto;
                        })
                        .collect(Collectors.toList()))
                        .lastPage(orderRepository.countLastPage(specifications, pageAndSortDTO.getSize()))
                .build();

    }


    private void saveOrderCertificates(Order order, List<GiftCertificateDTO> giftCertificates) {
        List<GiftCertificate> certificates = giftCertificates.stream().map(giftCertificateDTO -> giftCertificateDTO.getId() == null ?
                certificateRepository.findByName(giftCertificateDTO.getName(), true).orElseThrow(() ->
                        new EntityNotFoundException(String.format(Translator.toLocale("certificate.not.found.by.name"), giftCertificateDTO.getName()))) :
                certificateRepository.findById(giftCertificateDTO.getId(), true).orElseThrow(() ->
                        new EntityNotFoundException(String.format(Translator.toLocale("entity.certificate.not.found"), giftCertificateDTO.getId()))))
                .collect(Collectors.toList());

        List<OrderCertificate> orderCertificates = certificates.stream().map(certificate -> OrderCertificate.builder()
                .order(order)
                .certificate(certificate)
                .fixedPrice(certificate.getPrice())
                .expirationDate(certificate.getExpirationDate())
                .build()).collect(Collectors.toList());
        orderCertificateRepository.save(orderCertificates);
    }

    private List<Specification<Order>> constructSpecifications(OrderSearchCriteriaDTO criteriaDTO) {
        List<Specification<Order>> specifications = new ArrayList<>();
        if (criteriaDTO.getUserId() != null) {
            specifications.add(new OrderHasUserIdSpecification(criteriaDTO.getUserId()));
        }
        if (criteriaDTO.getEmail() != null) {
            specifications.add(new OrderHasUserEmailSpecification(criteriaDTO.getEmail()));
        }
        if (criteriaDTO.getCertificatesIds() != null && !criteriaDTO.getCertificatesIds().isEmpty()) {
            specifications.add(new OrderHasCertificateByIdSpecification(criteriaDTO.getCertificatesIds()));
        }
        if (criteriaDTO.getCertificatesNames() != null && !criteriaDTO.getCertificatesNames().isEmpty()) {
            specifications.add(new OrderHasCertificateByNameSpecification(criteriaDTO.getCertificatesNames()));
        }
        return specifications;
    }
}
