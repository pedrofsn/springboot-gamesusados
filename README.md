# Descrição do projeto
Projeto backend de um sistema de anúncios de jogos. Neste sistema os usuários regulares podem cadastrar anúncios nos jogos que foram previamente cadastrados por gerentes (somente).

# Funcionalidades disponíveis na API

### Admin
- Cadastrar usuário com perfil de gerente

### Gerente
- Cadastrar plataformas
- Cadastrar jogos
- Ativar/desativar anúncios
- Visualizar lista de denúncias

### Regular
- Listar anúncios
- Listar jogos por plataforma
- Cadastrar anúncio de um jogo
- Desativar um anúncio próprio
- Denunciar um jogo
- Denunciar um anúncio

### Sem perfil / Abertos
- Cadastro de usuário com perfil regular
- Upload de imagem
- Visualizar imagens
- Listar plataformas
- Listar jogos
- Pesquisar jogos
- Logar usuário


## Melhorias
- [ ] Implementar o cadastro de plataformas no perfil do gerente

## Pontos de atenção
O arquivo da imagem de um jogo deve coincidir com o id do jogo em questão, além de estar no único formato esperado que é PNG. Em outras palavras se o jogo de id 1 já tem uma imagem cadastrada, qualquer um que fizer uma requisição de upload de imagem de um novo arquivo com o mesmo nome (1.png) irá sobrescrever a imagem original do jogo de id 1. Não está previsto uma evolução ou melhoria neste sentido.
