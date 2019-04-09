package rest_web_services;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@code @XmlRootElement} defines that this object would be converted to xml
 * element from in-memory java object that would be used to send as a response
 * to webservice.
 * 
 * @author Ruba
 *
 */
@XmlRootElement(name = "adage")
public class Adage {
	private String words;
	private int wordCount;
	private int id;

	public Adage() {
	}

	@Override
	public String toString() {
		return String.format("%2d: ", id) + words + " -- " + wordCount + " words";
	}

	public void setWords(String words) {
		this.words = words;
		this.wordCount = words.trim().split("\\s+").length;
	}

	public String getWords() {
		return this.words;
	}

	public void setWordCount(int wordCount) {
	}

	public int getWordCount() {
		return this.wordCount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}