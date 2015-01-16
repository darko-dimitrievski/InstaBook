package com.example.Implementation;

import com.example.Infrastructure.ApplyEffect;
import com.facebook.samples.hellofacebook.Instabook;

public abstract class BasicApplyEffect {

	private Instabook mainActivity;

	public BasicApplyEffect(Instabook mainActivity) {
		super();
		this.mainActivity = mainActivity;
	}
	
	public void addToList(ApplyEffect ae){
		mainActivity.addEffect(ae);
	
	}
	
	

}
