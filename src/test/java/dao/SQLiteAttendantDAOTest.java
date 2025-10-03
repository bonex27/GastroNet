package dao;

import domainModel.Attendant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SQLiteAttendantDAOTest {

    private SQLiteAttendantDAO AttendantDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        //Set up database before test
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp () throws SQLException {
        AttendantDAO = new SQLiteAttendantDAO();
        Database.getConnection().prepareStatement("DELETE FROM Attendant").executeUpdate();

        Database.getConnection().prepareStatement("INSERT INTO Attendant(fiscal_code, name, surname, iban) VALUES ('AAABBB01A12B123A','Pietro', 'Bonechi','IT1234')").executeUpdate();
        Database.getConnection().prepareStatement("INSERT INTO Attendant(fiscal_code, name, surname, iban) VALUES ('BBBCCC01A12B123A','Andrea', 'Petrucci','IUT5678')").executeUpdate();

    }

    @Test
    void testGetAttendantByCF() throws SQLException {
        Attendant addedAttendant = AttendantDAO.get("AAABBB01A12B123A");
        Assertions.assertNotNull(addedAttendant);
        Assertions.assertEquals("AAABBB01A12B123A",addedAttendant.getCf());
        Assertions.assertEquals("Pietro",addedAttendant.getFirstName());
        Assertions.assertEquals("Bonechi",addedAttendant.getLastName());
        Assertions.assertEquals("IT1234",addedAttendant.getIban());

    }

    @Test
    void testGetAllAttendant() throws SQLException {

        List<Attendant> Attendants;
        Attendants = AttendantDAO.getAll();

        Assertions.assertNotNull(Attendants);
        Assertions.assertEquals(2,Attendants.size());

    }

    @Test
    void testInsertAttendant() throws SQLException {
        Attendant c1 = new Attendant("Mario","Rossi","CCC1234DDD","IT0987");
        AttendantDAO.insert(c1);

        Attendant addedAttendant = AttendantDAO.get("CCC1234DDD");

        Assertions.assertNotNull(addedAttendant);
        Assertions.assertEquals("CCC1234DDD", addedAttendant.getCf());
        Assertions.assertEquals("Mario", addedAttendant.getFirstName());
        Assertions.assertEquals("Rossi", addedAttendant.getLastName());
        Assertions.assertEquals("IT0987", addedAttendant.getIban());
    }

    @Test
    void testUpdateAttendant() throws SQLException {

        Attendant updatedAttendant =  new Attendant("NewPietro","NewBonechi","AAABBB01A12B123A","NewCard");
        AttendantDAO.update(updatedAttendant);

        updatedAttendant = AttendantDAO.get("AAABBB01A12B123A");
        Assertions.assertNotNull(updatedAttendant);
        Assertions.assertEquals("AAABBB01A12B123A",updatedAttendant.getCf());
        Assertions.assertEquals("NewPietro",updatedAttendant.getFirstName());
        Assertions.assertEquals("NewBonechi",updatedAttendant.getLastName());
        Assertions.assertEquals("NewCard",updatedAttendant.getIban());
    }
    @Test
    void testDeleteAttendant() throws SQLException {
        AttendantDAO.delete("AAABBB01A12B123A");
        Attendant deletedAttendant = AttendantDAO.get("CCC1234DDD");
        Assertions.assertNull(deletedAttendant);

    }
}
