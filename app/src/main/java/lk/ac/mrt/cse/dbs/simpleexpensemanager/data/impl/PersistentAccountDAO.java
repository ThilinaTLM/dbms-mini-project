package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private final PersistentDBHelper sqlDBHelper;

    public PersistentAccountDAO(PersistentDBHelper sqlDB) {
        this.sqlDBHelper = sqlDB;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> account_numbers = new ArrayList<>();
        try (SQLiteDatabase db = sqlDBHelper.getReadableDatabase()) {
            String query = String.format("SELECT %s FROM %s", PersistentDBHelper.TBC_ACC_NO, PersistentDBHelper.TB_ACC);
            try (Cursor cus = db.rawQuery(query, null)) {
                if (cus.moveToFirst()) {
                    do {
                        account_numbers.add(cus.getString(0));
                    } while (cus.moveToNext());
                }

            } catch (Exception e) {
                // do nothing
            }
        } catch (Exception e) {
            // do nothing
        }
        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        try (SQLiteDatabase db = sqlDBHelper.getReadableDatabase()) {
            String query = String.format("SELECT * FROM %s", PersistentDBHelper.TB_ACC);
            try (Cursor cus = db.rawQuery(query, null)) {
                if (cus.moveToFirst()) {
                    do {
                        accounts.add(
                                new Account(
                                        cus.getString(0),
                                        cus.getString(1),
                                        cus.getString(2),
                                        cus.getDouble(3)
                                )
                        );
                    } while (cus.moveToNext());
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        try (SQLiteDatabase db = sqlDBHelper.getReadableDatabase()) {
            String query = String.format("SELECT * FROM %s WHERE %s = ?", PersistentDBHelper.TB_ACC, PersistentDBHelper.TBC_ACC_NO);
            try (Cursor cus = db.rawQuery(query, new String[]{accountNo})) {
                if (cus.moveToFirst()) {
                    account = new Account(
                            cus.getString(0),
                            cus.getString(1),
                            cus.getString(2),
                            cus.getDouble(3)
                    );
                } else {
                    throw new InvalidAccountException("Couldn't find account");
                }
            } catch (InvalidAccountException iae) {
                throw iae;
            } catch (Exception e) {
                throw new InvalidAccountException("Invalid data to read");
            }
        } catch (InvalidAccountException iae) {
            throw iae;
        } catch (Exception e) {
            throw new InvalidAccountException("Couldn't retrieve requested data");
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        try (SQLiteDatabase db = sqlDBHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();

            cv.put(PersistentDBHelper.TBC_ACC_NO, account.getAccountNo());
            cv.put(PersistentDBHelper.TBC_ACC_BANK, account.getBankName());
            cv.put(PersistentDBHelper.TBC_ACC_HOLDER, account.getAccountHolderName());
            cv.put(PersistentDBHelper.TBC_ACC_BAL, account.getBalance());

            db.insert(PersistentDBHelper.TB_ACC, null, cv);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        try (SQLiteDatabase db = sqlDBHelper.getWritableDatabase()) {
            db.delete(PersistentDBHelper.TB_ACC,
                    PersistentDBHelper.TBC_ACC_NO + " = ?", new String[]{accountNo});
        } catch (Exception e) {
            throw new InvalidAccountException("Removing Account Data Failed");
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        try (SQLiteDatabase db = sqlDBHelper.getWritableDatabase()) {
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
                default:
                    throw new InvalidAccountException("Updating Account Balance Failed");
            }

            if (account.getBalance() < 0) {
                throw new InvalidAccountException("Not enough balance");
            }

            ContentValues cv = new ContentValues();
            cv.put(PersistentDBHelper.TBC_ACC_BAL, account.getBalance());
            db.update(
                    PersistentDBHelper.TB_ACC, cv,
                    String.format("%s = ?", PersistentDBHelper.TBC_ACC_NO),
                    new String[]{accountNo}
            );
        } catch (InvalidAccountException iae) {
            throw iae;
        } catch (Exception e) {
            throw new InvalidAccountException("Updating Account Balance Failed");
        }
    }
}
