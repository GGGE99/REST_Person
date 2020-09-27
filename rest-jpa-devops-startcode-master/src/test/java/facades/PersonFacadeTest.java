package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2, p3;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Jens", "Jensen", "12345678");
        p1.setAddress(new Address("Lyngby hovedgade 2", "2800", "Lyngby"));
        p2 = new Person("Jønke", "Larsen", "87654321");
        p2.setAddress(new Address("Jærgersborg vej 21", "2800", "Lyngby"));
        p3 = new Person("Hans", "Madssen", "14789632");
        p3.setAddress(new Address("Bagsværd hovedgade 51", "2880", "Bagsværd"));
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetFacadeExample() {
        System.out.println("GET Farcade test");
        EntityManagerFactory _emf = null;
        PersonFacade expResult = null;
        PersonFacade result = PersonFacade.getPersonFacade(_emf);
        assertNotEquals(expResult, result);
    }

    @Test
//    @Disabled
    public void testAddPerson() throws Exception {
        System.out.println("Test addPerson");
        Person p = new Person("Aksel", "Sørnsen", "14785236");
        p.setAddress(new Address("Farum midtby 21", "3520", "Farum"));
        PersonDTO expdto = new PersonDTO(p);
        PersonDTO result = facade.addPerson(expdto.getfName(), expdto.getlName(), expdto.getPhone(), expdto.getStreet(), expdto.getZip(), expdto.getCity());
        assertEquals(expdto.getfName(), result.getfName());
        assertEquals(expdto.getlName(), result.getlName());
        assertEquals(expdto.getPhone(), result.getPhone());
    }

    @Test
    public void testDeletePerson() throws Exception {
        System.out.println("Test delete person");
        int id = p3.getId();
        PersonDTO expResult = new PersonDTO(p3);
        PersonDTO result = facade.deletePerson(id);
        assertThat(result, samePropertyValuesAs(expResult));
    }

    @Test
    public void testGetPerson() throws Exception {
        System.out.println("Test get Person");
        int id = p1.getId();
        PersonDTO expResult = new PersonDTO(p1);
        PersonDTO result = facade.getPerson(id);
        assertThat(result, samePropertyValuesAs(expResult));
    }

    @Test
    public void testGetAllPersons() {
        System.out.println("Test get all persons");
        int expResult = 3;
        PersonsDTO result = facade.getAllPersons();
        assertEquals(expResult, result.getAll().size());
        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        PersonDTO p3DTO = new PersonDTO(p3);
        assertThat(result.getAll(), containsInAnyOrder(samePropertyValuesAs(p1DTO), samePropertyValuesAs(p2DTO), samePropertyValuesAs(p3DTO)));
    }

    @Test
    public void testEditPerson() throws Exception {
        System.out.println("editPerson");
        PersonDTO p = new PersonDTO(p1);
        PersonDTO expResult = new PersonDTO(p1);
        expResult.setfName("peter");
        p.setfName("peter");
        PersonDTO result = facade.editPerson(p);
        assertEquals(expResult.getfName(), result.getfName());
    }
}
