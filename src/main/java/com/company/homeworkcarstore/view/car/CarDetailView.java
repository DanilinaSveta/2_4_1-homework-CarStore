package com.company.homeworkcarstore.view.car;

import com.company.homeworkcarstore.entity.Car;
import com.company.homeworkcarstore.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "cars/:id", layout = MainView.class)
@ViewController(id = "Car.detail")
@ViewDescriptor(path = "car-detail-view.xml")
@EditedEntityContainer("carDc")
public class CarDetailView extends StandardDetailView<Car> {
}