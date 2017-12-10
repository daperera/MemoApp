package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

public class MemoType {
	
	// the name of the attributes of this memo
	private HashSet<String> attributeName;
	
	/*
	 * This variable stores the type of the different attributes
	 * This type is either a string, or an other memoType
	 * @value : "nameOfTheMemo" when the attribute refers to an other memo
	 * 			"string" otherwise 
	 */
	private HashSet<String> attributeType; 
	
	// the name the memo Type
	private String name; 
	
	/*
	 *  the locations of the html files describing the interfaces through 
	 *  which the user can view and modify this type of memos
	 */
	private HashSet<String> htmlFileLocation;
	
	// the location of the file where we store and retrieve data concerning this type of memo
	private String infoFileLocation;
	
	public void MemoType() {
		loadFromFile();
		computeFileLocation();
	}
	
	public void modifyName(String name) {
		this.name = name;
		computeFileLocation();
	}
	
	public void addAttribute(String name) {
		attributeName.add(name);
	}
	
	public void removeAttribute(String name) {
		attributeName.remove(name);
	}
	
	public void renameAttribute(String oldName, String newName) {
		attributeName.remove(oldName);
		attributeName.add(newName);
	}
	
	public void addHtmlFileLocation(String location) {
		htmlFileLocation.add(location);
	}
	
	public void removeHtmlFileLocation(String location) {
		htmlFileLocation.remove(location);
	}
	
	public void saveToFile() {
		try {
			ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(infoFileLocation));
			oos.writeObject(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadFromFile() {
		try {
			ObjectInputStream ois= new ObjectInputStream(new FileInputStream(infoFileLocation));
			this = ois.readObject();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void computeFileLocation() {
		
	}
	
}
