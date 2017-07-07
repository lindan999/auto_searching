package com.heloo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.chilkatsoft.CkCsv;
import com.chilkatsoft.CkSshKey;

public class BrowserSupport {

	public static WebDriver OpenProxyChrome(String proxy_socks, int port, String chromedriver) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--proxy-server=socks5://" + proxy_socks + ":" + port);
		options.addArguments("--host-resolver-rules=MAP * 0.0.0.0 , EXCLUDE 127.0.0.1");
		options.addExtensions(new File(pc_mannual_search.webRTC_extension));
		options.addArguments("--disable-bundled-ppapi-flash");
		options.addArguments("--disable-plugins-discovery");
		options.addArguments("--disable-internal-flash");
		System.setProperty("webdriver.chrome.driver", chromedriver);
		WebDriver driver = new ChromeDriver(options);
		random_sleep(2, 4);
		driver.manage().deleteAllCookies();
		return driver;
	}

	public static WebDriver OpenProxyChromeMobile(String proxy_socks, int port) {
		String userAgent = "android";

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--proxy-server=socks5://" + proxy_socks + ":" + port);
		options.addArguments("--host-resolver-rules=MAP * 0.0.0.0 , EXCLUDE 127.0.0.1");
		options.addExtensions(new File(pc_mannual_search.webRTC_extension));
		options.addArguments("--disable-bundled-ppapi-flash");
		options.addArguments("--disable-plugins-discovery");
		options.addArguments("--disable-internal-flash");
		options.addArguments("--user-agent=" + userAgent);
		DesiredCapabilities cap = DesiredCapabilities.android();
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver driver = new ChromeDriver(cap);
		driver.manage().deleteAllCookies();
		return driver;
	}

	public static WebDriver perkTvProxyChrome(String proxy_socks, int port, String chromedriver, String userData) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--proxy-server=socks5://" + proxy_socks + ":" + port);
		options.addArguments("user-data-dir=" + userData);
		// options.addExtensions(new
		// File("C:\\Users\\Administrator\\Desktop\\auto_search\\bingpong_helper.crx"));
		System.setProperty("webdriver.chrome.driver", chromedriver);
		WebDriver driver = new ChromeDriver(options);
		clear_browsing_data(driver, userData);
		driver.manage().deleteAllCookies();
		return driver;
	}

	public static void clear_browsing_data(WebDriver driver, String userData) {
		String chromeAdvancedSettings = "chrome://settings/clearBrowserData";
		driver.navigate().to(chromeAdvancedSettings);
		WebElement frame = driver.findElement(By.xpath("//iframe[@src='chrome://settings-frame/clearBrowserData']"));
		WebDriver frameDriver = driver.switchTo().frame(frame);
		Select dropDown = new Select(frameDriver.findElement(By.id("clear-browser-data-time-period")));
		dropDown.selectByIndex(4);
		WebElement elm = driver.findElement(By.id("delete-cookies-checkbox"));
		if (!elm.isSelected())
			elm.click();
		elm = driver.findElement(By.xpath("//button[@id='clear-browser-data-commit']"));
		elm.click();
		random_sleep(20, 20);
	}

	// public static void pervent_leak_dns(WebDriver driver){
	// String webRTC_setting
	// ="chrome://extensions/?options=eiadekoaikejlgdbkbdfeijglgfdalml";
	// driver.navigate().to(webRTC_setting);
	// WebElement frame =
	// driver.findElement(By.xpath("//iframe[@src='chrome://extensions-frame/?options=eiadekoaikejlgdbkbdfeijglgfdalml']"));
	// WebDriver frameDriver = driver.switchTo().frame(frame);
	// Select dropDown = new Select(frameDriver.findElement(By.id("policy")));
	// dropDown.selectByIndex(2);
	// WebElement elm = driver.findElement(By.id("proxy"));
	// if (!elm.isSelected()) elm.click();
	// elm = driver.findElement(By.xpath("//button[@id='save']"));
	// elm.click();
	// random_sleep(20, 20);
	// }

	public static WebDriver OpenProxyFirefox(int proxy_type, String proxy_socks, int port) {
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();

		// Tell the Java bindings to use Marionette.
		// This will not be necessary in the future,
		// when Selenium will auto-detect what remote end
		// it is talking to.
		capabilities.setCapability("marionette", true);

		WebDriver driver = new RemoteWebDriver(capabilities); 
		driver.manage().deleteAllCookies();
		return driver;
	}

	public static void bing_Login(WebDriver driver, String ms_username, String ms_password, int is_mobile) {
		try {
			random_sleep(3, 10);
			if (is_mobile == 0) {
				WebElement sign_in_btn = driver.findElement(By.className("identityOption"));
				random_sleep(2, 6);
				sign_in_btn.findElement(By.tagName("a")).click();
				random_sleep(2, 6);
			}
			driver.findElement(By.id("i0116")).sendKeys(ms_username);
			random_sleep(2, 6);
			driver.findElement(By.id("i0118")).sendKeys(ms_password);
			random_sleep(2, 6);
			driver.findElement(By.id("idSIButton9")).click();
			random_sleep(2, 6);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public static void perktv_Login(WebDriver driver, String perk_email, String perk_pass) {
		try {
			random_sleep(3, 10);
			String parentHandle = driver.getWindowHandle();
			driver.findElement(By.className("login")).click();

			for (String winHandle : driver.getWindowHandles()) {
				if (!winHandle.equals(parentHandle))
					driver.switchTo().window(winHandle);
			}
			random_sleep(10, 20);
			driver.findElements(By.tagName("input")).get(0).sendKeys(perk_email);
			random_sleep(3, 10);
			driver.findElements(By.tagName("input")).get(1).sendKeys(perk_pass);
			random_sleep(3, 10);
			driver.findElement(By.linkText("Login")).click();
			random_sleep(3, 10);
			WebElement three_div = driver.findElement(By.className("threebythree"));
			three_div.findElements(By.tagName("a")).get(0).click();
			random_sleep(2, 6);
			driver.switchTo().window(parentHandle);
			random_sleep(3, 10);
			try {
				int a = driver.findElements(By.id("close_video_quality_notification")).size();
				if (a > 0)
					driver.findElements(By.id("close_video_quality_notification")).get(0).click();
			} catch (Exception ex) {

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public static void random_scroll(WebDriver driver, int between_from, int between_to) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		int max_scroll_down = random(350, 1100);
		int max_scroll_up = random(350, 1100);

		int do_scroll = 0;
		try {
			while (do_scroll < max_scroll_down) {
				int random_scroll_down = random(30, 100);
				jse.executeScript("window.scrollBy(0," + random_scroll_down + ")", "3");

				Thread.sleep(random(50, 300));
				do_scroll += random_scroll_down;
			}

			random_sleep(between_from, between_to);
			do_scroll = 0;
			while (do_scroll < max_scroll_up) {
				int random_scroll = random(30, 100);
				jse.executeScript("window.scrollBy(0,-" + random_scroll + ")", "3");
				Thread.sleep(random(50, 300));
				do_scroll += random_scroll;
			}
			random_sleep(2, 6);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	public static void back_to_bing(WebDriver driver) {
		for (int back = 0; back < 10; back++)
			if (!driver.getCurrentUrl().contains("bing.com")) {
				try {
					driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
					driver.navigate().back();
					random_sleep(1, 4);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	public static void random_sleep(int from, int to) {
		Random sleep = new Random();
		from = from * 1000;
		to = to * 1000;
		int sleep_time = (sleep.nextInt(to - from + 1) + from);
		try {
			Thread.sleep(sleep_time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int random(int from, int to) {
		Random x = new Random();
		return (x.nextInt(to - from + 1) + from);
	}

	public static List<String> getFakeInfo() {
		List<String> ret = new ArrayList();
		try {
			// Runtime runtime = Runtime.getRuntime();
			// runtime.exec("taskkill /IM firefox.exe");
			WebDriver driver = BrowserSupport.OpenProxyChrome("127.0.0.1", 1080, pc_mannual_search.chrome_driver);
			driver.get("http://www.fakenamegenerator.com/gen-random-us-us.php");
			WebElement div_address = driver.findElement(By.className("address"));
			WebElement full_name = div_address.findElement(By.tagName("h3"));
			String fake_name = full_name.getText();
			String[] fake_full_name = fake_name.split("\\.");
			String first_name = fake_full_name[0];
			String last_name = fake_full_name[1];
			WebElement class_address = div_address.findElement(By.className("adr"));
			String full_address = class_address.getText();
			String[] fake_address = full_address.split("\\\n");
			String street = fake_address[0];
			String location = fake_address[1];
			String[] full_location = location.split(",");
			String city = full_location[0];
			String state_and_zip = full_location[1];
			String[] full_state = state_and_zip.trim().split("\\s+");
			String state = full_state[0];
			String zipcode = full_state[1];
			WebElement div_extra = driver.findElements(By.className("extra")).get(0);
			WebElement xxx = div_extra.findElements(By.className("dl-horizontal")).get(3);
			String div_phone = xxx.getText();
			String[] phone = div_phone.trim().split("\\\n");
			String phone_number = phone[1];
			String[] phone_split = phone_number.split("-");
			String phone_area = phone_split[0];
			String phone_city = phone_split[1];
			String phone_random = phone_split[2];

			ret.add(0, first_name);
			ret.add(1, last_name);
			ret.add(2, street);
			ret.add(3, city);
			ret.add(4, state);
			ret.add(5, zipcode);
			ret.add(6, phone_area);
			ret.add(7, phone_city + phone_random);
			System.out.println("success");
			driver.navigate().to("http://www.google.com");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String[] ssh_login(CkCsv server_file, int row, String link_bitvise) {

		String host = server_file.getCell(row, 1);
		String ssh_user = server_file.getCell(row, 2);
		String ssh_pass = server_file.getCell(row, 3);

		CkSshKey key = new CkSshKey();
		key.put_Password(ssh_pass);
		// success = key.FromOpenSshPublicKey(ssh_pass);
		String fingerprint;
		fingerprint = key.genFingerprint();
		System.out.println(fingerprint);

		String[] s = new String[] { link_bitvise, "-host=" + host, "-username=" + ssh_user, "-password=" + ssh_pass,
				// "-hide=main, hostKey, banner, popups,
				// traySFTP,
				// trayTerm, trayRDP, trayPopups",
				"-loginOnStartup" };

		return s;
	}

	public static void clear_server_file_content(CkCsv server_file, int num_row) {

		for (int j = 0; j < num_row; j++) {
			server_file.SetCell(j, 6, "");
			server_file.SetCell(j, 7, "");
			server_file.SetCell(j, 8, "");
			server_file.SetCell(j, 9, "");
			server_file.SetCell(j, 10, "");
			server_file.SetCell(j, 11, "");
			server_file.SetCell(j, 12, "");
		}
	}

	public static void clearTempFolder() throws IOException {

		try {
			File file = new File(System.getProperty("java.io.tmpdir"));
			FileUtils.cleanDirectory(file);
		}

		catch (IOException e) {
			// Do nothing since we do not worry about the files that cannot be
			// deleted
			// Include exception handler logic if you want to
		}
	}

	public static void shutdown_proceess(Runtime runtime) {
		try {
			runtime.exec("taskkill /F /IM BvSsh.exe");
			runtime.exec("taskkill /F /IM chrome.exe");
			runtime.exec("taskkill /F /IM chromedriver.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Boolean check_timeout(WebDriver driver) throws InterruptedException {
		Thread.sleep(1000);
		int timeout = driver.findElements(By.id("main-frame-error")).size();
		Boolean is_timeout = false;
		if (!is_timeout)
			for (int i = 0; i < 3; i++) {
				if (timeout > 0) {
					driver.navigate().refresh();
					BrowserSupport.random_sleep(6, 10);
					timeout = driver.findElements(By.id("main-frame-error")).size();
					if (i == 2) {
						is_timeout = true;
						break;
					}
				} else {
					break;
				}
			}
		return is_timeout;
	}
}
