package com.uibinder.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.core.java.util.Collections;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sun.java.swing.plaf.windows.resources.windows;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.base.UploadResponse;
import gwt.material.design.addins.client.fileuploader.constants.FileMethod;
import gwt.material.design.addins.client.fileuploader.events.AddedFileEvent.AddedFileHandler;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent.SuccessHandler;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCardImage;
import gwt.material.design.client.ui.MaterialCardTitle;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;

public class add_article extends Composite {

	private static add_articleUiBinder uiBinder = GWT.create(add_articleUiBinder.class);
	final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);

	interface add_articleUiBinder extends UiBinder<Widget, add_article> {
	}
	@UiField
	MaterialButton button;
	@UiField
	MaterialTextBox title,link,tags,newCategory;
	@UiField
	MaterialComboBox<String> category;
	@UiField
	MaterialTextArea text;
	@UiField
	MaterialFileUploader uploader;
	String file="dummy.png";
	ArrayList<String> categoryList=Uibinder.categoryList;
	@UiField
	MaterialButton addCategory,changeImage;
	@UiField
	MaterialCardImage uploadedImage;
	@UiField
	MaterialImage image;
	@UiField
	MaterialCardTitle imageName;
	
	



	public add_article() {
		initWidget(uiBinder.createAndBindUi(this));
		newCategory.setVisible(false);
		uploadedImage.setVisible(false);
		uploader.setMaxFileSize(2);	
		uploader.setAcceptedFiles("image/*");	
		text.setLength(300);
		
		changeImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				uploader.setVisible(true);
				uploadedImage.setVisible(false);
				
			}
		});
		addCategory.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(addCategory.getText()==""){
					newCategory.setVisible(true);
					category.setVisible(false);
					addCategory.setText("Add new Category");
					addCategory.setIconType(IconType.LIST);
				}
				else{
					newCategory.setVisible(false);
					category.setVisible(true);
					addCategory.setText("");
					addCategory.setIconType(IconType.ADD);
					
				}
				
				
			}
		});
		
		uploader.addSuccessHandler(new SuccessHandler<UploadFile>(){

			@Override
			public void onSuccess(SuccessEvent<UploadFile> event) {
				// TODO Auto-generated method stub
				file=event.getTarget().getName();	
				uploader.setVisible(false);
				image.setUrl("images/"+file);
				imageName.setText(file);
				uploadedImage.setVisible(true);
			}
		});	
		category.addOpenHandler(new OpenHandler<String>() {
			
			@Override
			public void onOpen(OpenEvent<String> event) {
				// TODO Auto-generated method stub
				Collection<String> collection=new ArrayList<String>();
				categoryList=Uibinder.categoryList;
				for(String category:categoryList){
				collection.add(category);
				}
				category.setItems(collection);
				
			}
		});
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		//ArrayList<String> sub_tags=new ArrayList<String>();
		ArrayList<String> sub_tags = new ArrayList<String>(Arrays.asList(tags.getText().split("\\s*,\\s*")));

		System.out.println("Add Article:"+file);
		String author=Cookies.getCookie("uid");
		String cat="";
		if(addCategory.getText()==""){
			cat=category.getSingleValue().toLowerCase();
		}
		else{
			cat = newCategory.getText().toLowerCase();
		}
		greetingService.add_article(author,title.getText(),"images/"+file, text.getText(), link.getText(),cat,sub_tags,new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				Window.alert(result);
				//sendNotification();
			}
			
			
			
		});
	}


}
