# Trabalho Final

Alunos: João Victor Bolsson Marques e Lucas Roges de Araujo

## Compilação

* Na IDE Netbeans, através do botão 'Build Project'.

* Via linha de comando:

```
ant -f <full-path to build.xml> -Dnb.internal.action.name=build jar
```

## Execução

* Na IDE Netbeans, através do botão 'Run Project' (cuidar parametrização: args[0] é o ip da máquina).

* Via linha de comando:

```
java -jar <path to dist/TrabFinal.jar> <ip>
```

## Funcionamento

O main está na classe Client.

Essa classe executa e cria uma thread para realizar a descoberta de máquinas usando o middleware.

A thread funciona o tempo inteiro, mas não trata reordenação do relógio de vetores se uma máquina entrar após envio da primeira mensagem. Logo, para correto funcionamento, executar todos os programas de início.

Na thread main, é criado o canal, que é responsável por escutar novas mensagens em sua thread. Na sequência, o programa entra em laço infinito para interagir com o usuário e futuramente enviar as mensagens via métodos do canal multicast.

No canal multicast, está sendo executada uma thread para receber mensagens, que usa o algoritmo de ordenação causal para controlar a liberação da mensagem recebida via *callback*.

Não foi implementado algoritmo de estabilização. O buffer para guardar mensagens está no cliente que enviou, mas nunca é limpo. Houve tentativa de fazer um algoritmo que conseguisse auxiliar na limpeza desse buffer, que seria um algoritmo adaptado em relação ao indicado na especificação, porém não tivemos sucesso em fazê-lo funcionar.

Se houver mensagens no buffer, o que acontecerá após o envio da primeira mensagem, o programa pergunta ao usuário qual mensagem quer mandar (índex do buffer) para qual usuário (índex da lista de ips). Após essa consulta, que pode ser ignorada, a aplicação pergunta ao usuário se quer enviar para todos a próxima mensagem, ou perguntará individualmente em caso negativo.