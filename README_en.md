### Prepared project
This project contains some elements from "Sale of used cars" area:
- Manufacturer and Model entities, and EngineType enumeration
- Views for listing and editing for Manufacturer and Model (Master-detail view template based)

### Task
In the exercise you need to extend data model with a new entity and add some logic for data reading and manipulation.

#### 1. Adding an Entity and an Enumeration
Create an enumeration class for setting the Car status:
- CarStatus - Enumeration:
    - In Stock (I) 
    - Sold (S).

Add a new entity, using Studio, and it's Entity designer  - Car with attributes listed below:
- registrationNumber (registration number, String). Mandatory.
    - Validation: exactly 6 symbols in length.
    - Validation rules are specified using validation annotations, available in the Entity designer.
- model (car model, associated with Model entity). Mandatory.
    - check suitable relation type between car and model.
- productionYear (car production year, integer)
    - Validation: from 1990 to 2030 inclusively.
- status - use the enum created above. Think about making the field mandatory or init it in advance with the default value.
- dateOfSale (sailing date). Date without time.
    - Chose suitable datatype.

Also:
- Set registrationNumber attribute as instance name of the Car entity.
- Create DB index to control the car's registrationNumber uniqueness.

#### 2. Car Views
- Create standard list and detail views for the Car entity.
- Launch the application.
- Check if registration number and production year validations work as expected (unique registration number too).
- Create 5 or more Car instances in the running application as demo data for further development,
#### Optionally:
- Work with prepared XML for demo data generation:
    - Copy and run additional migration script. Use the demo-data-cars.xml file from resources directory. To do so, create a new Liquibase Changelog file using studio and add changeset from demo-data-cars.xml to generated file. Remember to restart the application to trigger data generation.

#### 3. Read data with DataManager
Add a new action to the Manufacturers list view to count and display the number of cars of a particular manufacturer:
- Action text - "Calculate cars"
- When the button is pressed, app should count the number of cars of the selected manufacturer, grouping by Model#engineType.
- As mentioned above, gasoline and electric cars must be counted separately.
- After counting is done, show notification with text:
  "Electric cars: 3, gasoline cars: 5" (text formatting is not strict)

Recommendations:
- Add a new button to the Manufacturer List View buttons panel.
- Create a Spring bean for the logic mentioned above.
- Implement counting logic using DataManager and JPQL query.

#### 4. DataManager data manipulation
Implement a new action for a quick change of Car status from **In Stock** to **Sold** in Car list view.
Implementation logic should be like below:
- Action text "Mark as Sold"
- If a selected car's status is already "Sold" - show notification: "Already Sold".
- If a selected car's status is "In Stock" - then:
    - Change status to Sold.
    - Set the 'date of sale' field as "today".
    - Save the changes.
    - Show notification: "Done".

Think about what could go wrong if car's status is not initialized when car is created?

Recommendations:
- Add a new button to the existed buttons panel.
- Use the DataManager bean to save the changed entity.