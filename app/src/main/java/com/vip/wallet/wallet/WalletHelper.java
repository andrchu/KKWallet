package com.vip.wallet.wallet;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jgd.eoslibrary.Ecc;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.Key;
import com.quincysx.crypto.bip32.ExtendedKey;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bip44.AddressIndex;
import com.quincysx.crypto.bip44.BIP44;
import com.quincysx.crypto.bip44.CoinPairDerive;
import com.quincysx.crypto.bitcoin.BitCoinECKeyPair;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Address;
import com.vip.wallet.dao.AddressDao;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.utils.AESUtil;
import com.vip.wallet.utils.KeyPathUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.StringUtil;

import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 20:19
 * 描述	      ${TODO}
 */

public class WalletHelper {
    public static final String PWD_KEY = "62A32A4C250438F7";

    public static String encrypt(String data) {
        try {
            String encrypt = AESUtil.encrypt(data, PWD_KEY);
            return encrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String data) {
        try {
            return AESUtil.decrypt(data, PWD_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保存数据到本地
     */
    public static boolean writeWallet2File(AbsWallet wallet, String fileName) {
        if (!Constants.WALLET_DIR.exists())
            Constants.WALLET_DIR.mkdir();
        File file = new File(Constants.WALLET_DIR, fileName);
        String json = GsonAdapter.getGson().toJson(wallet);
        //对数据加密
        String encrypt = encrypt(json);
        //存入文件中
        return FileIOUtils.writeFileFromString(file, encrypt);
    }

    /**
     * 读取钱包文件
     *
     * @param cla            钱包类型
     * @param walletFileName 钱包文件名
     */
    public static <T extends AbsWallet> T readWallet(Class<T> cla, String walletFileName) {
        File file = new File(Constants.WALLET_DIR, walletFileName);
        String data = FileIOUtils.readFile2String(file);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        //解密
        String decrypt = WalletHelper.decrypt(data);
        return GsonAdapter.getGson().fromJson(decrypt, cla);
    }


    public static void createOrImportWallet(String seedKey, String path, String name, boolean isImport) throws Exception {

        CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
        AddressDao addressDao = ScApplication.getInstance().getDaoSession().getAddressDao();
        List<String> seedList = Arrays.asList(seedKey.split("[ ]+"));

        /*--------------- 创建ETH ---------------*/
        DeterministicSeed deterministicSeed = new DeterministicSeed(seedList, null, "", 0);

        DeterministicKeyChain build = DeterministicKeyChain.builder().seed(deterministicSeed).build();

        BigInteger privKey = build.getKeyByPath(KeyPathUtil.parsePath(path), true).getPrivKey();

        ECKeyPair ecKeyPair = ECKeyPair.create(privKey);

        String ads = Keys.getAddress(ecKeyPair);

        String privateKeyHex = Hex.toHexString(privKey.toByteArray());

        if (privateKeyHex.length() > 64)
            privateKeyHex = privateKeyHex.substring(privateKeyHex.length() - 64, privateKeyHex.length());

        EthWallet ethWallet = new EthWallet(StringUtil.formatAddress(ads), privateKeyHex);

        Card eth_card = new Card(null, StringUtils.isEmpty(name) ? "ETH" : name + "_ETH",
                encrypt(ethWallet.getPrivateKey()), encrypt(seedKey), ethWallet.getAddress(), 1, 0, System.currentTimeMillis(), isImport ? 1 : 0, "");
        List<Card> card_list = cardDao.queryBuilder().where(CardDao.Properties.DefAddress.eq(ethWallet.getAddress())).build().list();
        for (Card c : card_list)
            cardDao.delete(c);
        //插入卡包
        long eth_card_id = cardDao.insert(eth_card);
        //插入地址
        Address address = new Address(null, eth_card_id, ethWallet.getAddress(), encrypt(ethWallet.getPrivateKey()), System.currentTimeMillis(), 0);
        List<Address> list = addressDao.queryBuilder().where(AddressDao.Properties.Address.eq(ethWallet.getAddress())).build().list();
        for (Address a : list)
            addressDao.delete(a);
        addressDao.insert(address);

        /*--------------- 创建BTC ---------------*/
        byte[] seed = com.quincysx.crypto.bip39.MnemonicCode.toSeed(seedList, "");
        ExtendedKey extendedKey = ExtendedKey.create(seed);

        AddressIndex address1 =
                BIP44.m().purpose44()
                        .coinType(Constants.TEST ? CoinTypes.BitcoinTest : CoinTypes.Bitcoin)
                        .account(0)
                        .external()
                        .address(0);

        CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);

        com.quincysx.crypto.ECKeyPair derive1 = coinKeyPair.derive(address1);

        //插入card
        Card btc_card = new Card(null, StringUtils.isEmpty(name) ? "BTC" : name + "_BTC", encrypt(derive1.getPrivateKey()),
                encrypt(seedKey), derive1.getAddress(), 1, 1, System.currentTimeMillis(), isImport ? 1 : 0, "");
        List<Card> btc_card_list = cardDao.queryBuilder().where(CardDao.Properties.DefAddress.eq(btc_card.getDefAddress())).build().list();
        for (Card c : btc_card_list)
            cardDao.delete(c);
        long card1_id = cardDao.insert(btc_card);
        //插入地址
        Address address_btc = new Address(null, card1_id, derive1.getAddress(), encrypt(derive1.getPrivateKey()), System.currentTimeMillis(), 0);
        List<Address> btc_list = addressDao.queryBuilder().where(AddressDao.Properties.Address.eq(address_btc.getAddress())).build().list();
        for (Address a : btc_list)
            addressDao.delete(a);
        addressDao.insert(address_btc);
    }

    public static void createOrImportWallet(String seedKey) throws Exception {
        createOrImportWallet(seedKey, Constants.PATH, "", false);
    }

    /**
     * 助记词导入ETH
     */
    public static EthWallet importEthBySeedKey(String seedKey, String path) {

        List<String> seedList = Arrays.asList(seedKey.replaceAll("\\n", "").split("[ ]+"));

        DeterministicSeed deterministicSeed = new DeterministicSeed(seedList, null, "", 0);

        DeterministicKeyChain build = DeterministicKeyChain.builder().seed(deterministicSeed).build();

        BigInteger privKey = build.getKeyByPath(KeyPathUtil.parsePath(path), true).getPrivKey();

        ECKeyPair ecKeyPair = ECKeyPair.create(privKey);

        String ads = Keys.getAddress(ecKeyPair);

        String privateKeyHex = Hex.toHexString(privKey.toByteArray());

        if (privateKeyHex.length() > 64)
            privateKeyHex = privateKeyHex.substring(privateKeyHex.length() - 64, privateKeyHex.length());

        EthWallet ethWallet = new EthWallet(StringUtil.formatAddress(ads), privateKeyHex);

        return ethWallet;
    }

    /**
     * 私钥导入Eth
     *
     * @param privateKey
     * @return
     */
    public static EthWallet importEthByPrivateKey(String privateKey) {
        ECKeyPair keys = ECKeyPair.create(Hex.decode(privateKey.replace("0x", "").replace("0X", "")));
        String ads = Keys.getAddress(keys);

        String privateKeyHex = Hex.toHexString(keys.getPrivateKey().toByteArray());

        if (privateKeyHex.length() > 64)
            privateKeyHex = privateKeyHex.substring(privateKeyHex.length() - 64, privateKeyHex.length());

        return new EthWallet(StringUtil.formatAddress(ads), privateKeyHex);
    }

    /**
     * 助记词导入BTC
     *
     * @param seedKey
     * @return
     * @throws ValidationException
     */
    public static BtcWallet importBtcBySeedKey(String seedKey) throws ValidationException {
        List<String> seedList = Arrays.asList(seedKey.replaceAll("\\n", "").split("[ ]+"));
        byte[] seed = com.quincysx.crypto.bip39.MnemonicCode.toSeed(seedList, "");
        ExtendedKey extendedKey = ExtendedKey.create(seed);
        AddressIndex address =
                BIP44.m().purpose44()
                        .coinType(Constants.TEST ? CoinTypes.BitcoinTest : CoinTypes.Bitcoin)
                        .account(0)
                        .external()
                        .address(0);

        CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
        com.quincysx.crypto.ECKeyPair derive = coinKeyPair.derive(address);
        String privateKey = derive.getPrivateKey();
        BtcWallet btcWallet = new BtcWallet(derive.getAddress(), privateKey);
        btcWallet.path = "0";
        return btcWallet;
    }

    /**
     * 助记词导入BTC
     *
     * @param seedKey
     * @return
     * @throws ValidationException
     */
    public static BtcWallet importBtcBySeedKey(String seedKey,int index) throws ValidationException {
        List<String> seedList = Arrays.asList(seedKey.replaceAll("\\n", "").split("[ ]+"));
        byte[] seed = com.quincysx.crypto.bip39.MnemonicCode.toSeed(seedList, "");
        ExtendedKey extendedKey = ExtendedKey.create(seed);
        AddressIndex address =
                BIP44.m().purpose44()
                        .coinType(Constants.TEST ? CoinTypes.BitcoinTest : CoinTypes.Bitcoin)
                        .account(0)
                        .external()
                        .address(index);

        CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
        com.quincysx.crypto.ECKeyPair derive = coinKeyPair.derive(address);
        String privateKey = derive.getPrivateKey();
        BtcWallet btcWallet = new BtcWallet(derive.getAddress(), privateKey);
        btcWallet.path = "0";
        return btcWallet;
    }

    /**
     * 私钥导入BTC
     *
     * @param privateKey
     * @return
     * @throws ValidationException
     */
    public static BtcWallet importBtcByPrivateKey(String privateKey) throws ValidationException {
        BitCoinECKeyPair parse = BitCoinECKeyPair.parseWIF(privateKey);
        String privateKey1 = parse.getPrivateKey();
        String address = parse.getAddress();
        return new BtcWallet(address, privateKey1);
    }

    /**
     * ETH格式私钥导入BTC
     *
     * @param privateKey
     * @return
     * @throws ValidationException
     */
    public static BtcWallet ImportBtcByEthPrivateKey(String privateKey) throws ValidationException {
        byte[] decode = Hex.decode(privateKey);
        ExtendedKey extendedKey = ExtendedKey.parsePrivateKey(decode);
        Key master = extendedKey.getMaster();
        BitCoinECKeyPair parse = BitCoinECKeyPair.parse(master, false);
        String pk = parse.getPrivateKey();
        String address = parse.getAddress();
        return new BtcWallet(address, pk);
    }

    /**
     * 助记词导入EOS
     *
     * @param seedKey
     * @return
     */
    public static EosWallet importEosBySeedKey(String seedKey) {
        String privateKey = Ecc.seedPrivate(seedKey);
        String publicKey = Ecc.privateToPublic(privateKey);
        return new EosWallet(publicKey, privateKey);
    }

    /**
     * 私钥导入EOS
     *
     * @param privateKey
     * @return
     */
    public static EosWallet importEosByPrivateKey(String privateKey) {
        String publicKey = Ecc.privateToPublic(privateKey);
        return new EosWallet(publicKey, privateKey);
    }


    public static Card getFirstEthAddress() {
        List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.CardType.eq(1), CardDao.Properties.ChainType.eq(0)).build().list();
        if (ListUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public static Address getAddressObjAddress(String address) {
        List<com.vip.wallet.dao.Address> list = ScApplication.getInstance().getDaoSession().getAddressDao().queryBuilder()
                .where(AddressDao.Properties.Address.eq(address)).build().list();
        if (ListUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public static boolean eosCardIsEmpty() {
        List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.ChainType.eq(2)).build().list();
        return ListUtil.isEmpty(list);
    }

}
