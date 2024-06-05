package exercise.controller;

import exercise.mapper.GuestMapper;
import exercise.model.Guest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.repository.GuestRepository;
import exercise.dto.GuestDTO;
import exercise.dto.GuestCreateDTO;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/guests")
@RequiredArgsConstructor
public class GuestsController {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @GetMapping(path = "")
    public List<GuestDTO> index() {
        var products = guestRepository.findAll();
        return products.stream()
                .map(p -> guestMapper.map(p))
                .toList();
    }

    @GetMapping(path = "/{id}")
    public GuestDTO show(@PathVariable long id) {

        var guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest with id " + id + " not found"));
        var guestDto = guestMapper.map(guest);
        return guestDto;
    }

    // BEGIN
    @PostMapping
    public ResponseEntity<GuestDTO> create(@Valid @RequestBody GuestCreateDTO guestCreateDTO) {
        Guest guest = guestMapper.map(guestCreateDTO);
        guest = guestRepository.save(guest);

        var dto = guestMapper.map(guest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dto);
    }
    // END
}
