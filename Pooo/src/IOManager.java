import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Essa classe lida com as funcionalidades de entrada e saída de dados
 * Apesar de não ser essencial, foi adicionada para deixar o código da main mais limpo, e melhor categorizado
 * Fiquem à vontade para criar classes extras quando acharem necessário
 * Especialmente se elas forem realizar operações ao redor de um tema central e reduzirem o tamanho das outras classes
 */
public class IOManager
{
    /**
     * Esse método lê os dados de uma pessoa do arquivo person.in
     * É uma prática segura passar o scanner que está com a entrada de dados para que quem chamou a função
     * Que seja responsável por isso, e não ela mesma.
     * Poderia ser o nome de um arquivo de entrada e ela abriria o Scanner relativo a esse arquivo.
     * @param peopleList guardará a lista de pessoas lida do arquivo de entrada
     * @param scnr leitor de entrada que contém os dados a serem lidos
     * @throws ParseException
     */
    public static void readPerson(final ArrayList<Person> peopleList, final Scanner scnr) throws ParseException {
        Person auxPerson = null;
        int personClass;
        String auxName, auxCountry, auxBirth;
        do {
            personClass = (Integer.parseInt(scnr.nextLine()));
            auxName = scnr.nextLine();
            auxCountry = scnr.nextLine();
            auxBirth = scnr.nextLine();
            switch (personClass) {
                case (1):
                    auxPerson = new Director(auxName, auxCountry, auxBirth);
                    break;
                case (2):
                    auxPerson = new Actor(auxName, auxCountry, auxBirth);
                    break;
            }
            peopleList.add(auxPerson);
        } while (scnr.hasNextLine());
    }

    /**
     * Esse método lê os dados de um filme em específico
     * 
     * @param mediaList
     * @param peopleList
     * @param scnr
     */
    private static void readMovie(final ArrayList<Media> mediaList, final ArrayList<Person> peopleList,
            final Scanner scnr) {
        Video auxVideo;
        String auxName;
        Util.ageRatingsEnum auxAgeRating;
        ArrayList<Util.genresEnum> auxGenreList = null;
        ArrayList<Actor> auxActorList = null;
        Director auxDirector;
        int auxNItems;

        auxName = scnr.nextLine();
        // A próxima linha é um inteiro. Como ele é lido como string, precisa usar o
        // parse para transformar no tipo int, e então pegar a posição da lista de
        // diretores
        auxDirector = (Director) peopleList.get(Integer.parseInt(scnr.nextLine()));
        // O método valueof lê uma string e retorna o enum que tem o nome EXATAMENTE
        // IDENTICO à string
        auxAgeRating = (Util.ageRatingsEnum.valueOf(scnr.nextLine()));
        auxNItems = Integer.parseInt(scnr.nextLine());
        // Se lista de gêneros não for vazia, lê todos os que foram passados
        if (auxNItems > 0) {
            auxGenreList = new ArrayList<>();
            for (int i = 0; i < auxNItems; ++i)
                auxGenreList.add(Util.genresEnum.valueOf(scnr.nextLine()));
        }
        auxNItems = Integer.parseInt(scnr.nextLine());
        // Se lista de atores não for vazia, lê todos os que foram passados
        if (auxNItems > 0) {
            auxActorList = new ArrayList<>();
            for (int i = 0; i < auxNItems; ++i)
                auxActorList.add((Actor) peopleList.get(Integer.parseInt(scnr.nextLine())));
        }
        auxVideo = new Movie(auxName, auxDirector, auxAgeRating, auxGenreList, auxActorList);

        // Adiciona o Video lido à lista de midias. Poderia ser o retorno do método,
        // também, aí n precisaria passar a lista de midias.
        mediaList.add(auxVideo);
    }

    /**
     * Esse método lê os dados de uma série em específico
     * 
     * @param mediaList
     * @param peopleList
     * @param scnr
     */
    private static void readSeries(final ArrayList<Media> mediaList, final ArrayList<Person> peopleList,
            final Scanner scnr) {
        // Reading each line of file using Scanner class
        Series auxSeries;
        Episode auxEpisode;
        String auxName;
        Util.ageRatingsEnum auxAgeRating;
        ArrayList<Util.genresEnum> auxGenreList = null;
        ArrayList<Actor> auxActorList = null;
        Director auxDirector;
        int auxNItems, nEpisodes, nSeasons;

        auxName = scnr.nextLine();
        // O método valueof lê uma string e retorna o enum que tem o nome EXATAMENTE
        // IDENTICO à string
        auxAgeRating = (Util.ageRatingsEnum.valueOf(scnr.nextLine()));
        auxNItems = Integer.parseInt(scnr.nextLine());
        // Se lista de gêneros não for vazia, lê todos os que foram passados
        if (auxNItems > 0) {
            auxGenreList = new ArrayList<>();
            for (int i = 0; i < auxNItems; ++i)
                auxGenreList.add(Util.genresEnum.valueOf(scnr.nextLine()));
        }

        auxSeries = new Series(auxName, auxAgeRating, auxGenreList, null);

        // Para cada temporada, ver quantos episódios tem e ler as informações de cada
        // episódio
        nSeasons = Integer.parseInt(scnr.nextLine());
        for (int i = 0; i < nSeasons; ++i) {
            nEpisodes = Integer.parseInt(scnr.nextLine());
            for (int j = 0; j < nEpisodes; ++j) {
                auxName = scnr.nextLine();
                auxDirector = (Director) peopleList.get(Integer.parseInt(scnr.nextLine()));
                auxNItems = Integer.parseInt(scnr.nextLine());
                // Se lista de atores não for vazia, lê todos os que foram passados
                if (auxNItems > 0) {
                    auxActorList = new ArrayList<>();
                    for (int k = 0; k < auxNItems; ++k)
                        auxActorList.add((Actor) peopleList.get(Integer.parseInt(scnr.nextLine())));
                }
                // Cria e adiciona o episódio na serie
                auxEpisode = new Episode(auxName, auxDirector, auxSeries.getAgeRating(), auxSeries.getGenres(),
                        auxActorList, i + 1, j + 1);
                auxSeries.addEpisode(auxEpisode, i);
            }
        }
        // Adiciona a serie na lista de midias. Poderia ser o retorno do método, também,
        // aí n precisaria apssar a lista de midias.
        mediaList.add(auxSeries);
    }

    /**
     * Define qual especialização de mídia será lida e chama a função adequada
     * 
     * @param mediaList
     * @param peopleList
     * @param scnr
     */
    public static void readMedia(final ArrayList<Media> mediaList, final ArrayList<Person> peopleList,
            final Scanner scnr) {

        int mediaClass;

        do {
            mediaClass = Integer.parseInt(scnr.nextLine());
            switch (mediaClass) {
                case 1:
                    readMovie(mediaList, peopleList, scnr);
                    break;
                case 2:
                    readSeries(mediaList, peopleList, scnr);
                    break;
            }
        } while (scnr.hasNextLine());
    }

    /**
     * Imprime todas as midias da lista, de acordo com o que foi especificado no
     * exercicio
     * 
     * @param mediaList
     */
    public static void printMedia(final ArrayList<Media> mediaList) {
        ArrayList<Actor> auxActorList;

        for (final Media media : mediaList) {
            System.out.println("Media name: " + media.getName());
            System.out.println("Media age rating description: " + media.getAgeRatingDescription());
            System.out.println("Media genres: " + media.getGenres());
            // Não estava na especificação, mas já fiz
            // essa estrutura do instanceof retorna verdadeiro caso o objeto pertença à
            // classe
            if (media instanceof Video) {
                // Essa referência de Video deixa o código mais limpo e fácil de ler
                final Video vid = (Video) media;
                System.out.println("Video director name: " + vid.getDirector().getName());
                auxActorList = vid.getActors();
                if (auxActorList == null)
                    System.out.println("No Actors in video");
                else
                    for (int i = 0; i < auxActorList.size(); ++i)
                        System.out.println("Video Actor " + i + " Name: " + auxActorList.get(i).getName());
            }
            // Imprime o que foi pedido na especificação
            else if (media instanceof Series) {
                final Series serie = (Series) media;
                final int nSeasons = serie.getNSeasons();
                System.out.println("Number of Seasons: " + nSeasons);
                for (int i = 0; i < nSeasons; ++i)
                    System.out
                            .println("Number of Episodes in Season " + (i + 1) + ": " + serie.getNEpisodesInSeason(i));
            } else
                System.out.println("Deu erro");
            System.out.println();
        }
    }

    /**
     * Imprime as informações das pessoas como pedido no exercício
     * 
     * @param peopleList
     */
    public static void printPerson(final ArrayList<Person> peopleList) {
        // Esse tipo de for é chamado de foreach, é um loop que itera sobre todos os
        // elementos de uma lista/coleção
        for (final Person per : peopleList) {
            System.out.println("Person name: " + per.getName());
            System.out.println("Person country: " + per.getCountry());
            System.out.println("Person birthdate: " + per.getBirth());
            System.out.println("Person age: " + per.getAge());
            System.out.println("");
            // Gambiarrando o print de ator aqui, só pra ficar mais organizado
            // Mas no caso seria fazer uma função separada
            if (per instanceof Actor) {
                System.out.println("Actor name: " + per.getName());
            }
        }
    }

    // Só fiz essa função pra ver se tudo estava sendo lido corretamente. Não tem a
    // ver com o exercício em si.
    private static void printMediaDebbug(final ArrayList<Media> mediaList) {
        ArrayList<Actor> auxActorList;

        // Esse tipo de for é chamado de foreach, é um loop que itera sobre todos os
        // elementos de uma lista/coleção
        for (final Media vid : mediaList) {
            System.out.println("Video name: " + vid.getName());
            System.out.println("Video age rating description: " + vid.getAgeRatingDescription());
            System.out.println("Video genres: " + vid.getGenres());
            if (vid instanceof Video) {
                System.out.println("Video director name: " + ((Video) vid).getDirector().getName());
                auxActorList = ((Video) vid).getActors();
                if (auxActorList == null)
                    System.out.println("No Actors in video");
                else
                    for (int i = 0; i < auxActorList.size(); ++i)
                        System.out.println("Video Actor " + i + " Name: " + auxActorList.get(i).getName());
            } else if (vid instanceof Series) {
                final int nSeasons = ((Series) vid).getNSeasons();
                System.out.println("Number of Seasons: "+nSeasons);
                for(int i =0; i <nSeasons; ++i)
                    System.out.println("Number of Episodes in Season "+(i+1)+": "+((Series)vid).getNEpisodesInSeason(i));
            }
            else
                System.out.println("Deu erro");

            System.out.println();
        }
    }
}
