package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    // BEGIN
    @GetMapping
    public ResponseEntity<List<ProductDTO>> index() {
        var products = productRepository.findAll();

        var dtos = products.stream()
                .map(productMapper::map)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> show(@PathVariable long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        var dto = productMapper.map(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(dto);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        var product = productMapper.map(productCreateDTO);
        product = productRepository.save(product);

        var dto = productMapper.map(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        productMapper.update(productUpdateDTO, product);
        product = productRepository.save(product);

        var dto = productMapper.map(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
    // END
}
