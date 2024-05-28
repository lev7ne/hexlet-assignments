package exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // BEGIN
    @GetMapping("/{id}")
    public ResponseEntity<Product> show(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product data) {
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        productToUpdate.setPrice(data.getPrice());
        productToUpdate.setTitle(data.getTitle());
        return ResponseEntity.ok()
                .body(productToUpdate);
    }
    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}

//        * *GET /products/{id}* — просмотр конкретного товара
//        * *PUT /products/{id}* — обновление данных товара