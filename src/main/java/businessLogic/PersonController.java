package businessLogic;

import dao.DAO;
import domainModel.Person;

public abstract class PersonController<T extends Person> {
    private final DAO<T,String> dao;

    /**
     * Builds a base controller with the given DAO.
     *
     * @param dao DAO used to manage people.
     */
    protected PersonController(DAO<T, String> dao) {
        this.dao = dao;
    }

    /**
     * Persists a person and returns the fiscal code.
     *
     * @param newPerson person to persist.
     * @return fiscal code of the saved person.
     * @throws Exception if the DAO operation fails.
     */
    protected String addPerson(T newPerson) throws Exception {
        this.dao.insert(newPerson);
        return newPerson.getCf();
    }

    /**
     * Deletes a person by fiscal code.
     *
     * @param fiscalCode person fiscal code.
     * @return true if the person was deleted.
     * @throws Exception if the DAO operation fails.
     */
    protected boolean deletePerson(String fiscalCode) throws Exception {
        return this.dao.delete(fiscalCode);
    }

    /**
     * Fetches a person by fiscal code.
     *
     * @param fiscalCode person fiscal code.
     * @return person instance if found.
     * @throws Exception if the DAO operation fails.
     */
    protected T getPerson(String fiscalCode) throws Exception {
        return  this.dao.get(fiscalCode);
    }
}
