package ma.di.pres;

import ma.di.metier.IMetier;
import ma.di.metier.IMetierImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresentationSpringXml {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        IMetier metier=context.getBean(IMetier.class);
        System.out.println("Result of spring xml dependency injection: "+metier.calcul());
    }
}
