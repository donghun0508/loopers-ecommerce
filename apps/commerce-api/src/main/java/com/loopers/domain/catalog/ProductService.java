package com.loopers.domain.catalog;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public boolean existsById(Long productId) {
        return productRepository.existsById(productId);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdIn(productIds);
        List<Long> foundIds = products.stream()
                .map(Product::getId)
                .toList();
        List<Long> missingIds = productIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "ProductService.findAll(): 존재하지 않는 상품: " + missingIds);
        }
        return products;
    }

    @Transactional
    public List<Product> findAllWithLock(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdInWithLock(productIds);

        List<Long> foundIds = products.stream()
                .map(Product::getId)
                .toList();
        List<Long> missingIds = productIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND,
                    "ProductService.findAllWithLock(): 존재하지 않는 상품: " + missingIds);
        }
        return products;
    }

    @Transactional(readOnly = true)
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "ProductService.findById(): 상품을 찾을 수 없습니다. 상품 ID: " + productId));
    }

    @Transactional
    public Product findByIdWithLock(Long productId) {
        return productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "ProductService.findByIdWithOptimisticLock(): 상품을 찾을 수 없습니다. 상품 ID: " + productId));
    }

    @Transactional
    public Product findByIdWithOptimisticLock(Long productId) {
        return productRepository.findByIdWithOptimisticLock(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "ProductService.findByIdWithOptimisticLock(): 상품을 찾을 수 없습니다. 상품 ID: " + productId));
    }
}
