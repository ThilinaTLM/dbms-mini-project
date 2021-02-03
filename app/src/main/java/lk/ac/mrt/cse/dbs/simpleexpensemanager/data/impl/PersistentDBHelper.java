package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PersistentDB extends SQLiteOpenHelper {

    private static String TB_ACC = "account";
    private static String TBC_ACC_NO = "account_no";
    private static String TBC_ACC_BANK = "bank_name";
    private static String TBC_ACC_HOLDER = "account_holder_name";
    private static String TBC_ACC_BAL = "balance";

    private static String TB_TRS = "transaction_log";
    private static String TBC_TRS_ID = "id";
    private static String TBC_TRS_DATE = "date";
    private static String TBC_TRS_ACC_NUM = "account_no";
    private static String TBC_TRS_EXP_TYPE = "expense_type";
    private static String TBC_TRS_AMOUNT = "amount";


    private PersistentDB(@Nullable Context context) {
        super(context, "expense-man.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
              "CREATE TABLE account (" +
                      "    account_no VARCHAR(50) PRIMARY KEY," +
                      "    bank_name VARCHAR(100) NOT NULL," +
                      "    account_holder_name VARCHAR(150) NOT NULL," +
                      "    balance DECIMAL(17, 2) DEFAULT 0 )"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE transaction_log (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    date DATE NOT NULL,\n" +
                        "    account_no VARCHAR(50) NOT NULL,\n" +
                        "    expense_type VARCHAR(20) NOT NULL,\n" +
                        "    amount DECIMAL(17, 2) DEFAULT 0\n" +
                        ")\n"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
