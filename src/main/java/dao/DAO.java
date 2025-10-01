package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, ID> {

    T get(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
    void insert(T t) throws SQLException;
    void update(T t) throws SQLException;
    boolean delete(ID id) throws SQLException;

}
