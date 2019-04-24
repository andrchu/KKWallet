package com.jgd.eoslibrary.api.vo.transaction.push;


import com.jgd.eoslibrary.api.vo.BaseVo;

import java.util.ArrayList;

/**
 * @author espritblock http://eblock.io
 */
public class TxRequest extends BaseVo {

    public TxRequest() {

    }

    public TxRequest(String compression, Tx transaction, ArrayList<String> signatures) {
        this.compression = compression;
        this.transaction = transaction;
        this.signatures = signatures;
    }

    private String compression;

    private Tx transaction;

    private ArrayList<String> signatures;

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public Tx getTransaction() {
        return transaction;
    }

    public void setTransaction(Tx transaction) {
        this.transaction = transaction;
    }

    public ArrayList<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(ArrayList<String> signatures) {
        this.signatures = signatures;
    }

}
