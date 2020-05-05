import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Video extends Media {

    private static int counter = 0;

    private final Director director;
    private final ArrayList<Actor> actors;
    private final int id, lenght;

    private Logger logger;

    public Video(final String name, final Director director, final Util.ageRatingsEnum ageRating,
            final ArrayList<Util.genresEnum> genres, final ArrayList<Actor> actors) {
        super(name, ageRating, genres);

        this.director = director;

        /**
         * Atribui valor aos atores. Precisa ser feito fora de método pois são final
         * Vocês podem usar essa estrutura de chaves dentro de métodos para separar
         * regiões de código que não tem ligação com o resto Além de facilitar a leitura
         * para alguém (que pode ignorar o código ao ler o comentário que deixei acima)
         * Vocês podem usar os comandos de IDE de compactar os blocos de código pra
         * fazer isso virar 1 linha Com o comentário assim, no formato de Javadoc, a
         * primeira linha fica visível. Vocês podem até compactar o comentário
         */
        {
            if (actors == null || actors.size() <= 0) {
                logger.log(Level.WARNING, "Não foi passado um ator!");
                this.actors = null;
            }
            // Desde que tenha algo, podemos copiar tudo com o mesmo loop
            else {
                this.actors = new ArrayList<>();
                // Se for maior que o tamanho máximo, mensagem de erro
                if (actors.size() > Util.MAXGENRES)
                    logger.log(Level.WARNING, "Mais de 3 atores foram passados!");
                // Copiar até o máximo ou o tamanho atual
                // Precisa fazer assim para criar uma cópia "deep" (ou seja, copiar valores e
                // não endereços)
                for (int i = 0; (i < Util.MAXACTORS) && (i < actors.size()); ++i)
                    this.actors.add(actors.get(i));
            }
        }

        this.id = counter++;

        setLength(-1);
    }

    public int getId() {
        return id;
    }

    public Director getDirector() {
        return director;
    }

    Video(Video vid) {
        super(vid);
        director = vid.director;
        actors = vid.actors;
        id = vid.id;
        lenght = vid.lenght;
    }

    /**
     * Usamos aqui aquele método static de copiar arrays da classe Util Como ele é
     * genérico (ver comentário no método em Util) precisamos dar o cast para o tipo
     * adequado no retorno Ou seja, um arraylist de atores
     *
     * @return
     */
    public ArrayList<Actor> getActors() {
        final ArrayList<Actor> copy = (ArrayList<Actor>) Util.CopyArray(this.actors);
        return copy;
    }

}
