package com.zhh.handsome.tileapp.service;


import com.zhh.handsome.tileapp.model.Person;

import java.util.List;

public interface PersonService {
    Person createPerson(Long userId, String name);
    Person updatePerson(Long personId, Long userId, String name);
    void deletePerson(Long personId, Long userId);
    List<Person> getPersonsByUser(Long userId);
    Person getPersonById(Long personId);
    void addTileToPerson(Long personId, Long tileId);
    void removeTileFromPerson(Long personId, Long tileId);
    String analyzePersonTiles(Long personId);
}