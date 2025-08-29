package br.inf.rcconsultoria.model;

import br.inf.rcconsultoria.enums.PafTotalizadorParcialEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro J2 - Detalhes da NFC-e emitida em contingência
 * Cada linha representa um item de documento fiscal, conforme layout oficial da SEFAZ-SC.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafJ2 implements PafRegistro {

    private String cnpj;
    private LocalDate dataEmissao;
    private Integer numeroItem;
    private String codigoProduto;
    private String descricaoProduto;
    private BigDecimal quantidade;
    private String unidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorDesconto;
    private BigDecimal valorAcrescimo;
    private BigDecimal valorTotalLiquido;
    private PafTotalizadorParcialEnum totalizadorParcialEnum;
    private BigDecimal aliquotaTotalizadorParcial;
    private int casasDecimaisQuantidade;
    private int casasDecimaisValorUnitario;
    private Integer numeroNfce;
    private String serie;
    private String chaveAcesso;
    private String codigoTributacao; // CST/CSOSN

    @Override
    public String toLinha() {
        PafTotalizadorParcialEnum totalizador = totalizadorParcialEnum != null
                ? totalizadorParcialEnum
                : mapearTotalizador();

        return "J2" +
                padLeft(cnpj, 14) +
                formatData(dataEmissao) +
                padLeft(numeroItem.toString(), 3) +
                padRight(codigoProduto, 14) +
                padRight(descricaoProduto, 100) +
                formatDecimal(quantidade, 7, casasDecimaisQuantidade) +
                padRight(unidade, 3) +
                formatDecimal(valorUnitario, 8, casasDecimaisValorUnitario) +
                formatDecimal(valorDesconto, 8, 2) +
                formatDecimal(valorAcrescimo, 8, 2) +
                formatDecimal(valorTotalLiquido, 14, 2) +
                padRight(gerarCodigoTotalizador(totalizador), 7) +
                casasDecimaisQuantidade +
                casasDecimaisValorUnitario +
                padLeft(numeroNfce.toString(), 10) +
                padRight(serie, 3) +
                padLeft(chaveAcesso, 44);
    }

    private PafTotalizadorParcialEnum mapearTotalizador() {
        if (codigoTributacao == null) return PafTotalizadorParcialEnum.N;

        switch (codigoTributacao) {
            // CST regime normal
            case "00":
            case "10":
            case "20":
            case "101":
            case "102":
                return PafTotalizadorParcialEnum.T;

            case "30":
            case "60":
            case "70":
            case "201":
            case "202":
            case "203":
            case "500":
                return PafTotalizadorParcialEnum.F;

            case "40":
            case "103":
                return PafTotalizadorParcialEnum.I;

            case "41":
            case "50":
            case "51":
            case "90":
            case "300":
            case "400":
            case "900":
            default:
                return PafTotalizadorParcialEnum.N;
        }
    }


    private String gerarCodigoTotalizador(PafTotalizadorParcialEnum totalizador) {
        if (totalizador == null) {
            throw new IllegalStateException("Tipo de totalizador parcial não informado.");
        }

        switch (totalizador) {
            case T:
            case S:
                if (aliquotaTotalizadorParcial == null) {
                    throw new IllegalStateException("Alíquota não informada para totalizador parcial " + totalizador.getCodigo());
                }
                int valor = aliquotaTotalizadorParcial.setScale(2).movePointRight(2).intValue(); // ex: 18.00 → 1800
                String sufixo = String.format("%04d", valor);

                return totalizador.getCodigo().charAt(0) + sufixo; // T1800 ou S0500
            default:
                return totalizador.getCodigo(); // códigos fixos: F, I, N, etc.
        }
    }


    private String formatData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String formatDecimal(BigDecimal valor, int tamanho, int decimais) {
        if (valor == null) valor = BigDecimal.ZERO;
        BigDecimal ajustado = valor.setScale(decimais, RoundingMode.HALF_DOWN).movePointRight(decimais);
        return padLeft(ajustado.toBigInteger().toString(), tamanho);
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String valor, int tamanho) {
        return String.format("%" + tamanho + "s", valor == null ? "" : valor).replace(' ', '0');
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String valor, int tamanho) {
        return String.format("%-" + tamanho + "s", valor == null ? "" : valor);
    }
}
