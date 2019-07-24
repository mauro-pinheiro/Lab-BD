package lab5.xyzrentalcars.repositorio;

import lab5.xyzrentalcars.modelo.embutiveis.CNH;
import lab5.xyzrentalcars.modelo.embutiveis.Endereco;
import lab5.xyzrentalcars.modelo.embutiveis.Lugradouro;
import lab5.xyzrentalcars.modelo.embutiveis.Telefone;
import lab5.xyzrentalcars.modelo.entidades.Carro;
import lab5.xyzrentalcars.modelo.entidades.Cliente;
import lab5.xyzrentalcars.modelo.entidades.Reserva;
import lab5.xyzrentalcars.modelo.entidades.Sede;
import lab5.xyzrentalcars.modelo.enums.ClasseCarro;
import lab5.xyzrentalcars.modelo.enums.TipoLugradouro;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaRepositoryTest {
    private static EntityManagerFactory factory;
    private EntityManager manager;
    private ReservaRepository reservaRepository;

    @BeforeClass
    public static void inicio(){
        factory = Persistence.createEntityManagerFactory("rentalcars");
    }

    @Before
    public void antes(){
        manager = factory.createEntityManager();
        reservaRepository = new ReservaRepository(manager);
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
    public void deveRealizarReservaDeCarroLocalizadoEmOutrSede(){
        SedeRepository sedeRepository = new SedeRepository(manager);
        Sede sede1 = Sede.Builder.umaSede()
                .comNome("Sede 1")
                .comGerente("Carlos")
                .comMultaPorSedeDiferente(new BigDecimal(10.9))
                .comEndereco(Endereco.Builder
                        .umEndereco()
                        .comLugradouro(new Lugradouro(TipoLugradouro.Rua,"3"))
                        .noNumero("13")
                        .noBairro("Ipem Turu")
                        .constroi())
                .comTelefones(new Telefone("11","1111111111"))
                .constroi();
        Sede sede2 = Sede.Builder.umaSede()
                .comNome("Sede 2")
                .comGerente("Mauro")
                .comMultaPorSedeDiferente(new BigDecimal(10.9))
                .comEndereco(Endereco.Builder.umEndereco()
                        .comLugradouro(new Lugradouro(TipoLugradouro.Rua,"3"))
                        .noNumero("13")
                        .noBairro("Ipem Turu")
                        .constroi())
                .comTelefones(new Telefone("11","1111111111"))
                .constroi();

        sedeRepository.salvaOuAtualiza(sede1);
        sedeRepository.salvaOuAtualiza(sede2);

        Carro carro = Carro.Builder.umCarro()
                .atualmenteNaSede(sede1)
                .daClasse(ClasseCarro.Compacto)
                .comSedeDeOrigem(sede2)
                .constroi();

        CarroRepository carroRepository = new CarroRepository(manager);
        carroRepository.salvaOuAtualiza(carro);

        Cliente cliente = Cliente.Builder.umCliente()
                .comNome("Mauro")
                .comCPF("1234")
                .comCNH(new CNH("1234", LocalDate.now().plusMonths(10)))
                .constroi();
        new ClienteRepository(manager).salvaOuAtualiza(cliente);

        Reserva reserva = Reserva.Builder.umReserva()
                .paraCliente(cliente).doCarro(carro)
                .comSedeDeLocacao(sede2)
                .naSituacao(Reserva.Situcao.Ativa)
                .comDiarias(10)
                .comMulta(new BigDecimal(2.5))
                .comDataLocacao(LocalDate.now())
                .constroi();

        reservaRepository.salvaOuAtualiza(reserva);

        Assert.assertNotNull(reserva);
    }

    @Test(expected = IllegalArgumentException.class)
    public void clienteNaoDeveRealizarReservaSeTiverReservaNaoFinalizada(){
        SedeRepository sedeRepository = new SedeRepository(manager);
        Sede sede1 = Sede.Builder.umaSede()
                .comNome("Sede 1")
                .comGerente("Carlos")
                .comMultaPorSedeDiferente(new BigDecimal(10.9))
                .comEndereco(Endereco.Builder.umEndereco()
                        .comLugradouro(new Lugradouro(TipoLugradouro.Rua,"3"))
                        .noNumero("13")
                        .noBairro("Ipem Turu")
                        .constroi())
                .comTelefones(new Telefone("11","1111111111"))
                .constroi();
        Sede sede2 = Sede.Builder.umaSede()
                .comNome("Sede 2")
                .comGerente("Mauro")
                .comMultaPorSedeDiferente(new BigDecimal(10.9))
                .comEndereco(Endereco.Builder.umEndereco()
                        .comLugradouro(new Lugradouro(TipoLugradouro.Rua,"3"))
                        .noNumero("13")
                        .noBairro("Ipem Turu")
                        .constroi())
                .comTelefones(new Telefone("11","1111111111"))
                .constroi();

        sedeRepository.salvaOuAtualiza(sede1);
        sedeRepository.salvaOuAtualiza(sede2);

        Carro carro1 = Carro.Builder.umCarro()
                .atualmenteNaSede(sede1)
                .daClasse(ClasseCarro.Compacto)
                .comSedeDeOrigem(sede2)
                .constroi();
        Carro carro2 = Carro.Builder.umCarro()
                .atualmenteNaSede(sede1)
                .daClasse(ClasseCarro.Luxo)
                .comSedeDeOrigem(sede2)
                .constroi();

        CarroRepository carroRepository = new CarroRepository(manager);
        carroRepository.salvaOuAtualiza(carro1);
        carroRepository.salvaOuAtualiza(carro2);

        Cliente cliente = Cliente.Builder.umCliente()
                .comNome("Mauro")
                .comCPF("1234")
                .comCNH(new CNH("1234", LocalDate.now().plusMonths(10)))
                .constroi();
        new ClienteRepository(manager).salvaOuAtualiza(cliente);

        Reserva reserva1 = Reserva.Builder.umReserva()
                .paraCliente(cliente).doCarro(carro1)
                .comSedeDeLocacao(sede2)
                .naSituacao(Reserva.Situcao.Ativa)
                .comDiarias(10)
                .comMulta(new BigDecimal(2.5))
                .comDataLocacao(LocalDate.now())
                .constroi();

        Reserva reserva2 = Reserva.Builder.umReserva()
                .paraCliente(cliente).doCarro(carro2)
                .comSedeDeLocacao(sede2)
                .naSituacao(Reserva.Situcao.Ativa)
                .comDiarias(10)
                .comMulta(new BigDecimal(2.5))
                .comDataLocacao(LocalDate.now())
                .constroi();

        reservaRepository.salvaOuAtualiza(reserva1);
        reservaRepository.salvaOuAtualiza(reserva2);
    }

    @Test
    public void deveEfetuarReservaParaClientesSemPendencias(){
        SedeRepository sedeRepository = new SedeRepository(manager);
        Sede sede1 = Sede.Builder.umaSede()
                .comNome("Sede 1")
                .comGerente("Carlos")
                .comMultaPorSedeDiferente(new BigDecimal(10.9))
                .comEndereco(Endereco.Builder.umEndereco()
                        .comLugradouro(new Lugradouro(TipoLugradouro.Rua,"3"))
                        .noNumero("13")
                        .noBairro("Ipem Turu")
                        .constroi())
                .comTelefones(new Telefone("11","1111111111"))
                .constroi();
        Sede sede2 = Sede.Builder.umaSede()
                .comNome("Sede 2")
                .comGerente("Mauro")
                .comMultaPorSedeDiferente(new BigDecimal(10.9))
                .comEndereco(Endereco.Builder.umEndereco()
                        .comLugradouro(new Lugradouro(TipoLugradouro.Rua,"3"))
                        .noNumero("13")
                        .noBairro("Ipem Turu")
                        .constroi())
                .comTelefones(new Telefone("11","1111111111"))
                .constroi();

        sedeRepository.salvaOuAtualiza(sede1);
        sedeRepository.salvaOuAtualiza(sede2);

        Carro carro1 = Carro.Builder.umCarro()
                .atualmenteNaSede(sede1)
                .daClasse(ClasseCarro.Compacto)
                .comSedeDeOrigem(sede2)
                .constroi();
        Carro carro2 = Carro.Builder.umCarro()
                .atualmenteNaSede(sede1)
                .daClasse(ClasseCarro.Luxo)
                .comSedeDeOrigem(sede2)
                .constroi();

        CarroRepository carroRepository = new CarroRepository(manager);
        carroRepository.salvaOuAtualiza(carro1);
        carroRepository.salvaOuAtualiza(carro2);

        Cliente cliente = Cliente.Builder.umCliente()
                .comNome("Mauro")
                .comCPF("1234")
                .comCNH(new CNH("1234", LocalDate.now().plusMonths(10)))
                .constroi();
        new ClienteRepository(manager).salvaOuAtualiza(cliente);

        Reserva reserva1 = Reserva.Builder.umReserva()
                .paraCliente(cliente).doCarro(carro1)
                .comSedeDeLocacao(sede2)
                .naSituacao(Reserva.Situcao.Finalizada)
                .comDiarias(10)
                .comMulta(new BigDecimal(2.5))
                .comDataLocacao(LocalDate.now())
                .constroi();

        Reserva reserva2 = Reserva.Builder.umReserva()
                .paraCliente(cliente).doCarro(carro2)
                .comSedeDeLocacao(sede2)
                .naSituacao(Reserva.Situcao.Ativa)
                .comDiarias(10)
                .comMulta(new BigDecimal(2.5))
                .comDataLocacao(LocalDate.now())
                .constroi();

        reservaRepository.salvaOuAtualiza(reserva1);
        reservaRepository.salvaOuAtualiza(reserva2);

        Assert.assertEquals(reserva1.getSituacao(), Reserva.Situcao.Finalizada);
        Assert.assertNotNull(reserva2);
    }
}