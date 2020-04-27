package br.gov.serpro.wsdenatran.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.serpro.wsdenatran.common.enumerators.Ambiente;
import br.gov.serpro.wsdenatran.common.enumerators.KeyStoreType;
import br.gov.serpro.wsdenatran.common.exception.WSDenatranSetupException;
import br.gov.serpro.wsdenatran.common.utils.Utils;

public class WSDenatranSetup {

    private static final Logger LOG = LoggerFactory.getLogger(WSDenatranSetup.class);

    private boolean sslDebugEnabled = false;
    private Ambiente ambiente;
    private String endpoint;
    private KeyStoreType keyStoreType;
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;

    public WSDenatranSetup() {
        setAmbiente(Ambiente.HOMOLOGACAO);
    }

    public boolean isSslDebugEnabled() {
        return sslDebugEnabled;
    }

    public void setSslDebugEnabled(boolean sslDebugEnabled) {
        this.sslDebugEnabled = sslDebugEnabled;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;

        switch (ambiente) {
        case HOMOLOGACAO:
            this.endpoint = "https://wsrenavam.hom.denatran.serpro.gov.br";
            this.trustStorePath = Utils.saveTrustStore("/truststore/wsrenavam.hom.denatran.serpro.gov.br.jks");
            break;
        case PRODUCAO:
            this.endpoint = "https://renavam.denatran.serpro.gov.br";
            this.trustStorePath = Utils.saveTrustStore("/truststore/renavam.denatran.serpro.gov.br.jks");
            break;
        default:
            break;
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public KeyStoreType getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(KeyStoreType keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public SSLContext getSslContext() throws WSDenatranSetupException {
        File cert = new File(keyStorePath);
        SSLContext sslContext = null;

        if (!cert.exists()) {
            LOG.error("Certificado '{}' não existe.", keyStorePath);
            throw new WSDenatranSetupException("Certificado não encontrado.");
        }

        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType.name());

            keyStore.load(new FileInputStream(cert), keyStorePassword.toCharArray());
            sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, keyStorePassword.toCharArray()).build();
        } catch (KeyStoreException | KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException
                        | CertificateException | IOException e) {
            throw new WSDenatranSetupException("Falha ao ler dados do certificado.", e);
        }

        return sslContext;

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WSDenatranSetup [sslDebugEnabled=");
        builder.append(sslDebugEnabled);
        builder.append(", ambiente=");
        builder.append(ambiente);
        builder.append(", endpoint=");
        builder.append(endpoint);
        builder.append(", keyStoreType=");
        builder.append(keyStoreType);
        builder.append(", keyStorePath=");
        builder.append(keyStorePath);
        builder.append(", keyStorePassword=");
        builder.append(keyStorePassword);
        builder.append(", trustStorePath=");
        builder.append(trustStorePath);
        builder.append("]");
        return builder.toString();
    }

}
