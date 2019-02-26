package ui.windowdialog;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import dao.VehicleDao;
import model.Vehicle;
import ui.MainPage;

public class VehicleEditor extends Window {

    private static final long serialVersionUID = -4768711085883226548L;

    public VehicleEditor(Item sharesItem, VehicleDao vehicleDao, Grid tariffTable) {
        FormLayout formLayout = new FormLayout();
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());
        final BeanFieldGroup<Vehicle> binder = new BeanFieldGroup<>(Vehicle.class);
        binder.setItemDataSource(sharesItem);
        Button saveButton = new Button("Сохранить");
        saveButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (binder.isValid()) {
                try {
                    BeanItem<Vehicle> beanItem = (BeanItem<Vehicle>) sharesItem;
                    binder.commit();
                    if (sharesItem.getItemProperty("id").getValue() != null) {
                        Vehicle oldEntity = vehicleDao.read(sharesItem.getItemProperty("id").getValue());
                        Vehicle newEntity = beanItem.getBean();
                        newEntity.setId(oldEntity.getId());
                        vehicleDao.update(newEntity);
                        tariffTable.clearSortOrder();
                        close();
                        return;
                    }
                    Vehicle newEntity = beanItem.getBean();
                    vehicleDao.create(newEntity);
                    MainPage.vehicleBeanContainer.addBean(newEntity);
                    close();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Ошибки при заполнении формы", Notification.Type.WARNING_MESSAGE);
            }
        });
        formLayout.addComponent(binder.buildAndBind("Модель", "model"));
        formLayout.addComponent(binder.buildAndBind("Регистрационный номер", "number"));
        formLayout.addComponent(binder.buildAndBind("Водитель", "performer"));
        formLayout.addComponent(saveButton);
        formLayout.addComponent(cancelButton);
        setContent(formLayout);

    }
}
