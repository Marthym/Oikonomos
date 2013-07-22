package com.marthym.oikonomos.shared.view.data;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;

public class ContentPanelData {
	ContentPanelType type;
	protected ContentPanelData(ContentPanelType type) {
		this.type = type;
	}
	public ContentPanelType getContentType() {
		return this.type;
	}
}
