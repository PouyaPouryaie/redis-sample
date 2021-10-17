package ir.bigz.redis.person;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/persons")
    public ResponseEntity<Iterable<Person>> personList() {
        Iterable<Person> allRecords = personRepository.findAll();
        return ResponseEntity.ok(allRecords);
    }

    @GetMapping("/byFirstName/{firstName}")
    public ResponseEntity<Iterable<Person>> personsByFirstName(@PathVariable String firstName) {
        List<Person> personList = personRepository.findByFirstName(firstName);
        return ResponseEntity.ok(personList);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<?> personsById(@PathVariable String id) {
        Optional<Person> personById = personRepository.findById(id);
        if(personById.isPresent()){
            return ResponseEntity.ok(personById.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam String firstName, @RequestParam String lastName, @RequestBody Address address) {
        if(firstName == null) {
            return ResponseEntity.badRequest().build();
        }
        Person person = Person.builder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .build();
        Person savedPerson = personRepository.save(person);
        return ResponseEntity.ok(savedPerson.getUid());
    }
}
