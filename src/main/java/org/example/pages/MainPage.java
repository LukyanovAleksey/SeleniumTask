package org.example.pages;

import org.example.model.Cell;
import org.example.model.Row;
import org.example.model.Table;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainPage extends AbstractPage {
    private final String BASE_URL = "https://www.w3schools.com/sql/trysql.asp?filename=trysql_select_all";

    @FindBy(xpath = "//div[contains(@class, 'CodeMirror') and contains(@class, 'CodeMirror-wrap')]")
    private WebElement codeMirrorWrap;

    @FindBy(xpath = "//div[contains(@class, 'CodeMirror') and contains(@class, 'CodeMirror-wrap')]//textarea")
    private WebElement textArea;

    @FindBy(xpath = "//button[@class = 'ws-btn' and contains(text(), 'Run SQL')]")
    private WebElement runSqlButton;

    @FindBy(id = "divResultSQL")
    private WebElement resultSql;

    @FindBy(id = "accept-choices")
    private WebElement cookiesAcceptButton;

    private By table = By.xpath(".//table/tbody");
    private By row = By.xpath("./tr");
    private By column = By.xpath("./td");
    private By header = By.xpath("./th");


    public MainPage(WebDriver driver) {
        super(driver);
        try {
            driver.navigate().to(BASE_URL);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        PageFactory.initElements(this.driver, this);
        handleCookiesPopup(5);
    }

    public void setText(String query) {
        textArea.sendKeys(query);
    }

    public void focusOnTextArea() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(codeMirrorWrap)).click();
    }

    public void selectAll() {
        textArea.sendKeys(Keys.chord(Keys.CONTROL, "a"));
    }

    public void setNewQuery(String query) {
        focusOnTextArea();
        selectAll();
        setText(query);
    }

    public Table getTable() {
        waitSearchResults(30);
        List<WebElement> tableRows = resultSql.findElements(new ByChained(table, row));

        List<Row> rows = new ArrayList<>();
        for (WebElement tableRow : tableRows) {
            List<WebElement> cells = tableRow.findElements(column);
            List<Cell> cellValues = cells.stream().map(item -> new Cell(item.getText())).collect(Collectors.toList());
            if (cellValues.size() > 0) {
                Row row = new Row(cellValues);
                rows.add(row);
            }
        }
        return new Table(rows);
    }

    public String getColumnValueFromFoundRow(String columnName, String columnValue, String columnNameToSearch) {
        waitSearchResults(30);
        List<String> headers = getHeaders();
        int columnIndex = headers.indexOf(columnName);
        int columnIndexToSearch = headers.indexOf(columnNameToSearch);
        List<WebElement> allColumns = resultSql.findElements(new ByChained(table, row, column));

        int step = headers.size();
        int size = allColumns.size();
        // Limit to carefully avoid IndexOutOfBoundsException
        int limit = size / step + Math.min(size % step, 1);

        List<String> selectedColumnValues = Stream.iterate(columnIndex, i -> i + step)
                .limit(limit)
                .map(allColumns::get)
                .map(WebElement::getText)
                .collect(Collectors.toList());
        int rowIndex = selectedColumnValues.indexOf(columnValue);

        WebElement rowToSearch = resultSql.findElements(new ByChained(table, this.row)).get(rowIndex + 1); //headers included in table rows
        return rowToSearch.findElements(column).get(columnIndexToSearch).getText();
    }

    private List<String> getHeaders() {
        List<WebElement> headers = resultSql.findElements(new ByChained(table, row, header));
        return headers.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void run() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(runSqlButton)).click();
    }

    public void waitSearchResults(int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.textToBePresentInElement(resultSql, "Number of Records:"));
    }

    public boolean isResultContainsText(String text, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElement(resultSql, text));
    }

    public void executeQuery(String query) {
        setNewQuery(query);
        run();
    }

    public void handleCookiesPopup(int timeoutInSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.elementToBeClickable(cookiesAcceptButton)).click();
        } catch (TimeoutException e) { //if cookies popup is not displayed
            e.printStackTrace();
        }
    }

    public boolean isAlertPresented(int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}