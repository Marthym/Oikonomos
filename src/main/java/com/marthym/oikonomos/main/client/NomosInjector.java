package com.marthym.oikonomos.main.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.marthym.oikonomos.main.client.presenter.AccountsListPresenter;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;
import com.marthym.oikonomos.main.client.presenter.EditCategoryPresenter;
import com.marthym.oikonomos.main.client.presenter.EditPayeePresenter;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.presenter.TopNavigationPresenter;
import com.marthym.oikonomos.main.client.presenter.UserProfilePresenter;
import com.marthym.oikonomos.shared.services.AuthenticationServiceAsync;

@GinModules(NomosInjectorModule.class)
public interface NomosInjector extends Ginjector { 
	public static final NomosInjector INSTANCE = GWT.create(NomosInjector.class);
    public EventBus getEventBus();
    public OikonomosController getOikonomosController();
    public DashboardPresenter getDashboardPresenter();
    public TopNavigationPresenter getTopNavigationPresenter();
    public LeftMenuPresenter getLeftMenuPresenter();
    public EditAccountPresenter getEditAccountPresenter();
    public EditCategoryPresenter getEditCategoryPresenter();
    public AccountsListPresenter getAccountsListPresenter();
    public UserProfilePresenter getUserProfilePresenter();
    public EditPayeePresenter getEditPayeePresenter();
    
    public AuthenticationServiceAsync getAuthenticationServiceAsync();
}