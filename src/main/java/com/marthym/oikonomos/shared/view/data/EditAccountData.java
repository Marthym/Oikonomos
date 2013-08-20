package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.User;

public class EditAccountData extends ContentPanelData implements Serializable {
	private static final long serialVersionUID = 2939785649169271681L;
	
	private Account editAccount;

	public EditAccountData(){super(ContentPanelType.ACCOUNT);}
	public static EditAccountData cast(ContentPanelData data) {
		if (data != null) {
			EditAccountData editAccountData = new EditAccountData();
			try {
				editAccountData.currentUser = data.getCurrentUserData();
				if (data.isEmpty()) 
					return editAccountData;
		
				return (EditAccountData)data;
			} catch (ClassCastException e) {
				return editAccountData;
				
			} catch (OikonomosUnathorizedException e) {
				return editAccountData;
			}
		} else {
			return new EditAccountData();
		}
	}
	
	
	public final Account getEditAccount() { return this.editAccount;}
		
	public void setCurrentUserData(User user) { currentUser = user; }
	public void setAccount(Account account) { editAccount = account; }
}
