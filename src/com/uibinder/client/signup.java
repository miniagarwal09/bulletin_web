package com.uibinder.client;
import com.uibinder.client.GreetingService;
import com.uibinder.client.GreetingServiceAsync;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;

import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;

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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class signup extends Composite {
	
	final GreetingServiceAsync greetingService =GWT.create(GreetingService.class);
	private static signupUiBinder uiBinder = GWT.create(signupUiBinder.class);

	interface signupUiBinder extends UiBinder<Widget, signup> {
	}

	public signup() {
		initWidget(uiBinder.createAndBindUi(this));
		signin.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				RootPanel.get("signin").setVisible(true);
				RootPanel.get("signup").setVisible(false);
			}
		});
    
	}

	@UiField
	MaterialButton signup;
	@UiField
	MaterialTextBox up_username;
	@UiField
	MaterialTextBox up_password;	
	MaterialTextBox up_confirm_password;
	MaterialLabel error_label;
	@UiField
	MaterialLink signin;



	@UiHandler("signup")
	void onClick(ClickEvent e) {

		if(true){
			
		greetingService.signup(up_username.getText(),up_password.getText(),new AsyncCallback<String>(){
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(!result.equals("")){
					String sessionId=result;
					Cookies.setCookie("uid", up_username.getText());
					final long duration=1000*60*60*24*14;
					Date expires=new Date(System.currentTimeMillis()+duration);
					Cookies.setCookie("sid", sessionId,expires,null,"/",false);
					Window.alert("Signed Up");	
					RootPanel.get("signin").setVisible(false);
					RootPanel.get("signup").setVisible(false);
					RootPanel.get("dash").setVisible(true);
				}
				
			}
			});
		}
	}


}
