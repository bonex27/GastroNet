package businessLogic;

import dao.DAO;
import domainModel.Attendant;

public class AttendantController extends PersonController<Attendant>{

    /**
     * Builds a controller with the given attendant DAO.
     *
     * @param dao DAO used to manage attendants.
     */
    public AttendantController(DAO<Attendant, String> dao) {
        super(dao);
    }

    /**
     * Creates and persists a new attendant, returning its fiscal code.
     *
     * @param firstName attendant first name.
     * @param lastName attendant last name.
     * @param cf attendant fiscal code.
     * @param iban attendant IBAN.
     * @return fiscal code of the created attendant.
     * @throws Exception if the DAO operation fails.
     */
    public String addAttendant(String firstName, String lastName, String cf,String iban) throws Exception {
        Attendant c = new Attendant(firstName, lastName, cf,iban);
        return  super.addPerson(c);

    }
}
