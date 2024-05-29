package exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<List<Comment>> index() {
        var comments = commentRepository.findAll();

        return ResponseEntity.ok()
                .body(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> show(@PathVariable long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));

        return ResponseEntity.ok()
                .body(comment);
    }

    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Comment comment) {
        var newComment = commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@PathVariable long id,
                                        @RequestBody Comment data) {
        var commentToUpdate = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
        commentToUpdate.setBody(data.getBody());

        return ResponseEntity.ok()
                .body(commentToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        commentRepository.deleteById(id);
    }
}
// END
