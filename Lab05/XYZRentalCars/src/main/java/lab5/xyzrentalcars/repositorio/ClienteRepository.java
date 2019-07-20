package lab5.xyzrentalcars.repositorio;

import lab5.xyzrentalcars.modelo.entidades.Cliente;

import javax.persistence.EntityManager;

public class ClienteRepository {
    private final EntityManager manager;
    private DAOGenerico<Cliente> dao;

    public ClienteRepository(EntityManager manager){
        this.manager = manager;
        dao = new DAOGenerico<>(manager);
    }

    public Cliente buscaPorId(Integer id) {
        return dao.buscaPorId(Cliente.class, id);
    }

    public Cliente salvaOuAtualiza(Cliente cliente) {
        return dao.salvaOuAtualiza(cliente);
    }

    public void remove(Cliente cliente) {
        dao.remove(cliente);
    }
}
