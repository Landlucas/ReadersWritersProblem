# The readers and writers problem

## Motivação e contexto
O projeto tem como objetivo estudar o problema clássico de sincronização conhecido como "the readers and writers problem", problema dos leitores e escritores, aplicados a programação concorrente, a partir da bibliografia da disciplina **PROCESSAMENTO PARALELO - 4N - 202001**.

## O que acontece na aplicação (tl;dr)
Na execução inicial, é criado um único `Buffer` de notícias que tem dois tipos de processos clientes: Os escritores, geradores de notícias (`Publisher`), e os leitores de notícias (`Subscriber`).

Durante a execução diversas notícias são lidas (consumidas) por um `Publisher` e geradas (criadas) por um `Subscriber`.
 
Cada `Publisher` e `Subscriber` é executado em uma thread separada. Existem muitas threads concorrentes que desejam consumir o `Buffer`. É aceitável ter vários processos consumindo ao mesmo tempo, mas se uma thread estiver gerando uma notícia, nenhum outro processo poderá consumir ou gerar.

## Como executar
A aplicação recebe no total 3 argumentos do tipo `int`: 
1. O número de subscribers.
2. O número de publishers.
3. O número de notícias iniciais (opcional).

## Detalhamento
`Publishers` e `Subscribers` possuem apenas 1 método `run()` que permanece em execução:

```java
public void run() {
  while (true) {
    try {
    Thread.sleep((int) (Math.random() * esperaMax));
    } catch (InterruptedException e) {}
    this.buffer.generate(this.number);
  }
}
```
No exemplo do `run()` do `Publisher` acima, é gerada uma notícia com `Buffer.generate(int number)` a cada ciclo aleatório com tempo máximo definido por `esperaMax`.

Para o `Subscriber` é semelhante, mas chama o `Buffer.consume(int number)` para ler uma notícia:

```java
public void run() {
  while (true) {
    try {
    Thread.sleep((int) (Math.random() * esperaMax));
    } catch (InterruptedException e) {}
    if (this.buffer.getNoticias().size() > 0) {
      this.buffer.consume(this.number);
    }
  }
}
```
### Buffer

É onde a lista de notícias é armazenada e onde o controle dos processos é realizado para leitura e escrita.

Para gerar uma notícia existe o seguinte método:

```java
public synchronized void generate(int numPublisher) {
  while (this.subscribers != 0) {
    try {
      this.wait();
    } catch (InterruptedException e) {}
  }
  Noticia noticia = new Noticia(this.getNoticias().size());
  noticias.add(noticia);
  System.out.println("Publisher " + (numPublisher + 1) + " começou a gerar notícia " + (noticia.getNumber() + 1) + ".");
  try {
    Thread.sleep((int) (Math.random() * tempoMaxEscrita));
  } catch (InterruptedException e) {}
  System.out.println("Publisher " + (numPublisher + 1) + " parou geração notícia " + (noticia.getNumber() + 1) + ".");
  this.notifyAll();
}
```
Note-se a keyword `synchronized` no método. Essa garante que o objeto de `Buffer` estará **bloqueado**, sem que novas threads possam acessá-lo.

Se previamente existem leitores executando, primeiro é aguardado a liberação dessas threads com o `wait()`:

```java
while (this.subscribers != 0) {
  try {
    this.wait();
  } catch (InterruptedException e) {}
}
```
Uma nova notícia é criada e é simulado um tempo de escrita aleatório:

```java
Noticia noticia = new Noticia(this.getNoticias().size());
...
try {
  Thread.sleep((int) (Math.random() * tempoMaxEscrita));
} catch (InterruptedException e) {}
```
Após finalizar geração, notifica todas as outras threads que aguardavam o bloqueio:

```java
this.notifyAll();
```
Para consumir notícias, `Subscribers` utilizam o seguinte método:

```java
public void consume(int numSubscriber) {
  Noticia noticia = this.getNoticias().get((int) Math.round(Math.random() * (this.getNoticias().size() - 1)));
  synchronized (this) {
    this.subscribers++;
    System.out.println(
        "Subscriber " + (numSubscriber + 1) + " começou a consumir notícia " + (noticia.getNumber() + 1) + ".");
  }
  try {
    Thread.sleep((int) (Math.random() * tempoMaxLeitura));
  } catch (InterruptedException e) {
  }
  synchronized (this) {
    System.out.println(
        "Subscriber " + (numSubscriber + 1) + " parou de consumir notícia " + (noticia.getNumber() + 1) + ".");
    this.subscribers--;
    if (this.subscribers == 0) {
      this.notifyAll();
    }
  }
}
```
Diferente de `Buffer.generate()`, esse método não bloqueia o objeto. Temos apenas alguns blocos com `synchronized` para que as mensagem estejam sincronizadas com o número de leitores no `Buffer`.

Para a leitura, utiliza-se a mesma ideia de simulação de um tempo de leitura aleatório com `Thread.sleep()` e tempo `Math.random() * tempoMaxLeitura`.

Após finalizar consumo, se não há mais leitores, notifica possível thread de gerador que esteja aguardando para iniciar geração no método `generate(int numPublisher)`:
```java
if (this.subscribers == 0) {
  this.notifyAll();
}
```

## Considerações Finais

Com esse projeto conseguimos aprofundar nossa capacidade em desenvolver e gerenciar aplicações com processos simultâneos utilizando threads.

Além disso, observamos potenciais utlidades dessas práticas, como em aplicações que necessitam de procedimentos transacionais, bloqueando recursos compartilhados que não podem ser alterados por processos concorrentes.  

## Autores

Ivo Fritsch e Lucas Land.

## Licença

[![License: CC BY-NC 4.0](https://img.shields.io/badge/License-CC%20BY--NC%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by-nc/4.0/)

Pode usar a vontade, mas não esquece de nos referenciar.