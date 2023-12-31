package AIchat.jpa;

import AIchat.entity.Message;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Stateless
public class MessageJPA extends AbstractJPA<Integer, Message> {

    public List<Message> findByToId(Integer toId) {
        return stream().filter(p -> p.getUserToId() == toId)
                .collect(toList());
    }

    public List<Message> findByFromId(Integer fromId) {
        return stream().filter(p -> p.getUserFromId() == fromId)
                .collect(toList());
    }

    public List<Message> findByChat(Integer chatId) {
        return stream().filter(p -> p.getChatId() == chatId)
                .collect(toList());
    }

    @Override
    protected EntityManager entityManager() {
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        return em;
    }
}
