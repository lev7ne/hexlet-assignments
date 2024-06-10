package exercise.controller;

import java.util.List;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.service.BookService;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BookService bookService;

    // BEGIN
    @GetMapping
    public ResponseEntity<List<BookDTO>> index() {
        var books = bookService.index();

        return ResponseEntity.status(HttpStatus.OK)
                .body(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> show(@PathVariable long id) {
        var book = bookService.show(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(book);
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        var book = bookService.create(bookCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable long id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        var book = bookService.update(id, bookUpdateDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        bookService.delete(id);
    }
    // END
}
