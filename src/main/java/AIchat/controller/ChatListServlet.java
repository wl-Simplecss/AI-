package AIchat.controller;

import AIchat.util.CustomResponse;
import AIchat.entity.Chat;
import AIchat.service.ChatService;
import AIchat.util.CustomException;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ChatListServlet", value = "/chat/list")
public class ChatListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Integer userid = Integer.parseInt(request.getParameter("user_id"));
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            System.out.println("ChatListServlet.get.userid"+userid);
            List<Chat> list = ChatService.getChatList(userid);
            res.add("list", CustomResponse.convert2Array(list));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }
}
