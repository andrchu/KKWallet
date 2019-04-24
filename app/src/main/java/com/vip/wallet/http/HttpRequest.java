package com.vip.wallet.http;


import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.AdvertInfo;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.entity.AsKan;
import com.vip.wallet.entity.Balance;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.entity.EosAccount;
import com.vip.wallet.entity.EosAccountInfo;
import com.vip.wallet.entity.EosAuthInfo;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.entity.EosHeadInfo;
import com.vip.wallet.entity.Response;
import com.vip.wallet.entity.SearchToken;
import com.vip.wallet.entity.SymbolPrice;
import com.vip.wallet.entity.UnSpent;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.wallet.WalletHelper;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 14:56   <br/><br/>
 * 描述:	      网络请求类
 */
public class HttpRequest {

    public static final String API_KEY = "56RV8UJKDYJZ9A914NARJBCRXEIR91ECBV";
    private static HttpRequest mHttpRequest;
    private final ApiService mApiService;
    private final Web3j mWeb3j;
    private final EthService mEthService;

    private HttpRequest() {
        RetrofitConfig retrofitConfig = new RetrofitConfig();
        mApiService = retrofitConfig.getApiService();
        mEthService = retrofitConfig.getEthService();
        mWeb3j = Web3jFactory.build(new HttpService((Constants.WEB3J_URL)));
    }

    public static HttpRequest getInstance() {
        if (mHttpRequest == null) {
            synchronized (HttpRequest.class) {
                if (mHttpRequest == null)
                    mHttpRequest = new HttpRequest();
            }
        }
        return mHttpRequest;
    }

    /**
     * 登录成功后需要调用此方法重置请求配置
     */
    public static void reSet() {
        mHttpRequest = null;
    }

    public Web3j getWeb3j() {
        return mWeb3j;
    }

    /**
     * 获取所有钱包代币余额
     *
     * @param allAddress
     * @param chain_type
     * @return
     */
    public Observable<Response<ArrayList<WalletTokenInfos>>> getAllWalletTokenInfos(String allAddress, int chain_type) {
        return mApiService.getAllWalletTokenInfos(allAddress, chain_type);
    }

    /**
     * 获取代币交易记录
     *
     * @param contractAddress
     * @param address
     * @return
     */
    public Observable<Response<ArrayList<DealRecord>>> getTokenDealRecord(String contractAddress, String address, int page, int raw) {
        return mApiService.getTokenDealRecord(contractAddress, address, page, raw);
    }

    /**
     * 获取交易记录
     *
     * @param address
     */
    public Observable<Response<ArrayList<DealRecord>>> getDealRecords(String address, int page, int raw, int type) {
        return mApiService.getDealRecords(address, page, raw, type);
    }

    /**
     * 发送代币
     *
     * @param hashMap
     */
    public Observable<Response<String>> sendAmount(HashMap<String, String> hashMap) {
        return mApiService.sendAmount(hashMap);
    }

    /**
     * 用户反馈
     *
     * @param user
     * @param content
     * @return
     */
    public Observable<Response> userAdvice(String user, String content) {
        return mApiService.userAdvice(user, content);
    }

    /**
     * 获取其他代余额
     *
     * @param contractaddress
     * @param address
     * @return
     */
    public Observable<Balance> getOtherTokenBalance(String contractaddress, String address) {
        HashMap<String, String> map = new HashMap<>();
        map.put("module", "account");
        map.put("action", "tokenbalance");
        map.put("contractaddress", contractaddress);
        map.put("address", address);
        map.put("tag", "latest");
        map.put("apikey", "YourApiKeyToken");
        return mEthService.getOtherTokenBalance(map);
    }


    /**
     * 搜索代币
     *
     * @param key
     * @return
     */
    public Observable<Response<ArrayList<SearchToken>>> searchToken(String key) {
        return mApiService.searchToken(key);
    }

    /**
     * 检测是否有问答活动
     *
     * @param address 钱包地址
     */
    public Observable<Response<AsKan>> checkAskan(String address) {
        return mApiService.checkAskan(address);
    }

    /**
     * 检查是否有彩蛋活动
     */
    public Observable<Response> checkEggActivity(String address) {
        return mApiService.checkEggActivity(address);
    }

    /**
     * 获取公告
     *
     * @return
     */
    public Observable<Response<List<String>>> getInform() {
        return mApiService.getInform();
    }

    public Observable<Response> getConfig() {
        return mApiService.getConfig();
    }

    public Observable<Response<AdvertInfo>> getAdvertInfo() {
        return mApiService.getAdvertInfo();
    }

    public Observable<Response<ArrayList<UnSpent>>> getTransInfo(String address) {
        return mApiService.getTransInfo(address, 1);
    }

    public Observable<Response> registerAddress(String address) {
        return mApiService.registerAddress(address);
    }

    public Observable<Response<ArrayList<AppCenterItem>>> getAppCenterItems() {
        return mApiService.getAppCenterItem(WalletHelper.getFirstEthAddress().getDefAddress());
    }

    public Observable<Response<EosAccount>> getEosAccountByPublicKey(String publicKey) {
        return mApiService.getEosAccountByPublicKey(publicKey);
    }

    public Observable<Response<List<String>>> getEosBalance(String account) {
        return mApiService.getEosBalance(account);
    }

    /**
     * 代币单价
     *
     * @param symbol 代币 ; 如 ETH,btc…(不区分大小写)
     * @return
     */
    public Observable<Response<SymbolPrice>> getSymbolPrice(String symbol) {
        return mApiService.getSymbolPrice(symbol);
    }

    /**
     * 获取eos签名头信息
     *
     * @return
     */
    public Observable<Response<EosHeadInfo>> getEosHeadInfo() {
        return mApiService.getEosHeadInfo();
    }

    /**
     * 提交交易工单
     *
     * @param account
     * @param extra
     * @param list
     * @return
     */
    public Observable<Response<EosAuthf>> commitTransfer(String account, String extra, String list) {
        return mApiService.commitTransfer(account, extra, list);
    }

    /**
     * 获取EOS账户信息
     *
     * @param account
     * @return
     */
    public Observable<Response<EosAccountInfo>> getEosAccount(String account) {
        return mApiService.getEosAccount(account);
    }

    /**
     * 根据订单号获取授权信息
     *
     * @param order_id
     * @return
     */
    public Observable<Response<ArrayList<EosAuthInfo>>> getEosAuthInfo(String order_id) {
        return mApiService.getEosAuthInfo(order_id);
    }

    /**
     * Eos 授权
     *
     * @param signs
     * @param account
     * @param order_id
     * @return
     */
    public Observable<Response> eosAuth(String signs, String account, String order_id) {
        return mApiService.eosAuth(signs, account, order_id);
    }

}
