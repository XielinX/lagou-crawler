package Crawler;

import sun.awt.OSInfo;

import java.util.List;

/**
 * 拉钩爬虫
 *
 * @author xielx on 2019/8/17
 */
public class LagouCrawler {


	private static class SearchJob{
		// 薪资
		private String salary;
		// 数量
		private Integer count;
		//公司
		private List<String> companies;


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
	private static void initPropertites(){
		switch (OSInfo.getOSType()){
			case WINDOWS:
				System.setProperty("webdriver.chrome.driver",LagouCrawler.class.getResource("chromedriver_win32.exe").getPath());
				break;
			case MACOSX:
				System.setProperty("webdriver.chrome.driver",LagouCrawler.class.getResource("chromedriver_mac64").getPath());
				break;
			case LINUX:
				System.setProperty("webdriver.chrome.driver",LagouCrawler.class.getResource("chromedriver_linux64").getPath());
				break;
			default:
				System.out.println("当前仅支持的操作系统类型为:Linux/MacOS/Windows");
				throw new RuntimeException("不支持当前操作系统类型");
		}
	}



	public static void main(String[] args) {
		
	}

}
