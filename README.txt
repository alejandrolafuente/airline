1. INSTRUÇÕES PARA RODAR:

- O backend inteiro foi virtualizado e automatizado com Docker, para rodar execute na raíz o script com:

    ./builder.sh 

- Caso o terminal peça, execute antes o comando:

    chmod +x builder.sh

- Verifique que as portas de seu host estão livres para evitar conflitos. Quando o front for finalizado o sript será modificado para alocação dinâmica de portas (basta acrescentar um método)

2. OBSERVAÇÕES SOBRE ESTE PROJETO

- A especificação COMPLETA do projeto está no anexo "Enunciado.pdf" incluindo o diagrama arquitetural

- O gateway em Node JS e o FrontEnd em Angular ainda não estão prontos. Na verdade o Gateway está praticamente finalizado, mas primeiro será concluido o FrontEnd , depois o Gateway, todo o sistema será refinado (refatorado), e finalmente, tudo será conteinirizado e automatizado com o builder.sh

- O script também iniciará o PgAdmin já conectado aos bancos de dados, basta acessar localhost:5050

- Para logar no PgAdmin -> user: dac@email.com ; password: password

- Uma vez no PgAdmin, para conectar a cada banco de dados a senha é a mesma: postgres

TECNOLOGIAS USADAS NO PROJETO:

- Docker, Spring Java, Angular, Node JS, RabbitMQ, PostgreSQL, Mongo DB, PgAdmin

* Todo o código deste projeto é 100% de autoria própria