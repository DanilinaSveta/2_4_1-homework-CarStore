package com.company.homeworkcarstore.view.model;

import com.company.homeworkcarstore.entity.Model;
import com.company.homeworkcarstore.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;

@Route(value = "models", layout = MainView.class)
@ViewController(id = "Model.list")
@ViewDescriptor(path = "model-list-view.xml")
@LookupComponent("modelsDataGrid")
@DialogMode(width = "64em")
public class ModelListView extends StandardListView<Model> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Model> modelsDc;

    @ViewComponent
    private InstanceContainer<Model> modelDc;

    @ViewComponent
    private InstanceLoader<Model> modelDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("modelsDataGrid.create")
    public void onModelsDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Model entity = dataContext.create(Model.class);
        modelDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("modelsDataGrid.edit")
    public void onModelsDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        Model item = modelDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        modelsDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        modelDl.load();
        updateControls(false);
    }

    @Subscribe(id = "modelsDc", target = Target.DATA_CONTAINER)
    public void onModelsDcItemChange(final InstanceContainer.ItemChangeEvent<Model> event) {
        Model entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            modelDl.setEntityId(entity.getId());
            modelDl.load();
        } else {
            modelDl.setEntityId(null);
            modelDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(Model entity) {
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
}