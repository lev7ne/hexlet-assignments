package exercise.controller;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;
import exercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductRepository productRepository;

    // BEGIN
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> index() {
        var products = productRepository.findAll();

        var productDTOs = products.stream()
                .map(productMapper::map)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(productDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> show(@PathVariable long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        var productDTO = productMapper.map(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductCreateDTO dto) {
        var product = productMapper.map(dto);

        product = productRepository.save(product);
        var productDTO = productMapper.map(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable long id, @RequestBody ProductUpdateDTO dto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        productMapper.update(dto, product);
        product = productRepository.save(product);
        var productDTO = productMapper.map(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(productDTO);
    }
    // END
}
