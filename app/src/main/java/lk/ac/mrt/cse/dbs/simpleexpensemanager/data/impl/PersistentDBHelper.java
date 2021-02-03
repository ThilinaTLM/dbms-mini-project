package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PersistentDBHelper extends SQLiteOpenHelper {

    public static String TB_ACC = "account";
    public static String TBC_ACC_NO = "account_no";
    public static String TBC_ACC_BANK = "bank_name";
    public static String TBC_ACC_HOLDER = "account_holder_name";
    public static String TBC_ACC_BAL = "balance";

    public static String TB_TRS = "transaction_log";
    public static String TBC_TRS_ID = "id";
    public static String TBC_TRS_DATE = "date";
    public static String TBC_TRS_ACC_NUM = "account_no";
    public static String TBC_TRS_EXP_TYPE = "expense_type";
    public static String TBC_TRS_AMOUNT = "amount";


    public PersistentDBHelper(@Nullable Context context) {
        super(context, "180371K.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "".concat(String.format("CREATE TABLE %s (", TB_ACC))
                        .concat(String.format(" %s VARCHAR(50) PRIMARY KEY, ", TBC_ACC_NO))
                        .concat(String.format(" %s VARCHAR(100) NOT NULL,", TBC_ACC_BANK))
                        .concat(String.format(" %s VARCHAR(150) NOT NULL,", TBC_ACC_HOLDER))
                        .concat(String.format(" %s DECIMAL(17, 2) DEFAULT 0 ", TBC_ACC_BAL))
                        .concat(" )")
        );

        sqLiteDatabase.execSQL(
                "".concat(String.format("CREATE TABLE %s (", TB_TRS))
                        .concat(String.format(" %s INTEGER PRIMARY KEY AUTOINCREMENT,", TBC_TRS_ID))
                        .concat(String.format(" %s DATE NOT NULL,", TBC_TRS_DATE))
                        .concat(String.format(" %s VARCHAR(50) NOT NULL,", TBC_TRS_ACC_NUM))
                        .concat(String.format(" %s VARCHAR(50) NOT NULL,", TBC_TRS_EXP_TYPE))
                        .concat(String.format(" %s DECIMAL(17, 2) DEFAULT 0", TBC_TRS_AMOUNT))
                        .concat(" )")
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
