package com.uibinder.client;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
//import javafx.scene.layout.BackgroundFill;

public class signin extends Composite {

	 private static final signinUiBinder binder = GWT.create(signinUiBinder.class);
		final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);
	    interface signinUiBinder extends UiBinder<Widget, signin> {
	    }
	    public signin() {
	        initWidget(binder.createAndBindUi(this));
	        register.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					RootPanel.get("signin").setVisible(false);
					RootPanel.get("signup").setVisible(true);
					
				}
			});

	         	    } 
		@UiField MaterialTextBox in_username;
		@UiField MaterialTextBox in_password;
		@UiField MaterialButton signin;
		@UiField MaterialLabel error_label;
		@UiField MaterialLink register;
		@UiField MaterialCheckBox rememberMe;


		@UiHandler("signin")
		void onClick(ClickEvent e) {
			if(!in_username.getText().equals("")||in_password.getText().equals("")){
				
			greetingService.signin(in_username.getText(),in_password.getText(),new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub				
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					//Window.alert(result);

					if(!result.equals("Not present")){
						String sessionId=result;
						Cookies.setCookie("uid", in_username.getText());
						if(rememberMe.getValue()){
							final long duration=1000*60*60*24*14;
							Date expires=new Date(System.currentTimeMillis()+duration);
							Cookies.setCookie("sid", sessionId,expires,null,"/",false);
						}
						else{
							Cookies.setCookie("sid", sessionId);
						}
						MaterialToast.fireToast("You Have successfully logged in");
						error_label.setText("");
						
						RootPanel.get("signin").setVisible(false);
						RootPanel.get("signup").setVisible(false);
						RootPanel.get("dash").setVisible(true);
					}
					else{
						error_label.setText("Re-Enter Password");
					}
				//	Cookies.setCookie(in_username.getText(),in_password.getText());
					
				}
			});
		}
			else{
				error_label.setText("Re-Enter Password");
			}

	}
		
}
