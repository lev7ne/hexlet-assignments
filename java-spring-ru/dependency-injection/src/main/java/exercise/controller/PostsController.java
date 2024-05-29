package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Post;
import exercise.repository.CommentRepository;
import exercise.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BEGIN
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<List<Post>> index() {
        var posts = postRepository.findAll();

        return ResponseEntity.ok()
                .body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        return ResponseEntity.ok()
                .body(post);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        var newPost = postRepository.save(post);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable long id,
                                       @RequestBody Post data) {
        var postToUpdate = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        postToUpdate.setTitle(data.getTitle());
        postToUpdate.setBody(data.getBody());

        return ResponseEntity.ok()
                .body(postToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        postRepository.deleteById(id);
        commentRepository.deleteByPostId(id);
    }
}
// END
