# Publicar a Rede Frio SOA no GitHub

1. Extraia o ZIP.
2. Abra a pasta `RedeFrio_SOA_IntelliJ` pelo IntelliJ, deixe o Maven sincronizar e rode a API.
3. Teste no Insomnia, confirme `mvn test` (ou pela interface do IntelliJ) e salve **prints verdadeiros** em `evidencias/`.
4. No GitHub, clique em **New repository** e dê um nome como `redefrio-soa-gs-2026`.
5. Se usar IntelliJ: **Git > Enable Version Control Integration** (se necessário), **Commit**, configure o remoto e **Push** para `main`. Alternativa: GitHub Desktop > Add Existing Repository > Publish repository.
6. Confira que `README.md`, `pom.xml`, `src/`, `insomnia/`, `docs/`, `IA/` e `evidencias/` aparecem no GitHub.
7. Na aba **Actions**, verifique se a tarefa de testes ficou verde. Se falhar, leia o erro e corrija antes de entregar.
8. Copie a URL pública do repositório e envie na tarefa de **Arquitetura Orientada a Serviços (SOA) e Web Services** do Teams.

**Não publique** pastas `target/`, `.idea/`, arquivos locais do banco `data/*.mv.db`, senhas nem informações pessoais de beneficiários.

A instrução da FIAP (página 27 do briefing) pede **link do repositório GitHub**, documentação, evidências e testes Insomnia. **Não é pedido vídeo** para SOA.
