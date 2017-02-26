package src.com.virustotal.tool;

import java.util.ArrayList;

import com.google.common.base.MoreObjects.ToStringHelper;

public class LinkPojo 
{
	
	
	ArrayList<String> ListOfAntivirus;
	int index;
	String link;
	
	
	public void  setListOfAntivirus( ArrayList<String> ListOfAntivirus )
	{
	 this.ListOfAntivirus=ListOfAntivirus;
	}
	public void  setIndex(int index )
	{
	 this.index=index;
	}
	public void  setLink(String link )
	{
	 this.link=link;
	}
	
	
	public ArrayList<String>  getListOfAntivirus( ArrayList<String> ListOfAntivirus )
	{
	 return ListOfAntivirus;
	}
	public int  getIndex(int index )
	{
	 return index;
	}
	public String  getLink(String link )
	{
	 return link;
	}
	
	@Override
	public String toString()
	{
		return "[ " + index + " " + link+ " "+ ListOfAntivirus +" ]" ;
		
	}
	
	
	
	
}
