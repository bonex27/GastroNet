package domainModel;

public class Attendant extends Person {
    private String iban;

    public Attendant(String firstName, String lastName, String cf,String iban) {
        super(firstName, lastName, cf);
        this.iban = iban;
    }

    public Attendant(Attendant attendant) {
        super(attendant.getFirstName(), attendant.getLastName(), attendant.getCf());
        this.iban = attendant.getIban();
    }

    public String getIban()
    {
        return iban;
    }
}
