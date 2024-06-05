package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// BEGIN
@Getter
@Setter
public class GuestCreateDTO {
    @NotBlank(message = "Имя пользователя не должно быть пустым.")
    private String name;
    @Email(message = "Электронная почта должна быть валидной.")
    private String email;
    @Pattern(regexp = "^\\+\\d{11,13}$", message = "Номер телефона должен начинаться с символа '+' и содержать от 11 до 13 цифр.")
    private String phoneNumber;
    @Size(min = 4, max = 4, message = "Номер клубной карты должен состоять ровно из четырех цифр.")
    private String clubCard;
    @Future(message = "Срок действия клубной карты должен быть еще не истекшим.")
    private LocalDate cardValidUntil;
}
// END
