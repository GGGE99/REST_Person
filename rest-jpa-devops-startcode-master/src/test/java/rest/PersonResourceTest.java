package rest;

import dto.PersonDTO;
import entities.Address;
import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test

//@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2, p3;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
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

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testGetPersonMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Your Person API is up and running"));
    }

    @Test
    public void getAllPersons() {
        List<PersonDTO> personsDTOs;

        personsDTOs = given()
                .contentType("application/json")
                .when()
                .get("/person/all")
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);

        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        PersonDTO p3DTO = new PersonDTO(p3);

        assertThat(personsDTOs, containsInAnyOrder(samePropertyValuesAs(p1DTO), samePropertyValuesAs(p2DTO), samePropertyValuesAs(p3DTO)));
    }

    @Test
    public void addPerson() {
        given()
                .contentType(ContentType.JSON)
                .body(new PersonDTO("Marc", "Ekstrom", "11112222", "Who knows", "5599", "IDK"))
                .when()
                .post("person")
                .then()
                .body("fName", equalTo("Marc"))
                .body("lName", equalTo("Ekstrom"))
                .body("phone", equalTo("11112222"))
                .body("id", notNullValue())
                .body("street", equalTo("Who knows"))
                .body("zip", equalTo("5599"))
                .body("city", equalTo("IDK"));
    }

    @Test
    public void updatePerson() {
        PersonDTO person = new PersonDTO(p3);
        person.setPhone("12345678");

        given()
                .contentType(ContentType.JSON)
                .body(person)
                .when()
                .put("person/" + person.getId())
                .then()
                .body("fName", equalTo("Hans"))
                .body("lName", equalTo("Madssen"))
                .body("phone", equalTo("12345678"))
                .body("id", equalTo((int) person.getId()));
    }

    @Test
    public void testDelete() throws Exception {

        PersonDTO person = new PersonDTO(p1);

        given()
                .contentType("application/json")
                .delete("/person/" + person.getId())
                .then()
                .assertThat()
                .body("fName", equalTo(person.getfName()))
                .body("lName", equalTo(person.getlName()))
                .body("id", equalTo(person.getId()));

    }
}
