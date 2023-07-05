package AIchat.service;


import AIchat.entity.Chat;
import AIchat.entity.Comment;
import AIchat.jpa.ComJPA;
import AIchat.util.CustomException;

import java.util.List;

public class ComService {
    static ComJPA comJPA = new ComJPA();

    public static Comment createComment(Integer userId, Integer chatId, Comment comment) {
        Chat chat = ChatService.getChat(chatId);
        comment.setChat(chat);
        comment.setUserId(userId);
        return comJPA.create(comment);
    }

    public static Comment updateComment(Integer id, Comment user) {
        Comment u = getComment(id);
        user.setId(u.getId());
        return comJPA.update(user);
    }


    public static Comment getComment(Integer id) {
        Comment u = comJPA.findById(id);
        if (u == null) {
            throw new CustomException("User not found");
        }
        return u;
    }

    public static void deleteComment(Integer id) {
        comJPA.delete(id);
    }


    public static List<Comment> getCommentListByChat(Integer chatId) {
        return comJPA.findByChat(chatId);
    }

}
