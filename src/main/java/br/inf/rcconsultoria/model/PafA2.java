package br.inf.rcconsultoria.model;

import br.inf.rcconsultoria.enums.PafTipodocumentoEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registro A2 - Totalizadores parciais das operações de venda
 * Gera os valores consolidados das vendas realizadas, como valor bruto,
 * isenções, substituição tributária, etc.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafA2 implements PafRegistro {

    private LocalDate data; // AAAAMMDD
    private String meioPagamento;
    private PafTipodocumentoEnum tipoDocumento; // 1=NFC-e, 2=NF-e, 3=Não Tributável
    private BigDecimal valor; // 12 dígitos, sem ponto ou vírgula
    private String cpfCnpj; // 14 dígitos
    private Integer numeroDocumento; // 10 dígitos

    @Override
    public String toLinha() {
        return "A2" +
                formatData(data) +
                padRight(meioPagamento) +
                tipoDocumento.getCodigo() +
                padLeft(formatValor(valor), 12) +
                padLeft(cpfCnpj == null ? "" : cpfCnpj, 14) +
                padLeft(numeroDocumento != null ? numeroDocumento.toString() : "", 10);
    }

    private String formatData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String formatValor(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_DOWN).movePointRight(2).toBigInteger().toString();
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String s, int length) {
        return String.format("%" + length + "s", s == null ? "" : s);
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String s) {
        return String.format("%-" + 25 + "s", s == null ? "" : s);
    }
}
