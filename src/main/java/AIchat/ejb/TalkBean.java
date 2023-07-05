package AIchat.ejb;

import AIchat.entity.Chat;
import AIchat.entity.Message;
import AIchat.entity.User;
import AIchat.jpa.ChatJPA;
import AIchat.jpa.MessageJPA;
import AIchat.jpa.UserJPA;
import AIchat.util.CustomException;
import AIchat.util.CustomResponse;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.util.List;
import com.google.gson.JsonObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateful
@LocalBean
public class TalkBean {
    private ChatJPA chatJPA = new ChatJPA();
    private MessageJPA messageJPA = new MessageJPA();
    private UserJPA userJPA = new UserJPA();
    private List<Message> messageList = new java.util.ArrayList<>();

    public void makeInteraction(String message, Integer chatId) {

        List<Chat> c = chatJPA.findChatById(chatId);
        if (c == null) {
            throw new CustomException("Chat not found");
        }
        User u = c.get(0).getUser();
        User r = userJPA.findById(1);
        if (u == null || r == null) {
            throw new CustomException("User not found");
        }
        saveRequest(message, c.get(0), u, r);
        saveResponse(message, c.get(0), r, u);
    }

    private void saveRequest(String message, Chat chat, User from, User to) {
        Message msg = new Message();

        msg.setContent(message);
        msg.setChatId(chat.getId());
        msg.setUserFromId(from.getId());
        msg.setUserToId(to.getId());
        Message m = messageJPA.create(msg);
        messageList.add(m);
    }

    private void saveResponse(String message, Chat chat, User from, User to) {
        Message msg = new Message();
        System.out.println("message:"+message);

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:8080/AiChat_war/rest").path("talk").queryParam("content", message);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        JsonObject obj = CustomResponse.parseAsObject(response.readEntity(String.class));
        if (obj.get("code").getAsInt() < 200 || obj.get("code").getAsInt() >= 300) {
            throw new CustomException(obj.get("message").getAsString());
        }
        msg.setContent(obj.get("data").getAsJsonObject().get("content").toString());
        msg.setChatId(chat.getId());
        msg.setUserFromId(from.getId());
        msg.setUserToId(to.getId());

        Message m = messageJPA.create(msg);
        messageList.add(m);
    }

    public void endChat() {
        messageList.clear();
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
