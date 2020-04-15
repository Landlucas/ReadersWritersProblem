package app;

/**
 * Escritor de notícias (publisher)
 */
public class Publisher extends Thread {
  final static int esperaMax = 10000;
  private static int publishers = 0;
  private Buffer buffer;
  private int number;

  /**
   * Cria um Publisher para o buffer.
   * 
   * @param buffer buffer de noticias.
   */
  public Publisher(Buffer buffer) {
    this.buffer = buffer;
    this.number = Publisher.publishers++;
  }

  /**
   * Roda loop de geração de noticias.
   */
  public void run() {
    while (true) {
      try {
        Thread.sleep((int) (Math.random() * esperaMax));
      } catch (InterruptedException e) {
      }
      this.buffer.generate(this.number);
    }
  }
}