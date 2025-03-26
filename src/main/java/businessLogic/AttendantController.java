package businessLogic;

import dao.DAO;
import domainModel.Attendant;
import domainModel.Customer;

public class AttendantController extends PersonController<Attendant>{

    public AttendantController(DAO<Attendant, String> dao) {
        super(dao);
    }

    public String addAttendant(String firstName, String lastName, String cf,String iban) throws Exception {
        Attendant c = new Attendant(firstName, lastName, cf,iban);
        return  super.addPerson(c);

    }
}
