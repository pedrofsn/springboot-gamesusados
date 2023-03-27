## Descrição do projeto
Projeto backend de um sistema de anúncios de jogos. Neste sistema os usuários regulares podem cadastrar anúncios de jogos, que foram previamente cadastrados por gerentes (somente).

### Regras de negócio e Perfis de Usuário

#### Sem perfil 
- Cadastrar usuário com perfil regular
- Visualizar plataformas
- Visualizar jogos
- Pesquisar jogos
- Realizar login

#### Regular
- Visualizar anúncios
- Visualizar jogos por plataforma
- Cadastrar anúncios
- Visualizar anúncios
- Denunciar anúncios
- Denunciar jogos
- Desativar um anúncio próprio

#### Gerente
- Cadastrar plataformas
- Cadastrar jogos
- Publicar/suspender anúncios
- Visualizar lista de denúncias
- Visualizar lista de usuários

#### Admin
- Cadastrar usuário com perfil de gerente

## Pontos de atenção
- Não há um tratamento rigoroso para o salvamento de arquivos (de imagem). Qualquer upload com o mesmo nome/id irá sobrescerver a imagem existente.
- Não há tratamento ou validação sobre as proporções/redimensionamento de imagens;
- É esperado que as imagens estejam no formato PNG;