package com.uibinder.server;


import com.uibinder.client.GreetingService;
import com.uibinder.shared.Article;

import net.sf.json.JSON;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.HttpClientBuilder;

import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;


/**
 * The server-side implementation of the RPC service.
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	Database database=null;
	
	public void init(){
		try{
			//com.fourspaces.couchdb.Session session=new com.fourspaces.couchdb.Session(
				//"d9b157be-35d0-4886-af93-b0f3b8e03f0f-bluemix.cloudant.com/",
				//443,
				//"d9b157be-35d0-4886-af93-b0f3b8e03f0f-bluemix",
				//"f24fd5a5eccf0b81de145f429f7b2779403d5760f33b391e656ce5e55bf34b58"
				//);
			Session session=new Session("localhost",5984);
		database=session.getDatabase("new");
	}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Article> manage_articles(String author){
		init();
		Document doc;
	
		ArrayList<Article> articles=new ArrayList<Article>();
		try{
			doc=database.getDocument(author);
			net.sf.json.JSONObject object=new net.sf.json.JSONObject();
			object=doc.getJSONObject();
			Iterator<?> time_ids=object.keys();
			while(time_ids.hasNext()){
				
				Article article=new Article();
				String time_id=(String) time_ids.next();
				if(time_id.equals("_id")||time_id.equals("_rev")){
					continue;
				}
				net.sf.json.JSONObject article_jsonobject=(net.sf.json.JSONObject) object.get(time_id);
				article.setTitle((String)article_jsonobject.get("title"));
				article.setCategory((String)article_jsonobject.get("category"));
				article.setAuthor((String)article_jsonobject.get("author"));
				article.setLink((String)article_jsonobject.get("link of news"));
				article.setImage((String)article_jsonobject.get("link of image"));
				article.setMtext((String)article_jsonobject.get("text"));
				ArrayList<String> tags=new ArrayList<String>();
				for(int i=0;i<article_jsonobject.getJSONArray("Tags").size();i++){
					tags.add(article_jsonobject.getJSONArray("Tags").getString(i));
				}
				
				article.setTags(tags);
				article.setTime(time_id);
				articles.add(article);
			}
		}
		catch(Exception e){
			
		}
		return articles;
	}
	
	
	public String fetch_article_android(){
		init();
		net.sf.json.JSONObject object=new net.sf.json.JSONObject();
		Document doc;
		try{
			doc = database.getDocument("newsentry");
			object=doc.getJSONObject();
			}
			catch(Exception e){

			e.printStackTrace();
		}
		return object.toString();
	}
	
	

	@Override
	public String signup(String username, String password) {
		// TODO Auto-generated method stub
		init();
		String s="";
		try{

		Document doc=database.getDocument("newDoc");
		Document author_doc=new Document();
		author_doc.put("_id", username);
		doc.put(username, BCrypt.hashpw(password,BCrypt.gensalt()));
		database.saveDocument(doc, "newDoc");
		s=doc.toString();
		String token_id=createSessionIdForUser();
		Document token=database.getDocument("session");
		token.put(token_id,username);
		database.saveDocument(token);
		database.saveDocument(author_doc);
		
		}
		catch(Exception e){
		}
		
		return s;
	}

	public String signin(String username, String password) {
		// TODO Auto-generated method stub
		init();
		String s="Not present";
		try{
		Document doc=database.getDocument("newDoc");
		if(doc.containsKey(username)){
			String retrievedPassword_hash=doc.get(username).toString();
			boolean valid=BCrypt.checkpw(password, retrievedPassword_hash);
			System.out.println(doc.get(username.toString()));
			if(valid)
			
				s=createSessionIdForUser();
				Document token=database.getDocument("session");
				token.put(s,username);
				database.saveDocument(token);
				
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return s;
	}

	String createSessionIdForUser(){
		String jSessionId=this.getThreadLocalRequest().getSession(true).getId();
		return jSessionId;
	}
	
	public String add_article(String author,String title,String image, String text, String link,String category,ArrayList<String> sub_tags)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		init();
		
		Document doc,author_doc;
		try {
			author_doc=database.getDocument(author);
			doc = database.getDocument("newsentry");
			JSONObject newsentry = new JSONObject();
			newsentry.put("author",author);
			newsentry.put("title", title);		
			newsentry.put("link of image",image);
			newsentry.put("text", text);
			newsentry.put("link of news",link);
			newsentry.put("Tags",sub_tags);
			newsentry.put("category", category);
			long time = new Date().getTime();
			String articleid=String.valueOf(time);
			sendNotification(title,image,text,link,category,sub_tags,articleid);
			JSONObject article=new JSONObject();
			article.put(articleid, newsentry);
			if(!doc.containsKey(category)){
				doc.put(category, article);
			}
			else{
				net.sf.json.JSONObject old_category=doc.getJSONObject(category);
				old_category.put(articleid,newsentry);
				doc.put(category,old_category);	
			}
			author_doc.put(articleid,newsentry);
			database.saveDocument(author_doc);
			database.saveDocument(doc);
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return "data entered";
	 
	
	
	}
	
	public String add_registration_ids(String token){
		init();
		Document register;
		try{
			register=database.getDocument("register");
			long time = new Date().getTime();
			String time_stamp=String.valueOf(time);
			register.put(time_stamp, token);
			database.saveDocument(register);
			return (String) register.get(time_stamp);
		}
		catch(Exception e){
			return "failure";
		}
		
	}
	
	
	public void sendNotification(String title,String image, String text, String link,String category,ArrayList<String> sub_tags,String time)
	{
		// Declaration of Message Parameters
	    String message_url = new String("https://fcm.googleapis.com/fcm/send");
	    String message_key = new String("key=AIzaSyAj8EehZECKtIZuDT8RkZbAXkyGMvpy3yw");
	    net.sf.json.JSONArray registration_ids=new net.sf.json.JSONArray();

	    // Generating a JSONObject for the content of the message
	    JSONObject message = new JSONObject();
	    JSONObject notification = new JSONObject();
	    message.put("title", title);
	    message.put("image", image);
	    message.put("text",text);
	    message.put("link",link);
	    message.put("category",category);
	    message.put("tags",sub_tags);
	    message.put("time",time);
	    //notification.put("body",text);
	    //notification.put("title", title);
	    //notification.put("icon", "appicon");
	    //notification.put("click_action",".open_notification");
	    JSONObject protocol = new JSONObject();
	    try {
			Document register=database.getDocument("register");
			net.sf.json.JSONObject object=register.getJSONObject();
			Iterator<?> time_stamps=object.keys();
			while(time_stamps.hasNext()){
				String time_stamp=(String)time_stamps.next();
				if(time_stamp.equals("_id")||time_stamp.equals("_rev")){
					continue;
				}
				String token=(String) object.get(time_stamp);		
				registration_ids.add(token);	
			}
		
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    
	    protocol.put("registration_ids",registration_ids);
	    protocol.put("data", message);
	    //protocol.put("notification",notification);

	    // Send Protocol
	    try {
	    	
	        HttpClient httpClient = HttpClientBuilder.create().build();

	        HttpPost request = new HttpPost(message_url);
	        request.addHeader("content-type", "application/json");
	        request.addHeader("Authorization", message_key);

	        StringEntity params = new StringEntity(protocol.toString());
	        request.setEntity(params);
	        System.out.println(params);

	        HttpResponse response = httpClient.execute(request);
	        System.out.println(response.toString());
	    } catch (Exception e) {
	    }

	}
	
	
	public ArrayList<Article> fetch_article(){
		Document doc;
		init();
		ArrayList<Article> article_list=new ArrayList<Article>();
		try{
			doc = database.getDocument("newsentry");
			net.sf.json.JSONObject object=doc.getJSONObject();
			Iterator<?> categories=object.keys();
			while(categories.hasNext()){
				String category=(String)categories.next();
				if(category.equals("_id")||category.equals("_rev")){
					continue;
				}
				net.sf.json.JSONObject category_object=doc.getJSONObject(category);
				Iterator<?> time_ids=category_object.keys();
				while(time_ids.hasNext()){
					Article article=new Article();
					String time_id=(String) time_ids.next();
					net.sf.json.JSONObject article_jsonobject=(net.sf.json.JSONObject) category_object.get(time_id);
					if(article_jsonobject.get("title")!=null){
						article.setTitle((String)article_jsonobject.get("title"));
					}
					else{
						article.setTitle("No Title Provided");
					}
					article.setCategory(category);
					article.setAuthor((String)article_jsonobject.get("author"));
					article.setLink((String)article_jsonobject.get("link of news"));
					article.setImage((String)article_jsonobject.get("link of image"));
					article.setMtext((String)article_jsonobject.get("text"));
					ArrayList<String> tags=new ArrayList<String>();
					for(int i=0;i<article_jsonobject.getJSONArray("Tags").size();i++){
						tags.add(article_jsonobject.getJSONArray("Tags").getString(i));
					}
					
					article.setTags(tags);
					article.setTime(time_id);
					article_list.add(article);
				}
				
			}
			
		}catch(Exception e){

			e.printStackTrace();
			
		}
		return article_list;
	
		
	}
	
	
	public ArrayList<Article> fetch_article(String category){
		Document doc;
		init();
		ArrayList<Article> article_list=new ArrayList<Article>();
		try{
			doc = database.getDocument("newsentry");
			
			net.sf.json.JSONObject category_object=doc.getJSONObject(category);
			Iterator<?> time_ids=category_object.keys();
			while(time_ids.hasNext()){
				Article article=new Article();
				String time_id=(String) time_ids.next();
				net.sf.json.JSONObject article_jsonobject=(net.sf.json.JSONObject) category_object.get(time_id);
				if(article_jsonobject.get("title")!=null){
					article.setTitle((String)article_jsonobject.get("title"));
				}
				else{
					article.setTitle("No Title Provided");
				}
				article.setCategory(category);
				article.setAuthor((String)article_jsonobject.get("author"));
				article.setLink((String)article_jsonobject.get("link of news"));
				article.setImage((String)article_jsonobject.get("link of image"));
				article.setMtext((String)article_jsonobject.get("text"));
				ArrayList<String> tags=new ArrayList<String>();
				for(int i=0;i<article_jsonobject.getJSONArray("Tags").size();i++){
					tags.add(article_jsonobject.getJSONArray("Tags").getString(i));
				}
				
				article.setTags(tags);
				article.setTime(time_id);
				article_list.add(article);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return article_list;
		
	}
	
	public String token_is_valid(String token){
		Document tokendb;
		init();
		try {
			tokendb=database.getDocument("session");
			if(tokendb.containsKey(token)){
				String user_details=tokendb.getString(token);
				System.out.println("user details server:"+user_details+"token"+token);
				return user_details;
			}
			else return "user not valid";
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "user not valid";
		}
		
		
	}

	@Override
	public String update_article(Article article, String present_time,String author) {
		init();
		Document doc;
		try{
			doc=database.getDocument(author);
			net.sf.json.JSONObject newsentry=new net.sf.json.JSONObject();
			newsentry=doc.getJSONObject(present_time);
			doc.remove(present_time);
			newsentry.put("author",author);
			newsentry.put("title", article.getTitle());		
			newsentry.put("link of image",article.getImage());
			newsentry.put("text", article.getMtext());
			newsentry.put("link of news",article.getLink());
			newsentry.put("Tags",article.getTags());
			newsentry.put("category", article.getCategory());
			doc.put(article.getTime(), newsentry);
			database.saveDocument(doc);
			removeFromMainDocument(newsentry,article,present_time);
		}
		catch(Exception e){
			
		}
		// TODO Auto-generated method stub
		return "Updated";
	}
	
	public void removeFromMainDocument(net.sf.json.JSONObject newsentry,Article article,String present_time){
		Document doc;
		try{
			doc=database.getDocument("newsentry");
			net.sf.json.JSONObject old_articles=new net.sf.json.JSONObject();
			old_articles=doc.getJSONObject(article.getCategory());
			old_articles.remove(present_time);
			old_articles.put(article.getTime(), newsentry);
			doc.put(article.getCategory(),old_articles);
			database.saveDocument(doc);
		}
		catch(Exception e){
			
		}
	}
	
	public ArrayList<String> get_all_categories(){
		init();
		Document doc;
		ArrayList<String> categoryList=new ArrayList<String>();
		try {
			doc = database.getDocument("newsentry");
			net.sf.json.JSONObject object=doc.getJSONObject();
			Iterator<?> categories=object.keys();
			while(categories.hasNext()){
				String category=(String)categories.next();
				if(category.equals("_id")||category.equals("_rev")){
					continue;
				}
				categoryList.add(category);
				
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryList;
		
		
	}
	
}
