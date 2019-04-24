package com.quincysx.crypto.bip39;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author QuincySx
 * @date 2018/3/13 上午11:06
 */
public class RandomSeed {
    public static byte[] random(Words words) {
        return random(words, new SecureRandom());
    }

    public static byte[] random(Words words, Random random) {
        byte[] randomSeed = new byte[words.byteLength()];
        random.nextBytes(randomSeed);
        return randomSeed;
    }
}
