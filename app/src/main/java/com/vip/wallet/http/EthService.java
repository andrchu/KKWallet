package com.vip.wallet.http;

import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.Balance;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/29 0029 10:24
 * 描述	      ${TODO}
 */

public interface EthService {
    @GET(Constants.ETH_URL)
    Observable<Balance> getOtherTokenBalance(@QueryMap Map<String, String> map);
}
