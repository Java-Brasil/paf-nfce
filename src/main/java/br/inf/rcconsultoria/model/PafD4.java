package br.inf.rcconsultoria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro D4 - Cancelamento do DAV.
 * Informa o número do DAV cancelado, a data de emissão
 * e a data em que foi efetivado o cancelamento.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafD4 implements PafRegistro {

    private String cnpj;            // 14 caracteres
    private LocalDate dataEmissao; // 8
    private String numeroDav;       // 10
    private LocalDate dataCancelamento; // 8

    @Override
    public String toLinha() {
        return "D4" +
                padLeft(cnpj, 14) +
                dataEmissao.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                padRight(numeroDav, 10) +
                dataCancelamento.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
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

