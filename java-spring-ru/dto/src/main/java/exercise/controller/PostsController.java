package exercise.controller;

import exercise.dto.CommentDTO;
import exercise.dto.PostDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.model.Comment;
import exercise.model.Post;
import exercise.repository.CommentRepository;
import exercise.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// BEGIN
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<List<PostDTO>> index() {
        List<Post> posts = postRepository.findAll();
        List<Comment> comments = commentRepository.findAll();

        Map<Long, List<Comment>> sortedComments = comments.stream()
                .collect(Collectors.groupingBy(Comment::getPostId));

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> toPostDTO(post, sortedComments.getOrDefault(post.getId(), List.of())))
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(postDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        List<Comment> comments = commentRepository.findByPostId(id);

        PostDTO dto = toPostDTO(post, comments);

        return ResponseEntity.status(HttpStatus.OK)
                .body(dto);
    }

    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();

        dto.setId(comment.getId());
        dto.setBody(comment.getBody());

        return dto;
    }

    private PostDTO toPostDTO(Post post, List<Comment> comments) {
        PostDTO dto = new PostDTO();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setComments(comments.stream()
                .map(this::toCommentDTO)
                .toList());

        return dto;
    }
}
// END
