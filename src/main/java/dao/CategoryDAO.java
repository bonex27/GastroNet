package dao;

import domainModel.Category;

public interface CategoryDAO extends DAO<Category, Integer> {
    public int getNewId() throws Exception;
}
