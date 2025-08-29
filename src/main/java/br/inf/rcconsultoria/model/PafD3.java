package br.inf.rcconsultoria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro D3 - Itens do DAV.
 * Contém os produtos ou serviços incluídos no DAV (D2),
 * com código, descrição, unidade, quantidade e valor unitário.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafD3 implements PafRegistro {

    private String cnpj;            // 14 caracteres
    private LocalDate dataEmissao; // 8
    private String numeroDav;       // 10
    private int numeroItem;         // 3
    private String codigoProduto;   // 14
    private String descricao;       // 50
    private String unidade;         // 6
    private BigDecimal quantidade;  // 9 (com 3 casas decimais)
    private BigDecimal valorUnitario; // 14 (com 2 casas decimais)

    @Override
    public String toLinha() {
        return "D3" +
                padLeft(cnpj, 14) +
                dataEmissao.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                padRight(numeroDav, 10) +
                padLeft(String.valueOf(numeroItem), 3) +
                padRight(codigoProduto, 14) +
                padRight(descricao, 50) +
                padRight(unidade, 6) +
                padLeft(formatQtd(quantidade), 9) +
                padLeft(formatValor(valorUnitario), 14);
    }

    private String formatQtd(BigDecimal qtd) {
        return qtd.setScale(3, RoundingMode.HALF_DOWN).movePointRight(3).toBigInteger().toString();
    }

    private String formatValor(BigDecimal val) {
        return val.setScale(2, RoundingMode.HALF_DOWN).movePointRight(2).toBigInteger().toString();
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
