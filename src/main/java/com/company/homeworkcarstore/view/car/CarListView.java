package com.company.homeworkcarstore.view.car;

import com.company.homeworkcarstore.entity.Car;
import com.company.homeworkcarstore.entity.CarStatus;
import com.company.homeworkcarstore.entity.Manufacturer;
import com.company.homeworkcarstore.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import io.jmix.flowui.model.CollectionContainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;


@Route(value = "cars", layout = MainView.class)
@ViewController(id = "Car.list")
@ViewDescriptor(path = "car-list-view.xml")
@LookupComponent("carsDataGrid")
@DialogMode(width = "64em")
public class CarListView extends StandardListView<Car> {
    @ViewComponent
    private CollectionContainer<Car> carsDc;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private DataGrid<Car> carsDataGrid;

    @Subscribe(id="markAsSoldBtn", subject = "clickListener")
    public void onMarkAsSoldBtnClick(final ClickEvent<JmixButton> event){

        Car selectedStatus = carsDataGrid.getSingleSelectedItem();

        if (CarStatus.SOLD.equals(selectedStatus.getStatus())){
            notifications.create("Already Sold")
                    .withType(Notifications.Type.SUCCESS)
                    .withPosition(Notification.Position.MIDDLE)
                    .show();
        } else if (CarStatus.IN_STOCK.equals(selectedStatus.getStatus())){
            LocalDate localDate = LocalDate.now();
            Car year = carsDc.getItemOrNull();

            year.setDateOfSale(localDate);
            dataManager.save(year);

            selectedStatus.setStatus(CarStatus.SOLD);
            dataManager.save(selectedStatus);

            notifications.create("Done")
                    .withType(Notifications.Type.SUCCESS)
                    .withPosition(Notification.Position.MIDDLE)
                    .show();
        }
        else {
            notifications.create("status = null")
                    .withType(Notifications.Type.SUCCESS)
                    .withPosition(Notification.Position.MIDDLE)
                    .show();
        }

    }
}