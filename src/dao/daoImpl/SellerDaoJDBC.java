package dao.daoImpl;

import dao.SellerDao;
import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn = null;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

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
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
               seller = instatiateSeller(rs,instantiateDepartment(rs));
            }
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResource(rs);
            DB.closeResource(ps);
        }
        return seller;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
    private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
        int idSeller = rs.getInt("Id");
        String nameSaller = rs.getString("Name");
        String email = rs.getString("Email");
        LocalDate birthDate = LocalDate.parse(rs.getDate("BirthDate").toString());
        double baseSalary = rs.getDouble("BaseSalary");
        return new Seller(idSeller, nameSaller, email,birthDate, baseSalary,dep);
    }
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        int departmentId = rs.getInt("DepartmentId");
        String departmentName = rs.getString("DepName");
        return new Department(departmentId,departmentName);
    }
}
