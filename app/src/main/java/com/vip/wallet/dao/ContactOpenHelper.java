package com.vip.wallet.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.StandardDatabase;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public class ContactOpenHelper extends DaoMaster.OpenHelper {

    private static final String DB_NAME = "contact.db";

    private static final SortedMap<Integer, Migration> ALL_MIGRATIONS = new TreeMap<>();

    static {
        ALL_MIGRATIONS.put(0, new V1Migration());
        ALL_MIGRATIONS.put(1, new V2Migration());
        ALL_MIGRATIONS.put(2, new V3Migration());
        ALL_MIGRATIONS.put(3, new V4Migration());
        ALL_MIGRATIONS.put(4, new V5Migration());
        ALL_MIGRATIONS.put(5, new V6Migration());
        ALL_MIGRATIONS.put(6, new V7Migration());
    }

    public ContactOpenHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        //        executeMigrations(db, ALL_MIGRATIONS.keySet());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        SortedMap<Integer, Migration> migrations = ALL_MIGRATIONS.subMap(oldVersion, newVersion);
        executeMigrations(sqLiteDatabase, migrations.keySet());
    }


    private void executeMigrations(final SQLiteDatabase paramSQLiteDatabase, final Set<Integer>
            migrationVersions) {
        for (final Integer version : migrationVersions) {
            ALL_MIGRATIONS.get(version).migrate(paramSQLiteDatabase);
        }
    }
}

interface Migration {

    void migrate(SQLiteDatabase db);

}

class V1Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        //		ALTER TABLE table_name ADD field_name field_type;
        //        db.execSQL("ALTER TABLE NOTE ADD COLUMN test");
    }
}

/**
 * 2.1.0版本
 */
class V2Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        //		ALTER TABLE table_name ADD field_name field_type;
        db.execSQL("ALTER TABLE WALLET ADD COLUMN SEED_KEY_WORD TEXT");
        PushMessageDao.createTable(new StandardDatabase(db), true);
    }
}

/**
 * 2.2.0版本
 */
class V3Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        //selectToken
        db.execSQL("ALTER TABLE SELECT_TOKEN ADD COLUMN WALLET_ADDRESS TEXT");
        //wallet
        db.execSQL("ALTER TABLE WALLET ADD COLUMN AMOUNT_CNY TEXT");
        db.execSQL("ALTER TABLE WALLET ADD COLUMN AMOUNT_USD TEXT");
    }
}

/**
 * 2.3.2版本
 */
class V4Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        //wallet
        db.execSQL("ALTER TABLE WALLET ADD COLUMN IS_BACKUP INTEGER");
    }
}

/**
 * 2.4.0版本
 */
class V5Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        CardPkgDao.createTable(new StandardDatabase(db), true);
    }
}

/**
 * 2.5.0版本
 */
class V6Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        //create address and card table
        CardDao.createTable(new StandardDatabase(db), true);
        AddressDao.createTable(new StandardDatabase(db), true);
        //contact add chain_type
        db.execSQL("ALTER TABLE CONTACT ADD COLUMN CHAIN_TYPE INTEGER DEFAULT 0");
    }
}

/**
 * 2.6.0版本
 */
class V7Migration implements Migration {
    @Override
    public void migrate(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE CARD ADD COLUMN IS_BACK_UP INTEGER DEFAULT 0");
        db.execSQL("ALTER TABLE CARD ADD COLUMN ACCOUNT_NAME TEXT");
    }
}
