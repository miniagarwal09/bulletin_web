package com.uibinder.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.collections.comparators.ComparatorChain;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.shared.Article;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;

public class FetchArticles extends Composite {
	



	private static FetchArticlesUiBinder uiBinder = GWT.create(FetchArticlesUiBinder.class);

	interface FetchArticlesUiBinder extends UiBinder<Widget, FetchArticles> {
	}
	final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);
	/*
	 @UiTemplate("Main.ui.xml")
	 interface MainBinder extends UiBinder<Widget, FetchArticles> {}
	 private static MainBinder mainBinder = GWT.create(MainBinder.class);
	
	@UiField
	MaterialColumn display;
	*/
	@UiField 
	MaterialButton fetch_article_in_category;
	@UiField
	MaterialButton fetch;

	@UiField MaterialTitle category;
	@UiField
	MaterialComboBox<String> category_to_fetch;
	
	ArrayList<String> categoryList;

	public FetchArticles() {
		initWidget(uiBinder.createAndBindUi(this));
		
		category_to_fetch.addOpenHandler(new OpenHandler<String>() {
			
			@Override
			public void onOpen(OpenEvent<String> event) {
				// TODO Auto-generated method stub
				Collection<String> collection=new ArrayList<String>();
				categoryList=Uibinder.categoryList;
				for(String category:categoryList){
				collection.add(category);
				}
				category_to_fetch.setItems(collection);
				
			}
		});
		fetch_article_in_category.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				greetingService.fetch_article(category_to_fetch.getSingleValue(),new AsyncCallback<ArrayList<Article>>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

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
						//category.setTitle(category_to_fetch.getSingleValue());
						MaterialTitle materialTitle=new MaterialTitle(category_to_fetch.getSingleValue());
					//	materialTitle.setPaddingTop(60.0);
						materialTitle.setTextColor(Color.RED);
						RootPanel.get("display_articles").add(materialTitle);
						for(Article article : result){
							//articles+=article.toString()+"\n";
							Cards card=new Cards(article,"default");
							
							RootPanel.get("display_articles").add(card);
						}
							
					//	Window.alert(articles);
						
					}
					
				});
				
			}
			
		});
		
	fetch.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				greetingService.fetch_article(new AsyncCallback<ArrayList<Article>>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

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
						RootPanel.get("display_articles").setVisible(true);
						RootPanel.get("display_articles").clear();
						RootPanel.get("content").setVisible(false);
						RootPanel.get("fetch_articles").setVisible(false);
						for(Article article : result){
							//articles+=article.toString()+"\n";
							RootPanel.get("display_articles").add(new Cards(article,"default"));
						}
							
					//	Window.alert(articles);
						
					}
					
				});
			}
		});
	}

}
