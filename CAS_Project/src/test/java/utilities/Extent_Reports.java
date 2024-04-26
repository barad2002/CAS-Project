package utilities;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.ITestContext;

import org.testng.ITestListener;

import org.testng.ITestResult;
 
import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;

import com.aventstack.extentreports.Status;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import com.aventstack.extentreports.reporter.configuration.Theme;

import TestBase.BaseClass;


 
public class Extent_Reports implements ITestListener {

	public ExtentSparkReporter sparkReporter; //UI of the report

	public ExtentReports extentReports;  //Common information on the report

	public ExtentTest extentTest; //creating test case entries in the report and update status of the test reports

	public void onStart(ITestContext context) {

		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+".\\Extent_Reports\\My_CAS_Report.html");

		sparkReporter.config().setDocumentTitle("Automation Report"); //Title of the report

		sparkReporter.config().setReportName("Functional Testing"); //Name of the report

		sparkReporter.config().setTheme(Theme.DARK); 

		extentReports = new ExtentReports();

		extentReports.attachReporter(sparkReporter);

		extentReports.setSystemInfo("Computer Name", "Lenovo");

		extentReports.setSystemInfo("Environment", "QEA");

		extentReports.setSystemInfo("Tester Name", "Baradwaj E");

		extentReports.setSystemInfo("OS", "Windows11");

		extentReports.setSystemInfo("Browser Name", "Chrome, Edge");
		List<String> includedGroups = context.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()) {
		extentReports.setSystemInfo("Groups", includedGroups.toString());
		}

	}

	//If test case passed
	public void onTestSuccess(ITestResult result) {
		
		extentTest = extentReports.createTest(result.getName());
		
		extentTest.assignCategory(result.getMethod().getGroups());

		extentTest.log(Status.PASS, "PASSED Test Case is : "+result.getName());
	
		try {
			
			String imgPath = BaseClass.captureScreen(result.getName());
			
			extentTest.addScreenCaptureFromPath(imgPath);
			
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		

	}

	//If test case failed
	public void onTestFailure(ITestResult result) {

		extentTest = extentReports.createTest(result.getName());
		extentTest.assignCategory(result.getMethod().getGroups());

		extentTest.log(Status.FAIL, "Test case FAILED is : "+result.getName());

		extentTest.log(Status.INFO, "Test case FAILED due to : "+result.getThrowable().getMessage());

		try {
			
			String imgPath = BaseClass.captureScreen(result.getName());
			
			extentTest.addScreenCaptureFromPath(imgPath);
			
		} catch (Exception e1) 
		{
			e1.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {

		extentTest = extentReports.createTest(result.getName());
		extentTest.assignCategory(result.getMethod().getGroups());

		extentTest.log(Status.SKIP, "Test case SKIPPED is : "+result.getName());

		extentTest.log(Status.INFO, "Test case SKIPPED due to : "+result.getThrowable().getMessage());

	}

	public void onFinish(ITestContext context) {

		extentReports.flush();
		String path = System.getProperty("user.dir")+".\\Extent_Reports\\My_CAS_Report.html";
		File extentReport = new File(path);
		try {
			Desktop.getDesktop().browse(extentReport.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}

		

	}

}

