package exercise.controller;

import java.util.List;

import exercise.dto.ArticleCreateDTO;
import exercise.dto.ArticleDTO;
import exercise.dto.ArticleUpdateDTO;
import exercise.mapper.ArticleMapper;
import exercise.repository.UserRepository;
import exercise.utils.UserUtils;
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
import exercise.repository.ArticleRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final ArticleMapper articleMapper;

    private final UserUtils userUtils;

    // BEGIN
    @PostMapping("")
    public ResponseEntity<ArticleDTO> create(@Valid @RequestBody ArticleCreateDTO articleCreateDTO) {
        var article = articleMapper.map(articleCreateDTO);
        var id = userUtils.getCurrentUser().getId();

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        article.setAuthor(user);
        article = articleRepository.save(article);

        var result = articleMapper.map(article);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }
    // END

    @GetMapping("")
    List<ArticleDTO> index() {
        var tasks = articleRepository.findAll();

        return tasks.stream()
                .map(t -> articleMapper.map(t))
                .toList();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ArticleDTO show(@PathVariable Long id) {
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return articleMapper.map(article);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ArticleDTO update(@RequestBody @Valid ArticleUpdateDTO articleData, @PathVariable Long id) {
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        articleMapper.update(articleData, article);
        articleRepository.save(article);
        return articleMapper.map(article);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        articleRepository.deleteById(id);
    }
}
