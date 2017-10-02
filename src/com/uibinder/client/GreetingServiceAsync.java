package com.uibinder.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.uibinder.shared.Article;



/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void signup(String text, String text2, AsyncCallback<String> asyncCallback);

	void signin(String text, String text2, AsyncCallback<String> asyncCallback);
	
	void add_article(String author,String title,String image, String text, String link,String category,ArrayList<String> sub_tags,AsyncCallback<String> asyncCallback);

	void fetch_article(AsyncCallback<ArrayList<Article>> asyncCallback);
	
	void fetch_article(String category,AsyncCallback<ArrayList<Article>> asyncCallback);
	void manage_articles(String author,AsyncCallback<ArrayList<Article>> asyncCallback);
	void fetch_article_android(AsyncCallback<String> asyncCallback);
	
	void add_registration_ids(String token, AsyncCallback<String> asyncCallback);
	void token_is_valid(String token, AsyncCallback<String> asyncCallback);
	void  get_all_categories(AsyncCallback<ArrayList<String>> asyncCallback);
	void update_article(Article article,String present_time,String author,AsyncCallback<String> asyncCallback);
}
