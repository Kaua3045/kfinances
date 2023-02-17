- [X] Mover o gateway de auth para domain layer
- [X] Criar um UseCase para autenticação e adicionar no controller
- [X] Adicionar os testes para o UseCase de autenticação
- [X] Adicionar uma paginação para os Bills do usuário especifico
- [X] Talvez tirar throw EmailAlreadyExists e adicionar no Either no CreateAccountUseCase
- [X] Refactor nos request e response methods (output e input)
- [X] Refactor nos usecases (separar em pastas, create, delete, etc)
- [X] Fix: JWT expirou lança uma exception chamada ExpiredJwtException
- [ ] Passar o Unit do expire JWT para o .env, (criar uma classe que vai pegar o nome da .env e verificar se for igual retorna um ChronoUnit)
- [X] Criar Exception handle para internal server error
- [X] Adicionar alguns LOGS
- [X] Adicionar o Redis
- [X] Adicionar Cache para as Account
- [X] Verificar a performance do método create account
- [X] Adicionar o Sentry ou outro método para monitorar e salvar os errors
- [ ] Adicionar um output exclusivo na camada de dominio, no momento esta infringindo a regra do clen arch, por a domain layer estar acessando a application layer ou então só mudar o retorno para account e adicionar o retorno do createdat e updatedat
- [ ] Revisar a arquitetura toda, algumas coias estão errada