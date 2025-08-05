package dao.daoImpl;

import dao.SellerDao;
import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn = null;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(" insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, seller.getName());
            ps.setString(2, seller.getEmail());
            LocalDate birthDate = LocalDate.parse(seller.getBirthDate().toString());
            ps.setDate(3, java.sql.Date.valueOf(birthDate));
            ps.setDouble(4, seller.getBaseSalary());
            ps.setInt(5, seller.getDepartment().getId());

            int rowsAffect = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rowsAffect > 0) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
            } else {
                throw new DbException("Erro to insert into new seller! ");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(" update seller set Name = ?, Email =?, BirthDate =?, BaseSalary = ?, DepartmentId =? " +
                    "where id = ?");
            ps.setString(1,seller.getName());
            ps.setString(2, seller.getEmail());
            ps.setDate(3,java.sql.Date.valueOf(seller.getBirthDate()));
            ps.setDouble(4, seller.getBaseSalary());
            ps.setInt(5, seller.getDepartment().getId());
            ps.setInt(6, seller.getId());

            int rowsEffected = ps.executeUpdate();
            if (rowsEffected > 0){
                System.out.println("Rows effected: " + rowsEffected);
            }
            else {
                throw  new DbException("upadate error!");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResource(ps);
        }
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Seller findById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Seller seller = null;
        try {
            ps = conn.prepareStatement("select seller.*,department.Name as DepName " +
                    "from seller INNER join department " +
                    "on seller.DepartmentId = department.Id " +
                    "WHERE  seller.id =?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                seller = instatiateSeller(rs, instantiateDepartment(rs));
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
        return seller;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        List<Seller> sellers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select seller.*,department.Name as DepName " +
                    "from seller inner join department " +
                    "on seller.DepartmentId = department.Id " +
                    "where department.id = ?");
            ps.setInt(1, department.getId());
            rs = ps.executeQuery();
            Map<Integer, Department> departmentMap = new HashMap<>();
            while (rs.next()) {
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instatiateSeller(rs, dep);
                sellers.add(seller);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
        return sellers;
    }

    @Override
    public List<Seller> findAll() {
        List<Seller> sellers = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select seller.*,department.Name as DepName " +
                    "from seller inner join department " +
                    "on seller.DepartmentId = department.Id " +
                    "order by Name");
            rs = ps.executeQuery();
            Map<Integer, Department> departmentMap = new HashMap<>();
            while (rs.next()) {
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instatiateSeller(rs, dep);
                sellers.add(seller);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
        return sellers;
    }

    private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
        int idSeller = rs.getInt("Id");
        String nameSaller = rs.getString("Name");
        String email = rs.getString("Email");
        LocalDate birthDate = LocalDate.parse(rs.getDate("BirthDate").toString());
        double baseSalary = rs.getDouble("BaseSalary");
        return new Seller(idSeller, nameSaller, email, birthDate, baseSalary, dep);
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        int departmentId = rs.getInt("DepartmentId");
        String departmentName = rs.getString("DepName");
        return new Department(departmentId, departmentName);
    }
}
