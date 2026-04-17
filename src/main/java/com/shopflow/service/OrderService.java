package com.shopflow.service;

import com.shopflow.dto.OrderRequest;
import com.shopflow.model.*;
import com.shopflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));

        List<Product> products = productRepository.findAllById(request.getProductIds());

        if (products.isEmpty()) {
            throw new RuntimeException("Hiç ürün bulunamadı");
        }

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}