package com.uibinder.shared;



import java.util.ArrayList;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Article implements IsSerializable{
	

	/**
	 * 
	 */

	String mtext;
	String image;
	String link;
	ArrayList<String> tags;
	String Category;
	String title;
	String time;
	String author;
	
	
	public String getTime() {
		return time;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public Article(){
		mtext="";
		image="";
		link="";
		tags=new ArrayList<String>();
		Category="";
		title="";
		time="";
		
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMtext() {
		return mtext;
	}
	public void setMtext(String mtext) {
		this.mtext = mtext;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	@Override
	public String toString() {
		return "Article [mtext=" + mtext + ", image=" + image + ", link=" + link + ", tags=" + tags + ", Category="
				+ Category + ", title=" + title + ", time=" + time + "]";
	}



	

}
