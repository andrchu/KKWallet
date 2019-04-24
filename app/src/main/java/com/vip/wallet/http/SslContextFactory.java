package com.vip.wallet.http;

import com.vip.wallet.R;
import com.vip.wallet.config.ScApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Administrator on 2017/1/9.
 */

public class SslContextFactory extends SSLSocketFactory {
    private SSLSocketFactory delegate;

    public SslContextFactory() throws KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        String certificateAlias = Integer.toString(0);
        keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(ScApplication.getInstance().getResources().openRawResource(R.raw.wallet)));//拷贝好的证书
        SSLContext sslContext = SSLContext.getInstance("TLS");
        final TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        sslContext.init
                (
                        null,
                        trustManagerFactory.getTrustManagers(),
                        new SecureRandom()
                );
        delegate = sslContext.getSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost,
                localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if (socket != null &&
                (socket instanceof SSLSocket)) {
            String[] protocols = ((SSLSocket) socket).getEnabledProtocols();
            List supports = new ArrayList<>();
            if (protocols != null &&
                    protocols.length > 0) {
                supports.addAll(Arrays.asList(protocols));
            }
            Collections.addAll(supports, "TLSv1.1", "TLSv1.2");
                        ((SSLSocket) socket).setEnabledProtocols((String[]) supports.toArray(new String[supports.size()]));
        }
        return socket;
    }
}