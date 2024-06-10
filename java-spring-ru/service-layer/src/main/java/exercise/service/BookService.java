package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    // BEGIN
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public List<BookDTO> index() {
        var books = bookRepository.findAll();

        return books.stream()
                .map(bookMapper::map)
                .toList();
    }

    public BookDTO show(long id) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not Found: " + id));

        return bookMapper.map(book);
    }

    public BookDTO create(BookCreateDTO bookCreateDTO) {
        var book = bookMapper.map(bookCreateDTO);
        book = bookRepository.save(book);

        return bookMapper.map(book);
    }

    public BookDTO update(long id, BookUpdateDTO bookUpdateDTO) {
        var book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not Found: " + id));

        bookMapper.update(bookUpdateDTO, book);
        book = bookRepository.save(book);

        return bookMapper.map(book);
    }

    public void delete(long id) {
        bookRepository.deleteById(id);
    }
    // END
}
