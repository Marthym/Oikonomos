package com.marthym.oikonomos.main.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.main.client.components.TopNavigationBar;
import com.marthym.oikonomos.main.client.i18n.EditAccountConstants;
import com.marthym.oikonomos.main.client.presenter.AccountsListPresenter;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.presenter.TopNavigationPresenter;
import com.marthym.oikonomos.main.client.services.AccountServiceAsync;
import com.marthym.oikonomos.main.client.view.AccountsListView;
import com.marthym.oikonomos.main.client.view.DashboardView;
import com.marthym.oikonomos.main.client.view.EditAccountView;
import com.marthym.oikonomos.main.client.view.LeftMenuView;

public class NomosInjectorModule extends AbstractGinModule {
    @Override
    protected void configure() {
    	bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    	bind(DashboardPresenter.Display.class).to(DashboardView.class).in(Singleton.class);
    	bind(TopNavigationPresenter.Display.class).to(TopNavigationBar.class).in(Singleton.class);
    	bind(LeftMenuPresenter.Display.class).to(LeftMenuView.class).in(Singleton.class);
    	bind(AccountsListPresenter.Display.class).to(AccountsListView.class).in(Singleton.class);
    	bind(EditAccountPresenter.Display.class).to(EditAccountView.class).in(Singleton.class);
    	
    	bind(OikonomosErrorMessages.class).in(Singleton.class);
    	bind(EditAccountConstants.class).in(Singleton.class);
    	
    	bind(AccountServiceAsync.class).in(Singleton.class);
    }
}