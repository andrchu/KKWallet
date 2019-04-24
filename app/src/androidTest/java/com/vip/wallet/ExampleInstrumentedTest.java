package com.vip.wallet;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.AddressDao;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.utils.LogUtil;
import com.vip.wallet.utils.StringUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.qktech.wallet", appContext.getPackageName());
    }

    @Test
    public void seedWordTest() throws Exception {
        String[] strings = new String[]{"abc", "abce", "abcde", "abcdef", "abcdeg", "abcdeh", "abcdej", "abcdek", "abcdel", "abcdep", "abcdep2", "abcdep3"};
        //        String[] strings = new String[]{"abc", "abce", "abcde", "abcdef", "abcdeg", "abc", "abcdej", "abcdek", "abcdel", "abcdep", "abcdep2", "abcdep3"};
        //        List<String> strings = OwnWalletUtils.generateMnemonic(SEED_ENTROPY_EXTRA);
        List<String> list = Arrays.asList(strings);
        HashSet<String> s = new HashSet<>();
        for (String string : list) {
            s.add(string);
        }
        Log.i("ttt", s.size() + "::");
        assertEquals(s.size(), 12);
    }

    @Test
    public void testSubSeedKey() {
        String[] strings = new String[]{"abc", "abce", "abcde"};
        StringBuilder stringBuilder = new StringBuilder();
        for (String seed : strings)
            stringBuilder.append(seed).append(" ");
        String seedKey = stringBuilder.toString();

        String substring = seedKey.substring(0, seedKey.length() - 1);
        LogUtils.e("substring>>>" + substring);
        assertEquals(substring, "abc abce abcde");
        //        ScApplication.getInstance().getConfig().setSeedKey(seedKey.substring(0, seedKey.length() - 1));
    }


    public void test2() {

        BigInteger nTokens = new BigDecimal(8).multiply(BigDecimal.valueOf(Math.pow(10, 18))).toBigInteger();
        LogUtils.i("tokens>> " + nTokens);
        List<Type> params = Arrays.asList(new Address("0xtoaddress"), new Uint256(nTokens));
        List<TypeReference<?>> returnTypes = Arrays.asList(new TypeReference<Bool>() {
        });

        Function function = new Function("transfer", params, returnTypes);

        String data = FunctionEncoder.encode(function);

        Transaction functionCallTransaction = Transaction.createFunctionCallTransaction("0xfrom", new BigInteger("1"), new BigInteger("100000000"), new BigInteger("80000"), "0xhyaddress", data);

        Request et_xxxx = new Request("et_xxxx", Arrays.asList(functionCallTransaction), 1, null, EthEstimateGas.class);

        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(et_xxxx);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSql() {
        CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
        AddressDao addressDao = ScApplication.getInstance().getDaoSession().getAddressDao();
        cardDao.deleteAll();
        addressDao.deleteAll();
        long cardRowId1 = cardDao.insert(new Card(null, "card1", "", "card1 abc abd ddf jjj kkkl aaa fefe", "card1_address1", 1, 0, System.currentTimeMillis(), 0, ""));
        long cardRowId2 = cardDao.insert(new Card(null, "card2", "", "card2 adfad 2i3uiok   djksafjk aklsdl", "card2_address1", 1, 0, System.currentTimeMillis(), 0, ""));
        cardDao.deleteByKey(cardRowId2);

        long cardRowId3 = cardDao.insert(new Card(null, "card3", "", "card3 adfad 2i3uiok   djksafjk aklsdl", "card3_address1", 1, 0, System.currentTimeMillis(), 0, ""));
        addressDao.insert(new com.vip.wallet.dao.Address(null, cardRowId1, "card1_address11111,", "abcprivatekkey1111", System.currentTimeMillis(), 0));
        addressDao.insert(new com.vip.wallet.dao.Address(null, cardRowId1, "card1_address22222,", "abcprivatekkey22222", System.currentTimeMillis(), 0));
        addressDao.insert(new com.vip.wallet.dao.Address(null, cardRowId1, "card1_address3333,", "abcprivatekkey22222", System.currentTimeMillis(), 0));


        addressDao.insert(new com.vip.wallet.dao.Address(null, cardRowId3, "card3_address1111,", "abcprivatekkey1111", System.currentTimeMillis(), 0));
        addressDao.insert(new com.vip.wallet.dao.Address(null, cardRowId3, "card3_address9999,", "abcprivatekkey9999", System.currentTimeMillis(), 0));

        List<Card> cards = cardDao.loadAll();
        LogUtil.getInstance().i("============================================card start==============================================");
        for (Card card : cards) {
            card.getAddressList();
            LogUtil.getInstance().i(card.toString() + "\n-----------------------------------------------------------------------------");
        }

        LogUtil.getInstance().i("============================================card end==============================================");


        LogUtil.getInstance().i("============================================address start==============================================");

        List<com.vip.wallet.dao.Address> addresses = addressDao.loadAll();
        for (com.vip.wallet.dao.Address address : addresses) {
            try {
                Card card = address.getCard();
                LogUtil.getInstance().i(address.toString() + "*******" + card.name + card.id + "\n-----------------------------------------------------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.getInstance().i(e.getMessage());
            }
        }
        LogUtil.getInstance().i("============================================address end==============================================");
        assertEquals("11", "abc abce abcde");
    }

    @Test
    public void testAddress() {
        //        boolean ethAddress = StringUtil.isEthAddress("0X5c025ef4421189f1244d8d62edecd60904eC7903");
        //        assertEquals(ethAddress, true);
        boolean btcAddress = StringUtil.isBtcAddress("1ARLJnm8j1XLx6xep7LMM9bK94ExyJxPcVA");
        boolean btcAddress1 = StringUtil.isBtcAddress("1ARLJnm8j1XLx6xep7LMM9ARA"); //25
        boolean btcAddress2 = StringUtil.isBtcAddress("1ARLJnm8j1XLx6xep7LMM9ARAR"); //26
        assertEquals(btcAddress, true);
        //        assertEquals(btcAddress1, true);
        assertEquals(btcAddress2, true);
    }


    public void testEosTransfer() {

    }

    @Test
    public void testHtml() {
        String string = "<p>近年来，为改善生态环境质量，提高森林覆盖率，有效遏制水土流失，着力解决生态修复任务艰巨难题，黄平县按照《贵州省石漠化综合治理工程规划》，实施好年度石漠化综合治理工程，注重生态恢复与产业发展，兼顾工程治理与脱贫攻坚有效衔接，助力生态修复工作稳步推进。</p ><p><br/></p ><p>2016年，黄平县石漠化治理工程总投资880万元，中央预算内投资800万元，州级匹配80万元。项目主要采取林草植被恢复、草食畜牧业发展、小型水利水保工程治理石漠化面积15平方公里。工程在穿岩河、冷水河二条小流域实施，治理岩溶面积37.44平方公里，治理石漠化面积15平方公里，涉及2个乡镇4个行政村。2017年林业实施完成封山育林育草1279.33公顷，人工造林20.76公顷;农业实施完成人工种草90公顷，草地改良110公顷，棚圈建设10000平方米，青贮窖1200立方米，饲草机械10台;水利实施完成排涝渠1.49公里，引水渠5.9公里，机耕道4公里，山塘维修1座，蓄水池和沉沙池各37口。</p ><p><img src=\"http://39.108.87.155:80/uploadfile/ueditor/image/20180808/1533702041603056730.png\" title=\"1533702041603056730.png\" alt=\"微信图片_20180808121636.png\"/></p ><p>2017年，黄平县石漠化治理工程总投资1210万元，中央预算内投资1100万元，州级匹配110万元。项目主要采取林草植被恢复、草食畜牧业发展、小型水利水保工程治理石漠化面积19.8平方公里。工程在穿硐河小流域、白水河小流域实施，治理岩溶面积38平方公里，治理石漠化面积19.8平方公里，涉及3个乡镇11个行政村。2018年林业实施完成封山育林育草1472.8公顷，经济林137.88公顷;农业实施完成人工种草297.59公顷，棚圈建设18000平方米，青贮窖2750立方米;水利实施项目因受县级重大工程影响正在调整实施地点，力争在工程建设期限内完成建设任务。</p ><p><br/></p ><p>2018年，该县申请中央预算内资金1300万元(其中支持谷陇极贫乡镇100万元)，主要采取林草植被恢复、草食畜牧业发展、小型水利水保工程治理石漠化面积26平方公里。目前该项目工作已委托贵州智城公司编制项目实施方案并于7月9日在州发改委审批完成，待批复完成后，由县发改牵头，林业、农业、水务分项组织实施。</p ><p><br/></p ><p><img src=\"http://39.108.87.155:80/uploadfile/ueditor/image/20180808/1533702036838009988.png\" title=\"1533702036838009988.png\" alt=\"微信图片_20180808121809.png\"/></p >";
       /* String result;
        if (SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY).toString().replaceAll("￼", "");
        } else {
            result = Html.fromHtml(string).toString().replaceAll("￼", "");
        }
        LogUtils.i(result);*/
    }

    @Test
    public void testRxJava() {
    }

    @Test
    public void testSeed() {


    }


}
