package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// BEGIN
@Getter
@Setter
public class PostDTO {
    private long id;
    private String title;
    private String body;
    List<CommentDTO> comments;
}
// END
