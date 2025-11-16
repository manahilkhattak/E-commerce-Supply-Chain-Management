package com.ecommerce.supplychain.catalog.repository;

import com.ecommerce.supplychain.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductSku(String productSku);

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    List<Product> findBySupplierId(Long supplierId);

    List<Product> findByStatus(String status);

    List<Product> findByIsActive(Boolean isActive);

    boolean existsByProductSku(String productSku);

    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.reorderPoint AND p.isActive = true")
    List<Product> findProductsNeedingReorder();

    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minimumStockLevel AND p.isActive = true")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.currentStock = 0 AND p.isActive = true")
    List<Product> findOutOfStockProducts();

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.productSku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Product p WHERE p.currentStock BETWEEN :minStock AND :maxStock")
    List<Product> findByStockRange(@Param("minStock") Integer minStock, @Param("maxStock") Integer maxStock);
}