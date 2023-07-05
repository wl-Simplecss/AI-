package AIchat.controller;

import AIchat.entity.Message;
import AIchat.util.CustomException;
import AIchat.util.CustomResponse;
import com.google.gson.JsonObject;
import AIchat.ejb.MessBean;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MessageListServlet", value = "/message/list")
public class MessListServlet extends HttpServlet {
    @EJB
    private MessBean messBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Integer chatId = Integer.parseInt(request.getParameter("chat_id"));
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            MessBean mss=new MessBean();
            List<Message> list = mss.getMessageByChat(chatId);
            res.add("list", CustomResponse.convert2Array(list));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }
}
