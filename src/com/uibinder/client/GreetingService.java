package com.uibinder.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.shared.Article;


/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String signup(String text, String text2);
	String signin(String text, String text2);
	String add_article(String author,String title,String image, String text, String link,String category,ArrayList<String> sub_tags);
	ArrayList<Article> fetch_article();
	ArrayList<Article> fetch_article(String category);
	ArrayList<Article> manage_articles(String author);
	String fetch_article_android();
	String add_registration_ids(String token);
	String token_is_valid(String token);
	ArrayList<String> get_all_categories();
	String update_article(Article article,String present_time,String author);
}
