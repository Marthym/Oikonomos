package com.marthym.oikonomos.main.client.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.google.gwt.view.client.SingleSelectionModel;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.components.EditTransactionForm;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.main.client.presenter.AccountTransactionsPresenter;
import com.marthym.oikonomos.main.client.resources.DashboardViewResource;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;
import com.marthym.oikonomos.main.client.resources.OikonomosDataGridResource;
import com.marthym.oikonomos.main.client.resources.OikonomosDataGridResource.OikonomosDataGridCss;
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
	
	private SingleSelectionModel<TransactionDTO> selectionModel;
	private AccountTransactionsConstants constants;
	private OikonomosConstants oConstants;
	private static final OikonomosDataGridCss DATA_GRID_CSS = OikonomosDataGridResource.INSTANCE.dataGridStyle();
	private static final Comparator<TransactionDTO> transactionDateComparator = new Comparator<TransactionDTO>() {
		@Override public int compare(TransactionDTO t1, TransactionDTO t2) {
			if (t1 == null) return -1;
			if (t2 == null) return 1;
			
			int compareDate = t1.getTransactionDate().compareTo(t2.getTransactionDate());
			if (compareDate == 0) {
				compareDate = (int) (t1.getId() - t2.getId());
			}
			return compareDate;
		}
	};
	
	private ListDataProvider<TransactionDTO> dataProvider;
		
	@Inject
	public AccountTransactionsView(AccountTransactionsConstants constants, OikonomosConstants oConstants) {
		this.constants = constants;
		this.oConstants = oConstants;
		DashboardViewResource.INSTANCE.style().ensureInjected();
		MainFormViewResource.INSTANCE.style().ensureInjected();
		DATA_GRID_CSS.ensureInjected();
		
		transactionsGrid = new DataGrid<TransactionDTO>(100, OikonomosDataGridResource.INSTANCE, TransactionDTO.KEY_PROVIDER);
		transactionsGrid.setAutoHeaderRefreshDisabled(true);
		transactionsGrid.setEmptyTableWidget(new Label(constants.grid_empty_label()));
	    selectionModel = new SingleSelectionModel<TransactionDTO>(TransactionDTO.KEY_PROVIDER);
	    transactionsGrid.setSelectionModel(selectionModel);
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
		TextHeader hCheckColumn = new TextHeader("R");
		hCheckColumn.setHeaderStyleNames(DATA_GRID_CSS.center());
		transactionsGrid.addColumn(checkColumn, hCheckColumn);
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
		Column<TransactionDTO, SafeHtml> payeeColumn =
				new Column<TransactionDTO, SafeHtml>(new SafeHtmlCell()) {
					@Override
					public SafeHtml getValue(TransactionDTO object) {
						SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder().appendEscaped(object.getPayee().getEntityDescription());
						if (object.getTransactionComment() != null && !object.getTransactionComment().isEmpty()) {
							safeHtmlBuilder.appendHtmlConstant("<span class=\"hint--right hint--rounded "+DATA_GRID_CSS.info()+"\" data-hint=\"")
									.appendEscaped(object.getTransactionComment())
									.appendHtmlConstant("\">&nbsp;</span>");
						}
						safeHtmlBuilder.appendHtmlConstant("<br/>")
								.appendHtmlConstant("<span class=\""+DATA_GRID_CSS.category()+"\">")
								.appendEscaped(object.getCategory().getAbsoluteDescription())
								.appendHtmlConstant("</span>");
						return safeHtmlBuilder.toSafeHtml();
					}
				};
		transactionsGrid.addColumn(payeeColumn, constants.placeholder_payee());
		transactionsGrid.setColumnWidth(payeeColumn, 100, Unit.PCT);
		
		// Debit
		Column<TransactionDTO, Number> debitColumn =
				new Column<TransactionDTO, Number>(new NumberCell(numberFormat)) {
					@Override
					public Number getValue(TransactionDTO object) {
						return object.getDebit();
					}
				};
		TextHeader hDebitColumn = new TextHeader(constants.placeholder_debit());
		hDebitColumn.setHeaderStyleNames(DATA_GRID_CSS.right());
		debitColumn.setHorizontalAlignment(Column.ALIGN_RIGHT);
		transactionsGrid.addColumn(debitColumn, hDebitColumn);
		transactionsGrid.setColumnWidth(debitColumn, 100, Unit.PX);
		
		// Credit
		Column<TransactionDTO, Number> creditColumn =
				new Column<TransactionDTO, Number>(new NumberCell(numberFormat)) {
					@Override
					public Number getValue(TransactionDTO object) {
						return object.getCredit();
					}
				};
		TextHeader hCreditColumn = new TextHeader(constants.placeholder_credit());
		hCreditColumn.setHeaderStyleNames(DATA_GRID_CSS.right());
		creditColumn.setHorizontalAlignment(Column.ALIGN_RIGHT);
		transactionsGrid.addColumn(creditColumn, hCreditColumn);
		transactionsGrid.setColumnWidth(creditColumn, 100, Unit.PX);

		// Balance
		Column<TransactionDTO, Number> balanceColumn =
				new Column<TransactionDTO, Number>(new NumberCell(numberFormat)) {
					@Override
					public Number getValue(TransactionDTO object) {
						long debit = (object.getDebit()==null)?0:object.getDebit();
						long credit = (object.getCredit()==null)?0:object.getCredit();
						return credit-debit;
					}
				};
		TextHeader hBalanceColumn = new TextHeader(constants.grid_headed_balance());
		hBalanceColumn.setHeaderStyleNames(DATA_GRID_CSS.right());
		balanceColumn.setHorizontalAlignment(Column.ALIGN_RIGHT);
		transactionsGrid.addColumn(balanceColumn, hBalanceColumn);
		transactionsGrid.setColumnWidth(balanceColumn, 100, Unit.PX);
		
	}
	  
	@Override
	public void addTransactionGridLine(List<TransactionDTO> newTransactions) {
		LOG.finer("addTransactionGridLine ...");
		if (dataProvider == null) { 
			LOG.finer("create AccountTransactionsView ListDataProvider ...");
			dataProvider = new ListDataProvider<TransactionDTO>();
			dataProvider.addDataDisplay(transactionsGrid);
		}

		List<TransactionDTO> list = dataProvider.getList();
		list.removeAll(newTransactions);
		list.addAll(newTransactions);

		Collections.sort(list, transactionDateComparator);
	}

	@Override
	public void reset() {
		transactionForm.reset();
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
	public void setTransactionPayee(Payee payee) {
		transactionForm.setSeletedPayee(payee);
	}
	
	@Override
	public CategoryDTO getTransactionCategory() {
		return transactionForm.getSelectedCategory();
	}
	
	@Override
	public void setTransactionCategory(CategoryDTO category) {
		transactionForm.setSelectedCategory(category);
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

	@Override
	public HasSelectionChangedHandlers getTransactionsSelectionModel() {
		return selectionModel;
	}

	@Override
	public TransactionDTO getSelectedTransaction() {
		return selectionModel.getSelectedObject();
	}

	@Override
	public void setTransactionFormVisisble(boolean isVisible) {
		formDisclosure.setOpen(isVisible);
	}

	@Override
	public void setSelectedTransaction(TransactionDTO item) {
		if (item != null)
			selectionModel.setSelected(item, true);
		else 
			selectionModel.clear();
	}

}
