package dao;

import dao.daoImpl.DepartmentDaoJDBC;
import dao.daoImpl.SellerDaoJDBC;
import db.DB;

import java.sql.Connection;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }
    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
