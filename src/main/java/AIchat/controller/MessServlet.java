package AIchat.controller;

import AIchat.util.CustomResponse;
import AIchat.ejb.TalkBean;
import AIchat.util.CustomException;
import com.google.gson.JsonObject;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MessageServlet", value = "/message")
public class MessServlet extends HttpServlet {
    @EJB
    private TalkBean talkBean;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        Integer chatId = Integer.parseInt(request.getParameter("chat_id"));
        String content = request.getParameter("content");
        System.out.println("Messagepost  chat_id"+chatId+content);
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            System.out.println("this    "+chatId+content);
            talkBean=new TalkBean();
            talkBean.makeInteraction(content, chatId);
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }
}
