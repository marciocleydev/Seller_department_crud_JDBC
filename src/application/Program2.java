package application;

import dao.DaoFactory;
import dao.DepartmentDao;
import entities.Department;

import java.util.List;
import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        System.out.println("===TEST 1: Department findById ====");
        Department dep = departmentDao.findById(2);
        System.out.println(dep);
        System.out.println("\n===TEST 3: Department findAll ====");
        List<Department> deps = departmentDao.findAll();
        deps.forEach(x-> System.out.println(x));
        System.out.println("\n===TEST 4: Department insert into ====");
        Department departInsert = new Department(null, "Toys");
        departmentDao.insert(departInsert);
        System.out.println("new id: " + departInsert.getId());
        System.out.println("\n===TEST 5: Department Update ====");
        departInsert.setName("Nails");
        departInsert.setId(1);
        departmentDao.update(departInsert);
        System.out.println("new department: " + departInsert);
        System.out.println("\n===TEST 6: Department delete ====");
        System.out.print("Digite um id para ser deletado: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);


        sc.close();
    }
}
