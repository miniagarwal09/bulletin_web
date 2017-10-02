package com.uibinder.client;



import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Uibinder implements EntryPoint {
	
	final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);
	
	static ArrayList<String> categoryList=new ArrayList<String>();

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
	
	if(sessionMatch()){
		System.out.println("session matched");
		display_dashboard();
		

	}
	else{
		System.out.println("session not matched");
			display_signup();
	}
	
	}

	boolean sessionMatch(){
		String server_session_id=Cookies.getCookie("sid");
		if(tokenIsValid(server_session_id)){
			if(server_session_id!=null){
				return true;
			}
			return false;
		}
		else{
			return false;
		}
	}
	
	
	void display_signup(){
		RootPanel.get("signin").add(new signin());
		RootPanel.get("signup").add(new signup());
		RootPanel.get("signup").setVisible(false);
		RootPanel.get("dash").add(new Main());
		RootPanel.get("dash").setVisible(false);
		RootPanel.get("content").add(new add_article());
		RootPanel.get("content").setVisible(false);
		RootPanel.get("display_articles").setVisible(false);
		RootPanel.get("fetch_articles").add(new FetchArticles());
		RootPanel.get("fetch_articles").setVisible(false);
		
	}
	void display_dashboard(){
		greetingService.get_all_categories(new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				// TODO Auto-generated method stub
				categoryList=result;
				//Window.alert(categoryList.get(0));
				
				
			}
		});
		RootPanel.get("signin").add(new signin());
		RootPanel.get("signup").add(new signup());
		RootPanel.get("signup").setVisible(false);
		RootPanel.get("signin").setVisible(false);
		RootPanel.get("dash").add(new Main());
		RootPanel.get("content").add(new add_article());
		RootPanel.get("content").setVisible(false);
		RootPanel.get("display_articles").setVisible(false);
		RootPanel.get("fetch_articles").add(new FetchArticles());
		RootPanel.get("fetch_articles").setVisible(false);
		
	}
	
	boolean tokenIsValid(String s){
		greetingService.token_is_valid(s,new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(!result.equals("user not valid")){
					System.out.println("uid"+result);
					Cookies.removeCookie("uid");
					Cookies.setCookie("uid", result);
				}
				
			}
		});
		if(Cookies.getCookie("uid")!=null){
			System.out.println("uid"+Cookies.getCookie("uid"));
			return true;
		}
		else{
			return false;
		}

		
	}

}
