package entities;

import dto.PersonStyleDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Tester {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("Jønke", 1963);
        Person p2 = new Person("Blodie", 1959);

        Address a1 = new Address("Store torv 1", 2323, "Nr. snede");
        Address a2 = new Address("Langgade 34", 1212, "Valby");

        p1.setAddress(a1);
        p2.setAddress(a2);

        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);

        p1.addFee(f1);
        p2.addFee(f2);

        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle("ButterFly");
        SwimStyle s3 = new SwimStyle("Breas stroke");

        p1.addSwimStyle(s1);
        p1.addSwimStyle(s2);
        p2.addSwimStyle(s2);
        p2.addSwimStyle(s3);

        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.getTransaction().commit();

        em.getTransaction().begin();
        p1.removeSwimStyle(s2);
        em.getTransaction().commit();

        System.out.println("P1: " + p1.getP_id() + ", " + p1.getName());
        System.out.println("P1: " + p2.getP_id() + ", " + p2.getName());

        System.out.println("Jønkes gade: " + p1.getAddress().getStreet());

        System.out.println("Lad os se om tp-vejs virker: " + a1.getPerson().getName());
        
        Query q1 = em.createQuery("SELECT new dto.PersonStyleDto(p.name, p.year, s.styleName) FROM Person p JOIN p.styles s");
        
        List<PersonStyleDto> personDetails = q1.getResultList();
        for(PersonStyleDto ps: personDetails){
            System.out.println("Navn: " + ps.getName() + ", " + ps.getYear() + ", " + ps.getSwimStyle());
        }

    }

}
