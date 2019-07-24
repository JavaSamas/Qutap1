package com.exilant.qutap.plugin;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class SeleniumPluginHelper {
    static WebDriver driver;
    public String result = null;
    JSONObject JSONRes;
    JSONObject respJSON = new JSONObject();
    StringWriter errors = new StringWriter();
    String res = "";
    String error = "";

    public JSONObject open_Browser(String browserName) {
        try {
            if (browserName.equalsIgnoreCase("firefox")) {
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("browser.download.folderList", 2);
                profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
                profile.setPreference("pdfjs.disabled", true);
               
       //         System.setProperty("webdriver.gecko.driver", "/Users/abdulmunnaf/.m2/repository/org/seleniumhq/selenium/selenium-firefox-driver/3.6.0/selenium-firefox-driver-3.6.0-sources.jar");
                driver = new FirefoxDriver(profile);
                driver.manage().window().maximize();
                this.respJSON.put("status", (Object)"PASS");
                this.respJSON.put("response", (Object)"Firefox browser opened");
                this.respJSON.put("error", (Object)"");
            }
        }
        catch (WebDriverException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject enter_URL(String URL) {
        try {
            driver.navigate().to(URL);
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)("Navigeted to" + URL));
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public By locatorValue(String locatorType, String value) {
        By by;
        switch (locatorType) {
            case "id": {
                by = By.id((String)value);
                break;
            }
            case "name": {
                by = By.name((String)value);
                break;
            }
            case "xpath": {
                by = By.xpath((String)value);
                break;
            }
            case "css": {
                by = By.cssSelector((String)value);
                break;
            }
            case "linkText": {
                by = By.linkText((String)value);
                break;
            }
            case "partialLinkText": {
                by = By.partialLinkText((String)value);
                break;
            }
            default: {
                by = null;
            }
        }
        return by;
    }

    public JSONObject enter_Text(String locatorType, String value, String text) {
        try {
            By locator = this.locatorValue(locatorType, value);
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(new CharSequence[]{text});
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)(text + " Has entered"));
            this.respJSON.put("error", (Object)"");
        }
        catch (NoSuchElementException e) {
            System.err.format("No Element Found to enter text" + (Object)e, new Object[0]);
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject click(String locatorType, String value) throws InterruptedException {
        try {
            By locator = this.locatorValue(locatorType, value);
            WebElement element = driver.findElement(locator);
            element.click();
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"click action done");
            this.respJSON.put("error", (Object)"");
        }
        catch (NoSuchElementException e) {
            System.err.format("No Element Found to perform click" + (Object)e, new Object[0]);
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject switch_ToDefault() {
        try {
            driver.switchTo().defaultContent();
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"switched  to the default content");
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject switch_ToFrame(String index) {
        try {
            driver.switchTo().frame(Integer.parseInt(index));
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"switched  to the specified frame");
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject move_Cursor(String locatorType, String value) {
        try {
            Actions act = new Actions(driver);
            By locator = this.locatorValue(locatorType, value);
            WebElement element = driver.findElement(locator);
            act.moveToElement(element).perform();
            Thread.sleep(1000);
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"Cursor moved to the specified element");
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject close_Browser() {
        try {
            driver.quit();
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"Firefox browser closed");
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject refresh() {
        try {
            driver.navigate().refresh();
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"Page Refreshed");
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject wait(Integer time) {
        try {
            
        	
        	int a = time;
            System.out.println("wait()"+a +"called ........");
            Thread.sleep(a* 100);
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)("Waiting..." + time + " Seconds"));
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject verify_Text(String locatorType, String value, String text) {
        try {
            By locator = this.locatorValue(locatorType, value);
            WebElement element = driver.findElement(locator);
            String displayedValue = element.getText();
            if (displayedValue.contains(text)) {
                this.respJSON.put("status", (Object)"PASS");
                this.respJSON.put("response", (Object)displayedValue);
                this.respJSON.put("error", (Object)"");
            } else {
                this.respJSON.put("status", (Object)"FAIL");
                this.respJSON.put("response", (Object)displayedValue);
                this.respJSON.put("error", (Object)"Not valid text");
            }
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject clear_Text(String locatorType, String value) {
        try {
            By locator = this.locatorValue(locatorType, value);
            driver.findElement(locator).clear();
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"TextBox cleared");
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject select_Dropdown(String locatorType, String value, String selectValue) {
        try {
            By locator = this.locatorValue(locatorType, value);
            WebElement element = driver.findElement(locator);
            Select sdd = new Select(element);
            sdd.selectByVisibleText(selectValue);
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)("Selected value :" + selectValue));
            this.respJSON.put("error", (Object)"");
        }
        catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    }

    public JSONObject verify_Count(String locatorType, String value, String text) {
        try {
            By locator = this.locatorValue(locatorType, value);
            List elements = driver.findElements(locator);
            Integer count = elements.size();
            if (count.toString().equals(text)) {
                this.respJSON.put("status", (Object)"PASS");
                this.respJSON.put("response", (Object)count);
                this.respJSON.put("error", (Object)"");
            } else {
                this.respJSON.put("status", (Object)"FAIL");
                this.respJSON.put("response", (Object)count);
                this.respJSON.put("error", (Object)"Not a valid count");
            }
        }
        catch (Exception e) {
            this.error = e.getMessage();
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.error);
        }
        return this.respJSON;
    }
    
    public JSONObject right_Click(String locatorType, String value) {
    	try{
    		By locator = this.locatorValue(locatorType, value);
    		WebElement contextElement=driver.findElement(locator);
    		
    		Actions element=new Actions(driver);
    		element.contextClick(contextElement).build().perform();
    		
    		this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"context element clicked");
            this.respJSON.put("error", (Object)"");
    	}
    	catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    	
    	
    }
   
    public JSONObject switchTo_Popup(String locatorType, String value){
		try{
			By locator=this.locatorValue(locatorType, value);
			driver.findElement(locator).click();
			
			String myWindowHandle=driver.getWindowHandle();
			driver.switchTo().window(myWindowHandle);
			
			this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"switched to popup");
            this.respJSON.put("error", (Object)"");
			
		}
		catch (Exception e) {
            e.printStackTrace(new PrintWriter(this.errors));
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.errors.toString());
        }
        return this.respJSON;
    	
    }
    
    
    
    
    public JSONObject file_Upload(String filePath) {
        try {
            StringSelection stringSelection = new StringSelection(filePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            Robot robot = new Robot();
            robot.keyPress(157);
            robot.keyPress(9);
            robot.keyRelease(157);
            robot.keyRelease(9);
            robot.delay(500);
            robot.keyPress(157);
            robot.keyPress(16);
            robot.keyPress(71);
            robot.keyRelease(157);
            robot.keyRelease(16);
            robot.keyRelease(71);
            robot.delay(500);
            robot.keyPress(157);
            robot.keyPress(86);
            robot.keyRelease(157);
            robot.keyRelease(86);
            robot.delay(500);
            robot.keyPress(10);
            robot.keyRelease(10);
            robot.delay(3000);
            robot.keyPress(10);
            robot.keyRelease(10);
            robot.delay(3000);
            robot.keyPress(10);
            robot.keyRelease(10);
            this.respJSON.put("status", (Object)"PASS");
            this.respJSON.put("response", (Object)"File Uploaded Successfully");
            this.respJSON.put("error", (Object)this.error);
        }
        catch (Exception e) {
            this.error = e.getMessage();
            this.respJSON.put("status", (Object)"FAIL");
            this.respJSON.put("response", (Object)"");
            this.respJSON.put("error", (Object)this.error);
        }
        return this.respJSON;
    }
}
