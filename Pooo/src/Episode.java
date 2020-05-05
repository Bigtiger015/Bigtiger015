import java.util.ArrayList;

/**
 * A classe Episode herda de Video, adicionando informações extras específicas a uma série
 * Como número do episódio e a temporada
 */
public class Episode extends Video
{
    private final int episodeNumber;
    private final int seasonNumber;

    /**
     *
     * @param name nome do episódio
     * @param director diretor do episódio
     * @param ageRating classificação indicativa do episódio
     * @param genres gêneros do episódio
     * @param actors atores presentes no episódio
     * @param season temporada à qual o episódio pertence
     * @param episodeNumber número do episódio em relação à season
     */
    public Episode(String name, Director director, Util.ageRatingsEnum ageRating, ArrayList<Util.genresEnum> genres, ArrayList<Actor> actors, int season, int episodeNumber)
    {
        super(name, director, ageRating, genres, actors);
        this.episodeNumber = episodeNumber;
        this.seasonNumber = season;
    }
    
    public int getEpisodeNumber()
    {
        return episodeNumber;
    }

    public int getSeasonNumber()
    {
        return seasonNumber;
    }
}
class Episode2 implements Cloneable {
    Episode ep = new Episode();
    public Object clone() throws CloneNotSupportedException
   {
    Episode2 ep2 = (Episode2)super.clone();
    ep2.ep1 = new episodeNumber();
    ep.seasonNumber = new