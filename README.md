# Dependency Injection - Rapport Complet

## Table des Matières
1. [Introduction à l'Injection de Dépendances](#introduction)
2. [Architecture du Projet](#architecture)
3. [Approches d'Injection des Dépendances](#approches)
   - [Instanciation Statique](#statique)
   - [Instanciation Dynamique](#dynamique)
   - [Spring XML](#spring-xml)
   - [Spring Annotations](#spring-annotations)
4. [Comparaison des Approches](#comparaison)
5. [Conclusion](#conclusion)

## Introduction à l'Injection de Dépendances {#introduction}

L'injection de dépendances (DI - Dependency Injection) est un patron de conception qui permet de réduire le couplage entre les classes en externalisant la création et l'injection des dépendances. Au lieu qu'une classe crée directement ses dépendances, ces dernières lui sont fournies de l'extérieur.

### Avantages de l'Injection de Dépendances :
- **Couplage faible** : Les classes ne dépendent que d'abstractions (interfaces)
- **Testabilité** : Facilite les tests unitaires avec des mocks
- **Flexibilité** : Permet de changer d'implémentation facilement
- **Réutilisabilité** : Les composants sont plus modulaires

## Architecture du Projet {#architecture}

Le projet est structuré autour de deux interfaces principales :

### Interface IDao
```java
public interface IDao {
    double getData();
}
```

### Interface IMetier
```java
public interface IMetier {
    double calcul();
}
```

Cette architecture respecte le principe d'inversion de dépendance (DIP) où les modules de haut niveau ne dépendent pas des modules de bas niveau, mais tous deux dépendent d'abstractions.

## Approches d'Injection des Dépendances {#approches}

### 1. Instanciation Statique {#statique}

L'approche la plus simple mais la plus rigide. Les dépendances sont créées directement dans le code.

```java
// filepath: PresentationStatique.java
package ma.di.pres;

import ma.di.dao.DaoImpl;
import ma.di.dao.IDao;
import ma.di.metier.IMetierImpl;

public class PresentationStatique {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();
        IMetierImpl metier = new IMetierImpl(dao);
        System.out.println("Result: " + metier.calcul());
    }
}
```

![Console output Static Instantiation](resources/captures/SI.png)


**Avantages :**
- Simple à comprendre et implémenter
- Contrôle total sur la création des objets
- Pas de dépendance externe

**Inconvénients :**
- Couplage fort entre les classes
- Difficile à tester (pas de possibilité d'injection de mocks)
- Inflexible (changement d'implémentation nécessite modification du code)

### 2. Instanciation Dynamique {#dynamique}

Cette approche utilise la réflexion Java pour créer les instances à partir d'un fichier de configuration.

```java
// filepath: PresentationDynamique.java
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

            String metierClassname = scanner.nextLine();
            Class<?> cMetier = Class.forName(metierClassname);
            IMetierImpl metier = (IMetierImpl) cMetier.getConstructor(IDao.class).newInstance(dao);

            System.out.println("Result of Dynamic injection: " + metier.calcul());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```

**Fichier de configuration (config.txt) :**
```
ma.di.dao.DaoImpl
ma.di.metier.IMetierImpl
```

![Console output Instanciation Dynamique](resources/captures/DI.png)

**Avantages :**
- Flexibilité : changement d'implémentation sans recompilation
- Découplage partiel du code métier
- Introduction aux concepts d'IoC (Inversion of Control)

**Inconvénients :**
- Gestion d'erreurs complexe (ClassNotFoundException, etc.)
- Performance moindre due à la réflexion
- Code plus complexe à maintenir

### 3. Spring XML {#spring-xml}

Spring Framework avec configuration XML offre un conteneur IoC complet.

```java
// filepath: PresentationSpringXml.java
package ma.di.pres;

import ma.di.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresentationSpringXml {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Result of spring xml dependency injection: " + metier.calcul());
    }
}
```

**Configuration XML (config.xml) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN 2.0//EN"
        "http://www.springframework.org/dtd/spring-beans-2.0.dtd">

<beans>
    <bean id="dao" class="ma.di.dao.DaoImpl">
    </bean>
    
    <bean id="metier" class="ma.di.metier.IMetierImpl">
        <property name="dao" ref="dao"></property>
    </bean>
</beans>
```

![Console output Spring XML](resources/captures/XMLDI.png)

**Avantages :**
- Conteneur IoC professionnel et robuste
- Gestion automatique du cycle de vie des beans
- Configuration centralisée
- Support des scopes (singleton, prototype, etc.)
- Gestion des transactions, AOP, etc.

**Inconvénients :**
- Configuration XML verbose
- Pas de validation à la compilation
- Difficile de naviguer entre le code et la configuration

### 4. Spring Annotations {#spring-annotations}

Approche moderne utilisant les annotations pour la configuration.

```java
// filepath: PresentationSpringAnnotation.java
package ma.di.pres;

import ma.di.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresentationSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ma.di.dao", "ma.di.metier");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Result of dependency injection using spring annotations: " + metier.calcul());
    }
}
```

**Configuration avec annotations :**
```java
// Dans DaoImpl.java
@Component
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        // Simulation d'accès aux données
        return Math.random() * 100;
    }
}

// Dans IMetierImpl.java
@Component
public class IMetierImpl implements IMetier {
    @Autowired
    private IDao dao;
    
    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

![Console output Spring Annotations ](resources/captures/DIANN.png)

**Avantages :**
- Configuration proche du code
- Validation à la compilation
- Moins de configuration XML
- IntelliSense et refactoring facilités
- Approche moderne et recommandée

**Inconvénients :**
- Couplage entre le code métier et Spring
- Moins visible que la configuration XML
- Nécessite une bonne compréhension des annotations

## Comparaison des Approches {#comparaison}

| Critère | Statique | Dynamique | Spring XML | Spring Annotations |
|---------|----------|-----------|------------|-------------------|
| **Complexité** | ⭐ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ |
| **Flexibilité** | ⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Testabilité** | ⭐ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Performance** | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ |
| **Maintenabilité** | ⭐ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |

### Types d'Injection Supportés

1. **Injection par Constructeur** : Recommandée pour les dépendances obligatoires
2. **Injection par Setter** : Pour les dépendances optionnelles
3. **Injection par Field** : Simple mais moins testable

## Configuration Maven {#maven}

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.30</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
        <version>5.3.30</version>
    </dependency>
</dependencies>
```

## Conclusion {#conclusion}

L'injection de dépendances est un concept fondamental dans le développement d'applications modernes. Ce projet démontre l'évolution des approches :

1. **De l'instanciation statique** (simple mais rigide)
2. **À l'instanciation dynamique** (plus flexible mais complexe)
3. **Vers Spring Framework** (solution complète et professionnelle)

**Recommandations :**
- Utiliser **Spring Annotations** pour les nouveaux projets
- **Spring XML** pour les configurations complexes nécessitant une vue d'ensemble
- Éviter l'instanciation statique sauf pour des cas très simples
- L'instanciation dynamique peut servir de base pour comprendre les frameworks IoC

L'injection de dépendances améliore significativement la qualité du code en favorisant :
- La **modularité**
- La **testabilité** 
- La **maintenabilité**
- La **flexibilité**

Cette approche est essentielle pour développer des applications robustes et évolutives.
