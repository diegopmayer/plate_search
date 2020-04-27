package br.gov.serpro.wsdenatran.common;

import java.io.File;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.serpro.wsdenatran.common.exception.WSDenatranSetupException;
import br.gov.serpro.wsdenatran.rest.resources.ConsultaVeiculosClient;

public class WSDenatranClient {

    private static final Logger LOG = LoggerFactory.getLogger(WSDenatranClient.class);

    private static WSDenatranClient instance;
    private WSDenatranSetup setup;

    private WSDenatranClient() {
    }

    private WSDenatranClient(WSDenatranSetup setup) {
        this.setup = setup;

        LOG.info("Inicializando WSDenatranClient");
        LOG.info(setup.toString());

        if (setup.isSslDebugEnabled()) {
            LOG.info("Habilitando DEBUG SSL");
            System.setProperty("javax.net.debug", "ssl");
        }

        File trustStore = new File(setup.getTrustStorePath());
        if (trustStore.exists()) {
            LOG.info("Definindo javax.net.ssl.trustStore = {}", setup.getTrustStorePath());
            System.setProperty("javax.net.ssl.trustStore", setup.getTrustStorePath());
        } else {
            LOG.info("{} não encontrado", setup.getTrustStorePath());
        }
    }

    public static WSDenatranClient getInstance(WSDenatranSetup setup) {
        if (instance == null) {
            instance = new WSDenatranClient(setup);
        }

        return instance;
    }

    public ConsultaVeiculosClient getClient(Class<ConsultaVeiculosClient> class1) throws WSDenatranSetupException {
        ResteasyWebTarget rtarget = getTarget();
        ConsultaVeiculosClient simple = rtarget.proxy(ConsultaVeiculosClient.class);
        return simple;
    }

    private ResteasyWebTarget getTarget() throws WSDenatranSetupException {
        SSLContext sslContext = setup.getSslContext();

        ResteasyClientBuilder clientBuilder = (ResteasyClientBuilder) ResteasyClientBuilder.newBuilder();
        clientBuilder.sslContext(sslContext);
        Client client = clientBuilder.build();

        WebTarget target = client.target(setup.getEndpoint());
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
        return rtarget;
    }

}
