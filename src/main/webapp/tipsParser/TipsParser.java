package main.webapp.tipsParser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TipsParser {
    private WebDriver driver;
    private String pathToFileKey;
    private String pathToFileHints;
    private List<String> hints;

    public TipsParser(String pathToFileKey, String pathToFileHints) {

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        this.driver = new ChromeDriver(chromeOptions);

        this.pathToFileKey = System.getProperty("user.dir") + "\\" + pathToFileKey;
        this.pathToFileHints = System.getProperty("user.dir") + "\\" + pathToFileHints;
        hints = new ArrayList<>();
    }

    public static void main(String[] args) {
        TipsParser parser = new TipsParser(
                "key.txt",
                "parse-key.txt");
        parser.parserTips();
    }

    public void parserTips() {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFileKey))) {
            String word = null;

            while ((word = reader.readLine()) != null) {

                driver.get("https://yandex.ru/search/?text=" + word);

                WebElement element = driver.findElement(By.className("mini-suggest__input"));
                element.click();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<WebElement> elements = driver.findElements(By.className("mini-suggest__item"));

                for (WebElement elem : elements) {
                    hints.add(elem.getAttribute("data-text"));
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFileHints))) {
            for (String hint : hints) {
                writer.write(hint + "\n");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
