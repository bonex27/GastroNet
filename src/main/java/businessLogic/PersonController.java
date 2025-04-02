package businessLogic;

import dao.DAO;
import domainModel.Person;

public abstract class PersonController<T extends Person> {
    private final DAO<T,String> dao;

    protected PersonController(DAO<T, String> dao) {
        this.dao = dao;
    }

    protected String addPerson(T newPerson) throws Exception {
        this.dao.insert(newPerson);
        return newPerson.getCf();
    }

    protected boolean deletePerson(String fiscalCode) throws Exception {
        return this.dao.delete(fiscalCode);
    }

    protected T getPerson(String fiscalCode) throws Exception {
        return  this.dao.get(fiscalCode);
    }
}
