package com.vip.wallet.config;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;

/**
 * 创建者     金国栋
 * 创建时间   2018/1/22 9:54
 * 描述	      ${TODO}
 */
public interface Constants {
    //true 测试环境   false正式环境
    boolean TEST = false;

    //正式环境
    //    String BASE_URL = "https://api.etherscan.io/";

    //    String BASE_URL = "https://www.wallet.vip/";
    String BASE_URL = TEST ? "https://www.wallet.vip/" : "https://www.kkwallet.io/";
    //    String BASE_URL = "https://119.28.17.39/";

    //    String WEB3J_URL = TEST ? "https://ropsten.infura.io/SNGVcb4ahKTIJbfDfDBW" : "https://mainnet.infura.io/SNGVcb4ahKTIJbfDfDBW";
    String WEB3J_URL = /*TEST ? "https://ropsten.infura.io/SNGVcb4ahKTIJbfDfDBW" : */"https://mainnet.infura.io/SNGVcb4ahKTIJbfDfDBW";
    //    String WEB3J_URL = TEST ? "https://ropsten.infura.io/SNGVcb4ahKTIJbfDfDBW" : "http://120.78.211.91:8911";

    String CLAUSE_URL = BASE_URL + "app/index/agreement";

    //详情地址
    String DETAIL_URL = TEST ? "https://ropsten.etherscan.io/tx/" : "https://etherscan.io/tx/";

    //以太坊地址
    String ETH_URL = TEST ? "https://api-ropsten.etherscan.io/api/" : "https://api.etherscan.io/api/";

    //比特币地址
    String BTC_DETAIL_URL = TEST ? "https://testnet.blockchain.info/tx/" : "https://blockchain.info/tx/";

    //EOS RPC
    String EOS_RPC_URL = "http://mainnet.genereos.io";

    //EOS地址
    String EOS_DETAIL_URL = "https://explorer.eoseco.com/transactions/";

    int SUCCESS = 0;
    int ERROR = 1;
    int EMPTY = 2;
    int LOADING = 3;
    long SHOW_VIEW_OFFSET = 100;
    String SEED_WORDS = "seed_words";
    String SEED_WORD_STRING = "seed_word_string";
    String SELECT_SEED_WORDS = "select_seed_words";
    String SEED_WORDS_RANDOM = "seed_words_random";
    String SEED_WORD_POINTER = "seed_word_pointer";
    String SEED_KEY = "seed_key";


    //result code
    int SCAN_CODE_OK = 1001;
    int SCAN_CODE_ERROR = 1002;
    int SELECT_CONTACT_OK = 1003;

    String SCAN_CODE_RESULT = "scan_code_result";
    String CREATE_WALLET_INFO = "create_wallet_info";
    String WALLET_CACHE_DIR = "vip_cache_wallet";
    String ADDRESS = "address";
    String TX = "tx";
    String WALLET_INFO = "wallet_info";
    String CONTACT = "contact";
    String TOKEN = "token";
    String VIP_WALLET = "vip_wallet";   //sp_name
    String INIT = "init";
    long SPLASH_DELAY = 100;
    File WALLET_DIR = new File(ScApplication.getInstance().getFilesDir(), "wallet");
    String Contacts_ACTION = "activity_action";
    java.lang.String CURRENCY_UNIT = "currency_unit";
    String START_TYPE = "start_type";
    int START_TYPE_INIT = 1;        //初始
    int BACK_UP_TYPE_INIT = 2;        //备份
    int BACK_UP_TYPE_GOTO_URL = 3;        //跳转链接

    String PATH = "m/44'/60'/0'/0/0";
    int PWD_MIN_LENGTH = 4; //密码最小位数
    long EXIT_TIME = 2000;
    String PROPERTY = "property";
    String TOKEN_DEAL_RECORD = "token_deal_record";
    String WALLET_DEAL_RECORD = "wallet_deal_record";
    String DEAL_RECORD_CACHE = "deal_record_cache";
    int LOAD_ITEM_RAW = 20;
    String DEAL_RECORD = "deal_record";
    String TITLE_NAME = "title_name";
    String WEB_URL = "web_url";
    String IS_INIT_SHOW_ADDRESS = "is_init_show_address";
    String FQ_MODE = "fq_mode";
    String GL_MODE = "gl_mode";
    String INIT_SHOW_SHAM_PAGE = "init_show_sham_page";
    String IS_SHOW_PROPERTY = "is_show_property";
    String PUSH_MESSAGE = "push_message";
    String CURRENT_WALLET = "current_wallet";
    String ALL_WALLET_PROPERTY = "all_wallet_property";
    String WE_CHAT_ID = "KingKongWallet";
    String PWD_OPTION_TYPE = "pwd_option_type";
    String VERSION = "version";
    String OLD_PWD = "old_pwd";
    String API_VERSION = AppUtils.getAppVersionName().contains("-debug") ? AppUtils.getAppVersionName().split("-")[0] : AppUtils.getAppVersionName();
    String PRIVATE_KEY = "private_key";
    String PWD = "pwd";
    long PWD_DELAY = 500;
    String IMPORT_WALLET = "import_wallet";
    String USER_NAME = "user_name";
    java.lang.String ETH_TOKEN_LIST = "eth_token_list";
    String OPTION_TYPE = "option_type";
    String OUT_TOKEN = "out_token";
    java.lang.String IS_BACK_UP = "is_Back_up";
    int SPLASH_TIME_OUT = 3000;
    String ADVERT_INFO = "advert_info";
    int CONFIG_VERSION_16 = 16;    //2.5.0版本
    int CONFIG_VERSION_17 = 17;    //2.6.0版本
    int CONFIG_VERSION_18 = 18;    //2.6.1版本
    String CHAIN_TYPE = "chain_type";
    String CARD = "card";
    String JS_CALL_NAME = "android";
    //后台接口签名秘钥
    String SIGN_KEY = "5c025ef4421189f1244d8d62edecd60904ec7903";
    String IS_REGISTER = "is_register";
    String JS_CALL_BACK = "js_call_back";
    String BROWSER_INFO = "browser_info";
    String CURRENT_RECEIVE_ADDRESS = "current_receive_address";
    String IMPORT_CARD = "import_card";
    String CHAIN = "chain";
    String BUY_INFO = "buy_info";
    int PAY_SUCCESS = 1000;
    String DAPP_BROWSER_INFO = "dapp_browser_info";
    String DAPP_URL = "dapp_";

    String EOS_SIGNS = "eos_signs";
    String EOS_AUTH_INFO = "eos_auth_info";

    /**
     * EOS离线签名过期时间
     */
    long EXP = 60 * 60;
    String ACCOUNT_LIST = "account_list";
    String ACCOUNT = "account";
}
