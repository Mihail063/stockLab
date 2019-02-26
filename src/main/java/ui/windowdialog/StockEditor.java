package ui.windowdialog;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import dao.StockDao;
import model.Stock;
import ui.MainPage;

public class StockEditor extends Window {
    private static final long serialVersionUID = -7993306702939202239L;

    public StockEditor(Item compositionItem, StockDao stockDao, Grid customerTable) {
        FormLayout formLayout = new FormLayout();
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> close());
        final BeanFieldGroup<Stock> binder = new BeanFieldGroup<>(Stock.class);
        binder.setItemDataSource(compositionItem);
        Button saveButton = new Button("Сохранить");
        saveButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (binder.isValid()) {
                try {
                    BeanItem<Stock> beanItem = (BeanItem<Stock>) compositionItem;
                    binder.commit();
                    if (compositionItem.getItemProperty("id").getValue() != null) {
                        Stock oldEntity = stockDao.read(compositionItem.getItemProperty("id").getValue());
                        Stock newEntity = beanItem.getBean();
                        newEntity.setId(oldEntity.getId());
                        stockDao.update(newEntity);
                        customerTable.clearSortOrder();
                        close();
                        return;
                    }
                    Stock newEntity = beanItem.getBean();
                    stockDao.create(newEntity);
                    MainPage.stockBeanContainer.addBean(newEntity);
                    close();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Ошибки при заполнении формы", Notification.Type.WARNING_MESSAGE);
            }
        });
        formLayout.addComponent(binder.buildAndBind("Название склада", "name"));
        formLayout.addComponent(binder.buildAndBind("Адрес склада", "address"));
        formLayout.addComponent(saveButton);
        formLayout.addComponent(cancelButton);
        setContent(formLayout);

    }

}
