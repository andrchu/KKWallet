package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.subgraph.orchid.encoders.Hex;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.entity.BuyInfo;
import com.vip.wallet.entity.Response;
import com.vip.wallet.entity.SymbolPrice;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.BuyInfoContract;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.wallet.WalletHelper;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/26 0026 14:09
 * 描述	      ${TODO}
 */

public class BuyInfoPresenter extends RxPresenter<BuyInfoContract.IBuyInfoView> implements BuyInfoContract.IBuyInfoPresenter {
    public BuyInfoPresenter(BuyInfoContract.IBuyInfoView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    private HashMap<String, String> getMap(BuyInfo buyInfo, String haxData) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from_address", buyInfo.card.defAddress);
        map.put("to_address", buyInfo.toAddress);
        map.put("value", buyInfo.amount.equals("0") ? "0" : buyInfo.amount);
        map.put("token_address", "ETH");
        map.put("token_symbol", "ETH");
        map.put("memo", "dapp_pay");
        map.put("data", haxData);
        map.put("type", "0");
        LogUtils.i(GsonAdapter.getGson().toJson(map));
        return map;
    }

    @Override
    public void pay(BuyInfo buyInfo) {

        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<String>) subscriber -> {
            try {
                Web3j web3j = HttpRequest.getInstance().getWeb3j();

                BigInteger value = Convert.toWei(new BigDecimal(buyInfo.amount), Convert.Unit.ETHER).toBigInteger();

                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(buyInfo.card.defAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                BigInteger gasPrice = Convert.Unit.GWEI.getWeiFactor().multiply(new BigDecimal(buyInfo.gas_price)).toBigInteger();

                RawTransaction etherTransaction = RawTransaction.createTransaction(nonce, gasPrice, buyInfo.gas_limit, buyInfo.toAddress, value, buyInfo.data);

                byte[] privateKey = Hex.decode(WalletHelper.decrypt(buyInfo.card.getPrivateKey()));

                Credentials keys = Credentials.create(ECKeyPair.create(privateKey));
                //签名数据
                byte[] bytes = TransactionEncoder.signMessage(etherTransaction, ChainId.MAIN_NET, keys);

                String hexValue = Numeric.toHexString(bytes);

                LogUtils.i("hexValue >> " + hexValue);

                subscriber.onNext(hexValue);

            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(new Exception("未知错误"));
                subscriber.onCompleted();
            }
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        payToServer(buyInfo, s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.payError(e);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void getGasLimit(BuyInfo buyInfo) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<BigInteger>) subscriber -> {

            try {
                Web3j web3j = HttpRequest.getInstance().getWeb3j();

                BigInteger value = Convert.toWei(new BigDecimal(buyInfo.amount), Convert.Unit.ETHER).toBigInteger();

                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(buyInfo.card.defAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                Transaction functionCallTransaction = Transaction.createFunctionCallTransaction(buyInfo.card.defAddress, nonce
                        , new BigInteger(String.valueOf(buyInfo.gas_price)), new BigInteger("1"), buyInfo.toAddress, value, buyInfo.data);

                EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(functionCallTransaction).sendAsync().get();

                BigInteger gasLimit = ethEstimateGas.getAmountUsed().add(new BigInteger("10000"));

                subscriber.onNext(gasLimit);

            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(new Exception("获取失败！"));
            }

        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<BigInteger>() {
                    @Override
                    public void onNext(BigInteger bigInteger) {
                        view.getGasLimitSuccess(bigInteger);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getGasLimitError(e);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void getSymbolPrice() {
        Subscription subscription = HttpRequest.getInstance().getSymbolPrice("ETH")
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<SymbolPrice>() {
                    @Override
                    protected void onError(ApiHttpException exception) {

                    }

                    @Override
                    public void onNext(SymbolPrice symbolPrice) {
                        view.getSymbolPriceSuccess(symbolPrice);
                    }
                });
        addSubscribe(subscription);
    }

    private void payToServer(BuyInfo buyInfo, String haxData) {
        HashMap<String, String> map = getMap(buyInfo, haxData);
        Subscription subscribe = HttpRequest.getInstance().sendAmount(map)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response<String>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        exception.printStackTrace();
                        view.payError(exception);
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        if (response.isSuccess()) {
                            view.paySuccess(response.data);
                        } else {
                            onError(new ApiHttpException(response.message, 1));
                        }
                    }
                });
        addSubscribe(subscribe);

    }
}
