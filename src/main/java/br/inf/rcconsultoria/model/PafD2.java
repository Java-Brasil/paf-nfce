package br.inf.rcconsultoria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro D2 - Cabeçalho do Documento Auxiliar de Venda (DAV)
 * Representa as informações principais do DAV, como cliente, data e número.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafD2 implements PafRegistro {

    private String cnpj;            // 14 caracteres
    private LocalDate dataEmissao; // 8 (AAAA MM DD)
    private String numeroDav;       // 10 caracteres
    private String nomeCliente;     // 40 caracteres
    private String cpfCnpj;         // 14 caracteres
    private String placaVeiculo;   // 7 caracteres

    @Override
    public String toLinha() {
        return "D2" +
                padLeft(cnpj, 14) +
                dataEmissao.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                padRight(numeroDav, 10) +
                padRight(nomeCliente, 40) +
                padLeft(cpfCnpj, 14) +
                padRight(placaVeiculo, 7);
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String texto, int tamanho) {
        return String.format("%-" + tamanho + "s", texto == null ? "" : texto);
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String texto, int tamanho) {
        return String.format("%" + tamanho + "s", texto == null ? "" : texto).replace(' ', '0');
    }
}

