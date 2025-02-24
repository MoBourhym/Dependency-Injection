package ma.di.metier;

import ma.di.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class IMetierImpl implements IMetier{
    @Autowired()
    private IDao dao;
    @Override
    public double calcul() {
        double data= dao.getData();
        double result=data*data;
        return result;
    }

    public IMetierImpl() {

    }


    public IMetierImpl(IDao dao) {
        this.dao = dao;
    }

    public IDao getDao() {
        return dao;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
