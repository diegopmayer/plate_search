package br.gov.serpro.wsdenatran.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.serpro.wsdenatran.common.WSDenatranSetup;
import br.gov.serpro.wsdenatran.common.enumerators.Ambiente;
import br.gov.serpro.wsdenatran.common.enumerators.KeyStoreType;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static String saveTrustStore(String path) {
        try {
            File file = File.createTempFile("truststore", ".jks");

            InputStream link = Utils.class.getClass().getResourceAsStream(path);
            Files.copy(link, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);

            return file.getAbsolutePath();
        } catch (IOException e) {
            LOG.error("Falha ao salvar truststore", e);
        }

        return "";
    }

    public static CommandLine getCommandLineArguments(String[] args) {
        Options options = new Options();

        OptionGroup ogAmbientes = new OptionGroup();
        OptionGroup ogTipoCertificado = new OptionGroup();
        OptionGroup ogConsultas = new OptionGroup();

        ogConsultas.setRequired(true);

        options.addOption(Option.builder("usuario").required().hasArg().argName("CPF")
                        .desc("CPF para ser utilizado na requisição").build());
        options.addOption(Option.builder("c").required().longOpt("certificado").hasArg().argName("CERTIFICADO")
                        .desc("Caminho o certificado a ser utilizado na requisição (PKCS ou JKS)").build());
        options.addOption(Option.builder("s").required().longOpt("senha").hasArg().argName("SENHA")
                        .desc("Senha do certificado").build());
        options.addOption(Option.builder().longOpt("truststore").hasArg().argName("TRUSTSTORE")
                        .desc("Caminho para truststore (Somente para -des)").build());
        options.addOption(Option.builder().longOpt("endpoint").hasArg().argName("URL")
                        .desc("URL do servidor (Somente para -des)").build());

        ogAmbientes.addOption(Option.builder("pro").longOpt("producao")
                        .desc("Executar consulta no ambiente de produção").build());
        ogAmbientes.addOption(Option.builder("des").longOpt("desenvolvimento")
                        .desc("Executar consulta no ambiente de desenvolvimento").build());
        ogAmbientes.addOption(Option.builder("hom").longOpt("homologacao")
                        .desc("Executar consulta no ambiente de homologacao (PADRÃO)").build());

        ogTipoCertificado.addOption(Option.builder("JKS").desc("Informa tipo do certificado como JKS").build());
        ogTipoCertificado.addOption(
                        Option.builder("PKCS").desc("Informa tipo do certificado como PKCS (PADRÃO)").build());

        ogConsultas.addOption(
                        Option.builder("placa").hasArg().argName("PLACA").desc("Consulta veículo por placa").build());
        ogConsultas.addOption(Option.builder("chassi").hasArg().argName("CHASSI").desc("Consulta veículo por chassi")
                        .build());

        options.addOptionGroup(ogAmbientes);
        options.addOptionGroup(ogTipoCertificado);
        options.addOptionGroup(ogConsultas);

        options.addOption("help", false, "Exibe ajuda");

        HelpFormatter formatter = new HelpFormatter();

        StringBuilder exemplo = new StringBuilder();
        exemplo.append("Exemplos de uso: \n");
        exemplo.append("     java -jar consulta-veiculos-0.0.1.jar -c /tmp/certificado.p12 \\\n");
        exemplo.append("                                           -s changeit \\\n");
        exemplo.append("                                           -placa XPTO0000 \\\n");
        exemplo.append("                                           -usuario 00000000000\n");
        exemplo.append("     java -jar consulta-veiculos-0.0.1.jar -c /tmp/certificado.jks \\\n");
        exemplo.append("                                           -s changeit -JKS \\\n");
        exemplo.append("                                           -placa XPTO0000 \\\n");
        exemplo.append("                                           -usuario 00000000000\n");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("App", "\nWSDenatran\n\n", options, exemplo.toString(), true);
            System.exit(1);
        }

        return cmd;
    }

    public static WSDenatranSetup getWSDenatranSetup(CommandLine cmd) {
        WSDenatranSetup setup = new WSDenatranSetup();

        if (cmd.hasOption("des")) {
            setup.setAmbiente(Ambiente.DESENVOLVIMENTO);

            if (!cmd.hasOption("truststore") || !cmd.hasOption("endpoint")) {
                System.out.println(
                                "truststore e endpoint são parâmetros obrigatórios quando informado ambiente de desenvolvimento");
                System.exit(1);
            }

            setup.setEndpoint(cmd.getOptionValue("endpoint"));
            setup.setTrustStorePath(cmd.getOptionValue("truststore"));
        } else if (cmd.hasOption("pro")) {
            setup.setAmbiente(Ambiente.PRODUCAO);
        } else {
            setup.setAmbiente(Ambiente.HOMOLOGACAO);
        }

        if (cmd.hasOption("JKS")) {
            setup.setKeyStoreType(KeyStoreType.JKS);
        } else {
            setup.setKeyStoreType(KeyStoreType.PKCS12);
        }

        File f = new File(cmd.getOptionValue('c'));

        if (!f.exists()) {
            System.out.println("Arquivo de certificado não encontrado. ('" + f.getAbsolutePath() + "')");
            System.exit(1);
        }

        setup.setKeyStorePath(cmd.getOptionValue('c'));
        setup.setKeyStorePassword(cmd.getOptionValue('s'));

        return setup;
    }

}
