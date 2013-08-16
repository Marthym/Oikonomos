package com.marthym.oikonomos.shared.view.data;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;

public class ContentPanelData {
	ContentPanelType type;
	boolean isEmpty = false;
	
	protected ContentPanelData(ContentPanelType type) {
		this.type = type;
	}
	public ContentPanelType getContentType() {
		return this.type;
	}
	
	public static final ContentPanelData getEmptyData(ContentPanelType type) {
		ContentPanelData contentPanelData = new ContentPanelData(type);
		contentPanelData.isEmpty = true;
		return contentPanelData;
	}
	
	public final boolean isEmpty() {return isEmpty;}
}
