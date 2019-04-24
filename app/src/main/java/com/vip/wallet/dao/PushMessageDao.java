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
 * DAO for table "PUSH_MESSAGE".
*/
public class PushMessageDao extends AbstractDao<PushMessage, Long> {

    public static final String TABLENAME = "PUSH_MESSAGE";

    /**
     * Properties of entity PushMessage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Type = new Property(3, int.class, "type", false, "TYPE");
        public final static Property Data = new Property(4, String.class, "data", false, "DATA");
        public final static Property Time = new Property(5, long.class, "time", false, "TIME");
        public final static Property IsRead = new Property(6, boolean.class, "isRead", false, "IS_READ");
    };


    public PushMessageDao(DaoConfig config) {
        super(config);
    }
    
    public PushMessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PUSH_MESSAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"CONTENT\" TEXT," + // 2: content
                "\"TYPE\" INTEGER NOT NULL ," + // 3: type
                "\"DATA\" TEXT," + // 4: data
                "\"TIME\" INTEGER NOT NULL ," + // 5: time
                "\"IS_READ\" INTEGER NOT NULL );"); // 6: isRead
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PUSH_MESSAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PushMessage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
        stmt.bindLong(4, entity.getType());
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(5, data);
        }
        stmt.bindLong(6, entity.getTime());
        stmt.bindLong(7, entity.getIsRead() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PushMessage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
        stmt.bindLong(4, entity.getType());
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(5, data);
        }
        stmt.bindLong(6, entity.getTime());
        stmt.bindLong(7, entity.getIsRead() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PushMessage readEntity(Cursor cursor, int offset) {
        PushMessage entity = new PushMessage( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.getInt(offset + 3), // type
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // data
            cursor.getLong(offset + 5), // time
            cursor.getShort(offset + 6) != 0 // isRead
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PushMessage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.getInt(offset + 3));
        entity.setData(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTime(cursor.getLong(offset + 5));
        entity.setIsRead(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PushMessage entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PushMessage entity) {
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