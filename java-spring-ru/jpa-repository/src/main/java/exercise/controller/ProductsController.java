package exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductRepository productRepository;

    // BEGIN
    @GetMapping
    public ResponseEntity<List<Product>> index(@RequestParam(required = false, defaultValue = "0") int min,
                                               @RequestParam(required = false, defaultValue = "0") int max) {
        List<Product> result;
        Sort sort = Sort.by(Sort.Order.asc("price"));

        if (min == 0 && max == 0) {
            result = productRepository.findAll(sort);
        } else if (min == 0) {
            result = productRepository.findAllByPriceLessThanEqual(sort, max);
        } else if (max == 0) {
            result = productRepository.findAllByPriceGreaterThanEqual(sort, min);
        } else {
            result = productRepository.findAllByPriceBetween(sort, min, max);
        }

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .body(result);
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
