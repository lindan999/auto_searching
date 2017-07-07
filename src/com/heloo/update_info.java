package com.heloo;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.mustache.Value;

import com.chilkatsoft.CkCsv;

//import com.chilkasoft.CkCsv;

public class update_info {
	static {
		try {
			System.loadLibrary("chilkat");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public static void main(String[] args) {

		BrowserSupport.OpenProxyFirefox(1, "127.0.0.1", 1);
		CkCsv server_file = new CkCsv();
		// Prior to loading the CSV file, indicate that the 1st row
		// should be treated as column names:
		server_file.put_HasColumnNames(true);

		// Load the CSV records from the file:
		boolean success;
		success = server_file.LoadFile("E:\\mmo\\java_tool\\account_update.csv");
		if (success != true) {
			System.out.println(server_file.lastErrorText());
		}
		// xóa note file server

		int num_row = server_file.get_NumRows();
		for (int j = 0; j < num_row; j++) {
			server_file.SetCell(j, 6, "");
			server_file.SetCell(j, 7, "");
			server_file.SetCell(j, 8, "");
			server_file.SetCell(j, 9, "");
			server_file.SetCell(j, 10, "");
			server_file.SetCell(j, 11, "");
		}
		success = server_file.SaveFile("E:\\mmo\\java_tool\\account_update.csv");
		WebDriver driver = null;

		for (int row = 0; row < num_row; row++) {

			try {
				Boolean timeout = false;
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("taskkill /F /IM BvSsh.exe");
				// runtime.exec("taskkill /F /IM chrome.exe");
				String host = server_file.getCell(row, 1);
				String ssh_user = server_file.getCell(row, 2);
				String ssh_pass = server_file.getCell(row, 3);
				String ms_username = server_file.getCell(row, 4);
				String ms_password = server_file.getCell(row, 5);
				String bit_profile = "E:\\mmo\\linhvv\\AutoBotControl\\Bitvise\\profile\\bitvise.bscp";

				String[] s = new String[] { "E:\\mmo\\linhvv\\AutoBotControl\\Bitvise\\BvSsh.exe",
						"-profile=" + bit_profile, "-host=" + host, "-username=" + ssh_user, "-password=" + ssh_pass,
						"-keypairFile=xxx", "-loginOnStartup" };

				Thread.sleep(3000);
				runtime.exec(s);
				Thread.sleep(10000);
				Robot r = null;
				r = new Robot();
				r.keyPress(KeyEvent.VK_A);
				Thread.sleep(10000);
				driver = BrowserSupport.OpenProxyChrome("127.0.0.1", 1080,pc_mannual_search.chrome_driver);
				Thread.sleep(2000);
				driver.get(
						"https://www.bing.com/fd/auth/signin?action=interactive&provider=windows_live_id&src=rewardssi&perms=&sig=0BD7F0FDEAAB600A0E9BF800EB476118&return_url=https%3a%2f%2fwww.bing.com%3a443%2frewards%2fdashboard&Token=1");

				String title = driver.getTitle();
				if (!timeout)
					for (int i = 0; i < 3; i++) {
						if (title.equalsIgnoreCase("Problem loading page")) {
							driver.navigate().refresh();
							Thread.sleep(5000);
							if (i == 2) {
								timeout = true;
								break;
							}
						} else {
							break;
						}
					}
				if (!timeout) {
					// login
					BrowserSupport.bing_Login(driver, ms_username, ms_password, 0);
					Thread.sleep(2000);
					List<String> info = GetFakeInfo.getFakeInfo(driver);
					Thread.sleep(2000);
					driver.findElement(By.linkText("Account")).click();
					Thread.sleep(2000);

					Boolean isPresent = driver.findElements(By.id("UserProfile_MailingAddress1")).size() > 0;
					if (isPresent == true) {
					} else {
						driver.findElement(By.cssSelector("a[href*='/rewards/settings/editprofileframed']")).click();
					}
					WebElement address = driver.findElement(By.id("UserProfile_MailingAddress1"));
					if (address == null)
						System.out.println("chua dien thong tin");

					String mailing_address = info.get(2);
					String city = info.get(3);
					String state = info.get(4);
					String zipcode = info.get(5);
					String phone_area = info.get(6);
					String phone_number = info.get(7);

					WebElement we_address = driver.findElement(By.id("UserProfile_MailingAddress1"));
					we_address.clear();
					Thread.sleep(1500);
					we_address.sendKeys(mailing_address);
					WebElement we_city = driver.findElement(By.id("UserProfile_City"));
					we_city.clear();
					Thread.sleep(1500);
					we_city.sendKeys(city);
					WebElement we_state = driver.findElement(By.id("UserProfile_State"));
					we_state.sendKeys(state);
					Thread.sleep(1500);
					WebElement we_zipcode = driver.findElement(By.id("UserProfile_ZipCode"));
					we_zipcode.clear();
					Thread.sleep(1500);
					we_zipcode.sendKeys(zipcode);
					WebElement we_phone_area = driver.findElement(By.id("UserProfile_Phone_AreaCode"));
					we_phone_area.clear();
					Thread.sleep(1500);
					we_phone_area.sendKeys(phone_area);
					WebElement we_phone_number = driver.findElement(By.id("UserProfile_Phone_Number"));
					we_phone_number.clear();
					Thread.sleep(1500);
					we_phone_number.sendKeys(phone_number);
					Thread.sleep(1500);
					WebElement submit_bnt = driver.findElement(By.id("UserProfile_SubmitForm"));
					submit_bnt.click();
					Thread.sleep(2000);
					server_file.SetCell(row, 6, "successfull");
					server_file.SetCell(row, 7, mailing_address);
					server_file.SetCell(row, 8, city);
					server_file.SetCell(row, 9, state);
					server_file.SetCell(row, 10, zipcode);
					server_file.SetCell(row, 11, phone_area + "-" + phone_number);
					driver.findElement(By.linkText("Dashboard")).click();
					String point = driver.findElement(By.className("credits")).getText();
					server_file.SetCell(row, 12, point);
					Thread.sleep(2000);
					driver.close();
				} else {
					server_file.SetCell(row, 6, "time cmn out rooi");
					driver.close();
				}

				Thread.sleep(2000);
				runtime.exec("taskkill /F /IM BvSsh.exe");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				server_file.SetCell(row, 6, "time cmn out rooi");
				driver.close();

			}
			success = server_file.SaveFile("E:\\mmo\\java_tool\\account_update.csv");

		}

	}

}
