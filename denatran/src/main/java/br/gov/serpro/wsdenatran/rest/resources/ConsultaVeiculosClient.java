package br.gov.serpro.wsdenatran.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1/veiculos")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public interface ConsultaVeiculosClient {

    @GET
    @Path("/placa/{placa}")
    public Response consultarVeiculoPorPlaca(@HeaderParam(value = "x-cpf-usuario") String cpfUsuario,
                    @PathParam("placa") String placa, @QueryParam("idUltimoRegistro") Long idUltimoRegistro);

    @Path("/chassi/{chassi}")
    @GET
    public Response consultarVeiculoPorChassi(@HeaderParam(value = "x-cpf-usuario") String cpfUsuario,
                    @PathParam("chassi") String chassi, @QueryParam("idUltimoRegistro") Long idUltimoRegistro);

}