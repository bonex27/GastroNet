package domainModel;

public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected String cf;

    public Person(String firstName, String lastName, String cf) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cf = cf;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCf() {
        return cf;
    }

}
