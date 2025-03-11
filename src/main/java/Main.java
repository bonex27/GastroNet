import dao.Database;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

    }
}