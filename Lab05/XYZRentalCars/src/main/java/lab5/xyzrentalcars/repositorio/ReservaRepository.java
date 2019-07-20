package lab5.xyzrentalcars.repositorio;

import lab5.xyzrentalcars.modelo.entidades.Reserva;

import javax.persistence.EntityManager;

public class ReservaRepository {
    private final EntityManager manager;
    private DAOGenerico<Reserva> dao;

    public ReservaRepository(EntityManager manager){
        this.manager = manager;
        dao = new DAOGenerico<>(manager);
    }

    public Reserva buscaPorId(Integer id) {
        return dao.buscaPorId(Reserva.class, id);
    }

    public Reserva salvaOuAtualiza(Reserva reserva) {
        return dao.salvaOuAtualiza(reserva);
    }

    public void remove(Reserva reserva) {
        dao.remove(reserva);
    }
}
