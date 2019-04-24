package com.vip.wallet.entity;

import com.blankj.utilcode.util.StringUtils;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/18 0018 11:59
 * 描述	      ${TODO}
 */

public class PwdInfo implements Serializable {

    public String newPwd = "";
    public String oldPwd = "";
    public String confirmPassword = "";
    public int optionType;  //1.创建钱包  2.导入钱包  3.更改密码  4.输入密码  5.更新旧密码  6.注销账户 7.备份助记词  8.备份私钥
    public int step;    //5 输入旧密码   6 输入新密码  7 确认密码
    public static final int CREATE_WALLET = 1;
    public static final int IMPORT_WALLET = 2;
    public static final int UPDATE_PWD = 3;
    public static final int INPUT_PWD = 4;
    public static final int UPDATE_NEW_PWD = 55;
    public static final int LOG_OUT = 6;
    public static final int BACK_UP_SEED_WORD = 7;
    public static final int BACK_UP_PRIVATE_KEY = 8;


    public static final int INPUT_OLD_PWD = 5;
    public static final int INPUT_NEW_PWD = 6;
    public static final int INPUT_CONFIRM_PWD = 7;
    public static final int INPUT_FINISH = 8;

    public String getDesc() {
        if (optionType == INPUT_PWD || optionType == LOG_OUT || optionType == BACK_UP_SEED_WORD || optionType == BACK_UP_PRIVATE_KEY)
            return "请输入密码";
        if (optionType == CREATE_WALLET && step == INPUT_NEW_PWD)
            return "请输入密码";

        switch (step) {
            case INPUT_OLD_PWD:
                return "请输入旧密码";
            case INPUT_NEW_PWD:
                return "请输入新密码";
            case INPUT_CONFIRM_PWD:
                return "请再次输入密码";
        }

        return "请输入旧密码";
    }

    public String getTitle() {
        switch (optionType) {
            case CREATE_WALLET:
                return "创建账户";
            case IMPORT_WALLET:
                return "导入账户";
            case UPDATE_PWD:
                return "更改密码";
            case INPUT_PWD:
                return "输入密码";
            case UPDATE_NEW_PWD:
                return "更改密码";
            case LOG_OUT:
                return "注销账户";
            case BACK_UP_SEED_WORD:
                return "备份助记词";
            case BACK_UP_PRIVATE_KEY:
                return "备份私钥";
        }
        return "创建账户";
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
        switch (optionType) {
            case CREATE_WALLET:
            case IMPORT_WALLET:
                step = INPUT_NEW_PWD;
                break;
            case UPDATE_PWD:
                step = INPUT_OLD_PWD;
                break;
            case UPDATE_NEW_PWD:
                step = INPUT_NEW_PWD;
                break;
            case LOG_OUT:
            case BACK_UP_SEED_WORD:
            case BACK_UP_PRIVATE_KEY:
                step = INPUT_CONFIRM_PWD;
                break;
        }
    }

    public int nextStep() {
        if (step < INPUT_FINISH) {
            step++;
        }
        return step;
    }

    public boolean preStep() {
        if (optionType == CREATE_WALLET || optionType == IMPORT_WALLET || optionType == UPDATE_NEW_PWD) {
            if (step == INPUT_NEW_PWD) {
                return false;
            }
        }
        if (step == INPUT_FINISH) {
            step--;
            return false;
        }
        if (step > INPUT_OLD_PWD) {
            step--;
            cleanPwd(step + 1);
            cleanPwd(step);
            return true;
        }
        return false;
    }

    public void cleanPwd(int step) {
        switch (step) {
            case INPUT_NEW_PWD:
                newPwd = "";
                break;
            case INPUT_CONFIRM_PWD:
                confirmPassword = "";
                break;
            case INPUT_OLD_PWD:
                oldPwd = "";
                break;
            default:
                break;
        }
    }

    public String getCurrentPwd() {
        if (optionType == INPUT_PWD)
            return newPwd;
        switch (step) {
            case 5:
                return oldPwd;
            case 6:
                return newPwd;
            case 7:
                return confirmPassword;
            default:
                return oldPwd;
        }
    }

    /**
     * 输入密码
     *
     * @param pwd
     * @return boolean true表示这个步骤已完成
     */
    public boolean inputPwd(String pwd) {
        /*if (confirmPassword.length() >= 4)
            return true;*/
        switch (step) {
            case INPUT_OLD_PWD:
                //输入旧密码
                oldPwd = oldPwd + pwd;
                if (oldPwd.length() >= 4)
                    return true;
                break;
            case INPUT_NEW_PWD:
                //输入新密码
                newPwd = newPwd + pwd;
                if (newPwd.length() >= 4)
                    return true;
                break;
            case INPUT_CONFIRM_PWD:
                //再次输入密码
                confirmPassword = confirmPassword + pwd;
                if (confirmPassword.length() >= 4)
                    return true;
                break;
        }
        return false;
    }

    public void deletePwd() {
        switch (step) {
            case INPUT_OLD_PWD:
                //旧密码
                if (StringUtils.isEmpty(oldPwd))
                    return;
                oldPwd = oldPwd.substring(0, oldPwd.length() - 1);
                break;
            case INPUT_NEW_PWD:
                //新密码
                if (StringUtils.isEmpty(newPwd))
                    return;
                newPwd = newPwd.substring(0, newPwd.length() - 1);

                break;
            case INPUT_CONFIRM_PWD:
                //再次输入密码
                if (StringUtils.isEmpty(confirmPassword))
                    return;
                confirmPassword = confirmPassword.substring(0, confirmPassword.length() - 1);
                break;
        }
    }
}
