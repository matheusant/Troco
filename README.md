# Troco 🛒

Aplicativo Android nativo desenvolvido em **Kotlin** e **Jetpack Compose**, integrando-se com **Firebase (Authentication + Cloud Firestore)** para gerenciar as operações de uma mercearia de bairro e facilitar a interação entre clientes e comerciantes.

---

## 🎯 Objetivo do Projeto

**Troco** tem como objetivo digitalizar e otimizar a gestão de um mercadinho tradicional de bairro, oferecendo uma experiência moderna e prática tanto para a lojista quanto para os clientes da comunidade.

O sistema divide-se em dois perfis principais de usuário:
- **Cliente:** Pode navegar pelo catálogo de produtos da mercearia, consultar preços, verificar o histórico de compras e acompanhar o extrato do seu fiado ("caderneta").
- **Admin:** Possui ferramentas completas para gerenciamento de estoque e catálogo de produtos, além do controle rigoroso da caderneta de fiado dos clientes (registro de débitos, pagamentos e saldos devedores).

---

## 🛠️ Tecnologias e Arquitetura

O projeto foi construído utilizando as melhores práticas e ferramentas modernas do ecossistema Android:

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **Interface de Usuário:** [Jetpack Compose](https://developer.android.com/jetpack/compose) com Material 3 (UI 100% declarativa).
- **Arquitetura:** Clean Architecture / MVVM (Model-View-ViewModel) com separação clara de responsabilidades (Camadas de `data`, `domain` e `ui`).
- **Backend & Banco de Dados em Nuvem:** [Firebase Authentication](https://firebase.google.com/docs/auth) e [Cloud Firestore](https://firebase.google.com/docs/firestore) com suporte a sincronização em tempo real.
- **Persistência Local & Offline-First:** [Room](https://developer.android.com/training/data-storage/room) para persistência local e cache, integrado com **WorkManager** para sincronização robusta em segundo plano com o Firestore.
- **Assincronicidade:** Kotlin Coroutines & Flow (listagem reativa e estados de carregamento).
- **Navegação:** Jetpack Navigation Compose.

---

## ✨ Funcionalidades Implementadas

1. **Autenticação e Perfil de Usuário:**
   - Fluxo completo de login, validação de sessão e gerenciamento de perfil.
   - Suporte a papéis de usuário (`admin` vs `cliente`) mapeados no Firestore.

2. **Gerenciamento e Catálogo de Produtos:**
   - Cadastro, edição, remoção e listagem reativa de produtos.
   - Busca em tempo real e estados de carregamento otimizados.

3. **Sincronização Offline-First e Real-Time:**
   - Persistência local com Room para funcionamento offline.
   - Sincronização em tempo real com o Cloud Firestore.
   - Gerenciamento de tarefas em segundo plano utilizando WorkManager.

4. **Caderneta de Fiado:**
   - Acompanhamento de extratos, débitos e pagamentos.

---

## 👥 Perfis de Acesso

1. **Cliente:**
   - Visualização do catálogo de produtos disponíveis.
   - Consulta do próprio saldo e histórico de fiado.
   - Gerenciamento de perfil e autenticação.

2. **Administrador (Admin):**
   - Gestão completa de produtos (Adicionar, editar, atualizar estoque e preços).
   - Gerenciamento de fiados e caderneta dos clientes.

---

## 📂 Documentação de Apoio

Para detalhes técnicos avançados, regras de negócio e planejamento, consulte os arquivos na raiz do projeto:
- [`SPEC.md`](SPEC.md) — Especificação técnica e funcional completa (Contrato do projeto).
- [`PLAN.md`](PLAN.md) — Planejamento de fases, sprints e tarefas de desenvolvimento.
- [`CLAUDE.md`](CLAUDE.md) — Diretrizes de desenvolvimento e engenharia.

---

## 🚀 Como Executar o Projeto

1. **Pré-requisitos:**
   - Android Studio (versão Hedgehog ou superior recomendada).
   - JDK 17.
   - Configurar o projeto no Firebase Console e adicionar o arquivo `app/google-services.json`.

2. **Compilação e Testes (via Gradle):**
   ```bash
   # Compilar o código Kotlin em modo debug
   ./gradlew :app:compileDebugKotlin

   # Executar testes unitários
   ./gradlew :app:testDebugUnitTest
   ```

---

*Este README foi atualizado com as funcionalidades recentes implementadas no projeto.*
