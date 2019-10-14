package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.service.OrderService;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */
@RestController
@RequestMapping("/orders")
@Validated
@ExposesResourceFor(OrderDTO.class)
public class OrderController {

    private OrderService orderService;
    private LinkCreator linkCreator;

    public OrderController(OrderService orderService, LinkCreator linkCreator) {
        this.orderService = orderService;
        this.linkCreator = linkCreator;
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(linkCreator.toResource(orderService.save(orderDTO)));
    }

    @GetMapping
    public ResponseEntity findAll() {
        List<OrderDTO> dtos = orderService.findAll();
        return !dtos.isEmpty() ? ResponseEntity.ok(dtos.stream().map(orderDTO ->
                linkCreator.toResource(orderDTO))) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable @Min(0) Long id) {
        return ResponseEntity.ok(linkCreator.toResource(orderService.findOne(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Min(0) Long id) {
        orderService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable @Min(0) Long id, @Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(linkCreator.toResource(orderService.update(orderDTO, id)));
    }
}
