package exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactsController {
    private final ContactRepository contactRepository;

    // BEGIN
    @PostMapping
    public ResponseEntity<ContactDTO> create(@RequestBody ContactCreateDTO dto) {
        Contact contact = toEntity(dto);
        contact = contactRepository.save(contact);
        ContactDTO contactDTO = toDTO(contact);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactDTO);

    }

    private Contact toEntity(ContactCreateDTO dto) {
        var contact = new Contact();

        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setPhone(dto.getPhone());

        return contact;
    }

    private ContactDTO toDTO(Contact contact) {
        var contactDTO = new ContactDTO();

        contactDTO.setId(contact.getId());
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setPhone(contact.getPhone());
        contactDTO.setUpdatedAt(contact.getUpdatedAt());
        contactDTO.setCreatedAt(contact.getCreatedAt());

        return contactDTO;
    }
    // END
}
