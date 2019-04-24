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
 * DAO for table "CARD".
*/
public class CardDao extends AbstractDao<Card, Long> {

    public static final String TABLENAME = "CARD";

    /**
     * Properties of entity Card.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property PrivateKey = new Property(2, String.class, "privateKey", false, "PRIVATE_KEY");
        public final static Property SeedKey = new Property(3, String.class, "seedKey", false, "SEED_KEY");
        public final static Property DefAddress = new Property(4, String.class, "defAddress", false, "DEF_ADDRESS");
        public final static Property CardType = new Property(5, int.class, "cardType", false, "CARD_TYPE");
        public final static Property ChainType = new Property(6, int.class, "chainType", false, "CHAIN_TYPE");
        public final static Property CreateDate = new Property(7, long.class, "createDate", false, "CREATE_DATE");
        public final static Property IsBackUp = new Property(8, int.class, "isBackUp", false, "IS_BACK_UP");
        public final static Property AccountName = new Property(9, String.class, "accountName", false, "ACCOUNT_NAME");
    };

    private DaoSession daoSession;


    public CardDao(DaoConfig config) {
        super(config);
    }
    
    public CardDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CARD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"PRIVATE_KEY\" TEXT," + // 2: privateKey
                "\"SEED_KEY\" TEXT," + // 3: seedKey
                "\"DEF_ADDRESS\" TEXT," + // 4: defAddress
                "\"CARD_TYPE\" INTEGER NOT NULL ," + // 5: cardType
                "\"CHAIN_TYPE\" INTEGER NOT NULL ," + // 6: chainType
                "\"CREATE_DATE\" INTEGER NOT NULL ," + // 7: createDate
                "\"IS_BACK_UP\" INTEGER NOT NULL ," + // 8: isBackUp
                "\"ACCOUNT_NAME\" TEXT);"); // 9: accountName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CARD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Card entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String privateKey = entity.getPrivateKey();
        if (privateKey != null) {
            stmt.bindString(3, privateKey);
        }
 
        String seedKey = entity.getSeedKey();
        if (seedKey != null) {
            stmt.bindString(4, seedKey);
        }
 
        String defAddress = entity.getDefAddress();
        if (defAddress != null) {
            stmt.bindString(5, defAddress);
        }
        stmt.bindLong(6, entity.getCardType());
        stmt.bindLong(7, entity.getChainType());
        stmt.bindLong(8, entity.getCreateDate());
        stmt.bindLong(9, entity.getIsBackUp());
 
        String accountName = entity.getAccountName();
        if (accountName != null) {
            stmt.bindString(10, accountName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Card entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String privateKey = entity.getPrivateKey();
        if (privateKey != null) {
            stmt.bindString(3, privateKey);
        }
 
        String seedKey = entity.getSeedKey();
        if (seedKey != null) {
            stmt.bindString(4, seedKey);
        }
 
        String defAddress = entity.getDefAddress();
        if (defAddress != null) {
            stmt.bindString(5, defAddress);
        }
        stmt.bindLong(6, entity.getCardType());
        stmt.bindLong(7, entity.getChainType());
        stmt.bindLong(8, entity.getCreateDate());
        stmt.bindLong(9, entity.getIsBackUp());
 
        String accountName = entity.getAccountName();
        if (accountName != null) {
            stmt.bindString(10, accountName);
        }
    }

    @Override
    protected final void attachEntity(Card entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Card readEntity(Cursor cursor, int offset) {
        Card entity = new Card( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // privateKey
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // seedKey
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // defAddress
            cursor.getInt(offset + 5), // cardType
            cursor.getInt(offset + 6), // chainType
            cursor.getLong(offset + 7), // createDate
            cursor.getInt(offset + 8), // isBackUp
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // accountName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Card entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPrivateKey(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSeedKey(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDefAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCardType(cursor.getInt(offset + 5));
        entity.setChainType(cursor.getInt(offset + 6));
        entity.setCreateDate(cursor.getLong(offset + 7));
        entity.setIsBackUp(cursor.getInt(offset + 8));
        entity.setAccountName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Card entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Card entity) {
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
