package com.shaftEngine.validationsLibrary;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.shaftEngine.browserActionLibrary.BrowserActions;
import com.shaftEngine.elementActionLibrary.ElementActions;
import com.shaftEngine.ioActionLibrary.ReportManager;
import com.shaftEngine.ioActionLibrary.ScreenshotManager;

public class Assertions {

	private static int elementDoesntExist_timeout = 4;

	private static void fail(String message) {
		ReportManager.log(message);
		Assert.fail(message);
	}

	private static void fail(WebDriver driver, String message) {
		ScreenshotManager.captureScreenShot(driver, false);
		fail(message);
	}

	private static void fail(WebDriver driver, By elementLocator, String message) {
		ScreenshotManager.captureScreenShot(driver, elementLocator, false);
		fail(message);
	}

	private static void pass(String message) {
		ReportManager.log(message);
	}

	private static void pass(WebDriver driver, String message) {
		ScreenshotManager.captureScreenShot(driver, true);
		pass(message);
	}

	private static void pass(WebDriver driver, By elementLocator, String message) {
		ScreenshotManager.captureScreenShot(driver, elementLocator, true);
		pass(message);
	}

	/**
	 * Asserts that two objects are equal if AssertionType is true, or not equal if
	 * AssertionType is false.
	 * 
	 * @param expectedValue
	 *            the expected value (test data) of this assertion
	 * @param actualValue
	 *            the actual value (calculated data) of this assertion
	 * @param assertionType
	 *            either 'true' for a positive assertion that the objects are equal,
	 *            or 'false' for a negative assertion that the objects are not equal
	 */
	public static void assertEquals(Object expectedValue, Object actualValue, Boolean assertionType) {
		ReportManager.log("Assertion [" + "assertEquals" + "] is being performed, with expectedValue [" + expectedValue
				+ "], actualValue [" + actualValue + "], and assertionType [" + assertionType + "].");
		// String escapedExpectedValue = String.valueOf(expectedValue);
		// escapedExpectedValue =
		// escapeSpecialCharacters(String.valueOf(expectedValue));

		if (assertionType) {
			try {
				// Assert.assertEquals(expectedValue, actualValue);
				Assert.assertTrue((String.valueOf(actualValue)).matches(String.valueOf(expectedValue)));
				pass("Assertion Passed; actual value does match expected value [" + expectedValue + "].");
			} catch (AssertionError e) {
				fail("Assertion Failed; actual value [" + actualValue + "] does not match expected value ["
						+ expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		} else {
			try {
				// Assert.assertNotEquals(expectedValue, actualValue);
				Assert.assertFalse((String.valueOf(actualValue)).matches(String.valueOf(expectedValue)));
				pass("Assertion Passed; actual value [" + actualValue + "] does not match expected value ["
						+ expectedValue + "].");
			} catch (AssertionError e) {
				fail("Assertion Failed; actual value does match expected value [" + expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		}
	}

	/**
	 * Asserts that object is null if AssertionType is true, or is not null if
	 * AssertionType is false.
	 * 
	 * @param object
	 *            the object under test
	 * @param assertionType
	 *            either 'true' for a positive assertion that the object refers to
	 *            null, or 'false' for a negative assertion that the object doesn't
	 *            refer to null
	 */
	public static void assertNull(Object object, Boolean assertionType) {
		ReportManager.log("Assertion [" + "assertNull" + "] is being performed.");

		if (assertionType) {
			try {
				Assert.assertNull(object);
				pass("Assertion Passed; actual value is null.");
			} catch (AssertionError e) {
				fail("Assertion Failed; actual value is not null.");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		} else {
			try {
				Assert.assertNotNull(object);
				pass("Assertion Passed; actual value is not null.");
			} catch (AssertionError e) {
				fail("Assertion Failed; actual value is null.");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		}
	}

	/**
	 * Asserts that webElement found using the provided driver and locator exists if
	 * AssertionType is true, or does not exist if AssertionType is false.
	 * 
	 * @param driver
	 *            the current instance of Selenium webdriver
	 * @param elementLocator
	 *            the locator of the webElement under test (By xpath, id, selector,
	 *            name ...etc)
	 * @param assertionType
	 *            either 'true' for a positive assertion that the element exists, or
	 *            'false' for a negative assertion that the element doesn't exist
	 */
	public static void assertElementExists(WebDriver driver, By elementLocator, Boolean assertionType) {
		ReportManager.log("Assertion [" + "assertElementExists" + "] is being performed.");

		if (assertionType) {
			try {
				ElementActions.internalCanFindUniqueElement(driver, elementLocator);
				pass(driver, elementLocator,
						"Assertion Passed; element exists and is unique. Locator [" + elementLocator.toString() + "].");
			} catch (AssertionError e) {
				fail(driver, "Assertion Failed; element does not exist or is not unique. Locator ["
						+ elementLocator.toString() + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		} else {
			try {
				ElementActions.internalCanFindUniqueElement(driver, elementLocator, elementDoesntExist_timeout);
				fail(driver, elementLocator,
						"Assertion Failed; element exists and is unique. Locator [" + elementLocator.toString() + "].");
			} catch (AssertionError e) {
				pass(driver, "Assertion Passed; element does not exist or is not unique. Locator ["
						+ elementLocator.toString() + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		}
	}

	/**
	 * Asserts webElement attribute equals expectedValue if AssertionType is true,
	 * or does not equal expectedValue if AssertionType is false. Supports Text,
	 * TagName, Size, Other Attributes
	 * 
	 * @param driver
	 *            the current instance of Selenium webdriver
	 * @param elementLocator
	 *            the locator of the webElement under test (By xpath, id, selector,
	 *            name ...etc)
	 * @param elementAttribute
	 *            the desired attribute of the webElement under test
	 * @param expectedValue
	 *            the expected value (test data) of this assertion
	 * @param assertionType
	 *            either 'true' for a positive assertion that the element attribute
	 *            actual value matches the expected value, or 'false' for a negative
	 *            assertion that the element attribute actual value doesn't match
	 *            the expected value
	 */
	public static void assertElementAttribute(WebDriver driver, By elementLocator, String elementAttribute,
			String expectedValue, Boolean assertionType) {
		ReportManager.log("Assertion [" + "assertElementAttribute" + "] is being performed for target attribute ["
				+ elementAttribute + "].");
		// String escapedExpectedValue = expectedValue;
		// escapedExpectedValue = escapeSpecialCharacters(expectedValue);

		String actualValue = null;

		switch (elementAttribute.toLowerCase()) {
		case "text":
			actualValue = ElementActions.getText(driver, elementLocator);
			break;
		case "tagname":
			actualValue = ElementActions.getTagName(driver, elementLocator);
			break;
		case "size":
			actualValue = ElementActions.getSize(driver, elementLocator);
			break;
		default:
			actualValue = ElementActions.getAttribute(driver, elementLocator, elementAttribute);
			break;
		}

		if (assertionType) {
			try {
				Assert.assertTrue((String.valueOf(actualValue)).matches(String.valueOf(expectedValue)));
				pass(driver, elementLocator, "Assertion Passed; actual value of [" + elementAttribute
						+ "] does match expected value [" + expectedValue + "].");
			} catch (AssertionError e) {
				fail(driver, elementLocator, "Assertion Failed; actual value of [" + elementAttribute + "] equals ["
						+ actualValue + "] which does not match expected value [" + expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		} else {
			try {
				Assert.assertFalse((String.valueOf(actualValue)).matches(String.valueOf(expectedValue)));
				pass(driver, elementLocator, "Assertion Passed; actual value of [" + elementAttribute + "] equals ["
						+ actualValue + "] which does not match expected value [" + expectedValue + "].");
			} catch (AssertionError e) {
				fail(driver, elementLocator, "Assertion Failed; actual value of [" + elementAttribute
						+ "] does match expected value [" + expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		}
	}

	/**
	 * Asserts browser attribute equals expectedValue if AssertionType is true, or
	 * does not equal expectedValue if AssertionType is false. Supports CurrentUrl,
	 * PageSource, Title, WindowHandle, WindowPosition, WindowSize
	 * 
	 * @param driver
	 *            the current instance of Selenium webdriver
	 * @param browserAttribute
	 *            the desired attribute of the browser window under test
	 * @param expectedValue
	 *            the expected value (test data) of this assertion
	 * @param assertionType
	 *            either 'true' for a positive assertion that the browser attribute
	 *            actual value matches the expected value, or 'false' for a negative
	 *            assertion that the browser attribute actual value doesn't match
	 *            the expected value
	 */
	public static void assertBrowserAttribute(WebDriver driver, String browserAttribute, String expectedValue,
			Boolean assertionType) {

		ReportManager.log("Assertion [" + "assertBrowserAttribute" + "] is being performed for target attribute ["
				+ browserAttribute + "].");
		// String escapedExpectedValue = expectedValue;
		// escapedExpectedValue = escapeSpecialCharacters(expectedValue);

		String actualValue = null;

		switch (browserAttribute.toLowerCase()) {
		case "currenturl":
			actualValue = BrowserActions.getCurrentURL(driver);
			break;
		case "pagesource":
			actualValue = BrowserActions.getPageSource(driver);
			break;
		case "title":
			actualValue = BrowserActions.getCurrentWindowTitle(driver);
			break;
		case "windowhandle":
			actualValue = BrowserActions.getWindowHandle(driver);
			break;
		case "windowposition":
			actualValue = BrowserActions.getWindowPosition(driver);
			break;
		case "windowsize":
			actualValue = BrowserActions.getWindowSize(driver);
			break;
		default:
			actualValue = "";
			break;
		}

		if (assertionType) {
			try {
				Assert.assertTrue((String.valueOf(actualValue)).matches(String.valueOf(expectedValue)));
				pass(driver, "Assertion Passed; actual value of [" + browserAttribute + "] does match expected value ["
						+ expectedValue + "].");
			} catch (AssertionError e) {
				fail(driver, "Assertion Failed; actual value of [" + browserAttribute + "] equals [" + actualValue
						+ "] which does not match expected value [" + expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		} else {
			try {
				Assert.assertFalse((String.valueOf(actualValue)).matches(String.valueOf(expectedValue)));
				pass(driver, "Assertion Passed; actual value of [" + browserAttribute + "] equals [" + actualValue
						+ "] which does not match expected value [" + expectedValue + "].");
			} catch (AssertionError e) {
				fail(driver, "Assertion Failed; actual value of [" + browserAttribute + "] does match expected value ["
						+ expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		}
	}
	/*
	 * public static void assertFileExists(String fileFolderName, String fileName,
	 * Boolean assertionType) { if (assertionType) { try {
	 * FileManager.getAbsolutePath(fileFolderName, fileName);
	 * pass("Assertion Passed; a file was found in this directory [" +
	 * fileFolderName + "] with this name [" + fileName + "]."); } catch (Exception
	 * e) { fail("Assertion Failed; no file was found in this directory [" +
	 * fileFolderName + "] with this name [" + fileName + "]."); } } else { try {
	 * FileManager.getAbsolutePath(fileFolderName, fileName);
	 * fail("Assertion Failed; a file was found in this directory [" +
	 * fileFolderName + "] with this name [" + fileName + "]."); } catch (Exception
	 * e) { pass("Assertion Passed; no file was found in this directory [" +
	 * fileFolderName + "] with this name [" + fileName + "]."); }
	 * 
	 * } }
	 */

	public static void assertGreaterThanOrEquals(Number expectedValue, Number actualValue, Boolean assertionType) {
		ReportManager.log("Assertion [" + "assertGreaterThanOrEquals" + "] is being performed, with expectedValue ["
				+ expectedValue + "], actualValue [" + actualValue + "], and assertionType [" + assertionType + "].");

		if (assertionType) {
			try {
				Assert.assertTrue(actualValue.floatValue() >= expectedValue.floatValue());
				pass("Assertion Passed; actual value [" + actualValue + "] is greater than or equals expected value ["
						+ expectedValue + "].");
			} catch (AssertionError e) {
				fail("Assertion Failed; actual value [" + actualValue
						+ "] is not greater than or equals expected value [" + expectedValue + "].");
			}
		} else {
			try {
				Assert.assertFalse(actualValue.floatValue() >= expectedValue.floatValue());
				pass("Assertion Passed; actual value [" + actualValue
						+ "] is not greater than or equals expected value [" + expectedValue + "].");
			} catch (AssertionError e) {
				fail("Assertion Failed; actual value [" + actualValue + "] is greater than or equals expected value ["
						+ expectedValue + "].");
			} catch (Exception e) {
				ReportManager.log(e);
				fail("Assertion Failed; an unhandled exception occured.");
			}
		}
	}

}