package ru.netology.apporder;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("window-size=1366x768");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSubmitRequest() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("Иван Петров");
        elements.get(1).findElement(By.className("input__control")).sendKeys("+79015675443");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSubmitEmptyForm() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(0).findElement(By.className("input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSubmitIfEmptyName() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(1).findElement(By.className("input__control")).sendKeys("+79015675443");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(0).findElement(By.className("input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSubmitIfEmptyPhone() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("Иван Петров");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(1).findElement(By.className("input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSubmitIfEmptyCheckbox() throws InterruptedException {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("Иван Петров");
        elements.get(1).findElement(By.className("input__control")).sendKeys("+79015675443");
        driver.findElement(By.cssSelector("[role=button]")).click();
        TimeUnit.SECONDS.sleep(1);
        String textColor = elements.get(2).findElement(By.className("checkbox__box")).getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", textColor);
    }

    @Test
    void shouldNotSubmitIfLatinName() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("John Doe");
        elements.get(1).findElement(By.className("input__control")).sendKeys("+79015675443");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(0).findElement(By.className("input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSubmitIfNumeralsInName() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("John Doe11");
        elements.get(1).findElement(By.className("input__control")).sendKeys("+79015675443");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(0).findElement(By.className("input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSubmitIfPhoneWithoutPlus() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("Иван Петров");
        elements.get(1).findElement(By.className("input__control")).sendKeys("89015675443");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(1).findElement(By.className("input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSubmitIfLettersInPhone() {
        driver.get("http://localhost:9999/");
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id]"));
        elements.get(0).findElement(By.className("input__control")).sendKeys("Иван Петров");
        elements.get(1).findElement(By.className("input__control")).sendKeys("+79O15675443");
        elements.get(2).findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = elements.get(1).findElement(By.className("input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }
}