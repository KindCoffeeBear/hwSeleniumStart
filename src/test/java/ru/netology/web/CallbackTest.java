package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSentTestForm() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("Иванов Василий");
        inputPhone.sendKeys("+79112344456");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSentTestFormWithLatinInInputName() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("Ivanov Vasiliy");
        inputPhone.sendKeys("+79112344456");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSentTestFormWithSpecialCharacterInInputNameAndEmptyInputPhone() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("Ivanov&Vasiliy");
        inputPhone.sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSentTestFormWithEmptyInputName() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("");
        inputPhone.sendKeys("+79112344456");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSentTestFormWithEmptyInputPhone() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("Иванов Василий");
        inputPhone.sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSentTestFormWithTextInInputPhone() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("Иванов Василий");
        inputPhone.sendKeys("Телефон");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSentTestFormWithEmptyInputNameAndEmptyInputPhone() {
        WebElement inputName = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        WebElement inputPhone = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        inputName.sendKeys("");
        inputPhone.sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }
}
