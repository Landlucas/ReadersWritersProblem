package app;

import java.util.ArrayList;

/**
 * Um buffer de notícias que tem dois tipos de processos clientes: 
 * - Os escritores, geradores de notícias (publishers).
 * - Leitores de notícias (subscribers).
 */
public class Buffer {
    private ArrayList<Noticia> noticias;

    public Buffer(int num) {
        this.noticias = new ArrayList<Noticia>();
        for (int i = 0; i < num; i++) {
            this.noticias.add(new Noticia());
        }
    }

    /**
     * Adiciona nova notícia no buffer e inicia processo de escrita.
     * 
     * @param publisherNum
     */
    public synchronized void addNoticia(int publisherNum) {
        Noticia noticia = new Noticia();
        noticias.add(noticia);
        noticia.generate(publisherNum);
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