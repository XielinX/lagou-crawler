package Crawler;

import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import sun.awt.OSInfo;

import java.util.*;

/**
 * 拉钩爬虫
 *
 * @author xielx on 2019/8/17
 */
public class LagouCrawler {


	private static class SearchJob {
		// 薪资
		private String salary;
		// 数量
		private Integer count;
		//公司
		private List<String> companies = new ArrayList<String>();


		public String getSalary() {
			return salary;
		}

		public void setSalary(String salary) {
			this.salary = salary;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public List<String> getCompanies() {
			return companies;
		}

		public void setCompanies(List<String> companies) {
			this.companies = companies;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("工资待遇:")
							.append(salary)
							.append(" 的公司有:")
							.append(count)
							.append("家,公司是:")
							.append(companies);
			return builder.toString();
		}
	}

	/**
	 * 根据操作系统类型读取配置文件
	 */
	private static void initProperties() {
		switch (OSInfo.getOSType()) {
			case WINDOWS:
				System.setProperty("webdriver.chrome.driver", LagouCrawler.class.getClassLoader().getResource("chromedriver_win32.exe").getPath());
				break;
			case MACOSX:
				System.setProperty("webdriver.chrome.driver", LagouCrawler.class.getClassLoader().getResource("chromedriver_mac64").getPath());
				break;
			case LINUX:
				System.setProperty("webdriver.chrome.driver", LagouCrawler.class.getClassLoader().getResource("chromedriver_linux64").getPath());
				break;
			default:
				System.out.println("当前仅支持的操作系统类型为:Linux/MacOS/Windows");
				throw new RuntimeException("不支持当前操作系统类型");
		}
	}

	/**
	 * job筛选
	 * 工作地点:深圳
	 * 工作经验:3年及以下
	 * 学历要求:大专
	 * 公司规模:少于15人/15-50人
	 * 行业领域:移动互联网
	 *
	 * @param driver 驱动
	 */
	public static void selectOptions(WebDriver driver) {
		//城市
		//String cityName = "广州";
		//contains(String s1,String s2);s1内容有s2返回true
		//WebElement cityElement = driver.findElement(By.xpath("//div[@class='other-hot-city']//a[contains(text(),'" + cityName + "')]"));
		//cityElement.click();

		//工作经验
		choseCondition(driver, "工作经验", "3年及以下");

		//学历
		choseCondition(driver, "学历要求", "大专");

		//公司规模
		String[] companyArray = {"少于15人","15-50人"};
		for (String company : companyArray) {
			choseCondition(driver, "公司规模", company);
		}

		//行业
		choseCondition(driver, "行业领域", "移动互联网");

	}

	/**
	 * 设置筛选条件
	 *
	 * @param driver    驱动
	 * @param title     条件标题
	 * @param condition 条件
	 */
	private static void choseCondition(WebDriver driver, String title, String condition) {
		WebElement selectElement = driver.findElement(By.xpath("//li[@class='multi-chosen']/span[contains(text(),'" + title + "')]"));
		WebElement autoElement = selectElement.findElement(By.xpath("../a[contains(text(),'" + condition + "')]"));
		autoElement.click();
	}

	/**
	 * 获取薪资和公司
	 *
	 * @param driver 驱动
	 * @param map    Map
	 */
	private static void getSalaryByPage(WebDriver driver, Map<String, SearchJob> map) {
		List<WebElement> elementList = driver.findElements(By.className("list_item_top"));
		for (WebElement element : elementList) {
			//招聘薪资
			String money = element.findElement(By.className("money")).getText();
			//招聘公司
			String companyName = element.findElement(By.className("company_name")).getText();
			if (map.containsKey(money)) {
				SearchJob job = map.get(money);
				job.getCompanies().add(companyName);
				job.setSalary(money);
				job.setCount(job.getCount());
				map.put(money, job);
			} else {
				SearchJob job = new SearchJob();
				job.getCompanies().add(companyName);
				job.setSalary(money);
				job.setCount(1);
				map.put(money, job);
			}
		}

		//分页
		/*WebElement nextPageElement = driver.findElement(By.className("pager_next"));
		boolean canClickNextPageBtn = !nextPageElement.getAttribute("class").contains("pager_next_disabled");
		if (canClickNextPageBtn) {//下一页
			nextPageElement.click();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
			}
			getSalaryByPage(driver, map);
		}*/

	}

	/**
	 * 打印薪资
	 *
	 * @param map .
	 */
	private static void printSalary(Map<String, SearchJob> map) {
		ArrayList<SearchJob> jobList = Lists.newArrayList(map.values());
		Collections.sort(jobList, new Comparator<SearchJob>() {
			public int compare(SearchJob o1, SearchJob o2) {
				return o1.getCount() - o2.getCount();
			}
		});

		for (SearchJob searchJob : jobList) {
			System.out.println(searchJob);
		}

	}

	public static void main(String[] args) {

		WebDriver webDriver = null;
		System.out.println("===================开始爬虫======================");
		try {
			// 1.设置环境
			initProperties();
			//2获取web驱动(谷歌浏览器)
			webDriver = new ChromeDriver();
			//3.进入网址
			webDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");
			//4.job筛选
			selectOptions(webDriver);
			//5.解析页面,分页获取
			Map<String, SearchJob> jobMap = new HashMap<String, SearchJob>();
			getSalaryByPage(webDriver, jobMap);
			//6.打印

			printSalary(jobMap);
			//7.停止
			webDriver.quit();
			System.out.println("==============程序执行完毕==================");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			webDriver.quit();
		}
	}

}
