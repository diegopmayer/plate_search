package br.gov.serpro.wsdenatran.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class VeiculoPaginado {

    protected int quantidadeVeiculo;
    protected int quantidadeVeiculoReal;
    protected Long idUltimoRegistro;

    protected List<Veiculo> veiculo;

    public int getQuantidadeVeiculo() {
        return quantidadeVeiculo;
    }

    public void setQuantidadeVeiculo(int quantidadeVeiculo) {
        this.quantidadeVeiculo = quantidadeVeiculo;
    }

    public int getQuantidadeVeiculoReal() {
        return quantidadeVeiculoReal;
    }

    public void setQuantidadeVeiculoReal(int quantidadeVeiculoReal) {
        this.quantidadeVeiculoReal = quantidadeVeiculoReal;
    }

    public Long getIdUltimoRegistro() {
        return idUltimoRegistro;
    }

    public void setIdUltimoRegistro(Long value) {
        idUltimoRegistro = value;
    }

    public List<Veiculo> getVeiculo() {
        if (veiculo == null) {
            veiculo = new ArrayList<Veiculo>();
        }
        return this.veiculo;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculo = veiculos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VeiculoPaginado [quantidadeVeiculo=");
        builder.append(quantidadeVeiculo);
        builder.append(", quantidadeVeiculoReal=");
        builder.append(quantidadeVeiculoReal);
        builder.append(", idUltimoRegistro=");
        builder.append(idUltimoRegistro);
        builder.append(", veiculo=");
        builder.append(veiculo);
        builder.append("]");
        return builder.toString();
    }
}