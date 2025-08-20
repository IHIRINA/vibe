package com.zhh.handsome.tileapp.service.impl;


import com.zhh.handsome.tileapp.model.Person;
import com.zhh.handsome.tileapp.model.PersonTile;
import com.zhh.handsome.tileapp.model.Tile;
import com.zhh.handsome.tileapp.model.User;
import com.zhh.handsome.tileapp.repository.PersonRepository;
import com.zhh.handsome.tileapp.repository.PersonTileRepository;
import com.zhh.handsome.tileapp.repository.TileRepository;
import com.zhh.handsome.tileapp.repository.UserRepository;
import com.zhh.handsome.tileapp.service.PersonService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TileRepository tileRepository;

    @Autowired
    private PersonTileRepository personTileRepository;


    @Autowired
    private ChatClient chatClient;

    @Override
    public Person createPerson(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Person person = new Person();
        person.setUser(user);
        person.setName(name);
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());

        return personRepository.save(person);
    }

    @Override
    public Person updatePerson(Long personId, Long userId, String name) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        if (!person.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this person");
        }

        person.setName(name);
        person.setUpdatedAt(LocalDateTime.now());

        return personRepository.save(person);
    }

    @Override
    public void deletePerson(Long personId, Long userId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        if (!person.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this person");
        }

        // 删除关联的person_tiles记录
        List<PersonTile> personTiles = personTileRepository.findByPersonId(personId);
        personTileRepository.deleteAll(personTiles);

        // 删除person记录
        personRepository.delete(person);
    }

    @Override
    public List<Person> getPersonsByUser(Long userId) {
        return personRepository.findByUserId(userId);
    }

    @Override
    public Person getPersonById(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    @Override
    public void addTileToPerson(Long personId, Long tileId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Tile tile = tileRepository.findById(tileId)
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        // 检查用户是否有权限将此磁贴添加到此人物
        if (!person.getUser().getId().equals(tile.getUser().getId())) {
            throw new RuntimeException("Unauthorized to add this tile to the person");
        }

        // 检查关联是否已存在
        List<PersonTile> existing = personTileRepository.findByPersonId(personId)
                .stream()
                .filter(pt -> pt.getTile().getId().equals(tileId))
                .collect(Collectors.toList());

        if (existing.isEmpty()) {
            PersonTile personTile = new PersonTile();
            personTile.setPerson(person);
            personTile.setTile(tile);
            personTileRepository.save(personTile);
        }
    }

    @Override
    public void removeTileFromPerson(Long personId, Long tileId) {
        personTileRepository.deleteByPersonIdAndTileId(personId, tileId);
    }

    @Override
    public String analyzePersonTiles(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        List<PersonTile> personTiles = personTileRepository.findByPersonId(personId);
        if (personTiles.isEmpty()) {
            return "This person has no associated tiles.";
        }

        // 收集所有磁贴内容
        StringBuilder tilesContent = new StringBuilder();
        tilesContent.append("Analyze the following tiles associated with person '").append(person.getName()).append("':\n");

        for (PersonTile pt : personTiles) {
            tilesContent.append("- ").append(pt.getTile().getContent()).append("\n");
        }

        tilesContent.append("\nProvide a summary of this person based on the tiles.");

        // 调用OpenAI API进行分析

        return chatClient.prompt()
                .user(tilesContent.toString())
                .call()
                .content();
    }
}