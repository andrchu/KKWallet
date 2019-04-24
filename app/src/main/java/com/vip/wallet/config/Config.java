package com.vip.wallet.config;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.wallet.WalletHelper;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/13 0013 11:18
 * 描述	     配置
 */

public class Config {
    private boolean isInit;
    private int currency_unit;   //0-人民币  1-美元
    private final SPUtils mInstance;
    private String currentWalletAddress; //当前钱包
    private int version;    //版本
    private String pwd;    //密码
    private String seedKey; //助记词
    private String userName;//账户名
    private boolean isShowProperty; //是否显示资产
    private boolean isBackUp; //是否备份过   true - 备份过    false-未备份
    private boolean isRegister; //是否注册过地址
    private String currentReceiveAddress;   //当前选择收币地址

    public String getCurrentReceiveAddress() {
        return currentReceiveAddress;
    }

    public void setCurrentReceiveAddress(String currentReceiveAddress) {
        this.currentReceiveAddress = currentReceiveAddress;
        mInstance.put(Constants.CURRENT_RECEIVE_ADDRESS, currentReceiveAddress, true);
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
        mInstance.put(Constants.IS_REGISTER, isRegister, true);
    }

    public boolean isBackUp() {
        return isBackUp;
    }

    public void setBackUp(boolean backUp) {
        isBackUp = backUp;
        mInstance.put(Constants.IS_BACK_UP, isBackUp, true);
    }

    public boolean isShowProperty() {
        return isShowProperty;
    }

    public void setShowProperty(boolean showProperty) {
        isShowProperty = showProperty;
        mInstance.put(Constants.IS_SHOW_PROPERTY, isShowProperty, true);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        mInstance.put(Constants.USER_NAME, userName, true);
    }

    public String getSeedKey() {
        return seedKey;
    }

    public Config setSeedKey(String seedKey) {
        String hexPwd = WalletHelper.encrypt(seedKey);
        this.seedKey = seedKey;
        mInstance.put(Constants.SEED_KEY, hexPwd, true);
        return this;
    }

    public String getPwd() {
        return this.pwd;
    }

    public Config setPwd(String pwd) {
        String hexPwd = WalletHelper.encrypt(pwd);
        this.pwd = pwd;
        mInstance.put(Constants.PWD, hexPwd, true);
        return this;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
        mInstance.put(Constants.VERSION, version, true);
    }

    public String getCurrentWallet() {
        return currentWalletAddress;
    }

    public void setCurrentWallet(String currentWalletAddress) {
        this.currentWalletAddress = currentWalletAddress;
        mInstance.put(Constants.CURRENT_WALLET, currentWalletAddress, true);
    }

    {
        mInstance = ScApplication.getInstance().getSpInstance();
    }

    public Config(boolean isInit, int currency_unit, int version, String pwd, String seedKey,
                  String userName, boolean isShowProperty, boolean isBackUp, boolean isRegister
            , String currentReceiveAddress) {
        this.isInit = isInit;
        this.currency_unit = currency_unit;
        this.version = version;
        this.pwd = pwd;
        this.seedKey = seedKey;
        this.userName = userName;
        this.isShowProperty = isShowProperty;
        this.isBackUp = isBackUp;
        this.isRegister = isRegister;
        this.currentReceiveAddress = currentReceiveAddress;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
        mInstance.put(Constants.INIT, isInit, true);
    }

    public int getCurrency_unit() {
        return currency_unit;
    }

    public void setCurrency_unit(int currency_unit) {
        this.currency_unit = currency_unit;
        mInstance.put(Constants.CURRENCY_UNIT, currency_unit, true);
    }


    public void saveConfig() {
        mInstance.put(Constants.INIT, isInit);
        mInstance.put(Constants.CURRENCY_UNIT, currency_unit);
        mInstance.put(Constants.CURRENT_WALLET, currentWalletAddress);
        mInstance.put(Constants.VERSION, version);
    }

    public static Config getConfig() {
        SPUtils instance = ScApplication.getInstance().getSpInstance();
        boolean init = instance.getBoolean(Constants.INIT, false);
        int unit = instance.getInt(Constants.CURRENCY_UNIT, 0);
        int version = instance.getInt(Constants.VERSION, 0);
        String pwd = instance.getString(Constants.PWD);
        String seed_key = instance.getString(Constants.SEED_KEY);
        String user_name = instance.getString(Constants.USER_NAME);
        boolean isShowProperty = instance.getBoolean(Constants.IS_SHOW_PROPERTY, true);
        boolean isBackUp = instance.getBoolean(Constants.IS_BACK_UP, false);
        boolean is_register = instance.getBoolean(Constants.IS_REGISTER, false);
        //密码
        if (!StringUtils.isEmpty(pwd))
            pwd = WalletHelper.decrypt(pwd);
        //助记词
        if (!StringUtils.isEmpty(seed_key))
            seed_key = WalletHelper.decrypt(seed_key);
        String current_receive_address = instance.getString(Constants.CURRENT_RECEIVE_ADDRESS);

        return new Config(init, unit, version, pwd, seed_key, user_name, isShowProperty, isBackUp, is_register, current_receive_address);
    }

    public void logOut() {
        setBackUp(false);
        setSeedKey("");
        setCurrency_unit(0);
        setVersion(Constants.CONFIG_VERSION_17);
        setInit(false);
        setPwd("");
        setUserName("");
        setShowProperty(true);
        setRegister(false);
        setCurrentReceiveAddress("");
    }

    public void init() {
        //        SPUtils.getInstance(Constants.VIP_WALLET).clear(true);
    }
}
