package memorylog;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
public class MemlogFile {

	private ArrayList<Item> entries;

	public MemlogFile() {
		entries = null;
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
	@XmlElementWrapper(name="entries")
	@XmlElement(name="entry")
	public void setEntries(ArrayList<Item> entries) {
		this.entries = entries;
	}
}

