package com.heloo;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.chilkatsoft.CkCsv;
//import com.chilkasoft.CkCsv;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class auto_redeem {
	static {
		try {
			System.loadLibrary("chilkat");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public static void main(String[] args) {

		CkCsv server_file = new CkCsv();
		// Prior to loading the CSV file, indicate that the 1st row
		// should be treated as column names:
		server_file.put_HasColumnNames(true);

		// Load the CSV records from the file:
		boolean success;
		success = server_file.LoadFile("E:\\mmo\\linhvv\\AutoBotControl\\server.csv");
		if (success != true) {
			System.out.println(server_file.lastErrorText());
			return;
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
		success = server_file.SaveFile("E:\\mmo\\linhvv\\AutoBotControl\\server.csv");

		for (int row = 0; row < num_row; row++) {

			try {
				Boolean timeout = false;
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("taskkill /F /IM BvSsh.exe");
				runtime.exec("taskkill  /F /IM firefox.exe");
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
				r.keyPress(KeyEvent.VK_S);
				Thread.sleep(3000);
				r.keyPress(KeyEvent.VK_ESCAPE);
				Thread.sleep(10000);
				WebDriver driver = BrowserSupport.OpenProxyFirefox(1, "127.0.0.1", 1080);
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
					BrowserSupport.bing_Login(driver, ms_username, ms_password, 0);
					String point = driver.findElement(By.className("credits")).getText();
					Thread.sleep(2000);
					driver.findElement(By.linkText("Redeem")).click();
					Thread.sleep(5000);
					// driver.findElement(By.className("sprite_000100000004")).click();//
					// amazon
					driver.findElement(By.className("sprite_000100000054")).click();// gamestop

					Thread.sleep(1500);
					// WebElement redeem_pointList =
					// driver.findElements(By.className("number")).get(1);
					String redeem_point = driver.findElements(By.className("number")).get(1).getText();

					String csvDoc;
					csvDoc = server_file.saveToString();
					System.out.println(csvDoc);
					if (Integer.parseInt(point) < Integer.parseInt(redeem_point)) {
						server_file.SetCell(row, 6, "deo du diem de cash");

					} else {
						driver.findElement(By.id("SingleProduct_SubmitForm")).click();
						Thread.sleep(2000);
						driver.findElement(By.id("CheckoutReview_SubmitForm")).click();
						Thread.sleep(2000);
						String abc = driver.findElement(By.className("header")).getText();
						server_file.SetCell(row, 6, "successfull");
						server_file.SetCell(row, 7, abc);

					}
				} else {
					server_file.SetCell(row, 6, "time cmn out rooi");
				}

				Thread.sleep(2000);
				driver.close();
				runtime.exec("taskkill /F /IM BvSsh.exe");
				runtime.exec("taskkill /IM firefox.exe");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				server_file.SetCell(row, 6, "time cmn out rooi");

			}
			success = server_file.SaveFile("E:\\mmo\\linhvv\\AutoBotControl\\server.csv");

		}

	}
}
