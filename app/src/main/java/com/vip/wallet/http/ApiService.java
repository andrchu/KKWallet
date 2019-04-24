package com.vip.wallet.http;

import com.vip.wallet.entity.AdvertInfo;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.entity.AsKan;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 15:42   <br/><br/>
 * 描述:	      ${TODO}
 */

public interface ApiService {

    String ALL_WALLET_TOKEN_INFOS = "app/index/token_balances";

    String DEAL_RECORD_BY_TOKEN = "app/trans/token_deal_record";

    String DEAL_RECORD = "app/trans/deal_record";

    String SEND_AMOUNT = "app/trans/out";

    String USER_ADVICE = "app/index/advice";

    String SEARCH_TOKEN = "app/index/find_token";

    String CHECK_ASKAN = "app/activity/check_askan";

    String CHECK_EGG_ACTIVITY = "app/activity/check_cd";

    String CD_RES = "app/activity/cd_res";

    String CONF = "app/index/conf";

    String ADVERT = "/app/vote/adv";

    String TRANS_INFO = "/app/index/trans_info";

    String REGISTER_ADDRESS = "/app/index/register_address";

    String CENTER = "/app/index/center";

    String EOS_KEY_ACCOUNT = "/app/index/eos_key_account";

    String GET_EOS_BALANCE = "/app/index/get_eos_balance";

    String SYMBOL_PRICE = "/app/index/symbol_price";

    String GET_EOS_HEAD_INFO = "/app/trans/getEosHeadInfo";

    String EOS_AUTHF = "/app/trans/eos_authf";

    String EOS_ACCOUNT = "/app/index/eos_account";

    String EOS_AUTH_INFO = "/app/trans/eos_ao";

    String EOS_AUTH = "/app/trans/eos_auth";

    @POST(ALL_WALLET_TOKEN_INFOS)
    @FormUrlEncoded
    Observable<Response<ArrayList<WalletTokenInfos>>> getAllWalletTokenInfos(@Field("address") String allAddress,
                                                                             @Field("type") int chain_type);

    @POST(DEAL_RECORD_BY_TOKEN)
    @FormUrlEncoded
    Observable<Response<ArrayList<DealRecord>>> getTokenDealRecord(@Field("contractAddress") String contractAddress,
                                                                   @Field("address") String address,
                                                                   @Field("page") int page,
                                                                   @Field("raw") int raw);

    @POST(DEAL_RECORD)
    @FormUrlEncoded
    Observable<Response<ArrayList<DealRecord>>> getDealRecords(@Field("address") String address,
                                                               @Field("page") int page,
                                                               @Field("raw") int raw,
                                                               @Field("type") int type);

    @POST(SEND_AMOUNT)
    @FormUrlEncoded
    Observable<Response<String>> sendAmount(@FieldMap HashMap<String, String> hashMap);

    @POST(USER_ADVICE)
    @FormUrlEncoded
    Observable<Response> userAdvice(@Field("user") String user, @Field("content") String content);

    @POST(SEARCH_TOKEN)
    @FormUrlEncoded
    Observable<Response<ArrayList<SearchToken>>> searchToken(@Field("name") String key);

    @POST(CHECK_ASKAN)
    @FormUrlEncoded
    Observable<Response<AsKan>> checkAskan(@Field("address") String address);

    @POST(CHECK_EGG_ACTIVITY)
    @FormUrlEncoded
    Observable<Response> checkEggActivity(@Field("address") String address);

    @GET(CD_RES)
    Observable<Response<List<String>>> getInform();

    @GET(CONF)
    Observable<Response> getConfig();

    @GET(ADVERT)
    Observable<Response<AdvertInfo>> getAdvertInfo();

    @POST(TRANS_INFO)
    @FormUrlEncoded
    Observable<Response<ArrayList<UnSpent>>> getTransInfo(@Field("address") String address,
                                                          @Field("type") int type);

    @POST(REGISTER_ADDRESS)
    @FormUrlEncoded
    Observable<Response> registerAddress(@Field("address") String address);

    @GET(CENTER)
    Observable<Response<ArrayList<AppCenterItem>>> getAppCenterItem(@Query("address") String address);

    @POST(EOS_KEY_ACCOUNT)
    @FormUrlEncoded
    Observable<Response<EosAccount>> getEosAccountByPublicKey(@Field("public_key") String publicKey);

    @POST(GET_EOS_BALANCE)
    @FormUrlEncoded
    Observable<Response<List<String>>> getEosBalance(@Field("account") String account);

    @POST(SYMBOL_PRICE)
    @FormUrlEncoded
    Observable<Response<SymbolPrice>> getSymbolPrice(@Field("symbol") String symbol);

    @GET(GET_EOS_HEAD_INFO)
    Observable<Response<EosHeadInfo>> getEosHeadInfo();

    @POST(EOS_AUTHF)
    @FormUrlEncoded
    Observable<Response<EosAuthf>> commitTransfer(@Field("account") String account,
                                                  @Field("extra") String extra,
                                                  @Field("list") String list);

    @POST(EOS_ACCOUNT)
    @FormUrlEncoded
    Observable<Response<EosAccountInfo>> getEosAccount(@Field("account") String account);

    @POST(EOS_AUTH_INFO)
    @FormUrlEncoded
    Observable<Response<ArrayList<EosAuthInfo>>> getEosAuthInfo(@Field("order_id") String order_id);

    @POST(EOS_AUTH)
    @FormUrlEncoded
    Observable<Response> eosAuth(@Field("signs") String signs,
                                 @Field("account") String account,
                                 @Field("order_id") String order_id);
}