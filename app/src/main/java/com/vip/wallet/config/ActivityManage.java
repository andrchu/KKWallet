package com.vip.wallet.config;


import com.vip.wallet.base.BaseActivity;

import java.util.ArrayList;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 11:32   <br/><br/>
 * 描述:			管理Activity
 */
public class ActivityManage {

    private ArrayList<BaseActivity> activityContainer;
    private static ActivityManage mActivityManage;

    private ActivityManage() {
        activityContainer = new ArrayList<>();
    }

    public static ActivityManage getInstance() {
        if (mActivityManage == null) {
            synchronized (ActivityManage.class) {
                if (mActivityManage == null) {
                    mActivityManage = new ActivityManage();
                }
            }
        }
        return mActivityManage;
    }

    /**
     * 获取Activity容器
     *
     * @return ArrayList
     */
    public ArrayList<BaseActivity> getActivityContainer() {
        return activityContainer;
    }

    /**
     * 检测某个Activity是否存活
     *
     * @return boolean true-存活的
     */
    public boolean isActive(Class<? extends BaseActivity> clazz) {
        for (BaseActivity activity : getActivityContainer()) {
            if (activity.getClass() == clazz) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测某个Activity是否显示在最前面
     *
     * @return boolean true-该activity显示在最前面
     */
    public boolean isTop(Class<? extends BaseActivity> clazz) {

        int size = getActivityContainer().size();

        if (size == 0) {
            return false;
        }

        BaseActivity activity = getActivityContainer().get(size - 1);

        return activity.getClass() == clazz;
    }


    /**
     * 把activity添加到集合中
     *
     * @param activity
     */
    public void addActivity(BaseActivity activity) {
        getActivityContainer().add(activity);
    }

    public BaseActivity getActivity(Class tClass) {
        for (BaseActivity baseActivity : getActivityContainer()) {
            if (baseActivity.getClass().getSimpleName().equals(tClass.getSimpleName())) {
                return baseActivity;
            }
        }
        return null;
    }


    /**
     * 从集合中移除
     *
     * @param activity
     */
    public void removeActivity(BaseActivity activity) {
        getActivityContainer().remove(activity);
    }

    //	/**
    //	 * 判断本应用是否显示在前台
    //	 */
    //	public boolean isForeground() {
    //		Context context = ScApplication.getInstance();
    //		ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
    //		List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    //		if (runningAppProcesses == null) {
    //			return false;
    //		}
    //		for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
    //			if (info.processName.equals(context.getPackageName()) && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
    //				return true;
    //			}
    //		}
    //		return false;
    //	}

    public void exit() {
        for (BaseActivity baseActivity : getActivityContainer()) {
            baseActivity.finish();
        }
        getActivityContainer().clear();
    }
}
