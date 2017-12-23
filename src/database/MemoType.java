package database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import processing.Config;

class MemoType {
	
	// the name of the attributes of this memo
	private final HashSet<String> attributeName;
	
	/*
	 * This variable stores the type of the different attributes
	 * This type is either a string, or an other memoType
	 * @value : "nameOfTheMemo" when the attribute refers to an other memo
	 * 			"string" otherwise 
	 */
	private final HashSet<String> attributeType; 
	
	// the name the memo Type
	private String name; 
	
	/*
	 *  the locations of the html files describing the interfaces through 
	 *  which the user can view and modify this type of memos
	 */
	private final  HashSet<String> htmlFileLocation;
	
	// the location of the file where we store and load this object
	private String fileLocation;
	
	
	// call this constructor when this type of memo has not been created yet. Otherwise, call 
	// the loadFromFile function
	public MemoType(String name) {
		this.name = name;
		computeFileLocation();
		attributeName = new HashSet<String>();
		attributeType = new HashSet<String>();
		htmlFileLocation = new HashSet<String>();
	}
	
	public void rename(String name) {
		this.name = name;
		computeFileLocation();
	}
	
	public void addAttribute(String name, String attributeType) {
		attributeName.add(name);
		this.attributeType.add(attributeType);
	}
	
	public void removeAttribute(String name) {
		attributeName.remove(name);
	}
	
	public void renameAttribute(String oldName, String newName) {
		attributeName.remove(oldName);
		attributeName.add(newName);
	}
	
	public void changeAttributeType(String oldType, String newType) {
		attributeType.remove(oldType);
		attributeType.remove(newType);
	}
	
	public void addHtmlFileLocation(String location) {
		htmlFileLocation.add(location);
	}
	
	public void removeHtmlFileLocation(String location) {
		htmlFileLocation.remove(location);
	}
	
	public void saveToFile() {
		try {
			ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(fileLocation));
			oos.writeObject(this);
			oos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//function called to obtain a memoType object corresponding of a type of memo which has already been created
	public static MemoType loadFromFile(String name) throws IOException {
		String infoFileLocation = retrieveFileLocation(name); 
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(infoFileLocation));
		try {
			MemoType memoType = (MemoType) ois.readObject();
			ois.close();
			return memoType;
		} catch (ClassNotFoundException e) {
			ois.close();
			throw new IOException(e.getMessage());
		}
	}
	
	private static String retrieveFileLocation(String name) {
		String directoryLocation = Config.getUserDirectoryLocation();
		String relativePath = "/" + name + "/" + ".type";
		return directoryLocation + relativePath;
	}
	
	private void computeFileLocation() {
		fileLocation = retrieveFileLocation(name);
	}
	
}
