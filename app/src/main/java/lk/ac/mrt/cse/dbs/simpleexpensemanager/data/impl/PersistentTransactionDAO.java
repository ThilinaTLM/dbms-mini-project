package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private final PersistentDBHelper sqlDBHelper;

    public PersistentTransactionDAO(PersistentDBHelper sqlDB) {
        this.sqlDBHelper = sqlDB;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        try (SQLiteDatabase db = sqlDBHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();

            String expenseTypeText = (expenseType == ExpenseType.EXPENSE) ? "Expense" : "Income";

            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String strDate = dateFormat.format(date);

            cv.put(PersistentDBHelper.TBC_TRS_DATE, strDate);
            cv.put(PersistentDBHelper.TBC_TRS_ACC_NUM, accountNo);
            cv.put(PersistentDBHelper.TBC_TRS_EXP_TYPE, expenseTypeText);
            cv.put(PersistentDBHelper.TBC_TRS_AMOUNT, amount);

            db.insert(PersistentDBHelper.TB_TRS, null, cv);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        try (SQLiteDatabase db = sqlDBHelper.getReadableDatabase()) {
            @SuppressLint("SimpleDateFormat")
            DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            String query = String.format("SELECT * FROM %s", PersistentDBHelper.TB_TRS);

            try (Cursor cus = db.rawQuery(query, null)) {
                if (cus.moveToFirst()) {
                    do {
                        ExpenseType expenseType = (cus.getString(3).equals("Expense")) ?
                                ExpenseType.EXPENSE : ExpenseType.INCOME;
                        Date date;
                        try {
                            date = iso8601Format.parse(cus.getString(1));
                        } catch (ParseException e) {
                            System.out.println(cus.getString(1));
                            date = new Date();
                        }

                        transactions.add(
                                new Transaction(
                                        date,
                                        cus.getString(2),
                                        expenseType,
                                        cus.getDouble(4)
                                )
                        );
                    } while (cus.moveToNext());
                }
            } catch (Exception e) {
                // do nothing
            }
        } catch (Exception e) {
            // do nothing
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        @SuppressLint("SimpleDateFormat")
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String query = String.format("SELECT * FROM %s LIMIT ?", PersistentDBHelper.TB_TRS);
        List<Transaction> transactions = new ArrayList<>();

        try (SQLiteDatabase db = sqlDBHelper.getReadableDatabase()) {
            try (Cursor cus = db.rawQuery(query, new String[]{Integer.toString(limit)})) {
                if (cus.moveToFirst()) {

                    do {
                        ExpenseType expenseType = (cus.getString(3).equals("Expense")) ?
                                ExpenseType.EXPENSE : ExpenseType.INCOME;
                        Date date;
                        try {
                            date = iso8601Format.parse(cus.getString(1));
                        } catch (ParseException e) {
                            System.out.println(cus.getString(1));
                            date = new Date();
                        }

                        transactions.add(
                                new Transaction(
                                        date,
                                        cus.getString(2),
                                        expenseType,
                                        cus.getDouble(4)
                                )
                        );
                    } while (cus.moveToNext());
                }

            } catch (Exception e) {
                // do nothing
            }
        } catch (Exception e) {
            // do nothing
        }
        return transactions;
    }
}
