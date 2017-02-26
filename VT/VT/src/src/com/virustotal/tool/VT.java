package src.com.virustotal.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.Wait;

/**
 * 
 * @author abhijeet.jain
 *
 */
public class VT {
   
    

   
    /**
     * 
     * @param args
     */
    
    
    public static void main(String[] args) 
    {
	ArrayList<LinkPojo>  listOfresult= new ArrayList<>();
	
	String [] searchList= {"Fortinet",
							"Google Safebrowsing",
							"BitDefender",
							"Sophos",
							"Kaspersky",
							"Avira (no cloud)"};
	String  path="";
	try{

	    System.setProperty("webdriver.chrome.driver", "E:\\Selenium\\chromedriver.exe");

	    WebDriver driver = new ChromeDriver();

	    Properties prop = new Properties();
	    prop.load(new FileInputStream("Project.properties"));

	    path= prop.getProperty("directory");

	    File directory= new File(path);
	    String [] file=directory.list();
	    int count=0;

	    for (String filePath : file)
	    {
		System.out.println(filePath);
		List<String> url = FileUtils.readLines(new File(path+filePath));
		for (String link : url)
		{
		    if("".equals(link))
		    {
			continue;
		    }
		    else
		    {		
			WebDriverWait wait = new WebDriverWait(driver,30);
			driver.get("https://www.virustotal.com/");
			driver.manage().window().maximize();
			driver.findElement(By.id("url-tab-chooser")).click();
			WebElement element = driver.findElement(By.xpath("//input[@id='url']"));
			element.click();
			
			LinkPojo testLink= new LinkPojo();
			
			testLink.setLink(link);
			testLink.setIndex(++count);
			
			element.sendKeys(link);
			element.sendKeys(Keys.RETURN);
			element=null;
			try
			{
			    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='btn-url-reanalyse']")));
			    element=driver.findElement(By.xpath("//a[@id='btn-url-reanalyse']"));
			}
			catch(Exception e){ 
			    
			    System.out.println("not reanalysing link : " + link + "[ error ] "+e.getCause());
			    
			    continue;
			}


			if (element!=null)
			{
			    element.click();
			}


	
			listOfresult.add( getresult(testLink ,searchList, driver, wait) );
			
			
			//System.out.println(listOfresult);
			
			driver.findElement(By.xpath("//html/body/div[1]/div[4]/div[1]/div/a/img")).click();



		    }
		}
	    }

	} catch(IOException e) 
	{
	    System.out.println( e.getCause() );
	}
	
	writeIntoExcel( listOfresult, path );
    }

    private static LinkPojo getresult(LinkPojo linkPojo, String[] searchList,WebDriver driver, WebDriverWait wait) 
    {
    	linkPojo.ListOfAntivirus= new ArrayList<String>();
		for (String s:searchList)
		{    
			WebElement element= null;
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//TD[text()='"+s+"']/parent::tr/td[2]")));
			
			element= driver.findElement(By.xpath("//TD[text()='"+s+"']/parent::tr/td[2]"));
			
			if ("text-red".equals(element.getAttribute("class")))
			{
				
				linkPojo.ListOfAntivirus.add(s);
			}
			
			
			 
			
		}
    	
    	System.out.println(linkPojo);
		return linkPojo;
	}

	static void writeIntoExcel(ArrayList <LinkPojo> list, String path)
    {
	FileOutputStream out=null;
	try
	{

	    out = new FileOutputStream(new File( path + "Report.xls"));

	} 
	catch (FileNotFoundException e) 
	{
	    e.printStackTrace();
	}

	HSSFWorkbook workbook = new HSSFWorkbook();

	HSSFSheet sheet= workbook.createSheet("Malicious");
	
	sheet.setColumnWidth(0, 200);

	HSSFSheet sheet2= workbook.createSheet("Clean");

	HSSFRow row= sheet.createRow(0);
	HSSFRow row2= sheet2.createRow(0);

	HSSFCell cellzero=row.createCell(0);
	cellzero.setCellValue("Malicious URL's ");
	
	
	row.createCell(1).setCellValue("Detected By ");
	
	row2.createCell(0).setCellValue("Clean URL's ");
	
	
	
	int maliciousCount=0,cleanCount =0;

	for(int i=1;i<=list.size();i++)
    	{
		 if(!list.get(i-1).ListOfAntivirus.isEmpty())
		 {
    	    row=sheet.createRow(++maliciousCount);
    
    	    HSSFCell cell1=row.createCell(0);
            HSSFCell cell2=row.createCell(1);
    	    
            cell1.setCellValue(list.get(i-1).link);
    	    cell2.setCellValue(list.get(i-1).ListOfAntivirus.toString());
		 }
		 else
		 {
			 row2=sheet2.createRow(++cleanCount);
			 HSSFCell cell1=row2.createCell(0);
			 cell1.setCellValue(list.get(i-1).link);
				 
		 }
    	}

	try
	{
	    workbook.write(out);
	    out.close();
	} 
	
	catch (IOException | NullPointerException e)
	{
	    
	    System.out.println("File is not writen in excel"+ e.getCause());
	}
    }

}




