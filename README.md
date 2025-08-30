# PAF-NFCe Brasil 🇧🇷

[![Maven Central](https://img.shields.io/maven-central/v/br.inf.rcconsultoria/paf-nfce)](https://central.sonatype.com/artifact/br.inf.rcconsultoria/paf-nfce)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.java.net/)

> Biblioteca Java para geração de relatórios PAF-NFCe (Programa Aplicativo Fiscal - Nota Fiscal do Consumidor Eletrônica) em conformidade com a legislação de Santa Catarina.

## 📋 Sobre

Esta biblioteca facilita a geração dos arquivos de relatórios fiscais PAF-NFCe exigidos pela [Secretaria de Estado da Fazenda de Santa Catarina](https://www.sef.sc.gov.br/), conforme especificado no **Anexo III do Ato DIAT 38/2020** (atualizado em agosto/2022).

### Powered by
[![JetBrains logo.](https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg)](https://jb.gg/OpenSource)

[Thanks to JetBrains for supporting this project!](https://www.jetbrains.com/?from=paf-nfce)

## Dúvidas, Sugestões ou Consultoria
[![Java Brasil](https://discordapp.com/api/guilds/519583346066587676/widget.png?style=banner2)](https://discord.gg/ZXpqnaV)

## Gostou do Projeto? Dê sua colaboração pelo Pix:

<div align="center">
    <img src="https://github.com/Java-Brasil/paf-nfce/blob/master/qrcode-pix.png" width="200" />
</div>

### ✨ Funcionalidades

- ✅ Geração de arquivos TXT com dados do DAV (Documento Auxiliar de Venda)
- ✅ Assinatura digital XML com certificado A1/A3
- ✅ Validação automática dos dados obrigatórios
- ✅ Suporte aos registros: U1, D2, D3, D4
- ✅ Compatível com Java 8+
- ✅ Interface fluente e fácil de usar

## 🚀 Instalação

### Maven
```xml
<dependency>
    <groupId>br.inf.rcconsultoria</groupId>
    <artifactId>paf-nfce</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
```gradle
implementation 'br.inf.rcconsultoria:paf-nfce:1.0.0'
```

## 📖 Uso Básico

### Exemplo Completo

```java
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.inf.rcconsultoria.enums.PafNumeroArquivo;
import br.inf.rcconsultoria.model.*;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;

public class ExemploPafNfce {
    
    public static void main(String[] args) throws Exception {
        // 1. Carregamento do certificado digital
        Certificado certificado = CertificadoService.certificadoPfx(
            "caminho/para/certificado.pfx", 
            "senha_do_certificado"
        );

        // 2. Configuração básica do PAF
        Paf paf = new Paf();
        paf.setNumeroArquivo(PafNumeroArquivo.IV);
        paf.setSistema("MEU-SISTEMA-PDV");
        paf.setBancoDados("BASE_PRODUCAO");
        paf.setNomeArquivo("DAV");

        // 3. Registro U1 (Obrigatório - Dados da empresa)
        paf.setRegistroU1(PafU1.builder()
                .cnpj("12345678000195")
                .inscricaoEstadual("123456789")
                .inscricaoMunicipal("987654321")
                .razaoSocial("MINHA EMPRESA LTDA")
                .build());

        // 4. Registro D2 (DAV - Documento Auxiliar de Venda)
        paf.getRegistrosD2().add(PafD2.builder()
                .cnpj("12345678000195")
                .dataEmissao(LocalDate.now())
                .numeroDav("0001234567")
                .nomeCliente("JOÃO DA SILVA")
                .cpfCnpj("12345678901")
                .placaVeiculo("ABC1D23")
                .build());

        // 5. Registro D3 (Itens do DAV)
        paf.getRegistrosD3().add(PafD3.builder()
                .cnpj("12345678000195")
                .dataEmissao(LocalDate.now())
                .numeroDav("0001234567")
                .numeroItem(1)
                .codigoProduto("PROD001")
                .descricao("Produto de Exemplo")
                .unidade("UN")
                .quantidade(new BigDecimal("2.5"))
                .valorUnitario(new BigDecimal("15.90"))
                .build());

        // 6. Registro D4 (Cancelamento de DAV - opcional)
        paf.getRegistrosD4().add(PafD4.builder()
                .cnpj("12345678000195")
                .dataEmissao(LocalDate.now())
                .numeroDav("0001234567")
                .dataCancelamento(LocalDate.now())
                .build());

        // 7. Geração dos arquivos
        PafGerador gerador = new PafGerador(paf, certificado);
        gerador.setPastaDestino(Paths.get("output"));
        gerador.gerar();

        System.out.println("✅ Arquivos PAF-NFCe gerados com sucesso!");
    }
}
```

## 📂 Estrutura dos Registros

### 📄 Registro U1 (Obrigatório)
```java
PafU1.builder()
    .cnpj("CNPJ da empresa")
    .inscricaoEstadual("IE da empresa")
    .inscricaoMunicipal("IM da empresa") // Opcional
    .razaoSocial("Razão social da empresa")
    .build()
```

### 📄 Registro D2 (DAV)
```java
PafD2.builder()
    .cnpj("CNPJ da empresa")
    .dataEmissao(LocalDate.now())
    .numeroDav("Número único do DAV")
    .nomeCliente("Nome do cliente")
    .cpfCnpj("CPF/CNPJ do cliente")
    .placaVeiculo("Placa do veículo") // Para delivery
    .build()
```

### 📄 Registro D3 (Itens do DAV)
```java
PafD3.builder()
    .cnpj("CNPJ da empresa")
    .dataEmissao(LocalDate.now())
    .numeroDav("Número do DAV")
    .numeroItem(1) // Sequencial do item
    .codigoProduto("Código do produto")
    .descricao("Descrição do produto")
    .unidade("Unidade (UN, KG, etc)")
    .quantidade(new BigDecimal("1.0"))
    .valorUnitario(new BigDecimal("10.00"))
    .build()
```

### 📄 Registro D4 (Cancelamento)
```java
PafD4.builder()
    .cnpj("CNPJ da empresa")
    .dataEmissao(LocalDate.now())
    .numeroDav("Número do DAV cancelado")
    .dataCancelamento(LocalDate.now())
    .build()
```

## 🔧 Configuração do Certificado

### Certificado A1 (.pfx)
```java
// Por arquivo
Certificado certificado = CertificadoService.certificadoPfx(
    "caminho/certificado.pfx", 
    "senha"
);

// Por bytes
byte[] pfxBytes = Files.readAllBytes(Paths.get("certificado.pfx"));
Certificado certificado = CertificadoService.certificadoPfxBytes(
    pfxBytes, 
    "senha"
);
```

### Certificado A3 (Token/Smart Card)
```java
Certificado certificado = CertificadoService.certificadoA3(
    "senha_do_token"
);
```

## 📋 Requisitos

- ☑️ **Java 8+** (JDK 8 ou superior)
- ☑️ **Certificado Digital A1 ou A3** válido
- ☑️ **CNPJ** ativo e regular

## 📄 Documentação Oficial

Esta biblioteca implementa as especificações do:
- **Anexo III do Ato DIAT 38/2020** (SEF/SC)
- Atualização: Agosto/2022

## 🤝 Contribuição

Contribuições são bem-vindas! Por favor:

1. Faça um **fork** do projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. **Commit** suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. **Push** para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um **Pull Request**
---

<div align="center">
  <strong>Desenvolvido com ❤️ para a comunidade Java Brasil</strong><br>
  <a href="https://github.com/Java-Brasil">🔗 Mais projetos da organização Java Brasil</a>
</div>
