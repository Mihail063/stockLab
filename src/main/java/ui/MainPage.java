package ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import dao.StockDao;
import dao.OrderDao;
import dao.VehicleDao;
import model.Orders;
import model.Stock;
import model.Vehicle;
import org.jetbrains.annotations.NotNull;
import ui.windowdialog.StockEditor;
import ui.windowdialog.OrdersEditor;
import ui.windowdialog.VehicleEditor;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

@Theme("valo")
@CDIUI("")
public class MainPage extends UI {

    @NotNull
    private static final String ADD = "Добавить";
    @NotNull
    private static final String UPDATE = "Редактировать";
    @NotNull
    private static final String REMOVE = "Удалить";

    @NotNull
    public static final String ORDERS_TABLE_NAME = "Заказы";

    @NotNull
    public static final String STOCK_TABLE_NAME = "Склады";

    @NotNull
    public static final String VEHICLE_TABLE_NAME = "Машины";

    @EJB
    private OrderDao orderDao;

    @EJB
    private StockDao stockDao;

    @EJB
    private VehicleDao vehicleDao;

    @NotNull
    public static final BeanContainer<String, Orders> ordersBeanContainer = new BeanContainer<>(Orders.class);

    @NotNull
    public static final BeanContainer<String, Stock> stockBeanContainer = new BeanContainer<>(Stock.class);

    @NotNull
    public static final BeanContainer<String, Vehicle> vehicleBeanContainer = new BeanContainer<>(Vehicle.class);


    @NotNull
    private final TabSheet tabsheet = new TabSheet();

    private Grid ordersTable = new Grid();
    private Grid stockTable = new Grid();
    private Grid vehicleTable = new Grid();

    @NotNull
    private MenuBar menubar = new MenuBar();


    final MenuBar.MenuItem file = menubar.addItem("Меню", null);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        @NotNull
        final VerticalLayout layout = new VerticalLayout();
        initButtonSet();
        layout.addComponent(menubar);
        initTabSheet();
        layout.addComponent(tabsheet);

        int height = getCurrent().getPage().getBrowserWindowHeight() - 50;
        tabsheet.setHeight(String.valueOf(height));
        setContent(layout);
        this.setSizeFull();

        this.getPage().addBrowserWindowResizeListener((Page.BrowserWindowResizeListener) browserWindowResizeEvent -> {
            int height1 = getCurrent().getPage().getBrowserWindowHeight() - 50;
            tabsheet.setHeight(String.valueOf(height1));
            setContent(layout);
            setSizeFull();
        });

    }

    private void initButtonSet() {
        file.addItem(ADD, addButton);
        file.addItem(REMOVE, removeButton);
        file.addItem(UPDATE, updateButton);
    }


    private void initTabSheet() {
        fillOrdersTable();
        ordersTable.setSizeFull();
        @NotNull VerticalLayout ordersTabSheet = new VerticalLayout();
        ordersTabSheet.setCaption(ORDERS_TABLE_NAME);
        ordersTabSheet.addComponent(ordersTable);
        ordersTabSheet.setSizeFull();
        tabsheet.addTab(ordersTabSheet, ORDERS_TABLE_NAME);
        ordersTabSheet.setMargin(true);

        fillStockTable();
        stockTable.setSizeFull();
        @NotNull VerticalLayout projectTabSheet = new VerticalLayout();
        projectTabSheet.setCaption(STOCK_TABLE_NAME);
        projectTabSheet.addComponent(stockTable);
        projectTabSheet.setSizeFull();
        tabsheet.addTab(projectTabSheet, STOCK_TABLE_NAME);
        projectTabSheet.setMargin(true);

        fillVehicleTable();
        vehicleTable.setSizeFull();
        @NotNull VerticalLayout userProjectTabSheet = new VerticalLayout();
        userProjectTabSheet.setCaption(VEHICLE_TABLE_NAME);
        userProjectTabSheet.addComponent(vehicleTable);
        userProjectTabSheet.setSizeFull();
        tabsheet.addTab(userProjectTabSheet, VEHICLE_TABLE_NAME);
        userProjectTabSheet.setMargin(true);

    }

    private void fillVehicleTable() {
        vehicleBeanContainer.removeAllItems();
        vehicleTable.setEditorEnabled(false);
        vehicleBeanContainer.setBeanIdProperty("id");
        vehicleBeanContainer.addAll(vehicleDao.getAll());
        vehicleTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        vehicleTable.setContainerDataSource(vehicleBeanContainer);
        vehicleTable.setColumnOrder(
                "id",
                "performer",
                "model",
                "number"
        );
        vehicleTable.removeColumn("id");
        vehicleTable.getColumn("performer").setHeaderCaption("Водитель");
        vehicleTable.getColumn("model").setHeaderCaption("Модель");
        vehicleTable.getColumn("number").setHeaderCaption("Регистрационный номер");
    }

    private void fillStockTable() {
        stockBeanContainer.removeAllItems();
        stockTable.setEditorEnabled(false);
        stockBeanContainer.setBeanIdProperty("id");
        stockBeanContainer.addAll(stockDao.getAll());
        stockTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        stockTable.setContainerDataSource(stockBeanContainer);
        stockTable.setColumnOrder(
                "id",
                "name",
                "address"
        );
        stockTable.removeColumn("id");
        stockTable.getColumn("name").setHeaderCaption("Название");
        stockTable.getColumn("address").setHeaderCaption("Адрес склада");
    }

    private void fillOrdersTable() {
        ordersBeanContainer.removeAllItems();
        ordersTable.setEditorEnabled(false);
        ordersBeanContainer.setBeanIdProperty("id");
        if (orderDao != null) {
            ordersBeanContainer.addAll(orderDao.getAll());
        }
        ordersTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        ordersTable.setContainerDataSource(ordersBeanContainer);
        ordersTable.setColumnOrder(
                "id",
                "stock",
                "vehicle",
                "date",
                "destination",
                "comment",
                "price"
        );
        ordersTable.removeColumn("id");
        ordersTable.getColumn("stock").setHeaderCaption("Склад");
        ordersTable.getColumn("vehicle").setHeaderCaption("Машина");
        ordersTable.getColumn("date").setHeaderCaption("Дата доставки");
        ordersTable.getColumn("destination").setHeaderCaption("Место доставки");
        ordersTable.getColumn("comment").setHeaderCaption("Комментарий к заказу");
        ordersTable.getColumn("price").setHeaderCaption("Цена заказа");
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainPage.class, productionMode = false)
    public static class MyUIServlet extends VaadinCDIServlet {
    }

    private MenuBar.Command updateButton = (MenuBar.Command) selectedItem -> {
        String id = null;
        if (tabsheet.getSelectedTab().getCaption().equals(ORDERS_TABLE_NAME) && ordersTable.getSelectedRow() != null) {
            id = (String) ordersTable.getSelectedRow();
            final BeanItem<Orders> newUserItem = new BeanItem<>(ordersBeanContainer.getItem(id).getBean());
            addWindow(new OrdersEditor(newUserItem, orderDao, stockDao, vehicleDao, ordersTable));
            ordersTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(STOCK_TABLE_NAME) && stockTable.getSelectedRow() != null) {
            id = (String) stockTable.getSelectedRow();
            final BeanItem<Stock> newCustomeItem = new BeanItem<>(stockBeanContainer.getItem(id).getBean());
            addWindow(new StockEditor(newCustomeItem, stockDao, stockTable));
            stockTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(VEHICLE_TABLE_NAME) && vehicleTable.getSelectedRow() != null) {
            id = (String) vehicleTable.getSelectedRow();
            final BeanItem<Vehicle> newTariffItem = new BeanItem<>(vehicleBeanContainer.getItem(id).getBean());
            addWindow(new VehicleEditor(newTariffItem, vehicleDao, vehicleTable));
            vehicleTable.deselect(id);
        } else {
            Notification.show("Не выбрана строка в таблице.", Notification.Type.HUMANIZED_MESSAGE);
        }
    };

    private MenuBar.Command removeButton = (MenuBar.Command) selectedItem -> {
        String id = null;
        if (tabsheet.getSelectedTab().getCaption().equals(ORDERS_TABLE_NAME) && ordersTable.getSelectedRow() != null) {
            id = (String) ordersTable.getSelectedRow();
            orderDao.delete(id);
            ordersBeanContainer.removeItem(id);
            ordersTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(STOCK_TABLE_NAME) && stockTable.getSelectedRow() != null) {
            id = (String) stockTable.getSelectedRow();
            if (orderDao.getAllByStock(stockDao.read(id)).size() > 0) {
                Notification.show("Отпланируйте все заказы от этого склада", Notification.Type.ERROR_MESSAGE);
                return;
            }
            stockDao.delete(id);
            stockBeanContainer.removeItem(id);
            stockTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(VEHICLE_TABLE_NAME) && vehicleTable.getSelectedRow() != null) {
            id = (String) vehicleTable.getSelectedRow();
            if (orderDao.getAllByVehicle(vehicleDao.read(id)).size() > 0) {
                Notification.show("Отпланируйте все заказы от этой машины", Notification.Type.ERROR_MESSAGE);
                return;
            }
            vehicleDao.delete(id);
            vehicleBeanContainer.removeItem(id);
            vehicleTable.deselect(id);
        } else {
            Notification.show("Не выбрана строка в таблице.", Notification.Type.HUMANIZED_MESSAGE);
        }
    };

    private MenuBar.Command addButton = (MenuBar.Command) selectedItem -> {
        String id = null;
        if (tabsheet.getSelectedTab().getCaption().equals(ORDERS_TABLE_NAME)) {
            id = (String) ordersTable.getSelectedRow();
            final BeanItem<Orders> newUserItem = new BeanItem<>(new Orders());
            addWindow(new OrdersEditor(newUserItem, orderDao, stockDao, vehicleDao, ordersTable));
            ordersTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(STOCK_TABLE_NAME)) {
            id = (String) stockTable.getSelectedRow();
            final BeanItem<Stock> newCustomeItem = new BeanItem<>(new Stock());
            addWindow(new StockEditor(newCustomeItem, stockDao, stockTable));
            stockTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(VEHICLE_TABLE_NAME)) {
            id = (String) vehicleTable.getSelectedRow();
            final BeanItem<Vehicle> newTariffItem = new BeanItem<>(new Vehicle());
            addWindow(new VehicleEditor(newTariffItem, vehicleDao, vehicleTable));
            vehicleTable.deselect(id);
        }
    };

    @Override
    public int hashCode() {
        return super.hashCode() + 2;
    }
}