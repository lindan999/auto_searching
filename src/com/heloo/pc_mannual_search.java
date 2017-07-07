package com.heloo;

import java.awt.Robot;
import java.awt.event.KeyEvent;
//import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.chilkatsoft.CkCsv;
import com.google.common.eventbus.AllowConcurrentEvents;

//import com.chilkasoft.CkCsv;

public class pc_mannual_search {
	public static int HOST=1;
	public static int SSH_USER=2;
	public static int SSH_PASS=3;
	public static int MS_USER=4;
	public static int MS_PASS=5;
	public static int NOTE=6;
	public static int LEVEL=7;
	public static int ACCOUNT_STATUS=8;
	public static int FULL_POINT=9;
	public static int IS_AMAZON=10;
	public static int REDEEM_POINT=11;
	public static int CUR_POINT=12;
	public static int EARN_POINT=13;
	public static int TIME=14;
	public static String dashboard = "https://www.bing.com/rewards/dashboard";
	public static String mobile_sign_in = "https://www.bing.com/rewards/signin";
	public static String server_link = "C:\\Users\\Linda\\Desktop\\auto_search\\server.csv";
	public static String chrome_driver = "C:\\Users\\Linda\\Desktop\\auto_search\\chromedriver.exe";
	public static String webRTC_extension = "C:\\Users\\Linda\\Desktop\\auto_search\\WebRTC.crx";
	public static String link_bitvise = "C:\\Users\\Linda\\Desktop\\auto_search\\Bitvise\\BvSsh.exe";
	public static String pc_search_btn = "sb_form_go";
	public static String mobile_search_btn = "";
	public static String bing = "https://www.bing.com";

	static {
		try {
			System.load("C:\\Users\\Linda\\Desktop\\auto_search\\chilkat.dll");

		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public static void main(String[] args) {

		CkCsv server_file = new CkCsv();
		CkCsv keyword_file = new CkCsv();
		// Prior to loading the CSV file, indicate that the 1st row
		// should be treated as column names:s
		server_file.put_HasColumnNames(true);
		keyword_file.put_HasColumnNames(true);

		// Load the CSV records from the file:
		boolean success;
		success = server_file.LoadFile(server_link);
		success = keyword_file.LoadFile("C:\\Users\\Linda\\Desktop\\auto_search\\key_word.csv");
		if (success != true) {
			System.out.println(server_file.lastErrorText());
			return;
		}

		// xóa note file server
		String note = "";
		String account_status = "";
		String daily_status = "";
		String redeem_point = "";
		String earn_point = "";
		String old_point = "";
		String after_point = "";
		String is_amazon="";
		String member_type="";
		int num_row = server_file.get_NumRows();
		BrowserSupport.clear_server_file_content(server_file, num_row);

		//save file
		success = server_file.SaveFile(server_link);
		
		//get curent date
		Calendar cal = Calendar.getInstance();
		int start_date= cal.DAY_OF_YEAR;
		while (true) {
			Runtime runtime = Runtime.getRuntime();
			BrowserSupport.shutdown_proceess(runtime);
			num_row = server_file.get_NumRows();
			Random account = new Random();
			int row = account.nextInt(num_row);

			
			Calendar next = Calendar.getInstance();
			int next_day = next.DAY_OF_YEAR;
			
			//if nextday came, clear server_file
			if (start_date < next_day) {
				BrowserSupport.clear_server_file_content(server_file, num_row);
				success = server_file.SaveFile(server_link);
				start_date = next_day;
			}
			
			//check account is banned or not
			if (server_file.getCell(row, TIME).length() < 25 && !server_file.getCell(row, ACCOUNT_STATUS).contains("Baned")) {
				try {
					try {
						BrowserSupport.clearTempFolder();
					} catch (Exception e) {

					}
					
					server_file.SetCell(row, NOTE, "  running");
					success = server_file.SaveFile(server_link);
					note = "";
					account_status = "";
					daily_status = "";
					redeem_point = "";
					after_point = "";
					long startTime = System.currentTimeMillis();
					
					//auto accept when new ssh 
					// TODO find better solution
					Robot r = null;
					r = new Robot();
					r.keyPress(KeyEvent.VK_WINDOWS);
					r.keyPress(KeyEvent.VK_D);
					r.keyRelease(KeyEvent.VK_WINDOWS);
					r.keyRelease(KeyEvent.VK_D);
					Boolean timeout = false;

					//login to ssh
					String ms_username = server_file.getCell(row, MS_USER);
					String ms_password = server_file.getCell(row, MS_PASS);
					BrowserSupport.random_sleep(2, 4);
					String[] s = BrowserSupport.ssh_login(server_file, row, link_bitvise);
					runtime.exec(s);

					Thread.sleep(10000);

					r.keyPress(KeyEvent.VK_ALT);
					Thread.sleep(500);
					r.keyPress(KeyEvent.VK_S);
					Thread.sleep(500);
					r.keyRelease(KeyEvent.VK_S);
					Thread.sleep(500);
					r.keyRelease(KeyEvent.VK_ALT);
					
					Thread.sleep(5000);
					r.keyPress(KeyEvent.VK_ESCAPE);
					r.keyRelease(KeyEvent.VK_ESCAPE);
					Thread.sleep(5000);
					
					//open chrome with proxy
					WebDriver driver = BrowserSupport.OpenProxyChrome("127.0.0.1", 1080, chrome_driver);
					Thread.sleep(2000);
					
					//goto login page
					driver.get(dashboard);

					timeout = BrowserSupport.check_timeout(driver);
					if (timeout) {
						note = "Host error";
						driver.quit();
						server_file.SetCell(row, NOTE, note);
						success = server_file.SaveFile(server_link);
						continue;
					}

					//do login
					BrowserSupport.bing_Login(driver, ms_username, ms_password, 0);
					BrowserSupport.random_sleep(5, 10);

					//check account need to be phone verified or not
					int ver_phone_div = driver.findElements(By.id("iSelectProof")).size();
					int suspended = driver.findElements(By.className("serviceAbusePageContainer")).size();
					int ver_phone_number = driver.findElements(By.id("frmVerifyProof")).size();
					int account_baned = driver.findElements(By.className("signupWarningMessage")).size();
					int confirm_change_mobile = driver.findElements(By.id("iLandingViewAction")).size();
					
					//
					if (confirm_change_mobile > 0)
						driver.findElement(By.id("iLandingViewAction")).click();
					//account is banned, 
					if (account_baned > 0) {
						BrowserSupport.random_sleep(5, 20);
						account_status = "Account Baned  -- Should remove Account --- Dung tiec";
					}

					//account need to be verified, need verify phone number manually
					if (ver_phone_number > 0) {
						BrowserSupport.random_sleep(60, 120);
						ver_phone_number = driver.findElements(By.id("frmVerifyProof")).size();
						if (ver_phone_number > 0)
							account_status = "Verify Account";
					}

					if (ver_phone_div > 0) {
						BrowserSupport.random_sleep(60, 120);
						ver_phone_div = driver.findElements(By.id("iSelectProof")).size();
						if (ver_phone_div > 0)
							account_status = "Verify Account";

					} else if (suspended > 0) {//account is suspended, need verify phone number manually
						BrowserSupport.random_sleep(60, 120);
						suspended = driver.findElements(By.className("serviceAbusePageContainer")).size();
						if (suspended > 0)
							account_status = "Account Suspened";

					}
					
					// login succeeded
					if (ver_phone_div == 0 && suspended == 0 && ver_phone_number == 0 && account_baned == 0) {
						server_file.SetCell(row, ACCOUNT_STATUS, "");
						success = server_file.SaveFile(server_link);

						//get account info
						WebElement member_status = driver.findElement(By.id("user-status"));
						member_type = member_status.findElement(By.className("level-label")).getText();
						old_point = driver.findElement(By.className("credits")).getText();

						if (Integer.parseInt(old_point) >= 900) {
							server_file.SetCell(row, ACCOUNT_STATUS, "over 900 point");
							server_file.SetCell(row, LEVEL, member_type);
							server_file.SetCell(row, CUR_POINT, old_point);
							server_file.SaveFile(server_link);
							continue;
						}

						//get bonus
						WebElement bonus_div = driver.findElements(By.className("tileset")).get(0);

						int is_bonus = bonus_div.findElements(By.className("open-check")).size();

						//if has bonus, click to bonus link
						if (is_bonus > 0) {
							for (int a = 0; a < is_bonus; a++) {
								WebElement bonus_div1 = driver.findElements(By.className("tileset")).get(0);
								WebElement get_alink = bonus_div1.findElements(By.tagName("a")).get(0);
								String href = get_alink.getAttribute("href");
								String quiz = get_alink.getAttribute("id");

								//quiz
								if (quiz.contains("Quiz")) {
									driver.navigate().to(href);
									BrowserSupport.random_sleep(10, 15);
									driver.navigate().refresh();
									if (driver.findElements(By.id("quizWelcomeContainer")).size() > 0) {
										WebElement quiz_wel = driver.findElement(By.id("quizWelcomeContainer"));
										String class_quiz = quiz_wel.getAttribute("class");
										if (!class_quiz.contains("b_hide")) {
											quiz_wel.findElement(By.id("rqStartQuiz")).click();
										}
									}

									for (int click = 0; click < 5; click++) {
										BrowserSupport.random_sleep(15, 30);
										if (driver.findElements(By.id("currentQuestionContainer")).size() > 0) {
											WebElement cur_question = driver
													.findElement(By.id("currentQuestionContainer"));
											WebElement complete_panel = driver
													.findElement(By.id("quizCompleteContainer"));
											String complete_class = complete_panel.getAttribute("class");
											String class_question = cur_question.getAttribute("class");

											if (driver.findElements(By.id("currentQuestionContainer")).size() > 0)
												cur_question = driver.findElement(By.id("currentQuestionContainer"));
											else
												break;
											if (!class_question.contains("b_hide")) {
												if (driver.findElements(By.id("rqAnswerOption0")).size() > 0)
													driver.findElement(By.id("rqAnswerOption0")).click();
											} else {
												break;
											}
											BrowserSupport.random_sleep(15, 30);

											if (driver.findElements(By.id("currentQuestionContainer")).size() > 0)
												cur_question = driver.findElement(By.id("currentQuestionContainer"));
											else
												break;
											class_question = cur_question.getAttribute("class");
											if (!class_question.contains("b_hide")) {
												if (driver.findElements(By.id("rqAnswerOption1")).size() > 0)
													driver.findElement(By.id("rqAnswerOption1")).click();
											} else {
												break;
											}
											BrowserSupport.random_sleep(15, 30);
											if (driver.findElements(By.id("currentQuestionContainer")).size() > 0)
												cur_question = driver.findElement(By.id("currentQuestionContainer"));
											else
												break;
											// cur_question =
											// driver.findElement(By.id("currentQuestionContainer"));
											class_question = cur_question.getAttribute("class");
											if (!class_question.contains("b_hide")) {
												if (driver.findElements(By.id("rqAnswerOption2")).size() > 0)
													driver.findElement(By.id("rqAnswerOption2")).click();
											} else {
												break;
											}
											BrowserSupport.random_sleep(15, 30);
											if (driver.findElements(By.id("currentQuestionContainer")).size() > 0)
												cur_question = driver.findElement(By.id("currentQuestionContainer"));
											else
												break;
											class_question = cur_question.getAttribute("class");
											if (!class_question.contains("b_hide")) {
												if (driver.findElements(By.id("rqAnswerOption3")).size() > 0)
													driver.findElement(By.id("rqAnswerOption3")).click();
											} else {
												break;
											}
										}
									}

								} else {
									driver.navigate().to(href);
									BrowserSupport.random_scroll(driver, 5, 20);

								}

								BrowserSupport.random_sleep(5, 20);

								driver.navigate().to(dashboard);

								BrowserSupport.random_sleep(5, 20);

							}
						}

						//get pc and mobile point
						WebElement pc_div = driver.findElement(By.id("srch1-2-15-NOT_T1T3_Control-Exist"));
						WebElement mobile_div = driver.findElement(By.id("mobsrch1-2-10-NOT_T1T3_Control-Exist"));
						int is_pc = pc_div.findElements(By.className("open-check")).size();
						int is_mobile = mobile_div.findElements(By.className("open-check")).size();
						int remain_pc = 15;
						int remain_mobile = 10;
						int remain_pc_search = 1;
						int remain_mobile_search = 1;
						// if remain pc points, calculate number of searching
						if (is_pc > 0) {
							String progress = pc_div.findElement(By.className("progress")).getText();
							String[] xyzd = progress.split("of");
							remain_pc = Integer.parseInt(xyzd[0].trim());
							remain_pc_search = (15 - remain_pc) * 2 + BrowserSupport.random(3, 8);
						} else {
							//if no point is remained, do some search
							remain_pc = 1;
							remain_pc_search = BrowserSupport.random(3, 8);
						}

						//  if remain mobile points
						if (is_mobile > 0) {
							String progress = mobile_div.findElement(By.className("progress")).getText();
							String[] xyzd = progress.split("of");
							remain_mobile = Integer.parseInt(xyzd[0].trim());
							remain_mobile_search = (10 - remain_mobile) * 2 + BrowserSupport.random(2, 7);
						} else {
							// if no, do some search
							int abc = BrowserSupport.random(1, 10);
							if (abc < 9) {
								remain_mobile = 1;
								remain_mobile_search = BrowserSupport.random(2, 7);
							}
						}

						if (remain_pc < 15) {

							driver.navigate().to(bing);
							BrowserSupport.random_sleep(3, 10);
							Boolean bing_timeout = BrowserSupport.check_timeout(driver);
							if (!bing_timeout) {
								do_searching(driver, remain_pc_search, keyword_file, server_file, row, note, 0);
							}
						}

						if (remain_mobile < 10) {
							// driver.quit();
							WebDriver driver_mobile = BrowserSupport.OpenProxyChromeMobile("127.0.0.1", 1080);
							driver_mobile.navigate().to(mobile_sign_in);
							BrowserSupport.random_sleep(3, 10);
							// driver.manage().timeouts().pageLoadTimeout(60,
							// TimeUnit.SECONDS);
							BrowserSupport.bing_Login(driver_mobile, ms_username, ms_password, 1);

							driver_mobile.navigate().to(bing);
							BrowserSupport.random_sleep(3, 10);
							do_searching(driver_mobile, remain_mobile_search, keyword_file, server_file, row, note, 1);
							driver_mobile.close();
						}
						
						try {
							driver.navigate().to(dashboard);
						} catch (Exception e) {
							note = "Searching timeout";
							server_file.SetCell(row, NOTE, note);
							success = server_file.SaveFile(server_link);
							continue;
						}
						
						//get account info after searching
						BrowserSupport.random_sleep(3, 10);
						pc_div = driver.findElement(By.id("srch1-2-15-NOT_T1T3_Control-Exist"));
						mobile_div = driver.findElement(By.id("mobsrch1-2-10-NOT_T1T3_Control-Exist"));
						bonus_div = driver.findElements(By.className("tileset")).get(0);
						
						is_bonus = bonus_div.findElements(By.className("open-check")).size();
						is_pc = pc_div.findElements(By.className("open-check")).size();
						is_mobile = mobile_div.findElements(By.className("open-check")).size();
						
						member_status = driver.findElement(By.id("user-status"));
						after_point = member_status.findElement(By.className("credits")).getText();
						
						BrowserSupport.random_sleep(2, 4);
						driver.findElement(By.linkText("Redeem")).click();
						BrowserSupport.random_sleep(3, 7);
						
						int is_myDynamicElement = (new WebDriverWait(driver, 120))
								.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("product")))
								.size();
						int num_of_product = driver.findElements(By.className("product")).size();
						
						//check account has amazon gift card or not
						int is_amazong = 0;
						if (is_myDynamicElement > 0)
							for (int pro = 0; pro < num_of_product; pro++) {
								WebElement product = driver.findElements(By.className("product")).get(pro);
								int li_size = product.findElements(By.tagName("li")).size();
								for (int li = 0; li < li_size; li++) {
									WebElement li_product = product.findElements(By.tagName("li")).get(li);
									int xxxx = (new WebDriverWait(driver, 120)).until(
											ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("name")))
											.size();
									String amzone = li_product.findElement(By.className("name")).getText();
									if (amzone.contains("Amazon")) {
										redeem_point = li_product.findElement(By.className("number")).getText();
										is_amazong = 1;
										break;
									}
								}
								if (is_amazong == 1)
									break;
							}
						BrowserSupport.random_sleep(2, 4);
						
						//check account is need to redeem or not
						if (!"".equals(redeem_point))
							if (!"".equals(after_point))
								if (Integer.parseInt(redeem_point) <= Integer.parseInt(after_point))
									account_status = " Account Cash";
								else
									account_status = " ";
						
						//if full point or not
						if (is_bonus == 0 && is_pc == 0 && is_mobile == 0)
							daily_status = "Full Daily";
						BrowserSupport.random_sleep(2, 4);
						if (is_amazong == 0)
							 is_amazon= " no Amazon";

					}

					BrowserSupport.random_sleep(2, 4);
					BrowserSupport.shutdown_proceess(runtime);
					//calculate searching time
					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;

					int seconds = (int) (totalTime / 1000) % 60;
					int minutes = (int) ((totalTime / (1000 * 60)) % 60);
					int hours = (int) ((totalTime / (1000 * 60 * 60)) % 24);
					String run_time = " " + hours + ":" + minutes + ":" + seconds;
					if (!"".equals(server_file.getCell(row, TIME))) {
						run_time = server_file.getCell(row, TIME) + "---" + run_time;
					}
					
					//write to file
					server_file.SetCell(row, TIME, run_time);
					server_file.SetCell(row, NOTE, note);
					server_file.SetCell(row, LEVEL, member_type);
					server_file.SetCell(row, ACCOUNT_STATUS, account_status);
					server_file.SetCell(row, IS_AMAZON, is_amazon);
					server_file.SetCell(row, FULL_POINT, daily_status);
					server_file.SetCell(row, REDEEM_POINT, redeem_point);
					server_file.SetCell(row, CUR_POINT, after_point);
					if (!"".equals(old_point))
						if (!"".equals(after_point)) {
							earn_point = "" + (Integer.parseInt(after_point) - Integer.parseInt(old_point));

						}
					if (!"".equals(server_file.getCell(row, EARN_POINT)))
						if (!"".equals(earn_point)) {
							earn_point = ""
									+ (Integer.parseInt(server_file.getCell(row, EARN_POINT)) + Integer.parseInt(earn_point));
						}
					server_file.SetCell(row, EARN_POINT, earn_point);
					success = server_file.SaveFile(server_link);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					note = "Time Out";
					server_file.SetCell(row, NOTE, note);
					server_file.SetCell(row, LEVEL, member_type);
					server_file.SetCell(row, ACCOUNT_STATUS, account_status);
					server_file.SetCell(row, IS_AMAZON, is_amazon);
					server_file.SetCell(row, FULL_POINT, daily_status);
					server_file.SetCell(row, REDEEM_POINT, redeem_point);
					server_file.SetCell(row, CUR_POINT, after_point);
					server_file.SetCell(row, EARN_POINT, earn_point);
					success = server_file.SaveFile(server_link);

				}

			} else {

			}
		}
	}
	// }

	public static void openNewTab(WebDriver driver, int num_of_web_link) {

		Actions newTab = new Actions(driver);
		for (int xxxx = 0; xxxx < num_of_web_link; xxxx++) {

			try {
				WebElement search_result = driver.findElement(By.id("b_results"));
				int random_web = BrowserSupport.random(xxxx, xxxx + BrowserSupport.random(1, 2));
				WebElement searching_web = search_result.findElements(By.className("b_algo")).get(random_web);
				driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
				WebElement link = searching_web.findElements(By.tagName("a")).get(0);
				BrowserSupport.random_sleep(3, 6);
				newTab.keyDown(Keys.CONTROL).click(link).keyUp(Keys.CONTROL).build().perform();

			} catch (Exception ex) {// page load over 60s

			}

		}
		// WebElement link = driver.findElement(By.xpath("//*[@id='link']"));

		BrowserSupport.random_sleep(2, 5);
		// handle windows change
		String parentHandle = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equalsIgnoreCase(parentHandle)) {
				driver.switchTo().window(winHandle);
				BrowserSupport.random_sleep(5, 15);
				driver.close();

			}
		}
		BrowserSupport.random_sleep(2, 6);
		driver.switchTo().window(parentHandle);
	}

	/**
	 * @param driver
	 * @param remain_search
	 * @param keyword_file
	 * @param server_file
	 * @param row
	 * @param note
	 * @param search_type
	 * @throws InterruptedException
	 */
	public static void do_searching(WebDriver driver, int remain_search, CkCsv keyword_file, CkCsv server_file, int row,
			String note, int search_type) {
		// try {
		// Random key_word = new Random();
		int num_searched = 0;
		int random_num_search = BrowserSupport.random(10, 22);

		for (int sear_count = 0; sear_count < remain_search; sear_count++) {
			if (num_searched > random_num_search)
				break;
			int searching_error = 0;
			Boolean searching_timeout = false;
			int kwRow = BrowserSupport.random(0, 1999);
			int kwCol = BrowserSupport.random(0, 9);
			String key_search = keyword_file.getCell(kwRow, kwCol);

			if (!searching_timeout) {
				try {
					int btn_search_avail = driver.findElements(By.id("sb_form_q")).size();
					if (btn_search_avail > 0) {
						BrowserSupport.random_sleep(2, 6);
						
						WebElement search_tab = driver.findElement(By.id("sb_form_q"));
						search_tab.clear();
						search_tab.sendKeys(key_search);
						
						BrowserSupport.random_sleep(1, 2);
						search_tab.sendKeys(Keys.RETURN);
						searching_timeout = BrowserSupport.check_timeout(driver);
						BrowserSupport.random_sleep(2, 4);
					} else {
						break;
					}
				} catch (Exception ex2) {

				}

				if (!searching_timeout) {// begin search
					// webs
					Random click = new Random();
					int is_click_web = click.nextInt(9);

					if (is_click_web < 8) {// 80% click web
						int num_of_web_link = BrowserSupport.random(1, 4);
						try {
							openNewTab(driver, num_of_web_link);
						} catch (Exception e) {
							e.printStackTrace();
							searching_error = 1;
						}
					}
					if (searching_error == 1)
						break;
					int random_news = 0;

					// news
					int is_click_news = click.nextInt(9);
					int is_news = 0;
					try {
						WebElement search_result = driver.findElement(By.id("b_results"));
						is_news = search_result.findElements(By.id("ans_news")).size();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (is_click_news < 2)// 20% click news
						if (is_news > 0) {
							try {
								WebElement search_result = driver.findElement(By.id("b_results"));
								WebElement div_news = search_result.findElement(By.id("ans_news"));
								int num_of_news_link = div_news.findElements(By.tagName("a")).size() - 1;
								random_news = click.nextInt(num_of_news_link);
								driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
								div_news.findElements(By.tagName("a")).get(random_news).click();
								BrowserSupport.random_sleep(2, 4);
								BrowserSupport.random_scroll(driver, 5, 25);
								driver.navigate().back();
								BrowserSupport.random_sleep(2, 4);
							} catch (Exception ex) {// page load over 60s
								try {
									driver.navigate().back();
									BrowserSupport.random_sleep(2, 4);
								} catch (Exception cant_back) {
									searching_error = 1;
								}
							}
						}
					if (searching_error == 1)
						break;
					
					// images
					int is_click_image = click.nextInt(9);
					int is_images = 0;
					try {
						WebElement search_result = driver.findElement(By.id("b_results"));
						is_images = search_result.findElements(By.id("irp")).size();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (is_click_image < 6)// 60% click images
						if (is_images > 0) {
							try {
								WebElement search_result = driver.findElement(By.id("b_results"));
								WebElement div_images = search_result.findElement(By.id("irp"));
								int num_of_images_link = div_images.findElements(By.tagName("a")).size() - 1;
								int random_image = click.nextInt(num_of_images_link);

								driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
								div_images.findElements(By.tagName("a")).get(random_image).click();
								BrowserSupport.random_sleep(2, 6);		
								BrowserSupport.random_scroll(driver, 5, 20);
								driver.navigate().back();
								BrowserSupport.random_sleep(2, 8);
							} catch (Exception ex) {// page load over 30s
								try {
									driver.navigate().back();
								} catch (Exception cant_bacsk) {
									searching_error = 1;
								}
							}
						}
					if (searching_error == 1)
						break;
					
					// videos
					int is_click_video = click.nextInt(9);
					int is_videos = 0;
					try {
						WebElement search_result = driver.findElement(By.id("b_results"));
						is_videos = search_result.findElements(By.id("vidans2")).size();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (is_click_video < 0)// 20% click video
						if (is_videos > 0) {
							try {
								WebElement search_result = driver.findElement(By.id("b_results"));
								WebElement div_videos = search_result.findElement(By.id("vidans2"));
								int num_of_videos_link = div_videos.findElements(By.tagName("a")).size() - 1;
								random_news = click.nextInt(num_of_videos_link);
								driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
								div_videos.findElements(By.tagName("a")).get(random_news).click();
								BrowserSupport.random_sleep(5, 25);
								driver.navigate().back();
								BrowserSupport.random_sleep(2, 4);
							} catch (Exception ex) {// page load over 60s
								try {
									driver.navigate().back();
									BrowserSupport.random_sleep(2, 4);
								} catch (Exception cant_back) {
									searching_error = 1;
								}
							}
						}
					if (searching_error == 1)
						break;
				}
			} else {
				note = "Searching timeout";
				server_file.SetCell(row, NOTE, note);
				server_file.SaveFile(server_link);
				break;
			}
			
			try {
				if (searching_error != 1) {
					BrowserSupport.random_scroll(driver, 8, 16);

				}
			} catch (Exception ex1) {
				System.out.println("");
				break;
			}
			num_searched++;
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			if (searching_error == 1)
				break;
		}
	}
}
