package com.vip.wallet.config;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.vip.wallet.BuildConfig;
import com.vip.wallet.R;
import com.vip.wallet.dao.ContactOpenHelper;
import com.vip.wallet.dao.DaoMaster;
import com.vip.wallet.dao.DaoSession;
import com.vip.wallet.entity.User;
import com.vip.wallet.ui.activity.HomeActivity;
import com.vip.wallet.wallet.EthWallet;
import com.vip.wallet.wallet.WalletHelper;

import java.io.File;
import java.security.Security;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.Fragmentation;
import ren.yale.android.cachewebviewlib.CacheWebView;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 14:16   <br/><br/>
 * 描述:	      ${TODO}
 */
public class ScApplication extends MultiDexApplication {
    private Handler mHandler;
    private User mUser;
    private ContactOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private Config mConfig;
    private EthWallet mEthWallet;
    private boolean isUnLock = true;   //true 未解锁   false已解锁
    private SPUtils spInstance;

    public boolean isUnLock() {
        return isUnLock;
    }

    public void setUnLock(boolean unLock) {
        isUnLock = unLock;
    }

    public EthWallet getEthWallet() {
        if (mEthWallet == null) {
            mEthWallet = WalletHelper.readWallet(EthWallet.class, "eth.wt");
        }
        return mEthWallet;
    }

    public void setEthWallet(EthWallet ethWallet) {
        mEthWallet = ethWallet;
    }

    public Config getConfig() {
        if (mConfig == null) {
            mConfig = Config.getConfig();
        }
        return mConfig;
    }

    public void resetConfig() {
        mConfig = null;
    }

    public String getUnitSymbol() {
        boolean b = getConfig().getCurrency_unit() == 0;    //0-人民币
        return b ? "≈\u00A5" : "≈＄";
    }

    public String getUnit() {
        boolean b = getConfig().getCurrency_unit() == 0;    //0-人民币
        return b ? "\u00A5" : "＄";
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        if (mUser == null)
            mUser = User.getUser();
        return mUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initObj();
        initUtils();
        initFragmention();
        initDatabase();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                initGlide();
                initProvider();
                initZxing();
                initUmengStat();
                initAutoUpdate();
                initJPush();
                initX5();
            }
        }, 500);
    }

    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    private void initGlide() {
        //        Glide.get(this).getRegistry().replace(GlideUrl.class, InputStream.class, );
    }


    private void initFragmention() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                // 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                .handleException(e -> {
                    // 建议在该回调处上传至我们的Crash监测服务器
                    // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                    // Bugtags.sendException(e);


                })
                .install();
    }

    private void initWebView() {
        File cacheFile = new File(this.getCacheDir(), "sc_web_cache");
        CacheWebView.getCacheConfig().init(this, cacheFile.getAbsolutePath(), 1, 1)
                .enableDebug(true);//100M 磁盘缓存空间,10M 内存缓存空间
        CacheWebView.servicePreload(this, "https://etherscan.io/");
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if (JPushInterface.isPushStopped(this)) {
            JPushInterface.resumePush(this);
        }
        JPushInterface.setAlias(this, 10, DeviceUtils.getAndroidID());
    }

    private void initAutoUpdate() {
        //        自动初始化开关
        Beta.autoInit = true;
        //        自动检查更新开关
        Beta.autoCheckUpgrade = true;
        //        升级检查周期设置
        Beta.upgradeCheckPeriod = 10 * 60 * 1000;
        //        延迟初始化
        Beta.initDelay = 1000;
        //        设置通知栏大图标
        Beta.largeIconId = R.mipmap.ic_launcher;
        //        设置状态栏小图标
        Beta.smallIconId = R.mipmap.ic_launcher_round;
        //        设置是否显示消息通知
        Beta.enableNotification = true;
        //        设置sd卡的Download为更新资源存储目录
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //        添加可显示弹窗的Activity
        Beta.canShowUpgradeActs.add(HomeActivity.class);
        //        设置自定义升级对话框UI布局
        Beta.upgradeDialogLayoutId = R.layout.dialog_update_app;

        Bugly.init(getApplicationContext(), "447cbc70b4", false);
    }

    private void initUmengStat() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_DUM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5ab35b94f43e486bcf0001c8", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        String androidID = DeviceUtils.getAndroidID();
        LogUtils.i("androidID :: " + androidID);
        MobclickAgent.onProfileSignIn(androidID);
    }


    private void initZxing() {
        ZXingLibrary.initDisplayOpinion(this);
    }

    private void initProvider() {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private void initUtils() {
        Utils.init(this);
    }

    private void initObj() {
        mDDApplication = this;
        mHandler = new Handler();
        spInstance = SPUtils.getInstance(Constants.VIP_WALLET);
    }

    public SPUtils getSpInstance() {
        return spInstance;
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 初始化greenDao
     */
    private void initDatabase() {
        mHelper = new ContactOpenHelper(this, null);
        mDb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private static ScApplication mDDApplication;

    /**
     * 获取Application对象
     *
     * @return DDApplication
     */
    public static ScApplication getInstance() {
        return mDDApplication;
    }
}
