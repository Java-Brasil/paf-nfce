package br.inf.rcconsultoria.model;

import br.inf.rcconsultoria.enums.PafIndicadorValorPercentual;
import br.inf.rcconsultoria.enums.PafTipoEmissaoEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro J1 - NFC-e emitida pelo PAF-NFC-e
 * Contém os dados resumidos de cada NFC-e emitida, incluindo valores e identificação da nota.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafJ1 implements PafRegistro {

    private String cnpj; // 14
    private LocalDate dataEmissao; // yyyyMMdd
    private BigDecimal subtotal; // 14
    private BigDecimal desconto; // 13
    private PafIndicadorValorPercentual indicadorDesconto; // 'V' ou 'P'
    private BigDecimal acrescimo; // 13
    private PafIndicadorValorPercentual indicadorAcrescimo; // 'V' ou 'P'
    private BigDecimal valorLiquido; // 14
    private PafTipoEmissaoEnum tipoEmissao; // 1 dígito (tpEmis)
    private String chaveAcesso; // 44
    private Integer numeroNfce; // 10
    private String serie; // 3
    private String cpfCnpjAdquirente; // 14

    @Override
    public String toLinha() {
        return "J1" +
                padLeft(cnpj, 14) +
                dataEmissao.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                padLeft(formatDecimal(subtotal, 14), 14) +
                padLeft(formatDecimal(desconto, 13), 13) +
                indicadorDesconto.getCodigo() +
                padLeft(formatDecimal(acrescimo, 13), 13) +
                indicadorAcrescimo.getCodigo() +
                padLeft(formatDecimal(valorLiquido, 14), 14) +
                tipoEmissao.getCodigo() +
                padLeft(chaveAcesso, 44) +
                padLeft(numeroNfce != null ? numeroNfce.toString() : "", 10) +
                padRight(serie) +
                padLeft(cpfCnpjAdquirente != null ? cpfCnpjAdquirente : "00000000000000", 14);
    }

    private String formatDecimal(BigDecimal valor, int tamanho) {
        if (valor == null) return padLeft("", tamanho);
        return valor.setScale(2).movePointRight(2).toBigInteger().toString();
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String valor, int tamanho) {
        if (valor == null) valor = "";
        return String.format("%" + tamanho + "s", valor).replace(' ', '0');
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String valor) {
        if (valor == null) valor = "";
        return String.format("%-" + 3 + "s", valor);
    }
}
