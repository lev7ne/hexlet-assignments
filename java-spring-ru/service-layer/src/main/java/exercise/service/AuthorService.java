package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    // BEGIN
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public List<AuthorDTO> index() {
        var authors = authorRepository.findAll();

        return authors.stream()
                .map(authorMapper::map)
                .toList();
    }

    public AuthorDTO show(long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        return authorMapper.map(author);
    }

    public AuthorDTO create(AuthorCreateDTO authorCreateDTO) {
        var author = authorMapper.map(authorCreateDTO);
        author = authorRepository.save(author);

        return authorMapper.map(author);
    }

    public AuthorDTO update(long id, AuthorUpdateDTO authorUpdateDTO) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        authorMapper.update(authorUpdateDTO, author);
        author = authorRepository.save(author);

        return authorMapper.map(author);
    }

    public void delete(long id) {
        authorRepository.deleteById(id);
    }
    // END
}
