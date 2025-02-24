package ma.di.pres;

import ma.di.dao.DaoImpl;
import ma.di.dao.IDao;
import ma.di.metier.IMetierImpl;

public class PresentationStatique {
    public static void main(String[] args) {
        DaoImpl dao=new DaoImpl();
        IMetierImpl metier=new IMetierImpl(dao);
        System.out.println("Result: "+ metier.calcul());
    }
}
