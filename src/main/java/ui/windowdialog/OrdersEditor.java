package ui.windowdialog;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import dao.StockDao;
import dao.OrderDao;
import dao.VehicleDao;
import model.Orders;
import model.Stock;
import model.Vehicle;
import ui.MainPage;


public class OrdersEditor extends Window {

    private static final long serialVersionUID = -4905260526000303401L;

    private JPAContainer<Vehicle> vehicle = JPAContainerFactory.make(Vehicle.class, "main");
    private ComboBox vehicleComboBox = new ComboBox("Машина", vehicle.getItemIds());

    private JPAContainer<Stock> stock = JPAContainerFactory.make(Stock.class, "main");
    private ComboBox stockComboBox = new ComboBox("Склад", stock.getItemIds());

    public OrdersEditor(Item menuItem, OrderDao orderDao, StockDao stockDao, VehicleDao vehicleDao, Grid userTable) {
        FormLayout formLayout = new FormLayout();
        for (Object id : vehicleComboBox.getItemIds()) {
            vehicleComboBox.setItemCaption(id, vehicleDao.read(id).toString());
        }
        for (Object id : stockComboBox.getItemIds()) {
            stockComboBox.setItemCaption(id, stockDao.read(id).getName());
        }
        BeanFieldGroup<Orders> binder = new BeanFieldGroup<>(Orders.class);
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());
        Button saveButton = new Button("Сохранить");

        binder.setItemDataSource(menuItem);
        BeanItem<Orders> beanItem = (BeanItem<Orders>) menuItem;
        vehicleComboBox.setNullSelectionAllowed(false);
        if (beanItem.getBean().getVehicle() != null) {
            vehicleComboBox.setValue(beanItem.getBean().getVehicle().getId());
        }

        stockComboBox.setNullSelectionAllowed(false);
        if (beanItem.getBean().getStock() != null) {
            stockComboBox.setValue(beanItem.getBean().getStock().getId());
        }

        formLayout.addComponent(vehicleComboBox);
        formLayout.addComponent(stockComboBox);
        saveButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (binder.isValid()) {
                try {
                    BeanItem<Orders> entityBeanItem = (BeanItem<Orders>) menuItem;
                    binder.commit();
                    if (menuItem.getItemProperty("id").getValue() != null) {
                        Orders oldEntity = orderDao.read(menuItem.getItemProperty("id").getValue());
                        Orders newEntity = entityBeanItem.getBean();
                        Vehicle vehicle = vehicleDao.read(vehicleComboBox.getValue());
                        newEntity.setVehicle(vehicle);
                        Stock projectEntity = stockDao.read(stockComboBox.getValue());
                        newEntity.setStock(projectEntity);
                        newEntity.setId(oldEntity.getId());
                        orderDao.update(newEntity);
                        userTable.clearSortOrder();
                        close();
                        return;
                    }
                    Orders newEntity = entityBeanItem.getBean();
                    if (vehicleComboBox.getValue() != null) {
                        Vehicle vehicle = vehicleDao.read(vehicleComboBox.getValue());
                        newEntity.setVehicle(vehicle);
                    }
                    if (stockComboBox.getValue() != null) {
                        Stock projectEntity = stockDao.read(stockComboBox.getValue());
                        newEntity.setStock(projectEntity);
                    }
                    orderDao.create(newEntity);
                    MainPage.ordersBeanContainer.addBean(newEntity);
                    close();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Ошибки при заполнении формы", Notification.Type.WARNING_MESSAGE);
            }
        });

        formLayout.addComponent(binder.buildAndBind("Дата доставки", "date"));
        formLayout.addComponent(binder.buildAndBind("Адрес доставки", "destination"));
        formLayout.addComponent(binder.buildAndBind("Комментарий к заказу", "comment"));
        formLayout.addComponent(binder.buildAndBind("Цена", "price"));
        formLayout.addComponent(saveButton);
        formLayout.addComponent(cancelButton);
        setContent(formLayout);

    }

}
