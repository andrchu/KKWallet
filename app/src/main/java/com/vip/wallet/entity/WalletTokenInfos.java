package com.vip.wallet.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.utils.ListUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/19 0019 15:15
 * 描述	      ${TODO}
 */

public class WalletTokenInfos implements MultiItemEntity, Serializable {

    /**
     * token_infos : [{"symbol":"BNB","total_balance":10.33333333,"total_amount_cny":900.44,"image_url":"https://etherscan.io/token/images/binance_28.png","decimals":18,"name":"Binance","contract_address":"0xB8c77482e45F1F44dE1745F52C74426C631bDD52","total_amount_usd":180.23},{"symbol":"DGD","total_balance":15.66666,"total_amount_cny":1200.84,"image_url":"https://etherscan.io/token/images/digix-logo.png","decimals":9,"name":"Digix Global","contract_address":"0xe0b7927c4af23765cb51314a0e0521a9645f0e2a","total_amount_usd":200.13}]
     * address : 0x05DAFFCA1c5631cFa18FB06dBeE7E07E589cDbfe
     * total_amount_cny : 6335.86
     * total_amount_usd : 1000.23
     */
    public ArrayList<TokenInfosEntity> token_infos = new ArrayList<>();
    public String address;
    public String wallet_name;
    public int item_type;
    public String total_amount_cny = "0";
    public String total_amount_usd = "0";
    public String seedWord;

    @Override
    public String toString() {
        return "WalletTokenInfos{" +
                "token_infos=" + token_infos +
                ", address='" + address + '\'' +
                ", wallet_name='" + wallet_name + '\'' +
                ", item_type=" + item_type +
                ", total_amount_cny='" + total_amount_cny + '\'' +
                ", total_amount_usd='" + total_amount_usd + '\'' +
                ", seedWord='" + seedWord + '\'' +
                '}';
    }

    public WalletTokenInfos() {
    }

    public WalletTokenInfos(String address) {
        this.address = address;
    }

    public String getTotalAmount() {
        boolean b = ScApplication.getInstance().getConfig().getCurrency_unit() == 0;
        BigDecimal cny_bigDecimal = new BigDecimal(total_amount_cny);
        BigDecimal usd_bigDecimal = new BigDecimal(total_amount_usd);
        return b ? cny_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : usd_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public int getTokenCount() {
        if (ListUtil.isEmpty(token_infos)) {
            return 0;
        }
        return token_infos.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WalletTokenInfos that = (WalletTokenInfos) o;

        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }

    @Override
    public int getItemType() {
        return item_type;
    }

    public static class TokenInfosEntity implements Serializable {
        /**
         * symbol : BNB
         * total_balance : 10.33333333
         * total_amount_cny : 900.44
         * image_url : https://etherscan.io/token/images/binance_28.png
         * decimals : 18
         * name : Binance
         * contract_address : 0xB8c77482e45F1F44dE1745F52C74426C631bDD52
         * total_amount_usd : 180.23
         */
        public String symbol;
        public String total_balance = "0";
        public String total_amount_cny = "0";
        public String image_url;
        public int decimals;
        public String name;
        public String contract_address;
        public String total_amount_usd = "0";

        public TokenInfosEntity() {
        }

        public String getTotalAmount() {
            boolean b = ScApplication.getInstance().getConfig().getCurrency_unit() == 0;
            BigDecimal cny_bigDecimal = new BigDecimal(total_amount_cny);
            BigDecimal usd_bigDecimal = new BigDecimal(total_amount_usd);
            return b ? cny_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : usd_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }

        public String getBalance() {
            /*BigDecimal balance_bigDecimal = new BigDecimal(total_balance);
            return balance_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();*/
            return total_balance;
        }

        public TokenInfosEntity(String symbol, String image_url, int decimals, String contract_address) {
            this.symbol = symbol;
            this.image_url = image_url;
            this.decimals = decimals;
            this.contract_address = contract_address;
        }

        public TokenInfosEntity(String contract_address) {
            this.contract_address = contract_address;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            TokenInfosEntity that = (TokenInfosEntity) o;

            return contract_address != null ? contract_address.equals(that.contract_address) : that.contract_address == null;

        }

        @Override
        public int hashCode() {
            return contract_address != null ? contract_address.hashCode() : 0;
        }
    }
}
