package com.vip.wallet.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WALLET".
*/
public class WalletDao extends AbstractDao<Wallet, Long> {

    public static final String TABLENAME = "WALLET";

    /**
     * Properties of entity Wallet.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Wallet_address = new Property(1, String.class, "wallet_address", false, "WALLET_ADDRESS");
        public final static Property Wallet_name = new Property(2, String.class, "wallet_name", false, "WALLET_NAME");
        public final static Property Wallet_pwd_hint = new Property(3, String.class, "wallet_pwd_hint", false, "WALLET_PWD_HINT");
        public final static Property Wallet_amount = new Property(4, double.class, "wallet_amount", false, "WALLET_AMOUNT");
        public final static Property Wallet_md5_pwd = new Property(5, String.class, "wallet_md5_pwd", false, "WALLET_MD5_PWD");
        public final static Property Token_count = new Property(6, int.class, "token_count", false, "TOKEN_COUNT");
        public final static Property SeedKeyWord = new Property(7, String.class, "seedKeyWord", false, "SEED_KEY_WORD");
        public final static Property Amount_cny = new Property(8, String.class, "amount_cny", false, "AMOUNT_CNY");
        public final static Property Amount_usd = new Property(9, String.class, "amount_usd", false, "AMOUNT_USD");
        public final static Property IsBackup = new Property(10, boolean.class, "isBackup", false, "IS_BACKUP");
    };


    public WalletDao(DaoConfig config) {
        super(config);
    }
    
    public WalletDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WALLET\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"WALLET_ADDRESS\" TEXT," + // 1: wallet_address
                "\"WALLET_NAME\" TEXT," + // 2: wallet_name
                "\"WALLET_PWD_HINT\" TEXT," + // 3: wallet_pwd_hint
                "\"WALLET_AMOUNT\" REAL NOT NULL ," + // 4: wallet_amount
                "\"WALLET_MD5_PWD\" TEXT," + // 5: wallet_md5_pwd
                "\"TOKEN_COUNT\" INTEGER NOT NULL ," + // 6: token_count
                "\"SEED_KEY_WORD\" TEXT," + // 7: seedKeyWord
                "\"AMOUNT_CNY\" TEXT," + // 8: amount_cny
                "\"AMOUNT_USD\" TEXT," + // 9: amount_usd
                "\"IS_BACKUP\" INTEGER NOT NULL );"); // 10: isBackup
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WALLET\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Wallet entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String wallet_address = entity.getWallet_address();
        if (wallet_address != null) {
            stmt.bindString(2, wallet_address);
        }
 
        String wallet_name = entity.getWallet_name();
        if (wallet_name != null) {
            stmt.bindString(3, wallet_name);
        }
 
        String wallet_pwd_hint = entity.getWallet_pwd_hint();
        if (wallet_pwd_hint != null) {
            stmt.bindString(4, wallet_pwd_hint);
        }
        stmt.bindDouble(5, entity.getWallet_amount());
 
        String wallet_md5_pwd = entity.getWallet_md5_pwd();
        if (wallet_md5_pwd != null) {
            stmt.bindString(6, wallet_md5_pwd);
        }
        stmt.bindLong(7, entity.getToken_count());
 
        String seedKeyWord = entity.getSeedKeyWord();
        if (seedKeyWord != null) {
            stmt.bindString(8, seedKeyWord);
        }
 
        String amount_cny = entity.getAmount_cny();
        if (amount_cny != null) {
            stmt.bindString(9, amount_cny);
        }
 
        String amount_usd = entity.getAmount_usd();
        if (amount_usd != null) {
            stmt.bindString(10, amount_usd);
        }
        stmt.bindLong(11, entity.getIsBackup() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Wallet entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String wallet_address = entity.getWallet_address();
        if (wallet_address != null) {
            stmt.bindString(2, wallet_address);
        }
 
        String wallet_name = entity.getWallet_name();
        if (wallet_name != null) {
            stmt.bindString(3, wallet_name);
        }
 
        String wallet_pwd_hint = entity.getWallet_pwd_hint();
        if (wallet_pwd_hint != null) {
            stmt.bindString(4, wallet_pwd_hint);
        }
        stmt.bindDouble(5, entity.getWallet_amount());
 
        String wallet_md5_pwd = entity.getWallet_md5_pwd();
        if (wallet_md5_pwd != null) {
            stmt.bindString(6, wallet_md5_pwd);
        }
        stmt.bindLong(7, entity.getToken_count());
 
        String seedKeyWord = entity.getSeedKeyWord();
        if (seedKeyWord != null) {
            stmt.bindString(8, seedKeyWord);
        }
 
        String amount_cny = entity.getAmount_cny();
        if (amount_cny != null) {
            stmt.bindString(9, amount_cny);
        }
 
        String amount_usd = entity.getAmount_usd();
        if (amount_usd != null) {
            stmt.bindString(10, amount_usd);
        }
        stmt.bindLong(11, entity.getIsBackup() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Wallet readEntity(Cursor cursor, int offset) {
        Wallet entity = new Wallet( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // wallet_address
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // wallet_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // wallet_pwd_hint
            cursor.getDouble(offset + 4), // wallet_amount
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // wallet_md5_pwd
            cursor.getInt(offset + 6), // token_count
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // seedKeyWord
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // amount_cny
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // amount_usd
            cursor.getShort(offset + 10) != 0 // isBackup
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Wallet entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWallet_address(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setWallet_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWallet_pwd_hint(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWallet_amount(cursor.getDouble(offset + 4));
        entity.setWallet_md5_pwd(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setToken_count(cursor.getInt(offset + 6));
        entity.setSeedKeyWord(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAmount_cny(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAmount_usd(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIsBackup(cursor.getShort(offset + 10) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Wallet entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Wallet entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
