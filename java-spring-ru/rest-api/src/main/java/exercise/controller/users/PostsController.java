package exercise.controller.users;

import exercise.Data;
import exercise.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Post>> getPostsForUserId(@PathVariable Integer id) {
        List<Post> post = posts.stream()
                .filter(p -> p.getUserId() == id)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(post);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> create(@PathVariable Integer id, @RequestBody Post post) {
        post.setUserId(id);
        posts.add(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }
}
// END