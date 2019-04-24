package com.vip.wallet.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okio.Buffer;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/28 0028 18:39
 * 描述	      ${TODO}
 */

public class SSLSocketClient {

    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    public static InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.
        String comodoRsaCertificationAuthority = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFljCCBH6gAwIBAgIQAncRJk7c3WvV3Fnrf8vE4zANBgkqhkiG9w0BAQsFADBy\n" +
                "MQswCQYDVQQGEwJDTjElMCMGA1UEChMcVHJ1c3RBc2lhIFRlY2hub2xvZ2llcywg\n" +
                "SW5jLjEdMBsGA1UECxMURG9tYWluIFZhbGlkYXRlZCBTU0wxHTAbBgNVBAMTFFRy\n" +
                "dXN0QXNpYSBUTFMgUlNBIENBMB4XDTE4MDMyMzAwMDAwMFoXDTE5MDMyMzEyMDAw\n" +
                "MFowGTEXMBUGA1UEAxMOd3d3LndhbGxldC52aXAwggEiMA0GCSqGSIb3DQEBAQUA\n" +
                "A4IBDwAwggEKAoIBAQC1WAfD4c9NKYnsRlmrg/6hHJ9JJDd2eMsvqucV9T4q54DG\n" +
                "Uex//TA5+xusjDJAt033P2rXwiiuF72AoXftyRzs/FA1NxEuk3lt6ZAotxAtF68y\n" +
                "5mU/N6ZcrvYIB9oEdPI85vwViRIkUROjR6A6mYuzqgQS86wj6DN3NexINxsb8pLv\n" +
                "y1Jx11prjFf+K9DJc6otkCRX6aaFSnyQC3LEh/PjWPlGodFOolTMCYAKvVD1b0NA\n" +
                "stEg9dHVhjfoHnol2ndRo2IbtIhZBcXoCF3BCU5sUtM/m6B7GGf1Jnp+3t3bGUZ+\n" +
                "ik19dOiWiTkxghulLMe7YEY8dZUouGck75cAkrLdAgMBAAGjggJ/MIICezAfBgNV\n" +
                "HSMEGDAWgBR/05nzoEcOMQBWViKOt8ye3coBijAdBgNVHQ4EFgQUgrBac7rQpgS7\n" +
                "3i89qUBSyFlJc/YwJQYDVR0RBB4wHIIOd3d3LndhbGxldC52aXCCCndhbGxldC52\n" +
                "aXAwDgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcD\n" +
                "AjBMBgNVHSAERTBDMDcGCWCGSAGG/WwBAjAqMCgGCCsGAQUFBwIBFhxodHRwczov\n" +
                "L3d3dy5kaWdpY2VydC5jb20vQ1BTMAgGBmeBDAECATCBgQYIKwYBBQUHAQEEdTBz\n" +
                "MCUGCCsGAQUFBzABhhlodHRwOi8vb2NzcDIuZGlnaWNlcnQuY29tMEoGCCsGAQUF\n" +
                "BzAChj5odHRwOi8vY2FjZXJ0cy5kaWdpdGFsY2VydHZhbGlkYXRpb24uY29tL1Ry\n" +
                "dXN0QXNpYVRMU1JTQUNBLmNydDAJBgNVHRMEAjAAMIIBBAYKKwYBBAHWeQIEAgSB\n" +
                "9QSB8gDwAHYApLkJkLQYWBSHuxOizGdwCjw1mAT5G9+443fNDsgN3BAAAAFiUO74\n" +
                "wAAABAMARzBFAiBAc3jpfYl6aAp/ZtLCK+FJ9b+j4FRsM/WzdErpFGw7yAIhAJlW\n" +
                "q+MZZeWAlebf/tPMIXqd+tOIwUJ6m548z17N8gg1AHYAb1N2rDHwMRnYmQCkURX/\n" +
                "dxUcEdkCwQApBo2yCJo32RMAAAFiUO758QAABAMARzBFAiByXUx8tAxveWYMXAvT\n" +
                "hsTMsXTQQy5Jos2ka47bM0kN7AIhAPHqJzddfT2o3jHaUo0xl1kTVAL3T5ItSKu3\n" +
                "zwmnKer0MA0GCSqGSIb3DQEBCwUAA4IBAQB7cp13RehW3J7syNGj1TU2NiJUFZ6d\n" +
                "WfBA4p8BQHWhVlljWSMJHHPZX4WioVgD88FZv0TDfM4wD5bWOi3g1Nd/rQMKqEKl\n" +
                "aZXKSHUtYuuCWDGrDcHYJ3SOrdPS6JsVhCEbzYB6GbJV9x0XCP2+XzmkYXlxBLz1\n" +
                "P//Ej1TVyRMDAV5kM8IfdifIg1AtYSJ0nk1q2B2OU4VKPECdl5VZD08mUzqA00n8\n" +
                "EJJHzEccOmDkL/6Iu1we7dR+qMo0jvV5aYTjkUS8HaeEn2qmou2MD3k8dcQRGV19\n" +
                "krCCZYRzjJaiHVqbThyfox6rtZOJG+rupHtwLPdKIeARMndFLMsHwExO\n" +
                "-----END CERTIFICATE-----";
        String entrustRootCertificateAuthority = "-----BEGIN CERTIFICATE-----\n" +
                "MIIErjCCA5agAwIBAgIQBYAmfwbylVM0jhwYWl7uLjANBgkqhkiG9w0BAQsFADBh\n" +
                "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
                "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n" +
                "QTAeFw0xNzEyMDgxMjI4MjZaFw0yNzEyMDgxMjI4MjZaMHIxCzAJBgNVBAYTAkNO\n" +
                "MSUwIwYDVQQKExxUcnVzdEFzaWEgVGVjaG5vbG9naWVzLCBJbmMuMR0wGwYDVQQL\n" +
                "ExREb21haW4gVmFsaWRhdGVkIFNTTDEdMBsGA1UEAxMUVHJ1c3RBc2lhIFRMUyBS\n" +
                "U0EgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCgWa9X+ph+wAm8\n" +
                "Yh1Fk1MjKbQ5QwBOOKVaZR/OfCh+F6f93u7vZHGcUU/lvVGgUQnbzJhR1UV2epJa\n" +
                "e+m7cxnXIKdD0/VS9btAgwJszGFvwoqXeaCqFoP71wPmXjjUwLT70+qvX4hdyYfO\n" +
                "JcjeTz5QKtg8zQwxaK9x4JT9CoOmoVdVhEBAiD3DwR5fFgOHDwwGxdJWVBvktnoA\n" +
                "zjdTLXDdbSVC5jZ0u8oq9BiTDv7jAlsB5F8aZgvSZDOQeFrwaOTbKWSEInEhnchK\n" +
                "ZTD1dz6aBlk1xGEI5PZWAnVAba/ofH33ktymaTDsE6xRDnW97pDkimCRak6CEbfe\n" +
                "3dXw6OV5AgMBAAGjggFPMIIBSzAdBgNVHQ4EFgQUf9OZ86BHDjEAVlYijrfMnt3K\n" +
                "AYowHwYDVR0jBBgwFoAUA95QNVbRTLtm8KPiGxvDl7I90VUwDgYDVR0PAQH/BAQD\n" +
                "AgGGMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjASBgNVHRMBAf8ECDAG\n" +
                "AQH/AgEAMDQGCCsGAQUFBwEBBCgwJjAkBggrBgEFBQcwAYYYaHR0cDovL29jc3Au\n" +
                "ZGlnaWNlcnQuY29tMEIGA1UdHwQ7MDkwN6A1oDOGMWh0dHA6Ly9jcmwzLmRpZ2lj\n" +
                "ZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbFJvb3RDQS5jcmwwTAYDVR0gBEUwQzA3Bglg\n" +
                "hkgBhv1sAQIwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29t\n" +
                "L0NQUzAIBgZngQwBAgEwDQYJKoZIhvcNAQELBQADggEBAK3dVOj5dlv4MzK2i233\n" +
                "lDYvyJ3slFY2X2HKTYGte8nbK6i5/fsDImMYihAkp6VaNY/en8WZ5qcrQPVLuJrJ\n" +
                "DSXT04NnMeZOQDUoj/NHAmdfCBB/h1bZ5OGK6Sf1h5Yx/5wR4f3TUoPgGlnU7EuP\n" +
                "ISLNdMRiDrXntcImDAiRvkh5GJuH4YCVE6XEntqaNIgGkRwxKSgnU3Id3iuFbW9F\n" +
                "UQ9Qqtb1GX91AJ7i4153TikGgYCdwYkBURD8gSVe8OAco6IfZOYt/TEwii1Ivi1C\n" +
                "qnuUlWpsF1LdQNIdfbW3TSe0BhQa7ifbVIfvPWHYOu3rkg1ZeMo6XRU9B4n5VyJY\n" +
                "RmE=\n" +
                "-----END CERTIFICATE-----\n";
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .writeUtf8(entrustRootCertificateAuthority)
                .inputStream();
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * <p>See also {@link }, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     * <p>
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     * <p>
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    public static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    public static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
