package dto;

import entities.Address;
import entities.Person;

public class PersonDTO {

    private Integer id;
    private String fName;
    private String lName;
    private String phone;
    private String street;
    private String zip;
    private String city;

    public PersonDTO(Person p) {
        this.fName = p.getfName();
        this.lName = p.getlName();
        this.phone = p.getPhone();
        this.id = p.getId();
        this.street = p.getAddress().getStreet();
        this.zip = p.getAddress().getZip();
        this.city = p.getAddress().getCity();
    }

    public PersonDTO(String fn, String ln, String phone) {
        this.fName = fn;
        this.lName = ln;
        this.phone = phone;
    }

    public PersonDTO(String fName, String lName, String phone, String street, String zip, String city) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
