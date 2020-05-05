import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;

public class Actor
{
    private final String name;
    private int age;
    private final String country;
    private final LocalDate birth;

    /**
     * Como um dos métodos que usei no construtor pode gerar exceção (veremos isso em algumas aulas no futuro)
     * É preciso jogar (throw) uma exceção para quem chamar esse construtor. Vocês podem ficar jogando exceções até a main por enquanto
     * Façam o que a IDE recomendar que está tranquilo.
     */
    public Actor(String name, String country, String birth) throws ParseException
    {
        this.name = name;
        this.country = country;
        this.birth = LocalDate.parse(birth);
        updateAge();
    }

    /**
     * Função que define a idade atual baseado no nascimento e dia atual
     * Ela substitui o setter de idade, uma vez que o usuário n deveria poder escolher uma idade diferente da que já
     * Podemos calcular no programa, sem riscos de erro.
     */
    public void updateAge()
    {
        LocalDate currDate = LocalDate.now();
        Period diff = Period.between(this.birth, currDate);
        age = diff.getYears();
    }

    //Podemos colocar os getters em uma linha assim, caso sejam simples.
    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public String getCountry()
    {
        return country;
    }

    public String getBirth(){ return birth.toString(); }

}
