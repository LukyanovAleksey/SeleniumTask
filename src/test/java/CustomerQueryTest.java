import org.example.model.Customer;
import org.example.model.Table;
import org.example.pages.MainPage;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestStatusListener.class)
public class CustomerQueryTest extends BaseTest {
    private final String SELECT_ALL_FROM_CUSTOMERS = "SELECT * FROM Customers";

    private final String SELECT_ALL_FROM_CUSTOMERS_BY_CITY = "SELECT * FROM Customers\n" +
            "WHERE City = 'London';";

    private final String INSERT_NEW_CUSTOMER = "INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)\n" +
            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";

    private final String SELECT_NEWLY_CREATED_CUSTOMER = "SELECT * FROM Customers\n" +
            "WHERE CustomerName = '%s'\n" +
            "AND ContactName = '%s'\n" +
            "AND Address = '%s'\n" +
            "AND City = '%s'\n" +
            "AND PostalCode = '%s'\n" +
            "AND Country = '%s';";

    private final String UPDATE_EXISTING_CUSTOMER = "UPDATE Customers\n" +
            "SET ContactName = '%s', City = '%s'\n" +
            "WHERE CustomerName = '%s';";

    private final String SELECT_NEWLY_UPDATED_CUSTOMER = "SELECT * FROM Customers\n" +
            "WHERE CustomerName = '%s'\n" +
            "AND ContactName = '%s'\n" +
            "AND City = '%s';";

    private final String WRONG_SELECT_QUERY = "SELECT * FROM Customers\n" +
            "WHERE Cityy = 'London';";

    /**
     * 1. Вывести все строки таблицы Customers и убедиться,
     * что запись с ContactName равной 'Giovanni Rovelli'
     * имеет Address = 'Via Ludovico il Moro 22'.
     */
    @Test
    public void testThatExactContactNameHasAnExactAddress() {
        MainPage mainPage = new MainPage(getDriver());
        mainPage.executeQuery(SELECT_ALL_FROM_CUSTOMERS);
        Table table = mainPage.getTable();
        String address = mainPage.getColumnValueFromFoundRow(table, "ContactName", "Giovanni Rovelli", "Address");

        Assert.assertEquals(address, "Via Ludovico il Moro 22");
    }

    /**
     * 2. Вывести только те строки таблицы Customers, где city='London'.
     * Проверить, что в таблице ровно 6 записей.
     */
    @Test
    public void testThatSixRowsHaveCityEqualsLondon() {
        MainPage mainPage = new MainPage(getDriver());
        mainPage.executeQuery(SELECT_ALL_FROM_CUSTOMERS_BY_CITY);
        Table table = mainPage.getTable();

        Assert.assertEquals(table.getRows().size(), 6);
    }

    /**
     * 3. Добавить новую запись в таблицу Customers и проверить, что эта запись добавилась.
     */
    @Test
    public void testThatNewCustomerCanBeCreated() {
        MainPage mainPage = new MainPage(getDriver());
        Customer customer = new Customer
                .CustomerBuilder("John Doe")
                .setContactName("Jane Doe")
                .setAddress("1228 Diamond St")
                .setCity("Frankfurt")
                .setPostalCode("94131")
                .setCountry("USA")
                .build();

        String insertQuery = String.format(INSERT_NEW_CUSTOMER,
                customer.getCustomerName(),
                customer.getContactName(),
                customer.getAddress(),
                customer.getCity(),
                customer.getPostalCode(),
                customer.getCountry());
        mainPage.executeQuery(insertQuery);
        Assert.assertTrue(mainPage.isResultContainsText("You have made changes to the database. Rows affected: 1", 10));

        String selectQuery = String.format(SELECT_NEWLY_CREATED_CUSTOMER,
                customer.getCustomerName(),
                customer.getContactName(),
                customer.getAddress(),
                customer.getCity(),
                customer.getPostalCode(),
                customer.getCountry());
        mainPage.executeQuery(selectQuery);
        Table table = mainPage.getTable();
        Assert.assertEquals(table.getRows().size(), 1);
    }

    /**
     * 4. Обновить все поля (кроме CustomerID) в любой записи таблицы Customers
     * и проверить, что изменения записались в базу.
     */
    @Test
    public void testThatCustomerCanBeUpdated() {
        MainPage mainPage = new MainPage(getDriver());
        Customer customer = new Customer
                .CustomerBuilder("Lehmanns Marktstand")
                .setContactName("Alfred Schmidt")
                .setCity("Frankfurt").build();

        String queryUpdate = String.format(UPDATE_EXISTING_CUSTOMER,
                customer.getContactName(),
                customer.getCity(),
                customer.getCustomerName());
        mainPage.executeQuery(queryUpdate);
        Assert.assertTrue(mainPage.isResultContainsText("You have made changes to the database. Rows affected: 1", 10));

        String querySelect = String.format(SELECT_NEWLY_UPDATED_CUSTOMER,
                customer.getCustomerName(),
                customer.getContactName(),
                customer.getCity());
        mainPage.executeQuery(querySelect);
        Table table = mainPage.getTable();
        Assert.assertEquals(table.getRows().size(), 1);
    }

    /**
     * 5. Придумать собственный автотест и реализовать (тут все ограничивается только вашей фантазией).
     * Выполнить select запрос с несуществующим названием столбца и проверить наличие всплывающего окна об ошибке.
     */
    @Test
    public void testThatAlertExistsWhenIncorrectQuery() {
        MainPage mainPage = new MainPage(getDriver());
        mainPage.executeQuery(WRONG_SELECT_QUERY);
        Assert.assertTrue(mainPage.isAlertPresented(10));

        Alert alert = getDriver().switchTo().alert();
        Assert.assertEquals(alert.getText(), "Error 1: could not prepare statement (1 no such column: Cityy)");

        alert.accept();
        Assert.assertFalse(mainPage.isAlertPresented(2));
    }
}