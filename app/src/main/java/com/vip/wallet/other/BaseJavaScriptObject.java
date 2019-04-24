package com.vip.wallet.other;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Base64;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.dao.Contact;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.ui.activity.ScannerCodeActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.utils.EncryptUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.utils.SystemUtil;
import com.vip.wallet.utils.ToastUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.vip.wallet.utils.StringUtil.getString;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/4 0004 15:51
 * 描述	      ${TODO}
 */

public class BaseJavaScriptObject {
    private BaseActivity mActivity;

    public BaseJavaScriptObject(BaseActivity activity) {
        mActivity = activity;
    }

    public static class Main {
        public String method;
        public String data;
        public String callback;

        public Main(String method, String data, String callback) {
            this.method = method;
            this.data = data;
            this.callback = callback;
        }
    }

    public static class Copy {

        /**
         * isShowSuccess : true
         * content : 内容
         */
        public boolean isShowSuccess;
        public String content;
    }

    public static class Share {

        /**
         * img_base64 :
         * title : 分享
         * type : 0
         * content : 内容
         * url :
         */
        public String img_base64;
        public String title;
        public int type;
        public String content;
        public String url;
    }

    public static class ShowHint {

        /**
         * type : 0
         * content : 内容
         */
        public String type;
        public String content;
    }

    public static class TitleViewAttrs {

        /**
         * bgColor : abababab+
         * title : 标题
         * textColor : a7f7e787
         */
        public String bgColor;
        public String title;
        public String textColor;
        public int showShadow;  //0-显示  1-隐藏
    }

    public static class ShowLoading {
        /**
         * titile :
         * isShow : 0
         */
        public String titile;
        public int isShow;
    }

    public static class OpenNativeWeb {

        /**
         * title : 快讯
         * url : http://www.kkwallet.io
         */
        public String title;
        public String url;
    }

    @android.webkit.JavascriptInterface
    public String nativeMethod(String jsonData) {
        try {
            LogUtils.e("jsonData >> " + jsonData);
            LogUtils.i("webView call Thread >> " + Thread.currentThread().getName());
            Main main;
            try {
                main = GsonAdapter.getGson().fromJson(jsonData, Main.class);
            } catch (JsonSyntaxException e) {
                JsonParser parser = new JsonParser();  //创建JSON解析器
                JsonObject parse = (JsonObject) parser.parse(jsonData);
                String method = parse.get("method").getAsString();
                JsonElement data = parse.get("data");
                String callback = parse.get("callback").getAsString();
                main = new Main(method, GsonAdapter.getGson().toJson(data), callback);
            }

            Class<? extends BaseJavaScriptObject> aClass = this.getClass();
            Method m = aClass.getMethod(main.method, Main.class);
            Object object = m.invoke(this, main);
            if (object != null) {
                return (String) object;
            } else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 复制内容
     */
    public void copy(Main main) {
        Copy copy = GsonAdapter.getGson().fromJson(String.valueOf(main.data), Copy.class);
        SystemUtil.clipString(ScApplication.getInstance(), copy.content);
        if (copy.isShowSuccess)
            ToastUtil.toastL(StringUtil.getString(R.string.copy_success));
    }

    /**
     * 获取地址列表包含私钥
     * 0 - ETH ; 1 - BTC ; 2 EOS
     */
    public String getAddressListByChain(Main main) {
        List<Card> list = ScApplication.getInstance().getDaoSession()
                .getCardDao()
                .queryBuilder()
                .where(CardDao.Properties.ChainType.eq(main.data))
                .build()
                .list();

        ArrayList<HashMap<String, String>> datas = new ArrayList<>();
        for (Card card : list) {
            HashMap<String, String> map = new HashMap<>();
            map.put("address", card.getDefAddress());
            map.put("privateKey", WalletHelper.decrypt(card.getPrivateKey()));
            map.put("chain_type", String.valueOf(card.chainType));
            map.put("accountName", card.getAccountName());
            datas.add(map);
        }
        String s = GsonAdapter.getGson().toJson(datas);
        return s;
    }

    /**
     * 通过地址获取私钥
     *
     * @return
     */
    public String getPrivateKeyByAddress(Main main) {
        List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao()
                .queryBuilder()
                .where(CardDao.Properties.DefAddress.eq(main.data))
                .build()
                .list();
        if (ListUtil.isEmpty(list)) {
            return "";
        }
        Card card = list.get(0);
        HashMap<String, String> map = new HashMap<>();
        map.put("address", card.getDefAddress());
        map.put("privateKey", WalletHelper.decrypt(card.getPrivateKey()));
        map.put("chain_type", String.valueOf(card.chainType));
        map.put("accountName", card.getAccountName());
        String s = GsonAdapter.getGson().toJson(map);
        LogUtils.i("getPrivateKeyByAddress >> " + s);
        return s;
    }

    /**
     * 获取移动端设备信息
     *
     * @return
     */
    public String getDeviceInfo(Main main) {
        HashMap<String, String> map = new HashMap<>();
        String androidID = DeviceUtils.getAndroidID();
        map.put("deviceId", androidID);
        return GsonAdapter.getGson().toJson(map);
    }

    /**
     * 获取APP信息
     *
     * @return
     */
    public String getAppInfo(Main main) {
        HashMap<String, String> map = new HashMap<>();
        map.put("version_name", AppUtils.getAppVersionName().contains("-debug") ? AppUtils.getAppVersionName().split("-")[0] : AppUtils.getAppVersionName());
        map.put("version_code", String.valueOf(AppUtils.getAppVersionCode()));
        return GsonAdapter.getGson().toJson(map);
    }

    /**
     * 用系统浏览器打开URL
     */
    public void openUrlByNativeBrowser(Main main) {
        Uri uri = Uri.parse(main.data);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ScApplication.getInstance().startActivity(intent);
    }

    /**
     * 关闭页面
     */
    public void finish(Main main) {
        if (mActivity != null) {
            mActivity.runOnUiThread(() -> mActivity.finish());
        }
    }

    /**
     * 分享
     */
    @android.webkit.JavascriptInterface
    public void share(Main main) {
        Share share = GsonAdapter.getGson().fromJson(main.data, Share.class);
        if (share.type == 0) {
            //分享文字
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, share.content);
            startActivity(Intent.createChooser(textIntent, share.title));
        } else {
            //分享图片
            if (mActivity != null)
                mActivity.showLoadingDialog(getString(R.string.later));
            Observable.unsafeCreate((Observable.OnSubscribe<File>) subscriber -> {
                try {
                    //解码图片
                    if (StringUtils.isEmpty(share.img_base64)) {
                        subscriber.onNext(null);
                        return;
                    }
                    byte[] decode = Base64.decode(share.img_base64, Base64.DEFAULT);
                    Bitmap bitmap = ImageUtils.bytes2Bitmap(decode);
                    //保存图片
                    if (bitmap == null) {
                        subscriber.onNext(null);
                        return;
                    }
                    File imageFile = new File(ScApplication.getInstance().getExternalCacheDir(), "temp.jpg");
                    ImageUtils.save(bitmap, imageFile, Bitmap.CompressFormat.JPEG, true);
                    subscriber.onNext(imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }).compose(RxUtil.rxSchedulerHelper())
                    .subscribe(new SimpSubscriber<File>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            ToastUtil.toastL(e.getMessage());
                            if (mActivity != null)
                                mActivity.hideLoadingDialog();
                        }

                        @Override
                        public void onNext(File file) {
                            if (file == null) {
                                if (mActivity != null)
                                    mActivity.hideLoadingDialog();
                                return;
                            }
                            //分享图片
                            String appPackageName = AppUtils.getAppPackageName();
                            Uri photoUri = FileProvider.getUriForFile(
                                    ScApplication.getInstance(),
                                    appPackageName + ".fileProvider",
                                    file);
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                            shareIntent.setType("image/*");
                            startActivity(Intent.createChooser(shareIntent, share.title));
                            if (mActivity != null) {
                                mActivity.hideLoadingDialog();
                            }
                        }
                    });
        }
    }

    /**
     * 提示
     * type - 0 成功 -1 失败 (Android 端忽略type)
     */
    public void showHint(Main main) {
        ShowHint showHint = GsonAdapter.getGson().fromJson(main.data, ShowHint.class);
        ToastUtil.toastL(showHint.content);
    }

    /**
     * 扫描二维码
     */
    public void openScannerQr(Main main) {
        if (mActivity != null) {
            ScannerCodeActivity.startScanner(mActivity, main.callback);
        }
    }

    /**
     * 获取联系人列表
     *
     * @param main
     * @return
     */
    public String getContactsList(Main main) {
        List<Contact> contacts = ScApplication.getInstance().getDaoSession().getContactDao().loadAll();
        return GsonAdapter.getGson().toJson(contacts);
    }

    /**
     * 显示或隐藏loading对话框
     *
     * @param main
     */
    public void showLoading(Main main) {
        ShowLoading showLoading = GsonAdapter.getGson().fromJson(main.data, ShowLoading.class);
        if (mActivity != null) {
            if (showLoading.isShow == 0) {
                mActivity.showLoadingDialog(showLoading.titile);
            } else {
                mActivity.hideLoadingDialog();
            }
        }

    }

    /**
     * 获取签名
     *
     * @param main
     * @return
     */
    public String getSignString(Main main) {
        return EncryptUtil.getSignString(main.data);
    }

    /**
     * 用APP内置浏览器打开URL
     */
    public void modalNativeWebController(Main main) {
        OpenNativeWeb openNativeWeb = GsonAdapter.getGson().fromJson(main.data, OpenNativeWeb.class);
        if (mActivity != null) {
            WebActivity.startWebActivity(mActivity, new BrowserInfo(openNativeWeb.title, openNativeWeb.url));
        }
    }
}