package com.vip.wallet.utils;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.security.SecureRandom;
import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 20:03
 * 描述	      ${TODO}
 */

public class WalletUtil {
    public static final int SEED_ENTROPY_EXTRA = 128;

    public static List<String> generateMnemonic(int entropyBitsSize) {
        byte[] entropy;
        entropy = new byte[entropyBitsSize / 8];

        SecureRandom sr = new SecureRandom();
        sr.nextBytes(entropy);

        return bytesToMnemonic(entropy);

    }

    public static List<String> bytesToMnemonic(byte[] bytes) {

        List<String> mnemonic;
        try {
            mnemonic = MnemonicCode.INSTANCE.toMnemonic(bytes);
        } catch (MnemonicException.MnemonicLengthException e) {
            throw new RuntimeException(e); // should not happen, we have 16bytes of entropy
        }
        return mnemonic;
    }
}
