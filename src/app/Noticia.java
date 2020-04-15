package app;

/**
 * Cada notícia é gerada pelos escritores e apenas consumida pelos
 * leitores.
 */
public class Noticia {
  private int number;

  /**
   * Inicializa noticia.
   * 
   * @param number Número da notícia
   */
  public Noticia(final int number) {
    this.number = number;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(final int number) {
    this.number = number;
  }
}