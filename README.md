# tourist-attractions-app
Este projeto faz parte da disciplina de Tópicos Avançados em Programação para Dispositivos Móveis do curso de Análise e Desenvolvimento de Sistemas (6º período) da Universidade Tecnológica Federal do Paraná. (UTFPR)

###👨‍💻 Desenvolvedores

    - Ana Flávia Perin
    - Gustavo Mestre
    - Jonathan Cassol
    - Matheus Guedes


---

## 📁 Estrutura do Fluxo de Trabalho do Git

Para manter um desenvolvimento organizado e colaborativo, adotamos um fluxo de trabalho baseado em branches com as seguintes diretrizes:

### 🔀 Branches principais

- **`main`**: Contém a versão estável e pronta para produção. Nenhum commit direto deve ser feito nessa branch.
- **`develop`**: Branch de desenvolvimento contínuo. Todas as novas funcionalidades e correções partem daqui.
- **`release`**: Branch intermediária entre `develop` e `main`, usada para testes e ajustes finais antes de uma nova versão ser publicada.

> O ciclo típico é: `develop` → `release` → `main`

---

### 🌿 Branches secundárias

- **`[id]-feature/nome-da-feature`**: Para o desenvolvimento de novas funcionalidades.
- **`[id]-fix/nome-do-fix`**: Para correções de bugs ou melhorias pontuais.
- **`[id]-hotfix/nome-do-hotfix`**: Para correções urgentes aplicadas diretamente na `main`.
- **`[id]-docs/nome-da-documentacao`**: Para alterações relacionadas à documentação do projeto.

> 🔤 **Padrão de nomeação:** Utilizamos o padrão `kebab-case` (palavras separadas por hífens) com prefixo numérico identificando o ID da task. Isso facilita a rastreabilidade com ferramentas de gerenciamento de tarefas.

> 🔧 Exemplos de nome de branch:
> - `8398-feature/cadastro-usuario`
> - `8401-fix/erro-login`
> - `8420-release/v1-0-0`

---

### ✅ Pull Requests

- Toda mudança deve ser enviada via **Pull Request** para a branch apropriada.
- **Features e fixes** são integrados em `develop`.
- Após validação e testes, `develop` é mesclada na `release`.
- Após testes finais, `release` é integrada na `main` (e em `develop` novamente, se necessário).
- PRs devem ser revisados por ao menos **um membro da equipe**.

---

### 📝 Commits

- Commits devem ser claros, concisos e utilizar prefixos padronizados:
  - `feat:` nova funcionalidade
  - `fix:` correção de bug
  - `docs:` mudanças na documentação
  - `refactor:` melhoria/refatoração sem alterar funcionalidades
  - `test:` inclusão ou alteração de testes
  - `chore:` tarefas de manutenção/configuração

> ✍️ Exemplo: `feat: implementa criação de projeto com validações`
>
> ![Fluxo do Git](https://github.com/alisonoliveira13/Extension-Manager/blob/main/Untitled-2025-04-07-2113.png)
