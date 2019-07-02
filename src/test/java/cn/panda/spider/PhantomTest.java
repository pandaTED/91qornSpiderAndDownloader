package cn.panda.spider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2019-04-01
 */

@Slf4j
public class PhantomTest {

    @Test
    public void getInfo() throws IOException, InterruptedException {


        String url = "https://www.v2ex.com/";

        DesiredCapabilities dcaps = DesiredCapabilities.chrome();

        dcaps.setBrowserName("chrome");
        dcaps.setVersion("73.0.3683.86");
        dcaps.setPlatform(Platform.WIN10);
        dcaps.setCapability("acceptSslCerts", true);
        dcaps.setCapability("takesScreenshot", false);
        dcaps.setCapability("cssSelectorsEnabled", true);
        dcaps.setJavascriptEnabled(true);
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");

        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(url);

        WebElement body = driver.findElement(By.tagName("body"));
//        String body_text = body.getText();
//        List<WebElement> elements = body.findElements(By.xpath("//*[@id=\"Rightbar\"]/div[2]/div[1]/table[1]/tbody/tr/td[3]/span"));
//        for (WebElement element : elements) {
//            System.out.println("================>"+element.getText());
//        }

//        System.out.println(body_text);


        String text = body.getText();
        List<WebElement> elements = body.findElements(By.xpath("//*[@id=\"Tabs\"]"));

        for (WebElement element : elements) {
            String text1 = element.getText();
            log.info("text1==========>{}",text1);
            List<WebElement> tab = element.findElements(By.className("tab"));
            for (WebElement webElement : tab) {
                log.info("============>{}",webElement.getText());
            }
        }

        WebElement element = body.findElement(By.xpath("//*[@id=\"Rightbar\"]/div[2]/div[2]/div[2]/a[2]"));

        log.info("登陆===================>{}",element.getText());

        element.click();

        driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);

        WebElement loginPage = driver.findElement(By.tagName("body"));

        String text1 = loginPage.getText();

        log.info("loginText=============>{}",text1);

//        System.out.println(text);

        WebElement element1 = loginPage.findElement(By.xpath("//*[@id=\"Main\"]/div[2]/div[2]/form/table/tbody/tr[3]/td[2]/div[1]"));

        log.info("loginPage-验证码====>{}",element1.getAttribute("style"));

        File screenshotAs = driver.getScreenshotAs(OutputType.FILE);

        Thread.sleep(3000);

        FileUtils.copyFile(screenshotAs,new File("loginPage.png"));


        Scanner sc = new Scanner(System.in);
        String yanzhnegma =  sc.nextLine();
        System.out.println("验证码是："+yanzhnegma);

        driver.quit();

    }

}
