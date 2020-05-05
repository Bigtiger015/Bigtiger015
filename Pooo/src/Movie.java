import java.util.ArrayList;

/**
 * Classe que armazena um filme. Ainda não foi pedida uma especialização em relação a um video
 * Mas deverá conter dados específicos de um filme
 */
public class Movie extends Video
{
    /**
     *
     * @param name
     * @param director
     * @param ageRating
     * @param genres
     * @param actors
     */
    public Movie(String name, Director director, Util.ageRatingsEnum ageRating, ArrayList<Util.genresEnum> genres, ArrayList<Actor> actors)
    {
        super(name, director, ageRating, genres, actors);
    }
}
