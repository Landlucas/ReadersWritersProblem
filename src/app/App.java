package app;

/**
 * The readers and writers problem.
 */
public class App {
  /**
   * Cria número específico de subscribers, publishers e notícias iniciais no buffer
   * 
   * @param args[0] O número de subscribers.
   * @param args[1] O número de publishers.
   * @param args[2] O número de notícias iniciais.
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Como usar: java App <numero de subscribers> <numero de publishers> <numero de noticias iniciais>");
    } else {
      final int SUBSCRIBERS = Integer.parseInt(args[0]);
      final int PUBLISHERS = Integer.parseInt(args[1]);
      final int NOTICIAS = Integer.parseInt(args[2]);
      Buffer buffer = new Buffer(NOTICIAS);
      for (int i = 0; i < SUBSCRIBERS; i++) {
        new Subscriber(buffer).start();
      }
      for (int i = 0; i < PUBLISHERS; i++) {
        new Publisher(buffer).start();
      }
    }
  }
}