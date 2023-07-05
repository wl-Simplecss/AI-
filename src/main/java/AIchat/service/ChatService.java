package AIchat.service;

import AIchat.jpa.ChatJPA;
import AIchat.entity.Chat;
import AIchat.entity.User;
import AIchat.util.CustomException;

import java.util.List;

public class ChatService {
    static ChatJPA chatJPA = new ChatJPA();

    public static Chat createChat(Integer user_id, Chat chat) {
        User user = UserService.getUser(user_id);
        chat.setUser(user);
        return chatJPA.create(chat);
    }

    public static Chat updateChat(Integer id, Chat chat) {
        Chat u = getChat(id);
        chat.setId(u.getId());
        return chatJPA.update(chat);
    }

    public static Chat getChat(Integer id) {
        Chat u = chatJPA.findById(id);
        if (u == null) {
            throw new CustomException("Chat not found");
        }
        return u;
    }


    public static void deleteChat(Integer id) {
        chatJPA.delete(id);
    }

    public static List<Chat> getChatList(Integer userId) {
        return chatJPA.findByUser(userId);
    }
}
