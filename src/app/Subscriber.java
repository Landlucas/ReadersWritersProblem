package app;

/**
 * Leitor de notícias (subscriber)
 */
public class Subscriber extends Thread {
  final static int esperaMax = 15000;
  private static int subscribers = 0;
  private Buffer buffer;
  private int number;

  /**
   * Cria um Subscriber para o buffer.
   * 
   * @param buffer buffer de noticias.
   */
  public Subscriber(Buffer buffer) {
    this.buffer = buffer;
    this.number = Subscriber.subscribers++;
  }

  /**
   * Roda loop de consumo de notícias aleatórias.
   */
  public void run() {
    while (true) {
      try {
        Thread.sleep((int) (Math.random() * esperaMax));
      } catch (InterruptedException e) {
      }
      if (this.buffer.getNoticias().size() > 0) {
        this.buffer.consume(this.number);
      }
    }
  }
}