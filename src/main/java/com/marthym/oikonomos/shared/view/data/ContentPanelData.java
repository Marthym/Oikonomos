package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.model.User;

public class ContentPanelData implements HasCurrentUserData, Serializable {
	private static final long serialVersionUID = -7627173530394194326L;
	
	private ContentPanelType type;
	private boolean isEmpty = false;
	private User currentUser;
	
	protected ContentPanelData(ContentPanelType type) {
		this.type = type;
	}
	public ContentPanelType getContentType() {
		return this.type;
	}
	
	public static final ContentPanelData getEmptyData(ContentPanelType type, User currentUser) {
		ContentPanelData contentPanelData = new ContentPanelData(type);
		contentPanelData.currentUser = currentUser;
		contentPanelData.isEmpty = true;
		return contentPanelData;
	}
	
	public final boolean isEmpty() {return isEmpty;}
	
	@Override
	public User getCurrentUserData() throws OikonomosUnathorizedException {
		if (currentUser == null) throw new OikonomosUnathorizedException("error.message.user.unauthorized", "currentUser is null !");
		return currentUser;
	}
	public void setCurrentUserData(User user) { currentUser = user; }
}
