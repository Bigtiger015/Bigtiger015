import java.util.ArrayList;

/**
 * Essa classe, filha de Media, representa uma série
 * Ela irá adicionar uma lista de temporadas de episódios, cada temporada com sua lista de episódios
 */
public class Series extends Media
{
    ArrayList<ArrayList<Episode>> episodes;

    /**
     * Construtor que recebe nome da série, classificação indicativa dela, gêneros e episódios atuais
     * @param name
     * @param ageRating
     * @param genres
     * @param episodes
     */
    public Series(String name, Util.ageRatingsEnum ageRating, ArrayList<Util.genresEnum> genres, ArrayList<ArrayList<Episode>> episodes)
    {
        super(name, ageRating, genres);
        this.episodes = episodes;
        if(this.episodes == null)
            this.episodes = new ArrayList<>();
    }

    /**
     * Adiciona um episódio novo à temporada especificada
     * @param episode
     * @param season
     */
    public void addEpisode(Episode episode, int season)
    {
        //Se a temporada é nova, cria o espaço para ela na lista de temporadas e adiciona o episódio
        if(episodes.size() < (season+1))
            episodes.add(new ArrayList<>());
        episodes.get(season).add(episode);
    }

    /**
     * Adiciona uma lista toda de episódios a uma temporada
     * Não foi especificado ainda, mas pode vir a ser útil depois.
     * @param episodeList
     * @param season
     */
    public void addEpisodeList(ArrayList<Episode> episodeList, int season)
    {
        for(Episode ep: episodeList)
        {
            addEpisode(ep, season);
        }
    }

    /**
     * Toda vez que um usuário adicionar uma nota para um episódio da série, essa nota vai influenciar a nota global da série
     * E também a do episódio individual
     * @param episode
     * @param season
     * @param userRating
     */
    public void addUserRating(int episode, int season, float userRating)
    {
        addUserRating(userRating);
        episodes.get(season).get(episode).addUserRating(userRating);
    }

    /**
     * Toda vez que o usuário assistir um episódio da série, incrementa o contador da série como um todo
     * E do episódio em questão
     * @param episode
     * @param season
     */
    public void incrementViews(int episode, int season)
    {
        incrementViews();
        episodes.get(season).get(episode).incrementViews();
    }

    public int getNSeasons()
    {
        return episodes.size();
    }

    public int getNEpisodesInSeason(int season)
    {
        return episodes.get(season).size();
    }
}
