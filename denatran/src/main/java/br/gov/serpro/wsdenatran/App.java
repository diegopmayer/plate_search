package br.gov.serpro.wsdenatran;

import javax.ws.rs.core.Response;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.serpro.wsdenatran.common.WSDenatranClient;
import br.gov.serpro.wsdenatran.common.WSDenatranSetup;
import br.gov.serpro.wsdenatran.common.exception.WSDenatranSetupException;
import br.gov.serpro.wsdenatran.common.utils.Utils;
import br.gov.serpro.wsdenatran.enumerators.TipoPesquisa;
import br.gov.serpro.wsdenatran.rest.dto.RestErrorResponse;
import br.gov.serpro.wsdenatran.rest.dto.VeiculoPaginado;
import br.gov.serpro.wsdenatran.rest.resources.ConsultaVeiculosClient;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws WSDenatranSetupException {
        CommandLine cmd = Utils.getCommandLineArguments(args);
        WSDenatranSetup setup = Utils.getWSDenatranSetup(cmd);

        String argumento = "";
        TipoPesquisa tipoPesquisa = null;

        if (cmd.hasOption("placa")) {
            argumento = cmd.getOptionValue("placa");
            tipoPesquisa = TipoPesquisa.PLACA;
        }

        if (cmd.hasOption("chassi")) {
            argumento = cmd.getOptionValue("chassi");
            tipoPesquisa = TipoPesquisa.CHASSI;
        }

        consultar(argumento, cmd.getOptionValue("usuario"), tipoPesquisa, setup);
    }

    private static void consultar(String argumento, String cpf, TipoPesquisa tipoPesquisa, WSDenatranSetup setup)
                    throws WSDenatranSetupException {
        ConsultaVeiculosClient consultaVeiculos = WSDenatranClient.getInstance(setup)
                        .getClient(ConsultaVeiculosClient.class);

        Response r = null;

        switch (tipoPesquisa) {
        case CHASSI:
            r = consultaVeiculos.consultarVeiculoPorChassi(cpf, argumento, 0L);
            break;
        case PLACA:
            r = consultaVeiculos.consultarVeiculoPorPlaca(cpf, argumento, 0L);
            break;
        default:
            LOG.error("Tipo de pesquisa inválida");
            return;
        }

        LOG.info("PESQUISANDO {} {}", tipoPesquisa.name(), argumento);
        LOG.info("STATUS: {}", r.getStatus());

        try {
            if (r.getStatus() == 200) {
                VeiculoPaginado vp = r.readEntity(VeiculoPaginado.class);
                LOG.info("ENTIDADE RETORNADA: {}", vp);
            } else {
                RestErrorResponse restError = r.readEntity(RestErrorResponse.class);
                LOG.info("ENTIDADE RETORNADA: {}", restError);
            }
        } catch (Exception e) {
            LOG.error("Falha ao fazer parse da entidade retornada", e);
        }
    }

}
