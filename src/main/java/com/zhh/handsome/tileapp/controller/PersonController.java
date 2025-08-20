package com.zhh.handsome.tileapp.controller;

import com.zhh.handsome.tileapp.model.Person;
import com.zhh.handsome.tileapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String name = request.get("name").toString();

            Person person = personService.createPerson(userId, name);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Person created successfully");
            response.put("data", Map.of(
                    "id", person.getId(),
                    "name", person.getName(),
                    "createdAt", person.getCreatedAt(),
                    "updatedAt", person.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @PutMapping("/{personId}")
    public ResponseEntity<?> updatePerson(@PathVariable Long personId, @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String name = request.get("name").toString();

            Person person = personService.updatePerson(personId, userId, name);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Person updated successfully");
            response.put("data", Map.of(
                    "id", person.getId(),
                    "name", person.getName(),
                    "updatedAt", person.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<?> deletePerson(@PathVariable Long personId, @RequestParam Long userId) {
        try {
            personService.deletePerson(personId, userId);

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "msg", "Person deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getPersonsByUser(@RequestParam Long userId) {
        try {
            List<Person> persons = personService.getPersonsByUser(userId);
            List<Map<String, ? extends Serializable>> personsData = new ArrayList<>();
            for (Person person : persons) {
                Map<String, ? extends Serializable> id = Map.of(
                        "id", person.getId(),
                        "name", person.getName(),
                        "createdAt", person.getCreatedAt(),
                        "updatedAt", person.getUpdatedAt()
                );
                personsData.add(id);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Persons retrieved successfully");
            response.put("data", personsData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @GetMapping("/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable Long personId) {
        try {
            Person person = personService.getPersonById(personId);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Person retrieved successfully");
            response.put("data", Map.of(
                    "id", person.getId(),
                    "name", person.getName(),
                    "createdAt", person.getCreatedAt(),
                    "updatedAt", person.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @PostMapping("/{personId}/tiles/{tileId}")
    public ResponseEntity<?> addTileToPerson(@PathVariable Long personId, @PathVariable Long tileId) {
        try {
            personService.addTileToPerson(personId, tileId);

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "msg", "Tile added to person successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{personId}/tiles/{tileId}")
    public ResponseEntity<?> removeTileFromPerson(@PathVariable Long personId, @PathVariable Long tileId) {
        try {
            personService.removeTileFromPerson(personId, tileId);

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "msg", "Tile removed from person successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @GetMapping("/{personId}/analyze")
    public ResponseEntity<?> analyzePersonTiles(@PathVariable Long personId) {
        try {
            String analysis = personService.analyzePersonTiles(personId);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Person tiles analyzed successfully");
            response.put("data", Map.of("analysis", analysis));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }
}