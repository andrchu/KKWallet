package com.vip.wallet.http;


import com.blankj.utilcode.util.DeviceUtils;
import com.vip.wallet.R;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.utils.LogUtil;
import com.vip.wallet.utils.MD5;
import com.vip.wallet.utils.StringUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 14:44   <br/><br/>
 * 描述:	      ${TODO}
 */
public class RetrofitConfig {

    public static final int TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 20000;
    public static final int WRITE_TIMEOUT = 20000;

    /**
     * 缓存最大值
     */
    public static final int CACHE_MAX_SIZE = 1024 * 1024 * 30;

    public RetrofitConfig() {
        init();
    }

    private void init() {
        initOkhttp();
        initEthOkHttp();
    }

    private OkHttpClient initEthOkHttp() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //统一加入参数
        Interceptor uniformPara = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                HttpUrl originalHttpUrl = request.url();

                HttpUrl.Builder urlBuild = originalHttpUrl.newBuilder();

                HttpUrl url = urlBuild.build();

                LogUtil.getInstance(this.getClass()).i(String.format("request url : %s", request.toString()));

                Request.Builder requestBuilder = request.newBuilder()
                        .url(url);
                return chain.proceed(requestBuilder.build());
            }
        };

        builder.addInterceptor(uniformPara);
        //设置超时
        builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public ApiService getApiService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(initOkhttp())
                .addConverterFactory(ScConverterFactory.create(GsonAdapter.getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public EthService getEthService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.ETH_URL)
                .client(initEthOkHttp())
                .addConverterFactory(ScConverterFactory.create(GsonAdapter.getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(EthService.class);
    }

    private OkHttpClient initOkhttp() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //统一加入参数
        Interceptor uniformPara = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                HttpUrl originalHttpUrl = request.url();

                HttpUrl.Builder urlBuild = originalHttpUrl.newBuilder();
                String value = String.valueOf(System.currentTimeMillis() / 1000);
                urlBuild.addQueryParameter("timestamp", value);
                try {
                    urlBuild.addQueryParameter("device_id", DeviceUtils.getAndroidID());
                } catch (Exception e) {
                    e.printStackTrace();
                    urlBuild.addQueryParameter("device_id", "unknown");
                }
                String md5 = MD5.getMD5(StringUtil.toBytes(value + Constants.SIGN_KEY));
                urlBuild.addQueryParameter("sign", md5.substring(0, md5.length() / 2));
                urlBuild.addQueryParameter("version", Constants.API_VERSION);
            /*if (!StringUtil.isEmpty(shopId) && User.getUserConfig().isLogin) {    //判断是否登录过
                urlBuild.addQueryParameter("version", AppUtils.getAppVersionCode() + ""); //版本标示
				urlBuild.addQueryParameter("system_version", String.valueOf(Build.VERSION.SDK_INT)); //设备系统版本号
			}*/
                HttpUrl url = urlBuild.build();
                RequestBody body = request.body();
                FormBody formBody = null;
                if (body != null)
                    formBody = (FormBody) body;

                StringBuilder sb = new StringBuilder();
                if (formBody != null) {
                    for (int i = 0; i < formBody.size(); i++) {
                        sb.append("\n").append(formBody.name(i)).append(":").append(formBody.value(i));
                    }
                }

                LogUtil.getInstance(this.getClass()).i(String.format("request url : %s\nField : %s", url.toString(), sb.toString()));

                Request.Builder requestBuilder = request.newBuilder()
                        .url(url);
                return chain.proceed(requestBuilder.build());
            }
        };

        builder.addInterceptor(uniformPara);
        //设置超时
        builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //忽略证书
        //        X509TrustManager trustManager;
        //        SSLSocketFactory sslSocketFactory;
        //        try {
        //            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
        //            SSLContext sslContext = SSLContext.getInstance("TLS");
        //            sslContext.init(null, new TrustManager[]{trustManager}, null);
        //            sslSocketFactory = sslContext.getSocketFactory();
        //        } catch (GeneralSecurityException e) {
        //            throw new RuntimeException(e);
        //        }
        //
        //        builder.sslSocketFactory(sslSocketFactory, trustManager);
        initCer(builder);


        return builder.build();
    }

    private void initCer(OkHttpClient.Builder builder) {

        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            String certificateAlias = Integer.toString(0);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(ScApplication.getInstance().getResources().openRawResource(Constants.TEST ? R.raw.wallet : R.raw.kkwallet)));//拷贝好的证书
            SSLContext sslContext = SSLContext.getInstance("TLS");
            final TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
