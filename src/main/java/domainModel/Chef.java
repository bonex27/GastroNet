package domainModel;

public class Chef extends Person {
    private String Iban;

    public Chef(String firstName, String lastName, String email, String cf, String Iban) {

        super(firstName, lastName, email, cf);
        this.Iban = Iban;
    }
}
