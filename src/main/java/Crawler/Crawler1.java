package Crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 简单爬虫
 *
 * @author xielx on 2019/8/18
 */
public class Crawler1 {


	private static void choseCondition(WebDriver driver, String title, String condition) {
		WebElement selectElement = driver.findElement(By.xpath("//li[@class='multi-chosen']/span[contains(text(),'" + title + "')]"));
		WebElement autoElement = selectElement.findElement(By.xpath("../a[contains(text(),'" + condition + "')]"));
		autoElement.click();
	}
	public static void main(String[] args) {
		//1.读取配置
		System.setProperty("webdriver.chrome.driver",Crawler1.class.getClassLoader().getResource("chromedriver_linux64").getPath());

		//2.获取谷歌浏览的web驱动
		WebDriver webDriver =  new ChromeDriver();

		//3.获取网页url
		webDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");

		//4.筛选
		choseCondition(webDriver,"工作经验","3年及以下");
		choseCondition(webDriver,"学历要求","大专");
		//
	}
}
