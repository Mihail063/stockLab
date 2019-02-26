package dao;

import model.Orders;
import model.Stock;
import model.Vehicle;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class OrderDao extends AbstractDao<Orders> {

    private static final String HQL_GET_ALL = "select entity from Orders entity";

    private static final String HQL_GET_ALL_BY_CUSTOMER = "select entity from Orders entity where entity.stock=:stock";

    private static final String HQL_GET_ALL_BY_TARIFF = "select entity from Orders entity where entity.vehicle=:vehicle";

    public OrderDao() {
        super(Orders.class);
    }

    public List<Orders> getAll(){
        return entityManager.createQuery(HQL_GET_ALL, Orders.class).getResultList();
    }

    public List<Orders> getAllByStock(Stock stock){
        return entityManager.createQuery(HQL_GET_ALL_BY_CUSTOMER, Orders.class)
                .setParameter("stock", stock)
                .getResultList();
    }

    public List<Orders> getAllByVehicle(Vehicle vehicle){
        return entityManager.createQuery(HQL_GET_ALL_BY_TARIFF, Orders.class)
                .setParameter("vehicle", vehicle)
                .getResultList();
    }
}
