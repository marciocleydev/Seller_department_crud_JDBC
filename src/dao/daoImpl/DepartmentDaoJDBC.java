package dao.daoImpl;

import dao.DepartmentDao;
import db.DB;
import db.DbException;
import entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn = null;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("insert into department (Name) " +
                    "value (?) ", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, department.getName());
            int rowsEffected = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rowsEffected > 0) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("update department set Name =? " +
                    "where Id = ? ");
            ps.setString(1, department.getName());
            ps.setInt(2,department.getId());
            int rowsEffected = ps.executeUpdate();
            if (rowsEffected == 0){
                throw new DbException("Update not completed!");
            }
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResource(ps);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("delete from department " +
                    "where Id = ?");
            ps.setInt(1,id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0){
                throw new DbException("delete incompleted!");
            }
            else {
                System.out.println("delete completed! rows affected: " + rowsAffected);
            }
        }
        catch (SQLException e ){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResource(ps);
        }
    }

    @Override
    public Department findById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * from department where Id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                int departmentId = rs.getInt("Id");
                String name = rs.getString("Name");
                return new Department(departmentId, name);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * from department ");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                Department dep = new Department(id, name);
                departments.add(dep);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
        return departments;
    }
}
