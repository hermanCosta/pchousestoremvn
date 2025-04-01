package com.pchouse.pchousestoremvn.controllers;

import com.pchouse.pchousestoremvn.dao.PersonDAO;
import com.pchouse.pchousestoremvn.models.Person;
import java.util.List;

public class PersonController {
    private final PersonDAO PERSON_DAO = new PersonDAO();
    
    // Fetches all persons from the database
    public List<Person> getAllPerson() {
        return PERSON_DAO.getAllPersonDAO();
    }
    
    // Adds a new person to the database
    public long addPerson(Person pPerson) {
        return PERSON_DAO.addPersonDAO(pPerson);
    }
    
    // Updates an existing person in the database
    public boolean updatePerson(Person pPerson) {
        return PERSON_DAO.updatePersonDAO(pPerson);
    }
    
    // Deletes a person from the database
    public boolean deletePerson(Person pPerson) {
        return PERSON_DAO.deletePersonDAO(pPerson);
    }
    
    // Searches for persons based on a search string
    public List<Person> searchPerson(String pSearch) {
        return PERSON_DAO.searchPersonDAO(pSearch);
    }
    
    // Searches for a person by their contact number
    public Person searchPersonByContactNo(String pContactNo) {
        return PERSON_DAO.searchPersonByContactNoDAO(pContactNo);
    }
}
