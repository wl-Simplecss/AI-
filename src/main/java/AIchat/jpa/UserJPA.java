package AIchat.jpa;

import AIchat.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Stateless
public class UserJPA extends AbstractJPA<Integer, User> {
    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<User> findByUsername(String keyword) {
        return stream().filter(p -> p.getUsername().equals(keyword))
                .collect(toList());
    }



    public List<User> findUserById(Integer id) {

        return stream().filter(p -> p.getId().equals(id))
                .collect(toList());
    }

    public long countByKeyword(String keyword) {
        return stream().filter(p -> p.getUsername().contains(keyword))
                .count();
    }

    @Override
    protected EntityManager entityManager() {
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        return em;
    }
}
