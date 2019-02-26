package dao;

import model.Vehicle;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class VehicleDao extends AbstractDao<Vehicle>{
    private static final String HQL_GET_ALL = "select entity from Vehicle entity";

    public VehicleDao() {
        super(Vehicle.class);
    }

    public List<Vehicle> getAll(){
        return entityManager.createQuery(HQL_GET_ALL, Vehicle.class).getResultList();
    }
}
