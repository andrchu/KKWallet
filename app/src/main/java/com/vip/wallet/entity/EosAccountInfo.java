package com.vip.wallet.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/24 0024 15:59
 * 描述	      ${TODO}
 */

public class EosAccountInfo implements Serializable{

    /**
     * head_block_num : 12763377
     * refund_request : null
     * total_resources : {"owner":"rickyzhao123","ram_bytes":7354,"net_weight":"0.9500 EOS","cpu_weight":"4.2500 EOS"}
     * head_block_time : 2018-08-24T07:57:56.500
     * created : 2018-06-21T07:36:40.500
     * ram_quota : 7354
     * net_limit : {"max":636806,"available":636599,"used":207}
     * core_liquid_balance : 2.7175 EOS
     * self_delegated_bandwidth : {"from":"rickyzhao123","to":"rickyzhao123","net_weight":"0.9500 EOS","cpu_weight":"4.2500 EOS"}
     * net_weight : 9500
     * cpu_weight : 42500
     * privileged : false
     * ram_usage : 6158
     * permissions : [{"parent":"owner","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS7fVqvJhpGJYDdchgP5nHcHys5dQtaeodgWmEyJvWvsPca7w5fb"}],"threshold":1,"accounts":[{"weight":1,"permission":{"actor":"jinguodong12","permission":"active"}}]},"perm_name":"active"},{"parent":"","required_auth":{"waits":[],"keys":[{"weight":1,"key":"EOS7fVqvJhpGJYDdchgP5nHcHys5dQtaeodgWmEyJvWvsPca7w5fb"}],"threshold":1,"accounts":[]},"perm_name":"owner"}]
     * account_name : rickyzhao123
     * last_code_update : 1970-01-01T00:00:00.000
     * cpu_limit : {"max":86643,"available":85393,"used":1250}
     * voter_info : {"owner":"rickyzhao123","proxy":"","last_vote_weight":"35110381728.82469940185546875","proxied_vote_weight":"0.00000000000000000","staked":84000,"is_proxy":0,"producers":["eosecoeoseco","eosmedinodes","eosunion1111","eosvillagebp","jedaaaaaaaaa"]}
     */
    public int head_block_num;
    public String refund_request;
    public TotalResourcesEntity total_resources;
    public String head_block_time;
    public String created;
    public int ram_quota;
    public NetLimitEntity net_limit;
    public String core_liquid_balance;
    public SelfDelegatedBandwidthEntity self_delegated_bandwidth;
    public int net_weight;
    public int cpu_weight;
    public boolean privileged;
    public int ram_usage;
    public ArrayList<PermissionsEntity> permissions;
    public String account_name;
    public String last_code_update;
    public CpuLimitEntity cpu_limit;
    public VoterInfoEntity voter_info;

    public static class TotalResourcesEntity implements Serializable{
        /**
         * owner : rickyzhao123
         * ram_bytes : 7354
         * net_weight : 0.9500 EOS
         * cpu_weight : 4.2500 EOS
         */
        public String owner;
        public int ram_bytes;
        public String net_weight;
        public String cpu_weight;
    }

    public static class NetLimitEntity implements Serializable{
        /**
         * max : 636806
         * available : 636599
         * used : 207
         */
        public int max;
        public int available;
        public int used;
    }

    public static class SelfDelegatedBandwidthEntity implements Serializable{
        /**
         * from : rickyzhao123
         * to : rickyzhao123
         * net_weight : 0.9500 EOS
         * cpu_weight : 4.2500 EOS
         */
        public String from;
        public String to;
        public String net_weight;
        public String cpu_weight;
    }

    public static class PermissionsEntity implements Serializable{
        /**
         * parent : owner
         * required_auth : {"waits":[],"keys":[{"weight":1,"key":"EOS7fVqvJhpGJYDdchgP5nHcHys5dQtaeodgWmEyJvWvsPca7w5fb"}],"threshold":1,"accounts":[{"weight":1,"permission":{"actor":"jinguodong12","permission":"active"}}]}
         * perm_name : active
         */
        public String parent;
        public RequiredAuthEntity required_auth;
        public String perm_name;

        public PermissionsEntity(String perm_name) {
            this.perm_name = perm_name;
        }

        public PermissionsEntity() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            PermissionsEntity that = (PermissionsEntity) o;

            return perm_name != null ? perm_name.equals(that.perm_name) : that.perm_name == null;

        }

        @Override
        public int hashCode() {
            return perm_name != null ? perm_name.hashCode() : 0;
        }

        public static class RequiredAuthEntity implements Serializable{
            /**
             * waits : []
             * keys : [{"weight":1,"key":"EOS7fVqvJhpGJYDdchgP5nHcHys5dQtaeodgWmEyJvWvsPca7w5fb"}]
             * threshold : 1
             * accounts : [{"weight":1,"permission":{"actor":"jinguodong12","permission":"active"}}]
             */
            public ArrayList<?> waits;
            public ArrayList<KeysEntity> keys = new ArrayList<>();
            public int threshold;
            public ArrayList<AccountsEntity> accounts = new ArrayList<>();

            public static class KeysEntity implements Serializable{
                /**
                 * weight : 1
                 * key : EOS7fVqvJhpGJYDdchgP5nHcHys5dQtaeodgWmEyJvWvsPca7w5fb
                 */
                public int weight;
                public String key;

                public KeysEntity() {
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o)
                        return true;
                    if (o == null || getClass() != o.getClass())
                        return false;

                    KeysEntity that = (KeysEntity) o;

                    return key != null ? key.equals(that.key) : that.key == null;

                }

                @Override
                public int hashCode() {
                    return key != null ? key.hashCode() : 0;
                }

                public KeysEntity(String key) {
                    this.key = key;
                }
            }

            public static class AccountsEntity implements Serializable{
                /**
                 * weight : 1
                 * permission : {"actor":"jinguodong12","permission":"active"}
                 */
                public int weight;
                public PermissionEntity permission;

                public static class PermissionEntity implements Serializable{
                    /**
                     * actor : jinguodong12
                     * permission : active
                     */
                    public String actor;
                    public String permission;
                }
            }
        }
    }

    public static class CpuLimitEntity implements Serializable{
        /**
         * max : 86643
         * available : 85393
         * used : 1250
         */
        public int max;
        public int available;
        public int used;
    }

    public static class VoterInfoEntity implements Serializable{
        /**
         * owner : rickyzhao123
         * proxy :
         * last_vote_weight : 35110381728.82469940185546875
         * proxied_vote_weight : 0.00000000000000000
         * staked : 84000
         * is_proxy : 0
         * producers : ["eosecoeoseco","eosmedinodes","eosunion1111","eosvillagebp","jedaaaaaaaaa"]
         */
        public String owner;
        public String proxy;
        public String last_vote_weight;
        public String proxied_vote_weight;
        public int staked;
        public int is_proxy;
        public ArrayList<String> producers;
    }
}
