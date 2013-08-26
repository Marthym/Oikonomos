package com.marthym.oikonomos.main.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.marthym.oikonomos.main.client.presenter.AccountsListPresenter;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.presenter.TopNavigationPresenter;

@GinModules(NomosInjectorModule.class)
public interface NomosInjector extends Ginjector { 
	public static final NomosInjector INSTANCE = GWT.create(NomosInjector.class);
    public EventBus getEventBus();
    public DashboardPresenter getDashboardPresenter();
    public TopNavigationPresenter getTopNavigationPresenter();
    public LeftMenuPresenter getLeftMenuPresenter();
    public EditAccountPresenter getEditAccountPresenter();
    public AccountsListPresenter getAccountsListPresenter();
}