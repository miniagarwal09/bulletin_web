package com.uibinder.client;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.shared.Article;

import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.animate.MaterialAnimator;
import gwt.material.design.client.ui.animate.Transition;

public class Main extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);
	final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);


	@UiField
	MaterialLink addArticle;
	@UiField 
	MaterialLink fetchArticle;
	@UiField 
	MaterialLink logout;
	@UiField 
	MaterialLink manage,btnSearch;
	@UiField
	MaterialColumn add;
	@UiField
	MaterialColumn fetch;
	@UiField 
	MaterialColumn display;
	@UiField
	MaterialRow rowCards;
	@UiField 
	MaterialSearch txtSearch;
	@UiField
	MaterialNavBar navBar,navBarSearch;

	

	interface MainUiBinder extends UiBinder<Widget, Main> {
	}

	@UiHandler("btnSearch")
	 void onSearch(ClickEvent e){
	 txtSearch.open();
	 }
	
	
	
	public Main() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		txtSearch.addOpenHandler(openEvent -> {
			 navBar.setVisible(false);
			 navBarSearch.setVisible(true);
			 });
		
		txtSearch.addCloseHandler(new CloseHandler<String>() {
			
			@Override
			public void onClose(com.google.gwt.event.logical.shared.CloseEvent<String> event) {
				// TODO Auto-generated method stub
				navBar.setVisible(true);
				navBarSearch.setVisible(false);	
				}
			 });
	
		rowCards.add(RootPanel.get("display_articles"));
		
		logout.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Cookies.removeCookie("sid");
				Cookies.removeCookie("uid");
				RootPanel.get("dash").setVisible(false);
				RootPanel.get("content").setVisible(false);
				RootPanel.get("fetch_articles").setVisible(false);
				RootPanel.get("display_articles").setVisible(false);
				RootPanel.get("signin").setVisible(true);
				RootPanel.get("signup").setVisible(false);
			}
		});
		
		manage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				greetingService.manage_articles(Cookies.getCookie("uid"), new AsyncCallback<ArrayList<Article>>() {
					
					@Override
					public void onSuccess(ArrayList<Article> result) {
						// TODO Auto-generated method stub
						Collections.sort(result,new Comparator<Article>(){

							@Override
							public int compare(Article o1, Article o2) {
								// TODO Auto-generated method stub
								if(Long.parseLong(o1.getTime())>Long.parseLong(o2.getTime())){
									return -1;
								}
								else{
									return 1;
								}
							}
						});
						String articles="";
						//display.add(RootPanel.get("display_articles"));
						RootPanel.get("display_articles").setVisible(true);
						RootPanel.get("display_articles").clear();
						RootPanel.get("content").setVisible(false);
						RootPanel.get("fetch_articles").setVisible(false);
					
						for(Article article : result){
							articles+=article.toString()+"\n";
							RootPanel.get("display_articles").add(new Cards(article,"manage"));
						}
						Window.alert(articles);
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				add.add(RootPanel.get("display_articles"));
				RootPanel.get("display_articles").setVisible(true);
			}
		});
		
		
		fetchArticle.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent e){
					fetch.add(RootPanel.get("fetch_articles"));
					RootPanel.get("content").setVisible(false);
					RootPanel.get("display_articles").setVisible(false);
					RootPanel.get("fetch_articles").setVisible(true);
					
				
				
				
				
			}
			
		});
		
		addArticle.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub


					add.add(RootPanel.get("content"));
					RootPanel.get("content").setVisible(true);
					RootPanel.get("display_articles").setVisible(false);	
					RootPanel.get("fetch_articles").setVisible(false);	
					
					
			}		
		});
		

	}


}
