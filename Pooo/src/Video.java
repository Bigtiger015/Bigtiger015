import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Video {
    private static int counter = 0;

    private final String name;
    private final Util.ageRatingsEnum ageRating;
    private final Director director;
    private final ArrayList<Actor> actors;
    private final int id;
    private final ArrayList<Util.genresEnum> genres;

    private int length, year, nViews, nUserRatings;
    private float userRating;
    private Logger logger;

    
    public Video(String name, Director director, Util.ageRatingsEnum ageRating, ArrayList<Util.genresEnum> genres, ArrayList<Actor> actors)
    {
        //Essa classe é responsável por criar mensagens de log na saída padrão. Ela é mais apropriada que o println
        //Mas não tem problema usar o println para o projeto.
        logger = Logger.getLogger(Video.class.getName());

        this.name = name;
        this.ageRating = ageRating;
        this.director = director;

        /**
         * Atribui valor aos gêneros. Precisa ser feito fora de método pois são final
         * Vocês podem usar essa estrutura de chaves dentro de métodos para separar regiões de código que não tem ligação com o resto
         * Além de facilitar a leitura para alguém (que pode ignorar o código ao ler o comentário que deixei acima)
         * Vocês podem usar os comandos de IDE de compactar os blocos de código pra fazer isso virar 1 linha
         * Com o comentário assim, no formato de Javadoc, a primeira linha fica visível. Vocês podem até compactar o comentário
         */
        {
            if(genres == null || genres.size() <= 0)
            {
                logger.log(Level.WARNING, "Não foi passado um gênero!");
                this.genres = null;
            }
            //Desde que tenha algo, podemos copiar tudo com o mesmo loop
            else
            {
                this.genres = new ArrayList<>();
                //Se for maior que o tamanho máximo, mensagem de erro
                if(genres.size() > Util.MAXGENRES)
                    logger.log(Level.WARNING, "Mais de 3 gêneros foram passados!");

                //Copiar até o máximo ou o tamanho atual
                //Precisa fazer assim para criar uma cópia "deep" (ou seja, copiar valores e não endereços)
                for (int i = 0; (i < Util.MAXGENRES) && (i < genres.size()); ++i)
                    this.genres.add(genres.get(i));
            }
        }

        /**
         * Atribui valor aos atores. Precisa ser feito fora de método pois são final
         * Vocês podem usar essa estrutura de chaves dentro de métodos para separar regiões de código que não tem ligação com o resto
         * Além de facilitar a leitura para alguém (que pode ignorar o código ao ler o comentário que deixei acima)
         * Vocês podem usar os comandos de IDE de compactar os blocos de código pra fazer isso virar 1 linha
         * Com o comentário assim, no formato de Javadoc, a primeira linha fica visível. Vocês podem até compactar o comentário
         */
        {
            if(actors == null || actors.size() <= 0)
            {
                logger.log(Level.WARNING, "Não foi passado um ator!");
                this.actors = null;
            }
            //Desde que tenha algo, podemos copiar tudo com o mesmo loop
            else
            {
                this.actors = new ArrayList<>();
                //Se for maior que o tamanho máximo, mensagem de erro
                if(actors.size() > Util.MAXGENRES)
                    logger.log(Level.WARNING, "Mais de 3 atores foram passados!");
                //Copiar até o máximo ou o tamanho atual
                //Precisa fazer assim para criar uma cópia "deep" (ou seja, copiar valores e não endereços)
                for (int i = 0; (i < Util.MAXACTORS) && (i < actors.size()); ++i)
                    this.actors.add(actors.get(i));
            }
        }

        this.id = counter++;
        nUserRatings = 0;
        userRating = 0.0f;
        setLength(-1);
        year = GregorianCalendar.getInstance().get(Calendar.YEAR);
    };

    public int getnUserRatings()
    {
        return nUserRatings;
    }
    Video(Video v){
        name = v.name;
        ageRating = v.ageRating;
        director = v.director;
        ArrayList<Actor> actors = v.actor;
        int id= v.id;
        genres = v.genres;
        length=v.length;
        year=v.year;
        nViews=v.nViews;
        nUserRatings= v.nUserRatings;
        userRating = v.userRating;



    }
    /**
     * Usamos aqui aquele método static de copiar arrays da classe Util
     * Como ele é genérico (ver comentário no método em Util) precisamos dar o cast para o tipo adequado no retorno
     * Ou seja, um arraylist do enum de gêneros
     * @return
     */
    public ArrayList<Util.genresEnum> getGenres()
    {
        ArrayList<Util.genresEnum> copy = (ArrayList<Util.genresEnum>) Util.CopyArray(this.genres);
        return copy;
    }

    /**
     * Usamos aqui aquele método static de copiar arrays da classe Util
     * Como ele é genérico (ver comentário no método em Util) precisamos dar o cast para o tipo adequado no retorno
     * Ou seja, um arraylist de atores
     * @return
     */
    public ArrayList<Actor> getActors()
    {
        ArrayList<Actor> copy = (ArrayList<Actor>) Util.CopyArray(this.actors);
        return copy;
    }

    public Util.ageRatingsEnum getAgeRating()
    {
        return ageRating;
    }

    // Só mostrando que com o que eu fiz no enum de age rating podemos retornar uma descrição completa da classificação
    public String getAgeRatingDescription()
    {
        return ageRating.getRatingDescription();
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        /**
         * Como definimos que se não é passado uma duração, ela é colocada como -1, aqui podemos checar se ela foi
         * instanciada ou não e mandar uma mensagem de erro adequada.
         */

        if(length < 0)
        {
            logger.log(Level.WARNING, "Tentando acessar uma duração não definida!");
        }
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Director getDirector() {
        return director;
    }

    public String getName() {
        return name;
    }

    public int getnViews()
    {
        return nViews;
    }

    //Essa função é a que "substitui" o setter de nviews
    public void incrementView()
    {
        nViews++;
    }

    //Fazer as condições propostas no exercício 01
    public int getYear()
    {
        return year;
    }

    //Fazer as condições propostas no exercício 01
    public void setYear(int year)
    {
        if(year < 1878)
            logger.log(Level.WARNING, "Não existiam filmes antes de 1878!");
        else if (year > (GregorianCalendar.getInstance().get(Calendar.YEAR)+1))
            logger.log(Level.WARNING, "O filme não pode ter data de lançamento maior que 1 ano a partir deste!");
        this.year = year;
    }

    public float getUserRating()
    {
        return userRating;
    }

    //Função que "substitui" o setter de userratting
    public void addUserRating(float userRating)
    {
        //Começa em 0, então precisa incrementar antes.
        this.nUserRatings++;
        //Formula para adicionar um valor na média: nova_media = media_antiga + ((novo_item-media_antiga)/numero_itens_novo
        //https://math.stackexchange.com/questions/22348/how-to-add-and-subtract-values-from-an-average
        this.userRating = this.userRating + ((userRating-this.userRating)/this.nUserRatings);
    }

    //Aqui temos um exemplo de como fazer um método de clone do Video, como alguns perguntaram na aula 08.
    //Vamos aprender depois a fazer isso com mais calma, mas fica o exemplo comentado.
    /*public Video clone()
    {
        Video video = null;
        try {
            video = new Video(this.name, this.director, this.ageRating, this.genres);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return video;
    }*/
}

