package br.inf.rcconsultoria.model;

import br.inf.rcconsultoria.enums.PafIndicadorEstoque;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro E2 - Movimento de estoque por produto.
 * Representa as entradas, saídas e saldo final de produtos
 * movimentados no período, para controle de estoque.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafE2 implements PafRegistro {

    private String cnpj;                  // 14 dígitos, apenas números
    private String codigoProduto;         // até 14 caracteres
    private String cest;                  // 7 caracteres
    private String ncm;                   // 8 caracteres
    private String descricao;             // 50 caracteres
    private String unidade;               // 6 caracteres
    private PafIndicadorEstoque indicador;// '+' ou '-'
    private BigDecimal quantidade;        // 9 dígitos, com 3 casas decimais
    private LocalDate dataEmissao;       // AAAAMMDD
    private LocalDate dataEstoque;       // AAAAMMDD

    @Override
    public String toLinha() {
        return "E2" +
                padLeft(cnpj, 14) +
                padRight(codigoProduto, 14) +
                padRight(cest, 7) +
                padRight(ncm, 8) +
                padRight(descricao, 50) +
                padRight(unidade, 6) +
                indicador.getCodigo() +
                padLeft(formatQuantidade(quantidade), 9) +
                formatDate(dataEmissao) +
                formatDate(dataEstoque);
    }

    private String formatQuantidade(BigDecimal qtd) {
        return qtd.setScale(3, RoundingMode.HALF_DOWN)
                .movePointRight(3)
                .toBigInteger()
                .toString();
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String texto, int tamanho) {
        if (texto == null) texto = "";
        return String.format("%-" + tamanho + "s", texto);
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String texto, int tamanho) {
        if (texto == null) texto = "";
        return String.format("%" + tamanho + "s", texto).replace(' ', '0');
    }

}

