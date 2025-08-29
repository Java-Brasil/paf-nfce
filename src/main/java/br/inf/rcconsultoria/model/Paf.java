package br.inf.rcconsultoria.model;

import br.inf.rcconsultoria.enums.PafNumeroArquivo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Paf {

    private final List<PafA2> registrosA2 = new ArrayList<>();
    private final List<PafP2> registrosP2 = new ArrayList<>();
    private final List<PafE2> registrosE2 = new ArrayList<>();
    private final List<PafJ1> registrosJ1 = new ArrayList<>();
    private final List<PafJ2> registrosJ2 = new ArrayList<>();
    private final List<PafD2> registrosD2 = new ArrayList<>();
    private final List<PafD3> registrosD3 = new ArrayList<>();
    private final List<PafD4> registrosD4 = new ArrayList<>();

    @Setter
    private PafNumeroArquivo numeroArquivo = PafNumeroArquivo.I;

    @Setter
    private String sistema;

    @Setter
    private String bancoDados;

    @Setter
    private PafU1 registroU1;

    @Setter
    private String nomeArquivo;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Paf)) return false;

        Paf paf = (Paf) o;
        return registrosA2.equals(paf.registrosA2) && registrosP2.equals(paf.registrosP2) && registrosE2.equals(paf.registrosE2) && registrosJ1.equals(paf.registrosJ1) && registrosJ2.equals(paf.registrosJ2) && registrosD2.equals(paf.registrosD2) && registrosD3.equals(paf.registrosD3) && registrosD4.equals(paf.registrosD4) && numeroArquivo == paf.numeroArquivo && Objects.equals(sistema, paf.sistema) && Objects.equals(bancoDados, paf.bancoDados) && registroU1.equals(paf.registroU1) && Objects.equals(nomeArquivo, paf.nomeArquivo);
    }

    @Override
    public int hashCode() {
        int result = registrosA2.hashCode();
        result = 31 * result + registrosP2.hashCode();
        result = 31 * result + registrosE2.hashCode();
        result = 31 * result + registrosJ1.hashCode();
        result = 31 * result + registrosJ2.hashCode();
        result = 31 * result + registrosD2.hashCode();
        result = 31 * result + registrosD3.hashCode();
        result = 31 * result + registrosD4.hashCode();
        result = 31 * result + numeroArquivo.hashCode();
        result = 31 * result + Objects.hashCode(sistema);
        result = 31 * result + Objects.hashCode(bancoDados);
        result = 31 * result + registroU1.hashCode();
        result = 31 * result + Objects.hashCode(nomeArquivo);
        return result;
    }

    @Override
    public String toString() {
        return "Paf{" +
                "registrosA2=" + registrosA2 +
                ", registrosP2=" + registrosP2 +
                ", registrosE2=" + registrosE2 +
                ", registrosJ1=" + registrosJ1 +
                ", registrosJ2=" + registrosJ2 +
                ", registrosD2=" + registrosD2 +
                ", registrosD3=" + registrosD3 +
                ", registrosD4=" + registrosD4 +
                ", numeroArquivo=" + numeroArquivo +
                ", sistema='" + sistema + '\'' +
                ", bancoDados='" + bancoDados + '\'' +
                ", registroU1=" + registroU1 +
                ", nomeArquivo='" + nomeArquivo + '\'' +
                '}';
    }
}

