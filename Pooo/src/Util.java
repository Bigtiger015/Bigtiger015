import java.util.ArrayList;

/**
 * É comum criarmos uma classe Util que contenha enumeradores, constantes e métodos de utilidade que possivelmente venham a ser usados
 * em mais de uma classe do seu projeto.
 * Esse tipo de documentação de código é conhecida como javadoc, pois é utilizada por um gerador de documentação de mesmo nome que pode gerar HTMLs
 * contendo toda a documentação do código, automaticamente.
 */

public class Util
{
    /**
     * Nas classes Util é comum definirmos as constantes do projeto, para ficarem de fácil acesso a todas as classes
     * Notem o static, que indica que só existe uma por classe e não por objeto!
     */
    public static final int MAXACTORS = 3;
    public static final int MAXGENRES = 3;

    /**
     * Como o rating de idade imprime uma descrição, faz mais sentido criarmos um método que retorna sua descrição ao invés de sobrescrevermos
     * seu método "toString", como abaixo em gênero.
     * Portanto, criamos uma String para conter a descrição e seu construtor para recebê-la. Assim como para cada variável do enum, já a declaramos chamando o construtor,
     * passando sua descrição.
     * O método getRatingDescription retorna a descrição de cada enum.
     * Assim, temos um método "limpo" que limita o usuário a colocar uma das das opções possíveis.
     * @author leotp
     */
    public enum ageRatingsEnum {
        LIVRE("Livre Para Todos Os Públicos", 0), DEZ ("Não Recomendado Para Menores de Dez Anos", 10), DOZE ("Não Recomendado Para Menores de Doze Anos", 12),
            CATORZE ("Não Recomendado Para Menores de Catorze Anos", 14), DEZESSEIS ("Não Recomendado Para Menores de Dezesseis Anos", 16),
            DEZOITO ("Não Recomendado Para Menores de Dezoito Anos", 18);

        private String desc;
        private int age;

        ageRatingsEnum(String desc, int age)
        {
            this.desc = desc;
            this.age = age;
        }

        public String getRatingDescription()
        {
            return desc;
        }
    }

    /**
     * Como os gêneros precisam apenas de seus nomes, não precisamos criar um método para devolver uma string de descrição como acima com os ratings de idade.
     * Podemos modificar (sobrescrever) o método toString, que originalmente retornaria a string com o nome da variável do enum, para sua versão gramaticalmente correta.
     * @author leotp
     */
    public enum genresEnum {
        ACAO{ public String toString() { return "Ação";} },
        AVENTURA{ public String toString() { return "Aventura";} },
        COMEDIA{ public String toString() { return "Comédia";} },
        ROMANCE{ public String toString() { return "Romance";} },
        FICCAO{ public String toString() { return "Ficção";} },
        TERROR{ public String toString() { return "Terror";} },
        SUSPENSE{ public String toString() { return "Suspense";} },
        POLICIAL{ public String toString() { return "Policial";} },
    }

    /**
     * Como precisamos fazer uma hard copy de Array Lists para mais de uma classe (atores e gêneros) em Video, fiz um método genérico aqui na Util.
     * Pois se precisarmos em outras classes, podemos reutilizá-los
     * Nós ainda não vimos métodos genéricos, mas vocês podem só substituir o "?" pela classe/tipo que estiverem usando caso tenham dúvidas
     * A ideia é que o "?" é uma espécie de curinga, que aceita qualquer tipo de entrada na função.
     * Por isso, ele vai fazer as operações independente do tipo do arraylist passado.
     * A ideia da função é retornar null caso o array passado seja null, ou copiar todos os itens presentes no array list para o novo, e retornar seu endereço
     * Outro ponto importante é que é um método static. Portanto, ele pode ser chamado sem instanciar um objeto Util, pois é um método da classe.
     * @param aList
     * @return aListCopy
     */
    public static ArrayList<?> CopyArray(ArrayList<?> aList)
    {
        if(aList == null)
            return null;
        ArrayList arrayCopy = new ArrayList();
        //Também posso fazer isso
        //arrayCopy.addAll(aList);
        for(int i = 0; i < aList.size(); ++i)
            arrayCopy.add(aList.get(i));
        return arrayCopy;
    }
}
