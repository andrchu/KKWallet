package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.quincysx.crypto.bitcoin.BTCTransaction;
import com.quincysx.crypto.bitcoin.BitCoinECKeyPair;
import com.quincysx.crypto.utils.HexUtils;
import com.subgraph.orchid.encoders.Hex;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.entity.Balance;
import com.vip.wallet.entity.BtcTransInfo;
import com.vip.wallet.entity.EosAccountInfo;
import com.vip.wallet.entity.OutToken;
import com.vip.wallet.entity.Response;
import com.vip.wallet.entity.UnSpent;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.SendTokenContract;
import com.vip.wallet.utils.CalcUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.wallet.WalletHelper;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 15:37
 * 描述	      ${TODO}
 */

public class SendTokenPresenter extends RxPresenter<SendTokenContract.ISendTokenView>
        implements SendTokenContract.ISendTokenPresenter {

    private Subscription mEthBalanceSubscribe;
    private Subscription mLimitSubScription;

    public SendTokenPresenter(SendTokenContract.ISendTokenView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void sendEthTypeAmount(OutToken outToken) {

        if (outToken.token.contractAddress.equals("ETH")) { //ETH
            sendEthAmount(outToken);
            return;
        }

        Subscription transfer = Observable.unsafeCreate((Observable.OnSubscribe<String>) subscriber -> {

            try {
                Web3j web3j = HttpRequest.getInstance().getWeb3j();
                //获取nonce
                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(outToken.fromAddress, DefaultBlockParameterName.LATEST).send();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                com.vip.wallet.dao.Address addressObjAddress = WalletHelper.getAddressObjAddress(outToken.fromAddress);

                byte[] privateKey = Hex.decode(WalletHelper.decrypt(addressObjAddress.getPrivateKey()));

                Credentials keys = Credentials.create(ECKeyPair.create(privateKey));

                BigInteger nTokens = outToken.outCount.multiply(new BigDecimal(Math.pow(10, outToken.token.decimals))).toBigInteger();

                LogUtils.i("tokens>> " + nTokens);

                List<Type> params = Arrays.asList(new Address(outToken.toAddress), new Uint256(nTokens));

                List<TypeReference<?>> returnTypes = Arrays.asList(new TypeReference<Bool>() {
                });

                Function function = new Function("transfer", params, returnTypes);

                String data = FunctionEncoder.encode(function);

                BigInteger gasPrice = Convert.Unit.GWEI.getWeiFactor().multiply(new BigDecimal(outToken.gasPrice_seek_bar)).toBigInteger();

                BigInteger gasLimit = outToken.gasLimit_server;

                LogUtils.i("gas Limit >> " + gasLimit + "         -------  gas Price >> " + gasPrice + "  ---------outToken.gasPrice_seek_bar = " + outToken.gasPrice_seek_bar);

                RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, outToken.token.contractAddress, new BigInteger("0"), data);

                LogUtils.i(GsonAdapter.getGson().toJson(rawTransaction));

                //签名数据
                byte[] bytes = TransactionEncoder.signMessage(rawTransaction, ChainId.MAIN_NET, keys);

                String hexValue = Numeric.toHexString(bytes);

                subscriber.onNext(hexValue);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(new Exception("未知错误"));
            } finally {
                subscriber.onCompleted();
            }
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.sendAmountError(e);
                    }

                    @Override
                    public void onNext(String haxData) {
                        sendAmountToServer(outToken, haxData);
                    }
                });
        addSubscribe(transfer);
    }

    private void sendAmountToServer(OutToken outToken, String haxData) {
        Subscription subscribe = HttpRequest.getInstance().sendAmount(getMap(outToken, haxData))
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.sendAmountError(exception);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            view.sendAmountSuccess(response.message);
                        } else {
                            onError(new ApiHttpException(response.message, response.code));
                        }
                    }
                });
        addSubscribe(subscribe);

    }

    private HashMap<String, String> getMap(OutToken outToken, String haxData) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from_address", outToken.fromAddress);
        map.put("to_address", outToken.toAddress);
        map.put("value", outToken.outCount.toString());
        String contractAddress = StringUtil.formatAddress(outToken.token.contractAddress);
        map.put("token_address", contractAddress.contains("ETH") || contractAddress.contains("BTC") || contractAddress.contains("EOS") ? StringUtil.getNo0xAddress(contractAddress) : contractAddress);
        map.put("token_symbol", outToken.token.tokenName);
        map.put("memo", outToken.memo);
        map.put("data", haxData);
        map.put("type", String.valueOf(outToken.chain_type));
        LogUtils.i(GsonAdapter.getGson().toJson(map));
        return map;
    }

    @Override
    public void sendEthAmount(OutToken outToken) {

        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<String>) subscriber -> {
            try {
                Web3j web3j = HttpRequest.getInstance().getWeb3j();

                BigInteger value = Convert.toWei(outToken.outCount, Convert.Unit.ETHER).toBigInteger();

                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(outToken.fromAddress, DefaultBlockParameterName.LATEST).send();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                BigInteger gasPrice = Convert.Unit.GWEI.getWeiFactor().multiply(new BigDecimal(outToken.gasPrice_seek_bar)).toBigInteger();

                BigInteger gasLimit = outToken.gasLimit_server;

                RawTransaction etherTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, outToken.toAddress, value);

                com.vip.wallet.dao.Address addressObjAddress = WalletHelper.getAddressObjAddress(outToken.fromAddress);

                byte[] privateKey = Hex.decode(WalletHelper.decrypt(addressObjAddress.getPrivateKey()));

                Credentials keys = Credentials.create(ECKeyPair.create(privateKey));
                //签名数据
                byte[] bytes = TransactionEncoder.signMessage(etherTransaction, ChainId.MAIN_NET, keys);

                String hexValue = Numeric.toHexString(bytes);

                subscriber.onNext(hexValue);

            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(new Exception("未知错误"));
                subscriber.onCompleted();
            }
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.sendAmountError(e);
                    }

                    @Override
                    public void onNext(String hexValue) {
                        sendAmountToServer(outToken, hexValue);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void getAllCount(OutToken outToken) {
        view.getAllCountSuccess(new Balance());
    }

    @Override
    public void getLimit(OutToken outToken) {
        if (outToken.token.contractAddress.equals("ETH")) {
            view.getLimitSuccess(new BigInteger("21000"));
            view.hideLoadingDialog();
            return;
        }
        if (mLimitSubScription != null)
            mLimitSubScription.unsubscribe();
        mLimitSubScription = Observable.unsafeCreate((Observable.OnSubscribe<BigInteger>) subscriber -> {
            try {
                Web3j web3j = HttpRequest.getInstance().getWeb3j();
                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(outToken.fromAddress, DefaultBlockParameterName.LATEST).send();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                BigInteger nTokens = outToken.outCount.multiply(BigDecimal.valueOf(Math.pow(10, outToken.token.decimals))).toBigInteger();
                LogUtils.i("tokens>> " + nTokens);
                List<Type> params = Arrays.asList(new Address("5c025ef4421189f1244d8d62edecd60904ec7903"), new Uint256(nTokens));
                List<TypeReference<?>> returnTypes = Arrays.asList(new TypeReference<Bool>() {
                });

                Function function = new Function("transfer", params, returnTypes);

                String data = FunctionEncoder.encode(function);

                Transaction etherTransaction = Transaction.createFunctionCallTransaction(outToken.fromAddress, nonce, new BigInteger("1"), new BigInteger("1"), outToken.token.contractAddress, data);
                EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(etherTransaction).sendAsync().get();
                if (ethEstimateGas.getError() == null) {
                    subscriber.onNext(Numeric.decodeQuantity(ethEstimateGas.getResult()));
                } else {
                    subscriber.onError(new Exception(ethEstimateGas.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Subscriber<BigInteger>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                        view.getLimitError(e);
                        LogUtils.e("get gas limit error >> " + e.getMessage());
                    }

                    @Override
                    public void onNext(BigInteger o) {
                        view.getLimitSuccess(o);
                        LogUtils.i("get gas limit >> " + o);
                    }
                });
        addSubscribe(mLimitSubScription);
    }

    @Override
    public void getEthBalance(String address) {

        if (mEthBalanceSubscribe != null) {
            mEthBalanceSubscribe.unsubscribe();
            view.hideLoadingDialog();
        }

        mEthBalanceSubscribe = Observable.unsafeCreate((Observable.OnSubscribe<BigDecimal>) subscriber -> {
            try {
                Web3j web3j = HttpRequest.getInstance().getWeb3j();
                EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

                if (ethGetBalance.getError() == null) {
                    BigDecimal divide = new BigDecimal(ethGetBalance.getBalance()).divide(Convert.Unit.ETHER.getWeiFactor());
                    subscriber.onNext(divide);
                } else
                    subscriber.onError(new Exception(ethGetBalance.getError().getMessage()));

            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Subscriber<BigDecimal>() {
                    @Override
                    public void onCompleted() {
                        view.hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getEthBalanceError(e);
                        LogUtils.e("get eth Balance error >> " + e.getMessage());
                    }

                    @Override
                    public void onNext(BigDecimal bigDecimal) {
                        view.getEthBalanceSuccess(bigDecimal);
                        LogUtils.i("get eth Balance >> " + bigDecimal);
                    }
                });
        addSubscribe(mEthBalanceSubscribe);
    }

    @Override
    public void getOtherTokenBalance(OutToken outToken) {
        Subscription subscribe = HttpRequest.getInstance().getOtherTokenBalance(outToken.token.contractAddress, outToken.fromAddress)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Balance>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.getTokenBalanceError();
                    }

                    @Override
                    public void onNext(Balance balance) {
                        if (StringUtils.isEmpty(balance.result)) {
                            onError(new ApiHttpException(balance.message, 1));
                        } else {
                            BigDecimal div = CalcUtil.div(balance.result, BigDecimal.TEN.pow(outToken.token.decimals).toString());
                            LogUtils.i("balance >> " + div.toString());
                            view.getTokenBalanceSuccess(div);
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void getBtcBalance(String defAddress) {
        Subscription subscribe = HttpRequest.getInstance().getTransInfo(defAddress)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<UnSpent>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.getBtcBalanceError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<UnSpent> unSpents) {
                        BtcTransInfo btcTransInfo = new BtcTransInfo();
                        btcTransInfo.unSpents = unSpents;
                        if (ListUtil.isEmpty(unSpents)) {
                            btcTransInfo.totalAmount = 0;
                        } else {
                            for (UnSpent unSpent : unSpents) {
                                btcTransInfo.totalAmount = CalcUtil.add(String.valueOf(btcTransInfo.totalAmount), String.valueOf(unSpent.amount)).doubleValue();
                            }
                        }
                        view.getBtcBalanceSuccess(btcTransInfo);

                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void sendBtcAmount(OutToken outToken, BtcTransInfo info) {

        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<String>) subscriber -> {

            try {
                BTCTransaction.Output[] outputs = new BTCTransaction.Output[2];
                BTCTransaction.Input[] inputs = new BTCTransaction.Input[info.unSpents.size()];
                for (int i = 0; i < info.unSpents.size(); i++) {
                    UnSpent unSpent = info.unSpents.get(i);
                    BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex(unSpent.txid), unSpent.vout);
                    inputs[i] = new BTCTransaction.Input(outPoint, BTCTransaction.Script.buildOutput(unSpent.address), 0xffffffff);
                }

                BigDecimal out = outToken.outCount;
                BigDecimal fee = new BigDecimal(outToken.fee);
                BigDecimal zhaoling = CalcUtil.sub(String.valueOf(outToken.mBtcTransInfo.totalAmount), out.toString()).subtract(fee);

                BTCTransaction.Output output1 = new BTCTransaction.Output(CalcUtil.mul(out.toString(), "100000000").longValue(), BTCTransaction.Script.buildOutput(outToken.toAddress));
                outputs[0] = output1;
                BTCTransaction.Output output2 = new BTCTransaction.Output(CalcUtil.mul(zhaoling.toString(), "100000000").longValue(), BTCTransaction.Script.buildOutput(outToken.fromAddress));
                outputs[1] = output2;

                BTCTransaction transaction = new BTCTransaction(inputs, outputs, 0);
                //签名
                com.vip.wallet.dao.Address addressObjAddress = WalletHelper.getAddressObjAddress(outToken.fromAddress);

                String decrypt = WalletHelper.decrypt(addressObjAddress.getPrivateKey());
                //                byte[] privateKey = org.spongycastle.util.encoders.Hex.decode(decrypt);

                //                ExtendedKey extendedKey = ExtendedKey.parsePrivateKey(privateKey);
                BitCoinECKeyPair key = BitCoinECKeyPair.parseWIF(decrypt);

                //                com.quincysx.crypto.ECKeyPair key = new BitCoinECKeyPair(keyPair, false);
                byte[] signByte = transaction.sign(key);

                String signTx = HexUtils.toHex(signByte);
                subscriber.onNext(signTx);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(new Exception("数据处理失败"));
            }
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<String>() {
                    @Override
                    public void onNext(String haxData) {
                        LogUtils.i("signTx >> " + haxData);
                        sendAmountToServer(outToken, haxData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("signTx error >> " + e.getMessage());
                        view.sendAmountError(e);
                    }
                });
        addSubscribe(subscribe);

    }

    @Override
    public void getEosBalance(String accountName) {
        Subscription subscribe = HttpRequest.getInstance().getEosBalance(accountName)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response<List<String>>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.getEosBalanceError(exception);
                    }

                    @Override
                    public void onNext(Response<List<String>> listResponse) {
                        if (listResponse.isSuccess()) {
                            if (ListUtil.isEmpty(listResponse.data)) {
                                view.getEosBalanceSuccess("0");
                            } else {
                                view.getEosBalanceSuccess(listResponse.data.get(0));
                            }
                        } else {
                            onError(new ApiHttpException("获取失败!", 1));
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void sendEosAmount(OutToken outToken) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<HashMap<String, String>>) subscriber -> {
            //获取私钥
            String privateKey = WalletHelper.decrypt(outToken.currentCard.privateKey);
            try {

                //签名交易数据
//                String signData = new Rpc(Constants.EOS_RPC_URL).transferSign(privateKey, "eosio.token",
//                        outToken.fromAddress, outToken.toAddress, outToken.outCount.toString() + " EOS", outToken.memo);
//                HashMap<String, String> map = getMap(outToken, signData);
//                subscriber.onNext(map);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<HashMap<String, String>>() {
                    @Override
                    public void onNext(HashMap<String, String> map) {
                        pushEosTransfer(map);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.sendAmountError(e);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void getEosAccountInfo(OutToken outToken) {
        Subscription subscribe = HttpRequest.getInstance().getEosAccount(outToken.fromAddress)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<EosAccountInfo>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        exception.printStackTrace();
                        view.getEosAccountInfoError(exception);
                    }

                    @Override
                    public void onNext(EosAccountInfo eosAccountInfo) {
                        view.getEosAccountInfoSuccess(eosAccountInfo);
                    }
                });
        addSubscribe(subscribe);
    }

    private void pushEosTransfer(HashMap<String, String> map) {
        Subscription subscribe = HttpRequest.getInstance().sendAmount(map)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        exception.printStackTrace();
                        view.sendAmountError(exception);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            view.sendAmountSuccess(response.message);
                        } else {
                            onError(new ApiHttpException(response.message, 1));
                        }
                    }
                });
        addSubscribe(subscribe);
    }
}
