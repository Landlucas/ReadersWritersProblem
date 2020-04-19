package app;

import java.util.ArrayList;

/**
 * Um buffer de notícias que tem dois tipos de processos clientes: Os
 * escritores, geradores de notícias (publishers) e os leitores de notícias
 * (subscribers).
 * 
 * Existem muitas threads concorrentes que desejam consumir. É aceitável ter
 * vários processos consumindo ao mesmo tempo, mas se uma thread estiver gerando,
 * nenhum outro processo poderá consumir ou gerar.
 */
public class Buffer {
  private ArrayList<Noticia> noticias;
  private int subscribers;
  final static int tempoMaxLeitura = 4000;
  final static int tempoMaxEscrita = 10000;

  public Buffer(int num) {
    this.noticias = new ArrayList<Noticia>();
    for (int i = 0; i < num; i++) {
      this.noticias.add(new Noticia(i));
    }
  }

  /**
   * Consome notícia.
   * 
   * @param numSubscriber Número do subscriber.
   */
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

  /**
   * Gera noticia.
   * 
   * @param numPublisher Número do publisher.
   */
  public synchronized void generate(int numPublisher) {
    while (this.subscribers != 0) {
      try {
        this.wait();
      } catch (InterruptedException e) {
      }
    }
    Noticia noticia = new Noticia(this.getNoticias().size());
    noticias.add(noticia);
    System.out.println("Publisher " + (numPublisher + 1) + " começou a gerar notícia " + (noticia.getNumber() + 1) + ".");
    try {
      Thread.sleep((int) (Math.random() * tempoMaxEscrita));
    } catch (InterruptedException e) {
    }
    System.out.println("Publisher " + (numPublisher + 1) + " parou geração notícia " + (noticia.getNumber() + 1) + ".");
    this.notifyAll();
  }

  /**
   * Retorna lista de notícias do buffer.
   * 
   * @return
   */
  public ArrayList<Noticia> getNoticias() {
    return this.noticias;
  }
}