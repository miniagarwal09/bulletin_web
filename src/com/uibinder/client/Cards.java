package com.uibinder.client;




import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.shared.Article;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardTitle;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTitle;

public class Cards extends Composite {

	private static CardsUiBinder uiBinder = GWT.create(CardsUiBinder.class);
	
	@UiField MaterialCardTitle title;
	@UiField MaterialCard card;

	@UiField MaterialImage image;
	@UiField MaterialLink link;
	@UiField MaterialLabel text;
	@UiField MaterialButton edit;

	interface CardsUiBinder extends UiBinder<Widget, Cards> {
	}

	public Cards() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@UiConstructor
	public Cards(Article article,String type) {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(article.getTitle());
		System.out.println("Image Name:"+article.getImage());
		
		image.setUrl(article.getImage());
		link.setHref(article.getLink());
		text.setText(article.getMtext());
		//image.setHeight(String.valueOf(card.getWidth()));
		//category_title.setTitle(article.getCategory());
		if(type.equals("default") && !article.getAuthor().equals(Cookies.getCookie("uid"))){
				edit.setVisible(false) ;
		}
		edit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				new Edit(article);
			}
		});
		
	}
	
	String createDate(String time){
		//DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date=new Date(Long.parseLong(time));
		
		return date.toString();
		
	}


}
