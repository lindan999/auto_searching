package com.heloo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GetFakeInfo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	public static List<String> getFakeInfo(WebDriver driver) {
		List<String> ret = new ArrayList();
		try {
			// Runtime runtime = Runtime.getRuntime();
			// runtime.exec("taskkill /IM firefox.exe");
			driver.navigate().to("http://www.fakenamegenerator.com/gen-random-us-us.php");
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
			Thread.sleep(2000);
			driver.navigate().to("https://www.bing.com/rewards/dashboard");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}
