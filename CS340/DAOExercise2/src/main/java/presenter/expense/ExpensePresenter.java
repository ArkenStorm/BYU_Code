package presenter.expense;

import dao.DAOFactoryHolder;
import dao.ExpenseDAO;
import database.ConnectionFactory;
import model.Budget;
import model.Expense;
import view.expense.IExpenseView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpensePresenter implements IExpensePresenter {
    private final IExpenseView _expenseView;
    private final Budget _budget;
    private List<Expense> _allExpenses;
    private Expense _editableExpense;
    private ExpenseDAO expenseDAO;

    public ExpensePresenter(IExpenseView expenseView, Object args) {
        _expenseView = expenseView;
        _budget = (Budget) args;
        expenseDAO = DAOFactoryHolder.getInstance().getFactory().createExpenseDAO();
    }

    @Override
    public void fetchExpenses() {
        setExpenses(expenseDAO.getAllExpenses(_budget.id));
        _expenseView.displayOptions();
    }

    @Override
    public void optionSelection(String selection) {
        switch (selection) {
            case "1":
                if (_allExpenses != null && !_allExpenses.isEmpty()) {
                    _expenseView.displayExpenses(_allExpenses);
                } else {
                    _expenseView.displayMessage("You have no expenses for this budget");
                }
                _expenseView.displayOptions();
                break;
            case "2":
                _editableExpense = new Expense();
                _editableExpense.createId();
                _editableExpense.month = _budget.month;
                _editableExpense.year = _budget.year;
                _editableExpense.budgetId = _budget.id;
                _expenseView.displayEditNamePrompt();
                break;
            case "3":
                if (_allExpenses == null || _allExpenses.isEmpty()) {
                    _expenseView.displayMessage("You don\'t have any expenses to edit");
                    _expenseView.displayOptions();
                } else {
                    _expenseView.displayEditExpensePrompt(_allExpenses);
                }
                break;
            case "4":
                if (_allExpenses == null || _allExpenses.isEmpty()) {
                    _expenseView.displayMessage("You don\'t have any expenses to delete");
                    _expenseView.displayOptions();
                } else
                    _expenseView.displayDeleteExpensePrompt(_allExpenses);
                break;
            case "5":
                _expenseView.pop();
                break;
            default:
                _expenseView.displayOptions();
                break;
        }
    }

    @Override
    public void editName(String name) {
        _editableExpense.name = name;
        _expenseView.displayEditDayPrompt();
    }

    @Override
    public void editDay(int day) {
        _editableExpense.day = day;
        _expenseView.displayEditAmountPrompt();
    }

    @Override
    public void editAmount(double amount) {
        _editableExpense.amount = amount;
        _expenseView.displaySavePrompt(_editableExpense);
    }

    @Override
    public void editSelectedExpense(String selection) {
        int index = isValidSelection(selection);

        _editableExpense = _allExpenses.get(index);

        _expenseView.displayEditDayPrompt();
    }

    @Override
    public void handleSave(String answer) {
        if (answer.toLowerCase().equals("y")) {
            String message;
            if (_allExpenses != null && _allExpenses.contains(_editableExpense)) {
                boolean success = expenseDAO.updateExpense(_editableExpense);
                message = success ? "Expense updated successfully" : "Expense update failed";
            } else {
                boolean success = expenseDAO.createExpense(_editableExpense);
                message = success ? "Expense created successfully" : "Expense creation failed";
            }
            setExpenses(expenseDAO.getAllExpenses(_budget.id));
            _expenseView.displayMessage(message);
        }
        _expenseView.displayOptions();
    }

    @Override
    public void deleteSelectedExpense(String selection) {
        int index = isValidSelection(selection);

        Expense expenseToDelete = _allExpenses.get(index);

        boolean success = expenseDAO.deleteExpense(expenseToDelete.id);
        String message = success ? "Expense deleted successfully" : "Expense deletion failed";
        _expenseView.displayMessage(message);
        if (success) {
            setExpenses(expenseDAO.getAllExpenses(_budget.id));
        }

        _expenseView.displayOptions();
    }


    private int isValidSelection(String selection) {
        int s;
        try {
            s = Integer.parseInt(selection);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            _expenseView.displayOptions();
            return -1;
        }

        int index = s - 1;

        if (index < 0 || index > _allExpenses.size() - 1) {
            _expenseView.displayOptions();
            return -1;
        }

        return index;
    }

    private void setExpenses(List<Expense> expenses) {
        if (expenses != null) {
            Collections.sort(expenses);
        }
        _allExpenses = expenses;
    }
}
