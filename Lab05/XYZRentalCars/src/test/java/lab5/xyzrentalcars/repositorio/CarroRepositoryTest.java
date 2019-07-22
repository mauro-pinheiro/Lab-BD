package lab5.xyzrentalcars.repositorio;

import lab5.xyzrentalcars.builders.CarroBuilder;
import lab5.xyzrentalcars.builders.EnderecoBuilder;
import lab5.xyzrentalcars.builders.SedeBuilder;
import lab5.xyzrentalcars.modelo.embutiveis.Telefone;
import lab5.xyzrentalcars.modelo.entidades.Carro;
import lab5.xyzrentalcars.modelo.entidades.Sede;
import lab5.xyzrentalcars.modelo.enums.ClasseCarro;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.*;

public class CarroRepositoryTest {
    private static EntityManagerFactory factory;
    private EntityManager manager;
    private CarroRepository carroRepository;

    @BeforeClass
    public static void inicio(){
        factory = Persistence.createEntityManagerFactory("rentalcars");
    }

    @Before
    public void antes(){
        manager = factory.createEntityManager();
        carroRepository = new CarroRepository(manager);
        manager.getTransaction().begin();
    }

    @After
    public void depois(){
        manager.getTransaction().rollback();
    }

    @AfterClass
    public static void fim(){
        factory.close();
    }

    @Test
    public void deveInserirUmCarro(){

    }

    @Test
    public void deveRecuperarTodosOsCarrosDaclasseCompacto(){
        Sede sede = new SedeRepository(manager).buscaPorNome("Sede 2").get(0);

        Carro carros[] = {
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Compacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.SubCompacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Compacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Luxo)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Compacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi()
        };

        int qtdeCompactos = 0;
        for (Carro c :
                carros) {
            if(c.getClasse().equals(ClasseCarro.Compacto))
                qtdeCompactos++;

            carroRepository.salvaOuAtualiza(c);
        }

        manager.flush();
        manager.clear();

        List<Carro> compactos = carroRepository.buscaPorClasse(ClasseCarro.Compacto);

        assertEquals(compactos.size(),qtdeCompactos);
        compactos.forEach(c -> assertEquals(c.getClasse(), ClasseCarro.Compacto));
    }

    @Test
    public void deveRecuperarTodosOsCarrosDaclasseLuxo(){
        Sede sede = new SedeRepository(manager).buscaPorNome("Sede 1").get(0);

        Carro carros[] = {
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Luxo)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.SubCompacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Compacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Luxo)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi(),
                CarroBuilder.umCarro()
                        .comClasse(ClasseCarro.Compacto)
                        .naSituacao(Carro.Situacao.Disponivel)
                        .comSedeDeOrigem(sede)
                        .constroi()
        };

        int qtdeLuxo = 0;
        for (Carro c :
                carros) {
            if(c.getClasse().equals(ClasseCarro.Luxo))
                qtdeLuxo++;

            carroRepository.salvaOuAtualiza(c);
        }

        manager.flush();
        manager.clear();

        List<Carro> luxos = carroRepository.buscaPorClasse(ClasseCarro.Luxo);

        System.out.println("qtdeLuxo = " + qtdeLuxo + "\nLuxos.size = " + luxos.size());
        assertEquals(luxos.size(),qtdeLuxo);
        luxos.forEach(c -> assertEquals(c.getClasse(), ClasseCarro.Luxo));
    }

}