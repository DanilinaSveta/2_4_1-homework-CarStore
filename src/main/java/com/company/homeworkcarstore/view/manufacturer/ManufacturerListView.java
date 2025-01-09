package com.company.homeworkcarstore.view.manufacturer;

import com.company.homeworkcarstore.entity.Car;
import com.company.homeworkcarstore.entity.EngineType;
import com.company.homeworkcarstore.entity.Manufacturer;
import com.company.homeworkcarstore.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.editor.DataGridEditor;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.dropdownbutton.DropdownButtonItem;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "manufacturers", layout = MainView.class)
@ViewController(id = "Manufacturer.list")
@ViewDescriptor(path = "manufacturer-list-view.xml")
@LookupComponent("manufacturersDataGrid")
@DialogMode(width = "64em")
public class ManufacturerListView extends StandardListView<Manufacturer> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Manufacturer> manufacturersDc;

    @ViewComponent
    private InstanceContainer<Manufacturer> manufacturerDc;

    @ViewComponent
    private InstanceLoader<Manufacturer> manufacturerDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private DataGrid<Manufacturer> manufacturersDataGrid;
    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("manufacturersDataGrid.create")
    public void onManufacturersDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Manufacturer entity = dataContext.create(Manufacturer.class);
        manufacturerDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("manufacturersDataGrid.edit")
    public void onManufacturersDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        Manufacturer item = manufacturerDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        manufacturersDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        manufacturerDl.load();
        updateControls(false);
    }

    @Subscribe(id = "manufacturersDc", target = Target.DATA_CONTAINER)
    public void onManufacturersDcItemChange(final InstanceContainer.ItemChangeEvent<Manufacturer> event) {
        Manufacturer entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            manufacturerDl.setEntityId(entity.getId());
            manufacturerDl.load();
        } else {
            manufacturerDl.setEntityId(null);
            manufacturerDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(Manufacturer entity) {
        ViewValidation viewValidation = getViewValidation();
        ValidationErrors validationErrors = viewValidation.validateUiComponents(form);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        validationErrors.addAll(viewValidation.validateBeanGroup(UiCrossFieldChecks.class, entity));
        return validationErrors;
    }

    private void updateControls(boolean editing) {
        UiComponentUtils.getComponents(form).forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }

    @Subscribe(id = "calculateCarsButton", subject = "clickListener")
    private void onButtonClick(final ClickEvent<JmixButton> event){

        Manufacturer selectedManufacturer = manufacturersDataGrid.getSingleSelectedItem();
        if (selectedManufacturer != null){
            List<Car> cars = dataManager.load(Car.class)
                    .query("select c from Car c where c.model.manufacturer = :manufacturer")
                    .parameter("manufacturer", selectedManufacturer)
                    .list();

            int electricCars = 0;
            int gasolineCars = 0;

            for (Car car : cars) {
                if (car.getModel().getEngineType() == EngineType.ELECTRIC) {
                    electricCars++;
                } else if (car.getModel().getEngineType() == EngineType.GASOLINE) {
                    gasolineCars++;
                }
            }

            notifications.create("Electric cars: "+ electricCars + ", gasoline cars: " + gasolineCars)
                    .show();
        }else {
            notifications.create("Please select a manufacturer")
                    .show();
        }
    }
}