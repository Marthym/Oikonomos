package com.marthym.oikonomos.main.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.main.client.components.TopNavigationBar;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.main.client.i18n.EditAccountConstants;
import com.marthym.oikonomos.main.client.presenter.AccountTabbedPresenter;
import com.marthym.oikonomos.main.client.presenter.AccountTransactionsPresenter;
import com.marthym.oikonomos.main.client.presenter.AccountsListPresenter;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;
import com.marthym.oikonomos.main.client.presenter.EditCategoryPresenter;
import com.marthym.oikonomos.main.client.presenter.EditPayeePresenter;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.presenter.TopNavigationPresenter;
import com.marthym.oikonomos.main.client.presenter.UserProfilePresenter;
import com.marthym.oikonomos.main.client.view.AccountTabbedView;
import com.marthym.oikonomos.main.client.view.AccountTransactionsView;
import com.marthym.oikonomos.main.client.view.AccountsListView;
import com.marthym.oikonomos.main.client.view.DashboardView;
import com.marthym.oikonomos.main.client.view.EditAccountView;
import com.marthym.oikonomos.main.client.view.EditCategoryView;
import com.marthym.oikonomos.main.client.view.EditPayeeView;
import com.marthym.oikonomos.main.client.view.LeftMenuView;
import com.marthym.oikonomos.main.client.view.UserProfileView;
import com.marthym.oikonomos.shared.services.AccountServiceAsync;
import com.marthym.oikonomos.shared.services.AuthenticationServiceAsync;
import com.marthym.oikonomos.shared.services.CategoryServiceAsync;
import com.marthym.oikonomos.shared.services.PayeeServiceAsync;
import com.marthym.oikonomos.shared.services.UserServiceAsync;

public class NomosInjectorModule extends AbstractGinModule {
    @Override
    protected void configure() {
    	bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    	bind(OikonomosController.class).in(Singleton.class);
    	bind(DashboardPresenter.Display.class).to(DashboardView.class).in(Singleton.class);
    	bind(TopNavigationPresenter.Display.class).to(TopNavigationBar.class).in(Singleton.class);
    	bind(LeftMenuPresenter.Display.class).to(LeftMenuView.class).in(Singleton.class);
    	bind(AccountsListPresenter.Display.class).to(AccountsListView.class).in(Singleton.class);
    	bind(AccountTabbedPresenter.Display.class).to(AccountTabbedView.class).in(Singleton.class);
    	bind(EditAccountPresenter.Display.class).to(EditAccountView.class).in(Singleton.class);
    	bind(EditCategoryPresenter.Display.class).to(EditCategoryView.class).in(Singleton.class);
    	bind(UserProfilePresenter.Display.class).to(UserProfileView.class).in(Singleton.class);
    	bind(EditPayeePresenter.Display.class).to(EditPayeeView.class).in(Singleton.class);
    	bind(AccountTransactionsPresenter.Display.class).to(AccountTransactionsView.class).in(Singleton.class);
    	
    	bind(OikonomosConstants.class).in(Singleton.class);
    	bind(OikonomosErrorMessages.class).in(Singleton.class);
    	bind(EditAccountConstants.class).in(Singleton.class);
    	bind(AccountTransactionsConstants.class).in(Singleton.class);
    	
    	bind(AccountServiceAsync.class).in(Singleton.class);
    	bind(AuthenticationServiceAsync.class).in(Singleton.class);
    	bind(UserServiceAsync.class).in(Singleton.class);
    	bind(CategoryServiceAsync.class).in(Singleton.class);
    	bind(PayeeServiceAsync.class).in(Singleton.class);
    }
}