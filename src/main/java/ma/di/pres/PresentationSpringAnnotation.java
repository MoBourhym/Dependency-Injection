package ma.di.pres;

import ma.di.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresentationSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ma.di.dao", "ma.di.metier");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Result of depemdency injection using spring annotations: " + metier.calcul());
    }
}
