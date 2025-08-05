package application;

import dao.DaoFactory;
import dao.SellerDao;
import entities.Department;
import entities.Seller;

import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("===TEST 1: Seller findById ====");
        Seller seller = sellerDao.findById(7);
        System.out.println(seller);
        System.out.println("\n===TEST 2: Seller findByDepartment ====");
        Department department = new Department(2,"Computers");
        List<Seller> sellers = sellerDao.findByDepartment(department);
        sellers.forEach(System.out::println);
        System.out.println("\n===TEST 3: Seller findAll ====");
        sellers = sellerDao.findAll();
        sellers.forEach(System.out::println);


        sc.close();

    }
}
