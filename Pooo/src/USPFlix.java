import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class USPFlix
{
    //Usamos alguns métodos que podem gerar execções. Por isso damos o throw aqui.
    //Vamos ver com calma depois sobre isso.
    public static void main(String[] args) throws ParseException, FileNotFoundException
    {
        ArrayList<Actor> actorList = new ArrayList<>();
        ArrayList<Director> directorList = new ArrayList<>();
        ArrayList<Video> videoList = new ArrayList<>();

        readDirectors(directorList);
        readActors(actorList);
        readVideos(videoList, directorList, actorList);
    }

    private static void readDirectors(ArrayList<Director> directorList) throws FileNotFoundException, ParseException
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("directors.in"));
        System.setIn(in);

        //Creating Scanner instance to read File in Java
        Scanner scnr = new Scanner(System.in);

        //Reading each line of file using Scanner class
        Director auxDir;
        String auxName, auxCountry, auxBirth;
        do {
            auxName = scnr.nextLine();
            auxCountry = scnr.nextLine();
            auxBirth = scnr.nextLine();
            auxDir = new Director(auxName, auxCountry, auxBirth);
            directorList.add(auxDir);
        } while(scnr.hasNextLine());

        //Debug para ver se leu tudo certo
        //Esse tipo de for é chamado de foreach, é um loop que itera sobre todos os elementos de uma lista/coleção
        for (Director dir: directorList)
        {
            System.out.println("Director name: "+dir.getName());
            System.out.println("Director country: "+dir.getCountry());
            System.out.println("Director birthdate: "+dir.getBirth());
            System.out.println("Director age: "+dir.getAge());
            System.out.println("");
        }
    }

    private static void readActors(ArrayList<Actor> actorList) throws FileNotFoundException, ParseException
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("actors.in"));
        System.setIn(in);

        //Creating Scanner instance to read File in Java
        Scanner scnr = new Scanner(System.in);

        //Reading each line of file using Scanner class
        Actor auxActor;
        String auxName, auxCountry, auxBirth;
        do {
            auxName = scnr.nextLine();
            auxCountry = scnr.nextLine();
            auxBirth = scnr.nextLine();
            auxActor = new Actor(auxName, auxCountry, auxBirth);
            actorList.add(auxActor);
        } while(scnr.hasNextLine());

        //Debug para ver se leu tudo certo
        //Esse tipo de for é chamado de foreach, é um loop que itera sobre todos os elementos de uma lista/coleção
        for (Actor actor: actorList)
        {
            System.out.println("Actor name: "+actor.getName());
            System.out.println("Actor country: "+actor.getCountry());
            System.out.println("Actor birthdate: "+actor.getBirth());
            System.out.println("Actor age: "+actor.getAge());
            System.out.println();
        }
    }

    private static void readVideos(ArrayList<Video> videoList, ArrayList<Director> directorList, ArrayList<Actor> actorList) throws FileNotFoundException, ParseException
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("videos.in"));
        System.setIn(in);

        //Creating Scanner instance to read File in Java
        Scanner scnr = new Scanner(System.in);

        //Reading each line of file using Scanner class
        Video auxVideo;
        String auxName;
        Util.ageRatingsEnum auxAgeRating;
        ArrayList<Util.genresEnum> auxGenreList;
        ArrayList<Actor> auxActorList;
        Director auxDirector;
        int auxNItems;


        do {
            auxName = scnr.nextLine();
            //A próxima linha é um inteiro. Como ele é lido como string, precisa usar o parse para transformar no tipo int, e então pegar a posição da lista de diretores
            auxDirector = directorList.get(Integer.parseInt(scnr.nextLine()));
            //O método valueof lê uma string e retorna o enum que tem o nome EXATAMENTE IDENTICO à string
            auxAgeRating = (Util.ageRatingsEnum.valueOf(scnr.nextLine()));
            auxNItems = Integer.parseInt(scnr.nextLine());
            if(auxNItems == 0)
                auxGenreList = null;
            else
            {
                auxGenreList = new ArrayList<>();
                for(int i = 0; i < auxNItems; ++i)
                {
                    auxGenreList.add(Util.genresEnum.valueOf(scnr.nextLine()));
                }
            }
            auxNItems = Integer.parseInt(scnr.nextLine());
            if(auxNItems == 0)
                auxActorList = null;
            else
            {
                auxActorList = new ArrayList<>();
                for(int i = 0; i < auxNItems; ++i)
                {
                    auxActorList.add(actorList.get(Integer.parseInt(scnr.nextLine())));
                }
            }
            auxVideo = new Video(auxName, auxDirector, auxAgeRating, auxGenreList, auxActorList);
            videoList.add(auxVideo);

        } while(scnr.hasNextLine());

        //Debug para ver se leu tudo certo
        //Esse tipo de for é chamado de foreach, é um loop que itera sobre todos os elementos de uma lista/coleção
        for (Video vid: videoList)
        {
            System.out.println("Video name: "+vid.getName());
            System.out.println("Video director name: "+vid.getDirector().getName());
            System.out.println("Video age rating description: "+vid.getAgeRatingDescription());
            System.out.println("Video genres: "+vid.getGenres());
            auxActorList = vid.getActors();
            if(auxActorList == null)
                System.out.println("No Actors in video");
            else
            {
                for(int i = 0; i < auxActorList.size(); ++i)
                {
                    System.out.println("Video Actor "+i+ " Name: "+auxActorList.get(i).getName());
                }
            }
            System.out.println();
        }
    }
}
