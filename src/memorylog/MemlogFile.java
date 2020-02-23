package memorylog;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
public class MemlogFile {

	private ArrayList<Item> entries;
	private String fileName;

	public MemlogFile() {
		entries = null;
		fileName = null;
	}

	//****************************************************************************************
	// getter for entries
	//****************************************************************************************
	public ArrayList<Item> getEntries() {
		return entries;
	}

	//****************************************************************************************
	// setter for entries
	//****************************************************************************************
	@XmlElement
	public void setEntries(ArrayList<Item> entries) {
		this.entries = entries;
	}

	//****************************************************************************************
	// getter for fileName
	//****************************************************************************************
	public String getFileName() {
		return fileName;
	}

	//****************************************************************************************
	// setter for fileName
	//****************************************************************************************
	@XmlElement
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

