package AIchat.ejb;

import AIchat.entity.Message;
import AIchat.jpa.MessageJPA;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
@LocalBean
public class MessBean {
    private static MessageJPA messageJPA = new MessageJPA();

    public List<Message> getMessageByChat(Integer chatId) {
        List<Message> list = messageJPA.findByChat(chatId);
        list.sort((p1, p2) -> p1.getCreateTime().compareTo(p2.getCreateTime()));
        return list;
    }
}
