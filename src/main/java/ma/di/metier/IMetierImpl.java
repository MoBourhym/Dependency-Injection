package ma.di.metier;

import ma.di.dao.IDao;

public class IMetierImpl implements IMetier{
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
