package dao;

import entities.Department;
import entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(int id);
    Department findById(int id);
    List<Department> findAll();
}
