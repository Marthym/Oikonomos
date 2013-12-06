package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.DataGrid;

public interface OikonomosDataGridResource extends DataGrid.Resources {
	public OikonomosDataGridResource INSTANCE = GWT.create(OikonomosDataGridResource.class);

	public interface OikonomosDataGridCss extends DataGrid.Style {
		String center();
		String right();
	}

	@Source({ DataGrid.Style.DEFAULT_CSS, "OikonomosDataGrid.css" })
	OikonomosDataGridCss dataGridStyle();
}
