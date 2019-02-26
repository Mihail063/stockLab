package dao;

import model.Stock;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class StockDao extends AbstractDao<Stock>{
    private static final String HQL_GET_ALL = "select entity from Stock entity";

    public StockDao() {
        super(Stock.class);
    }

    public List<Stock> getAll(){
        return entityManager.createQuery(HQL_GET_ALL, Stock.class).getResultList();
    }


}
