package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;
import com.marthym.oikonomos.shared.model.Account;

public class EditAccountData extends ContentPanelData implements Serializable {
	private static final long serialVersionUID = 2939785649169271681L;
	private Account editAccount;

	/**
	 * @Deprecated Use only for serialization issue
	 */
	@Deprecated
	public EditAccountData(){super(ContentPanelType.ACCOUNT);}
	public EditAccountData(Account editAccount){
		super(ContentPanelType.ACCOUNT);
		this.editAccount = editAccount;		
	}
	public static EditAccountData cast(ContentPanelData data) {
		try {
			if (data.isEmpty) {
				return new EditAccountData(new Account());
			} else {
				return (EditAccountData)data;
			}
		} catch (ClassCastException e) {
			return new EditAccountData(new Account());
		}
	}
	
	
	public final Account getEditAccount() { return this.editAccount;}
}
