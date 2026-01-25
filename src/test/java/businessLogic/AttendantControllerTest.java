package businessLogic;

import dao.Database;
import dao.SQLiteAttendantDAO;
import domainModel.Attendant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AttendantControllerTest {

    private AttendantController attendantController;
    private Attendant attendant;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp() throws SQLException {
        Database.getConnection().prepareStatement("DELETE FROM main.Attendant").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();

        SQLiteAttendantDAO attendantDAO = new SQLiteAttendantDAO();
        attendantController = new AttendantController(attendantDAO);
        attendant = new Attendant("Pietro", "Bonechi", "AAA", "ITAAA1234");
        attendantDAO.insert(attendant);
    }

    @Test
    void testAddAttendant_Success() throws Exception {
        String cf = attendantController.addAttendant("Mario", "Rossi", "RSSMRA80A01H501X", "IT60X0542811101000000123456");

        assertEquals("RSSMRA80A01H501X", cf);

        Attendant retrieved = attendantController.getPerson(cf);
        assertNotNull(retrieved);
        assertEquals("Mario", retrieved.getFirstName());
        assertEquals("Rossi", retrieved.getLastName());
    }

    @Test
    void testAddAttendant_DuplicateCF_ThrowsException() {
        assertThrows(Exception.class, () ->
                attendantController.addAttendant("Pietro", "Bonechi", "AAA", "ITBBB5678")
        );
    }

    @Test
    void testDeleteAttendant_Success() throws Exception {
        boolean result = attendantController.deletePerson("AAA");
        assertTrue(result);

        Attendant retrieved = attendantController.getPerson("AAA");
        assertNull(retrieved);
    }

    @Test
    void testGetAttendant_Success() throws Exception {
        Attendant retrieved = attendantController.getPerson("AAA");

        assertNotNull(retrieved);
        assertEquals("Pietro", retrieved.getFirstName());
        assertEquals("Bonechi", retrieved.getLastName());
        assertEquals("ITAAA1234", retrieved.getIban());
    }

    @Test
    void testGetAttendant_NotFound_ReturnsNull() throws Exception {
        Attendant retrieved = attendantController.getPerson("NONEXISTENT");
        assertNull(retrieved);
    }
}
