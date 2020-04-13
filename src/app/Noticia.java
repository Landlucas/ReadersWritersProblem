package app;

/**
 * Cada notícia é gerada e consumida pelos escritores e apenas consumida pelos
 * leitores
 * 
 * Existem muitas threads concorrentes que desejam consumir. É aceitável ter
 * vários processos lendo ao mesmo tempo, mas se uma thread estiver gerando,
 * nenhum outro processo poderá consumir ou gerar.
 */
public class Noticia {
  private static int noticias;
  private int number;
  private int subscribers;
  final static int tempoMaxLeitura = 6000;
  final static int tempoMaxEscrita = 10000;

  /**
   * Inicializa noticia.
   */
  public Noticia() {
    this.number = Noticia.noticias++;
    this.subscribers = 0;
  }

  /**
   * Consome notícia.
   * 
   * @param num Número do subscriber.
   */
  public void consume(int num) {
    synchronized (this) {
      this.subscribers++;
      System.out.println("Subscriber " + ( num + 1 ) + " começou a consumir notícia " + ( this.number + 1 ) + ".");
    }
    try {
      Thread.sleep((int) (Math.random() * tempoMaxLeitura));
    } catch (InterruptedException e) {
    }
    synchronized (this) {
      System.out.println("Subscriber " + ( num + 1 ) + " parou de consumir notícia " + ( this.number + 1 ) + ".");
      this.subscribers--;
      if (this.subscribers == 0) {
        this.notifyAll();
      }
    }
  }

  /**
   * Gera noticia.
   * 
   * @param num Número do publisher.
   */
  public synchronized void generate(int num) {
    while (this.subscribers != 0) {
      try {
        this.wait();
      } catch (InterruptedException e) {
      }
    }
    System.out.println("Publisher " + ( num + 1 ) + " começou a gerar notícia " + ( this.number + 1 ) + ".");
    try {
      Thread.sleep((int) (Math.random() * tempoMaxEscrita));
    } catch (InterruptedException e) {
    }
    System.out.println("Publisher " + ( num + 1 ) + " parou geração notícia " + ( this.number + 1 ) + ".");
    this.notifyAll();
  }
}