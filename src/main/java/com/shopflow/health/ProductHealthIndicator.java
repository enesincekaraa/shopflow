package com.shopflow.health;


import com.shopflow.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductHealthIndicator implements HealthIndicator {
    private final ProductRepository productRepository;

    @Override
    public Health health() {
        long productCount = productRepository.count();

        if (productCount >= 0) {
            return Health.up()
                    .withDetail("productCount", productCount)
                    .withDetail("message", "Ürün servisi çalışıyor")
                    .build();
        }

        return Health.down()
                .withDetail("message", "Ürün servisine ulaşılamıyor")
                .build();
    }
}
