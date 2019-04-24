package com.vip.wallet.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/11 0011 15:03
 * 描述	      ${TODO}
 */
@Entity
public class Address {
    @Id(autoincrement = true)
    public Long id;
    public long cardId;
    public String address;
    public String privateKey;
    public long createDate;
    public int pathCount;

    @ToOne(joinProperty = "cardId")
    public Card card;

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1676389215)
    public void setCard(@NotNull Card card) {
        if (card == null) {
            throw new DaoException(
                    "To-one property 'cardId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.card = card;
            cardId = card.getId();
            card__resolvedKey = cardId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 476964834)
    public Card getCard() {
        long __key = this.cardId;
        if (card__resolvedKey == null || !card__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CardDao targetDao = daoSession.getCardDao();
            Card cardNew = targetDao.load(__key);
            synchronized (this) {
                card = cardNew;
                card__resolvedKey = __key;
            }
        }
        return card;
    }

    @Generated(hash = 10293163)
    private transient Long card__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 543375780)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAddressDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1580986028)
    private transient AddressDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCardId() {
        return this.cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1088810850)
    public Address(Long id, long cardId, String address, String privateKey, long createDate,
            int pathCount) {
        this.id = id;
        this.cardId = cardId;
        this.address = address;
        this.privateKey = privateKey;
        this.createDate = createDate;
        this.pathCount = pathCount;
    }

    @Generated(hash = 388317431)
    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", cardId=" + cardId +
                ", address='" + address + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public int getPathCount() {
        return this.pathCount;
    }

    public void setPathCount(int pathCount) {
        this.pathCount = pathCount;
    }
}
