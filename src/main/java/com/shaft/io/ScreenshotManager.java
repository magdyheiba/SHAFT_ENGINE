package com.shaft.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.shaft.element.ElementActions;
import com.shaft.element.JSWaiter;

public class ScreenshotManager {
    private static final String SCREENSHOT_FOLDERPATH = "allure-results/screenshots/";
    private static final String SCREENSHOT_FOLDERNAME = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
    private static String screenshotFileName = "Screenshot";
    private static final String SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT = System.getProperty("screenshotParams_whenToTakeAScreenshot");
    private static final Boolean SCREENSHOT_PARAMS_ISFULLPAGESCREENSHOT = Boolean.valueOf(System.getProperty("screenshotParams_isFullPageScreenshot"));
    private static final String SCREENSHOT_PARAMS_SKIPPEDELEMENTSFROMSCREENSHOT = System.getProperty("screenshotParams_skippedElementsFromScreenshot");

    private ScreenshotManager() {
	throw new IllegalStateException("Utility class");
    }

    /*
     * A flag to determine when to take a screenshot. Always; after every browser
     * and element action. Never; never. ValidationPointsOnly; after every assertion
     * or verification point. FailuresOnly; after validation failures and element
     * action failures.
     */

    private static boolean globalPassFailStatus = false;
    /*
     * A flag to control the highlighting of the element green for passing yellow
     * for failing
     */
    private static String appendedText = "";

    /**
     * Used only on browser-based custom validation points. passFailStatus; true
     * means pass and false means fail.
     * 
     * @param driver
     *            the current instance of Selenium webdriver
     * @param passFailStatus
     *            A flag to determine whether the action has passed or failed
     */
    public static void captureScreenShot(WebDriver driver, boolean passFailStatus) {
	globalPassFailStatus = passFailStatus;
	if (passFailStatus) {
	    appendedText = "passed";
	} else {
	    appendedText = "failed";
	}

	if ((SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("Always")) || (SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("ValidationPointsOnly")) || (SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("FailuresOnly") && (!passFailStatus))) {
	    internalCaptureScreenShot(driver, null, appendedText, true);
	} else {
	    internalCaptureScreenShot(driver, null, appendedText, false);
	}
	// Note: Excluded the "Always" case as there will already be another screenshot
	// taken by the browser/element action // reversed this option to be able to
	// take a failure screenshot
    }

    /**
     * Used only on assertElementExists and verifyElementExists element-based custom
     * validation points. passFailStatus; true means pass and false means fail.
     * 
     * @param driver
     *            the current instance of Selenium webdriver
     * @param elementLocator
     *            the locator of the webElement under test (By xpath, id, selector,
     *            name ...etc)
     * @param passFailStatus
     *            A flag to determine whether the action has passed or failed
     */
    public static void captureScreenShot(WebDriver driver, By elementLocator, boolean passFailStatus) {
	globalPassFailStatus = passFailStatus;
	if (passFailStatus) {
	    appendedText = "passed";
	} else {
	    appendedText = "failed";
	}

	if ((SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("Always")) || (SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("ValidationPointsOnly")) || (SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("FailuresOnly") && (!passFailStatus))) {
	    internalCaptureScreenShot(driver, elementLocator, appendedText, true);
	} else {
	    internalCaptureScreenShot(driver, elementLocator, appendedText, false);
	}
	// Note: Excluded the "Always" case as there will already be another screenshot
	// taken by the browser/element action // reversed this option to be able to
	// take a failure screenshot
    }

    /**
     * Used in all browser actions, in failed element actions, in passed element
     * actions where the element can no longer be found, and in passed
     * switchToDefaultContent element action which requires no locator.
     * passFailStatus; true means pass and false means fail.
     * 
     * @param driver
     *            the current instance of Selenium webdriver
     * @param appendedText
     *            the text that needs to be appended to the name of the screenshot
     *            to make it more recognizable
     * @param passFailStatus
     *            A flag to determine whether the action has passed or failed
     */
    public static void captureScreenShot(WebDriver driver, String appendedText, boolean passFailStatus) {
	globalPassFailStatus = passFailStatus;

	if ((SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("Always")) || (SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("ValidationPointsOnly") && (!passFailStatus)) || ((SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("FailuresOnly")) && (!passFailStatus))) {
	    internalCaptureScreenShot(driver, null, appendedText, true);
	} else {
	    internalCaptureScreenShot(driver, null, appendedText, false);
	}
    }

    /**
     * Used only in passed element actions. Appended Text is added to the screenshot
     * name to signal why it was taken.
     * 
     * @param driver
     *            the current instance of Selenium webdriver
     * @param elementLocator
     *            the locator of the webElement under test (By xpath, id, selector,
     *            name ...etc)
     * @param appendedText
     *            the text that needs to be appended to the name of the screenshot
     *            to make it more recognizable
     */
    public static void captureScreenShot(WebDriver driver, By elementLocator, String appendedText, boolean passFailStatus) {
	globalPassFailStatus = passFailStatus;

	if (SCREENSHOT_PARAMS_WHENTOTAKEASCREENSHOT.equals("Always")) {
	    internalCaptureScreenShot(driver, elementLocator, appendedText, true);
	} else {
	    internalCaptureScreenShot(driver, elementLocator, appendedText, false);
	}
    }

    /**
     * Internal use only. Considers the screenshotParams_whenToTakeAScreenshot
     * parameter.
     * 
     * @param driver
     *            the current instance of Selenium webdriver
     * @param elementLocator
     *            the locator of the webElement under test (By xpath, id, selector,
     *            name ...etc)
     * @param appendedText
     *            the text that needs to be appended to the name of the screenshot
     *            to make it more recognizable
     * @param takeScreenshot
     *            determines whether or not to take a screenshot given the
     *            screenshotParams_whenToTakeAScreenshot parameter from the pom.xml
     *            file
     */
    private static void internalCaptureScreenShot(WebDriver driver, By elementLocator, String appendedText, boolean takeScreenshot) {
	new File(SCREENSHOT_FOLDERPATH).mkdirs();
	if (takeScreenshot) {
	    /**
	     * Force screenshot link to be shown in the results as a link not text
	     */
	    System.setProperty("org.uncommons.reportng.escape-output", "false");

	    /**
	     * Declare regularElementStyle, the WebElemnt, and Javascript Executor to
	     * highlight and unhighlight the WebElement
	     */
	    String regularElementStyle = "";
	    JavascriptExecutor js = null;
	    WebElement element = null;

	    /**
	     * If an elementLocator was passed, store regularElementStyle and highlight that
	     * element before taking the screenshot
	     */
	    if (elementLocator != null && (ElementActions.getElementsCount(driver, elementLocator) == 1)) {
		element = driver.findElement(elementLocator);
		js = (JavascriptExecutor) driver;
		regularElementStyle = highlightElementAndReturnDefaultStyle(element, js, setHighlightedElementStyle());
	    }

	    /**
	     * Take the screenshot and store it as a file
	     */
	    File src;

	    /**
	     * Attempt to take a full page screenshot, take a regular screenshot upon
	     * failure
	     */
	    src = takeScreenshot(driver);

	    /**
	     * Declare screenshot file name
	     */
	    String testCaseName = Reporter.getCurrentTestResult().getTestClass().getRealClass().getName();
	    screenshotFileName = System.currentTimeMillis() + "_" + testCaseName;
	    if (!appendedText.equals("")) {
		screenshotFileName = screenshotFileName + "_" + appendedText;
	    }

	    /**
	     * If an elementLocator was passed, unhighlight that element after taking the
	     * screenshot
	     * 
	     */
	    if (js != null) {
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, regularElementStyle);
	    }

	    /**
	     * Copy the screenshot to desired path, and append the appropriate filename.
	     * 
	     */
	    FileManager.copyFile(src.getAbsolutePath(), SCREENSHOT_FOLDERPATH + SCREENSHOT_FOLDERNAME + FileSystems.getDefault().getSeparator() + screenshotFileName + ".png");

	    /*
	     * File screenshotFile = new File(SCREENSHOT_FOLDERPATH + SCREENSHOT_FOLDERNAME
	     * + FileSystems.getDefault().getSeparator() + screenshotFileName + ".png"); try
	     * { FileUtils.copyFile(src, screenshotFile); } catch (IOException e) {
	     * ReportManager.log(e); }
	     */

	    addScreenshotToReport(src);
	}
    }

    private static File takeScreenshot(WebDriver driver) {
	if (SCREENSHOT_PARAMS_ISFULLPAGESCREENSHOT) {
	    try {
		if (SCREENSHOT_PARAMS_SKIPPEDELEMENTSFROMSCREENSHOT.length() > 0) {
		    List<WebElement> skippedElementsList = new ArrayList<>();
		    String[] skippedElementLocators = SCREENSHOT_PARAMS_SKIPPEDELEMENTSFROMSCREENSHOT.split(";");
		    for (String locator : skippedElementLocators) {
			skippedElementsList.add(driver.findElement(By.xpath(locator)));
		    }

		    WebElement[] skippedElementsArray = new WebElement[skippedElementsList.size()];
		    skippedElementsArray = skippedElementsList.toArray(skippedElementsArray);

		    return ScreenshotUtils.makeFullScreenshot(driver, skippedElementsArray);
		} else {
		    return ScreenshotUtils.makeFullScreenshot(driver);
		}
	    } catch (Exception e) {
		ReportManager.log(e);
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    }
	} else {
	    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}
    }

    private static void addScreenshotToReport(File screenshotFile) {
	/**
	 * Adding Screenshot to the Report.
	 * 
	 */
	try {
	    ReportManager.attach("Screenshot", screenshotFileName, new FileInputStream(screenshotFile));

	} catch (FileNotFoundException e) {
	    ReportManager.log(e);
	}
    }

    private static String highlightElementAndReturnDefaultStyle(WebElement element, JavascriptExecutor js, String highlightedElementStyle) {
	String regularElementStyle = element.getAttribute("style");
	if (regularElementStyle != null && !regularElementStyle.equals("")) {
	    js.executeScript("arguments[0].style.cssText = arguments[1];", element, regularElementStyle + highlightedElementStyle);
	} else {
	    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, highlightedElementStyle);
	}

	try {
	    JSWaiter.waitForLazyLoading();
	} catch (Exception e) {
	    ReportManager.log(e);
	}
	return regularElementStyle;
    }

    private static String setHighlightedElementStyle() {
	String highlightedElementStyle = "";
	if (globalPassFailStatus) {
	    highlightedElementStyle = "outline-offset:-3px !important; outline:3px solid #808080 !important; background:#46aad2 !important; background-color:#A5D2A5 !important; color:#000000 !important; -webkit-transition: none !important; -moz-transition: none !important; -o-transition: none !important; transition: none !important;";
	    // [incorta-blue: #46aad2] background-color:#A5D2A5
	} else {
	    highlightedElementStyle = "outline-offset:-3px !important; outline:3px solid #808080 !important; background:#FFFF99 !important; background-color:#FFFF99 !important; color:#000000 !important; -webkit-transition: none !important; -moz-transition: none !important; -o-transition: none !important; transition: none !important;";
	    // background-color:#ffff66
	}
	return highlightedElementStyle;
    }
}