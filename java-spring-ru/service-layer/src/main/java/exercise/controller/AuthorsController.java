package exercise.controller;

import exercise.dto.AuthorDTO;
import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    // BEGIN
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> index() {
        var authors = authorService.index();

        return ResponseEntity.status(HttpStatus.OK)
                .body(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> show(@PathVariable long id) {
        var author = authorService.show(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(author);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorCreateDTO authorCreateDTO) {
        var author = authorService.create(authorCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable long id, @Valid @RequestBody AuthorUpdateDTO authorUpdateDTO) {
        var author = authorService.update(id, authorUpdateDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(author);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        authorService.delete(id);
    }
    // END
}
