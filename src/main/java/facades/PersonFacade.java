/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author marcg
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    public PersonFacade() {
    }

    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();

        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException(String.format("No person with provided id found for id %d", id));
            } else {
                return new PersonDTO(person);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();

        try {
            Query query = em.createQuery("SELECT p FROM Person p");
            List<Person> persons = query.getResultList();
            PersonsDTO personsDTO = new PersonsDTO(persons);
            return personsDTO;
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone, String street, String zip, String city) {
        EntityManager em = emf.createEntityManager();
        try {
            Person p = new Person(fName, lName, phone);
            p.setAddress(new Address(street, zip, city));
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            PersonDTO pDTO = new PersonDTO(p);

            return pDTO;
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            Person per = em.find(Person.class, p.getId());
            if (per == null) {
                throw new PersonNotFoundException(String.format("Could not edit, provided id does not exist for id %d", p.getId()));
            } else {
                em.getTransaction().begin();
                per.setPhone(p.getPhone());
                per.setfName(p.getfName());
                per.setlName(p.getlName());
                em.getTransaction().commit();
                return new PersonDTO(per);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {

            Person p = em.find(Person.class, id);
            if (p == null) {
                throw new PersonNotFoundException(String.format("Could not delete, provided id does not exist for id %d", id));
            } else {
                em.getTransaction().begin();
                em.remove(p);
                em.getTransaction().commit();
                return new PersonDTO(p);
            }
        } finally {
            em.close();
        }
    }

}
