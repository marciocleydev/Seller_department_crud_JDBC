package application;

import dao.DaoFactory;
import dao.SellerDao;
import entities.Seller;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("===TEST 1: Seller findById ====");
        Seller seller = sellerDao.findById(7);
        System.out.println(seller);




        sc.close();

    }
}
