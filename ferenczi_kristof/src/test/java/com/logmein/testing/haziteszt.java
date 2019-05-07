package com.logmein.testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import static org.junit.Assert.*;

import java.nio.file.FileSystems;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.logmein.testing.pageobjects.*;




public class haziteszt {

	private static WebDriver driver;
	private Actions action;

	private Clothes clothes;
	private Cart cart;
	private ShoppingActions shoppingActions;
	private CartSummary summary;
	private SignInForm signinForm;
	private static Account account;

	@Before
	public void setup() {
		String driverPath = FileSystems.getDefault().getPath("src/test/resources/chromedriver.exe").toString();
		System.setProperty("webdriver.chrome.driver", driverPath);
		
		
		driver = new ChromeDriver();

		action = new Actions(driver);

		clothes = new Clothes(driver);
		cart = new Cart(driver);
		shoppingActions = new ShoppingActions(driver);
		signinForm = new SignInForm(driver);
		summary = new CartSummary(driver);
		account = new Account(driver);

		String baseUrl = "http://automationpractice.com/index.php";
		driver.manage().window().maximize();
		driver.get(baseUrl);
	}

	@AfterClass
	public static void closeAll() {
		account.getAccountLogout().click();
		driver.quit();
	}

	@Test
	public void E2E() {
		
    	
    	WebElement signInButton = driver.findElement(By.cssSelector("a.login"));
    	signInButton.click();
    	
    	WebElement submitLoginButton = driver.findElement(By.id("SubmitLogin"));
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(submitLoginButton));
        
        WebElement email = driver.findElement(By.id("email"));
        WebElement passwd = driver.findElement(By.id("passwd"));
    	
        email.sendKeys("dbchapee@gmail.com");
        passwd.sendKeys("teszt1234");
        submitLoginButton.click();
        
    	WebElement myAccountButton = driver.findElement(By.cssSelector("a.account"));
        (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(myAccountButton));
        
        assertEquals("The account logged in is not the expected one!", "Ferenczi Kristóf", myAccountButton.getText());
		
		
		
		Assert.assertTrue(clothes.getDressesBtn().isDisplayed());

		action.moveToElement(clothes.getDressesBtn()).perform();

		Assert.assertTrue(clothes.getEveningDressesBtn().isDisplayed());

		action.moveToElement(clothes.getEveningDressesBtn()).perform();
		clothes.getEveningDressesBtn().click();
		action.moveToElement(clothes.getEveningDressProduct(1)).perform();
		action.moveToElement(shoppingActions.getAddToCartBtn()).perform();
		action.click(shoppingActions.getAddToCartBtn()).build().perform();

		Assert.assertTrue(shoppingActions.getAddToCartBtn().isDisplayed());

		action.click(shoppingActions.getContinueShopingBtn()).build().perform();
		action.moveToElement(cart.getCartTab()).perform();

		Assert.assertEquals(cart.getCartProductsQty().size(), 1);
		
		
		
		cart.getCartTabCheckOutBtn().click();
		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$50.99");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$52.99");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");
		
		summary.getCartSummQtyPlus(1).click();
		driver.navigate().refresh();
		
		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$101.98");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$103.98");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");
		
		summary.getCartProceedBtn().click();
		
		summary.getCheckbox().click();
		
		
		summary.getDeliveryAddress().selectByIndex(1);
		summary.getBillingAddress().selectByIndex(1);
		Assert.assertNotEquals(summary.getDeliveryAddress().getFirstSelectedOption().getText(),summary.getBillingAddress().getFirstSelectedOption().getText());
		
		summary.getCartAddressBtn().click();
		
		summary.getCartSummTermsOfServiceCheck().click();
		summary.getCartProceedBtnTwo().click();
		Assert.assertEquals(summary.getTotalProductsPrice().getText(), "$103.98");
		summary.getCartSummPayByCheck().click();
		
		summary.getCartSummConfirmOrderBtn().click();
		
		Assert.assertTrue(summary.getCartSummSuccessMsg().isDisplayed());
		Assert.assertEquals(summary.getCartSummSuccessMsg().getText(), "Your order on My Store is complete.");
	}
}

	/*@Test
	public void deleteCartProducts() {
		Assert.assertEquals(cart.getCartProductsQty().size(), 3);

		action.moveToElement(cart.getCartTab()).perform();
		action.moveToElement(cart.getCartProductDeleteX(2)).perform();
		action.click(cart.getCartProductDeleteX(2)).build().perform();
		action.moveToElement(clothes.getDressesBtn()).perform();
		action.moveToElement(clothes.getEveningDressesBtn()).perform();
		clothes.getEveningDressesBtn().click();
		action.moveToElement(cart.getCartTab()).perform();

		Assert.assertEquals(cart.getCartProductsQty().size(), 2);
	}

	@Test
	public void checkingCartProductsQtyAndPrice() {
		Assert.assertEquals(cart.getCartProductsQty().size(), 2);

		action.moveToElement(clothes.getDressesBtn()).perform();
		action.moveToElement(clothes.getEveningDressesBtn()).perform();
		action.moveToElement(clothes.getEveningDressProduct(1)).perform();
		action.moveToElement(shoppingActions.getAddToCartBtn()).perform();
		action.click(shoppingActions.getAddToCartBtn()).build().perform();
		action.click(shoppingActions.getContinueShopingBtn()).build().perform();

		action.moveToElement(cart.getCartTab()).perform();
		action.moveToElement(cart.getCartProductsQty(1)).perform();

		Assert.assertEquals(cart.getCartProductsQty(1).getText(), "1");

		action.moveToElement(cart.getCartProductsQty(2)).perform();

		Assert.assertEquals(cart.getCartProductsQty(2).getText(), "2");

		action.moveToElement(cart.getCartShipingCost()).perform();

		Assert.assertEquals(cart.getCartShipingCost().getText(), "$2.00");

		action.moveToElement(cart.getCartTotalPrice()).perform();

		Assert.assertEquals(cart.getCartTotalPrice().getText(), "$132.96");
	}

	@Test
	public void continueToShoppingSummary() {
		action.moveToElement(cart.getCartTab()).perform();
		action.moveToElement(cart.getCartTabCheckOutBtn()).perform();

		Assert.assertTrue(cart.getCartTabCheckOutBtn().isDisplayed());

		action.click(cart.getCartTabCheckOutBtn()).build().perform();
		;

		Assert.assertTrue(summary.getCartSummaryTable().isDisplayed());
		Assert.assertEquals(summary.getCartSummTotalProductsNum().size(), 2);
		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$130.96");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$132.96");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");
		Assert.assertTrue(summary.getCartSummQtyPlus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyPlus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyPlus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyPlus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyMinus(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummQtyInput(2).isDisplayed());
		Assert.assertTrue(summary.getCartSummDeleteBtn(1).isDisplayed());
		Assert.assertTrue(summary.getCartSummDeleteBtn(2).isDisplayed());
	}

	@Test
	public void increaseQtyOfProduct1() {
		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$130.96");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$132.96");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");

		summary.getCartSummQtyPlus(1).click();
		driver.navigate().refresh();

		Assert.assertEquals(summary.getCartSummTotalProductsPrice().getText(), "$159.94");
		Assert.assertEquals(summary.getCartSummaryTotalPrice().getText(), "$161.94");
		Assert.assertEquals(summary.getCartSummTotalShipping().getText(), "$2.00");
	}

	@Test
	public void signinRequest() {
		summary.getCartProceedBtn().click();

		Assert.assertTrue(signinForm.getSignInForm().isDisplayed());

		//signinForm.setEmailField(EmailsGenerator.getCurrentEmail());
		signinForm.setPasswordField("tester123");
		signinForm.getSignInBtn().click();
	}

	@Test
	public void billingAndDeliveryAddress() {
		Assert.assertEquals(summary.getCartSummBillingAdressName().getText(), "John Doe");
		Assert.assertEquals(summary.getCartSummBillingAdressOne().getText(), "Centar");
		Assert.assertEquals(summary.getCartSummBillingAdressCityState().getText(), "Novi Sad, Connecticut 21000");
		Assert.assertEquals(summary.getCartSummBillingAdressCountry().getText(), "United States");
		Assert.assertEquals(summary.getCartSummBillingAdressHomePhone().getText(), "056");
		Assert.assertEquals(summary.getCartSummBillingAdressMobile().getText(), "066");
	}

	@Test
	public void termsOfServiceModal() {
		summary.getCartProceedBtnTwo().click();
		summary.getCartProceedBtnTwo().click();

		action.moveToElement(summary.getCartSummTermsOfServiceDialog()).perform();

		Assert.assertTrue(summary.getCartSummTermsOfServiceDialog().isDisplayed());

		action.moveToElement(summary.getCartSummTermsOfServiceDialogClose()).perform();
		action.click(summary.getCartSummTermsOfServiceDialogClose()).build().perform();

		driver.navigate().refresh();

		summary.getCartSummTermsOfServiceCheck().click();
		summary.getCartProceedBtnTwo().click();
	}

	@Test
	public void payment() {
		summary.getCartSummPayByBankWire().click();

		Assert.assertEquals(summary.getCartSummPayByBankWireConfirm().getText(), "BANK-WIRE PAYMENT.");

		summary.getCartSummOtherPaymentMethods().click();
		summary.getCartSummPayByCheck().click();

		Assert.assertEquals(summary.getCartSummPayByCheckConfirm().getText(), "CHECK PAYMENT");
	}

	@Test
	public void confirmOrder() {
		summary.getCartSummConfirmOrderBtn().click();

		Assert.assertTrue(summary.getCartSummSuccessMsg().isDisplayed());
		Assert.assertEquals(summary.getCartSummSuccessMsg().getText(), "Your order on My Store is complete.");
	}

	@Test
	public void checkIsOrderVisibleInOrderHistorySection() {
		account.getAccountBtn().click();

		Assert.assertTrue(account.getAccountOrderHistoryBtn().isDisplayed());

		account.getAccountOrderHistoryBtn().click();

		Assert.assertTrue(account.getAccountOrderListTable().isDisplayed());

		account.getAccountBtn().click();
		account.getAccountOrderHistoryBtn().click();

		Assert.assertEquals(account.getAccountOrdersLis().size(), 1);
	}
	*/

