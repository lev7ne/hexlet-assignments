package exercise.repository;

import exercise.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findAllByPriceBetween(Sort sort, int min, int max);

    List<Product> findAllByPriceLessThanEqual(Sort sort, int max);

    List<Product> findAllByPriceGreaterThanEqual(Sort sort, int min);
    // END
}
