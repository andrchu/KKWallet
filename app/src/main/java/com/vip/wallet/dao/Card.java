package com.vip.wallet.dao;

import com.vip.wallet.R;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.utils.StringUtil;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/11 0011 14:43
 * 描述	      ${TODO}
 */
@Entity
public class Card implements Serializable {
    @Id(autoincrement = true)
    public Long id;
    public String name;
    public String privateKey;
    public String seedKey;
    public String defAddress;
    public int cardType;    //1-默认卡包
    public int chainType;   //(0-ETH  1-Btc  2-EOS)
    public long createDate;
    public int isBackUp; //0-未备份  1-已备份
    public String accountName;//EOS账户名
    @Transient
    public boolean isLoadEnd;   //是否加载完
    @Transient
    public String amount = "0.00";   //资产

    public boolean isDefCard() {
        return cardType == 1;
    }


    public String getAddressOrAccount() {
        if (chainType == 2) {
            return accountName;
        } else {
            return defAddress;
        }
    }

    public int getIconResId() {
        switch (chainType) {
            case 0:
                return R.drawable.eth50;
            case 1:
                return R.drawable.btc50;
            case 2:
                return R.drawable.eos50;
            default:
                return R.drawable.eth50;
        }
    }

    public int getPropertyCardBgResId() {
        if (chainType == 0) {
            return R.drawable.property_card_eth;
        } else if (chainType == 1) {
            return R.drawable.property_card_btc;
        } else {
            return R.drawable.property_card_eos;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Card card = (Card) o;

        return id != null ? id.equals(card.id) : card.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @ToMany(referencedJoinProperty = "cardId")
    public List<Address> addressList;

    public String getChainTypeString() {
        return StringUtil.getChainName(chainType);
    }

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
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1977742708)
    public synchronized void resetAddressList() {
        addressList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1620504076)
    public List<Address> getAddressList() {
        if (addressList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AddressDao targetDao = daoSession.getAddressDao();
            List<Address> addressListNew = targetDao._queryCard_AddressList(id);
            synchronized (this) {
                if (addressList == null) {
                    addressList = addressListNew;
                }
            }
        }
        return addressList;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1693529984)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCardDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 599084715)
    private transient CardDao myDao;
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

    public int getChainType() {
        return this.chainType;
    }

    public void setChainType(int chainType) {
        this.chainType = chainType;
    }

    public int getCardType() {
        return this.cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getDefAddress() {
        return this.defAddress;
    }

    public void setDefAddress(String defAddress) {
        this.defAddress = defAddress;
    }

    public String getSeedKey() {
        return this.seedKey;
    }

    public void setSeedKey(String seedKey) {
        this.seedKey = seedKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1930087634)
    public Card(Long id, String name, String privateKey, String seedKey, String defAddress,
                int cardType, int chainType, long createDate, int isBackUp, String accountName) {
        this.id = id;
        this.name = name;
        this.privateKey = privateKey;
        this.seedKey = seedKey;
        this.defAddress = defAddress;
        this.cardType = cardType;
        this.chainType = chainType;
        this.createDate = createDate;
        this.isBackUp = isBackUp;
        this.accountName = accountName;
    }

    @Generated(hash = 52700939)
    public Card() {
    }

    @Override
    public String toString() {
        return GsonAdapter.getGson().toJson(this);
    }

    public int getIsBackUp() {
        return this.isBackUp;
    }

    public void setIsBackUp(int isBackUp) {
        this.isBackUp = isBackUp;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
