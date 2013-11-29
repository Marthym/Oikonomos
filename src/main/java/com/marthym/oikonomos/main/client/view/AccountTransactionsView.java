package com.marthym.oikonomos.main.client.view;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.components.EditTransactionForm;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.main.client.presenter.AccountTransactionsPresenter;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;
import com.marthym.oikonomos.main.client.resources.OikonomosDataGridResource;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;

public class AccountTransactionsView extends Composite implements AccountTransactionsPresenter.Display {
	private static final Logger LOG = Logger.getLogger(AccountTransactionsView.class.getName());
	private static AccountTransactionsViewUiBinder uiBinder = GWT.create(AccountTransactionsViewUiBinder.class);

	interface AccountTransactionsViewUiBinder extends UiBinder<Widget, AccountTransactionsView> {}
	
	@UiField(provided=true) 
			 DataGrid<TransactionDTO> transactionsGrid;
	@UiField DisclosurePanel formDisclosure;
	@UiField EditTransactionForm transactionForm;
	@UiField VerticalPanel vertical;
	
	private AccountTransactionsConstants constants;
	private OikonomosConstants oConstants;
	
	private ListDataProvider<TransactionDTO> dataProvider;
		
	@Inject
	public AccountTransactionsView(AccountTransactionsConstants constants, OikonomosConstants oConstants) {
		this.constants = constants;
		this.oConstants = oConstants;
		MainFormViewResource.INSTANCE.style().ensureInjected();
		OikonomosDataGridResource.INSTANCE.dataGridStyle().ensureInjected();
		
		transactionsGrid = new DataGrid<TransactionDTO>(100, OikonomosDataGridResource.INSTANCE, TransactionDTO.KEY_PROVIDER);
		transactionsGrid.setAutoHeaderRefreshDisabled(true);
		transactionsGrid.setEmptyTableWidget(new Label(constants.grid_empty_label()));
	    //final SelectionModel<TransactionDTO> selectionModel = new MultiSelectionModel<TransactionDTO>(TransactionDTO.KEY_PROVIDER);
	    //transactionsGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<TransactionDTO> createCheckboxManager());
		initTableColumns();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		vertical.setCellHeight(vertical.getWidget(0), "100%");
	}

	private void initTableColumns() {
		
		DateTimeFormat format = DateTimeFormat.getFormat(oConstants.dateFormat());
		NumberFormat numberFormat = NumberFormat.getFormat("#,##0.##");

		Column<TransactionDTO, Boolean> checkColumn =
				new Column<TransactionDTO, Boolean>(new CheckboxCell(false, false)) {
					@Override
					public Boolean getValue(TransactionDTO object) {
						// Get the value from the selection model.
						return (object.getReconciliation()!=null);
					}
				};
		transactionsGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("R"));
		transactionsGrid.setColumnWidth(checkColumn, 30, Unit.PX);

		// Accounting Document
		Column<TransactionDTO, String> accountingDocumentColumn =
				new Column<TransactionDTO, String>(new TextCell()) {
					@Override
					public String getValue(TransactionDTO object) {
						return object.getAccountingDocument();
					}
				};
		transactionsGrid.addColumn(accountingDocumentColumn, constants.placeholder_accountingDocument());
		transactionsGrid.setColumnWidth(accountingDocumentColumn, 145, Unit.PX);

		// Date
		Column<TransactionDTO, Date> transactionDateColumn =
				new Column<TransactionDTO, Date>(new DateCell(format)) {
					@Override
					public Date getValue(TransactionDTO object) {
						return object.getTransactionDate();
					}
				};
		transactionsGrid.addColumn(transactionDateColumn, constants.placeholder_date());
		transactionsGrid.setColumnWidth(transactionDateColumn, 95, Unit.PX);

		// Payee
		Column<TransactionDTO, String> payeeColumn =
				new Column<TransactionDTO, String>(new TextCell()) {
					@Override
					public String getValue(TransactionDTO object) {
						return object.getPayee().getEntityDescription();
					}
				};
		transactionsGrid.addColumn(payeeColumn, constants.placeholder_payee());
		transactionsGrid.setColumnWidth(payeeColumn, 100, Unit.PCT);

		// Credit
		Column<TransactionDTO, Number> creditColumn =
				new Column<TransactionDTO, Number>(new NumberCell(numberFormat)) {
					@Override
					public Number getValue(TransactionDTO object) {
						return object.getCredit();
					}
				};
		creditColumn.setHorizontalAlignment(Column.ALIGN_RIGHT);
		transactionsGrid.addColumn(creditColumn, constants.placeholder_credit());
		transactionsGrid.setColumnWidth(creditColumn, 100, Unit.PX);
		
		// Debit
		Column<TransactionDTO, Number> debitColumn =
				new Column<TransactionDTO, Number>(new NumberCell(numberFormat)) {
					@Override
					public Number getValue(TransactionDTO object) {
						return object.getDebit();
					}
				};
		debitColumn.setHorizontalAlignment(Column.ALIGN_RIGHT);
		transactionsGrid.addColumn(debitColumn, constants.placeholder_debit());
		transactionsGrid.setColumnWidth(debitColumn, 100, Unit.PX);

		// Balance
		Column<TransactionDTO, Number> balanceColumn =
				new Column<TransactionDTO, Number>(new NumberCell(numberFormat)) {
					@Override
					public Number getValue(TransactionDTO object) {
						return object.getCredit()-object.getDebit();
					}
				};
		balanceColumn.setHorizontalAlignment(Column.ALIGN_RIGHT);
		transactionsGrid.addColumn(balanceColumn, constants.grid_headed_balance());
		transactionsGrid.setColumnWidth(balanceColumn, 100, Unit.PX);
		
	}
	  
	@Override
	public void addTransactionGridLine(List<TransactionDTO> newTransactions) {
		LOG.finer("addTransactionGridLine ...");
		
		if (dataProvider == null) { 
			dataProvider = new ListDataProvider<TransactionDTO>(newTransactions);
			dataProvider.addDataDisplay(transactionsGrid);
			return;
		}
		List<TransactionDTO> transactions = dataProvider.getList();
		transactions.removeAll(newTransactions);
		transactions.addAll(newTransactions);
		
		LOG.finer("... addTransactionGridLine");
	}

	@Override
	public void reset() {
		transactionForm.getForm().reset();
	}

	@Override
	public HasClickHandlers getValidateButton() {
		return transactionForm.getValidateButton();
	}

	@Override
	public HasClickHandlers getResetButton() {
		return transactionForm.getResetButton();
	}

	@Override
	public Payee getTransactionPayee() {
		return transactionForm.getSeletedPayee();
	}

	@Override
	public CategoryDTO getTransactionCategory() {
		return transactionForm.getSelectedCategory();
	}

	@Override
	public HasValue<Date> getTransactionDate() {
		return transactionForm.getTransactionDate();
	}

	@Override
	public HasValue<String> getTransactionDebit() {
		return transactionForm.getTransactionDebit();
	}

	@Override
	public HasValue<String> getTransactionCredit() {
		return transactionForm.getTransactionCredit();
	}

	@Override
	public HasValue<String> getTransactionPaiementMean() {
		return transactionForm.getTransactionPaiementMean();
	}

	@Override
	public HasValue<String> getTransactionAccountingDocument() {
		return transactionForm.getTransactionAccountingDocument();
	}

	@Override
	public HasValue<String> getTransactionComment() {
		return transactionForm.getTransactionComment();
	}

	@Override
	public HasValue<String> getTransactionBudgetaryLine() {
		return transactionForm.getTransactionBudgetaryLine();
	}

}
