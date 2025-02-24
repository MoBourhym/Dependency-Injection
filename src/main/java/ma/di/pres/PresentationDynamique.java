package ma.di.pres;

import ma.di.dao.IDao;
import ma.di.metier.IMetier;
import ma.di.metier.IMetierImpl;

import java.io.File;
import java.util.Scanner;

public class PresentationDynamique {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("config.txt"));
            String daoClassName = scanner.nextLine();
            Class<?> cDao = Class.forName(daoClassName);
            IDao dao = (IDao) cDao.getConstructor().newInstance();

            String metierClassname=scanner.nextLine();
            Class<?> cMetier = Class.forName(metierClassname);
            IMetierImpl metier=(IMetierImpl) cMetier.getConstructor(IDao.class).newInstance(dao);


            System.out.println("Result of Dynamic injection: "+metier.calcul());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
