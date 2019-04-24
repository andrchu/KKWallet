package com.vip.wallet.entity;

import com.vip.wallet.R;
import com.vip.wallet.ui.fragment.LockFragment;
import com.vip.wallet.utils.StringUtil;

import java.io.Serializable;

import static com.vip.wallet.utils.StringUtil.getString;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/22 0022 15:02
 * 描述	      ${TODO}
 */

public class PwdStep implements Serializable{
    public int option_type;     //1-输入密码   2-修改密码   3-设置密码
    public int step = 0;    //0-旧密码  1-新密码  2-确认密码  3-完成
    public String prePwd;
    public String confirmPwd;

    public void setOption_type(int option_type) {
        this.option_type = option_type;
        if (option_type == LockFragment.SET_PWD) {
            step = 1;
        }
    }

    public String getPrePwd() {
        return prePwd;
    }

    public void setPrePwd(String prePwd) {
        this.prePwd = prePwd;
    }

    public void nextStep() {
        step++;
    }

    public String getErrorString() {
        if (option_type == LockFragment.INPUT_PWD) {
            return StringUtil.getString(R.string.pwd_error_input);
        } else {
            if (step == 0) {
                return StringUtil.getString(R.string.pwd_error_input);
            } else if (step == 1) {
                return StringUtil.getString(R.string.pwd_min_point);
            } else {
                return StringUtil.getString(R.string.pwd_un_equals);
            }
        }
    }

    public String getTitle() {
        switch (option_type) {
            case LockFragment.INPUT_PWD:
                return getString(R.string.input_pwd);
            case LockFragment.UPDATE_PWD:
                return getString(R.string.update_pwd);
            case LockFragment.SET_PWD:
                return getString(R.string.set_pwd);
            default:
                return getString(R.string.input_pwd);
        }
    }

    public String getDesc() {
        if (option_type == LockFragment.INPUT_PWD) {
            return getString(R.string.input_hand_pwd);
        }
        switch (step) {
            case 0:
                return getString(R.string.input_old_hand_pwd);
            case 1:
                if (option_type == LockFragment.SET_PWD)
                    return getString(R.string.set_hand_pwd);
                else
                    return getString(R.string.input_new_hand_pwd);
            case 2:
                return getString(R.string.confirm_hand_pwd);
        }
        return StringUtil.getString(R.string.set_finish);
    }
}