package com.uibinder.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.shared.Article;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent.SuccessHandler;
import gwt.material.design.addins.client.window.MaterialWindow;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;

public class Edit extends Composite {
	@UiField
	MaterialWindow window;
	@UiField
	MaterialButton button;
	@UiField
	MaterialTextBox title,link,tags,category;
	@UiField
	MaterialTextArea text;
	@UiField
	MaterialFileUploader uploader;
	String file;
	private static EditUiBinder uiBinder = GWT.create(EditUiBinder.class);
	final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);

	interface EditUiBinder extends UiBinder<Widget, Edit> {
	}
	
	public Edit(Article article) {
		initWidget(uiBinder.createAndBindUi(this));
		uploader.setMaxFileSize(2);	
		uploader.setAcceptedFiles("image/*");	
		
		uploader.addSuccessHandler(new SuccessHandler<UploadFile>(){

			@Override
			public void onSuccess(SuccessEvent<UploadFile> event) {
				// TODO Auto-generated method stub
				file="images/"+event.getTarget().getName();	
			}
		});	
		
		window.open();
		title.setText(article.getTitle());
		link.setText(article.getLink());
		text.setText(article.getMtext());
		file=article.getImage();
		String tagst="";
		ArrayList<String> tagsl=article.getTags();
		for(String tag : tagsl){
			tagst+=tag+",";
		}
		tagst=tagst.substring(0, tagst.length()-1);
		tags.setText(tagst);
		category.setText(article.getCategory());
		window.setHeight("500px");
		
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String present_time=article.getTime();
				article.setImage(file);
				article.setLink(link.getText().toString());
				article.setMtext(text.getText());
				article.setTitle(title.getText());
				article.setCategory(category.getText());
				long time = new Date().getTime();
				String articleid=String.valueOf(time);
				article.setTime(articleid);
				article.setTags(new ArrayList<String>(Arrays.asList(tags.getText().split("\\s*,\\s*"))));
				greetingService.update_article(article,present_time,Cookies.getCookie("uid"),new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						Window.alert("Updated");
						window.close();
					}
				});
				
			}
		});
	}


}