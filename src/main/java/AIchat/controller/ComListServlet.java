package AIchat.controller;

import AIchat.entity.Comment;
import AIchat.service.ComService;
import AIchat.util.CustomException;
import AIchat.util.CustomResponse;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CommentListServlet", value = "/comment/list")
public class ComListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Integer chatid = Integer.parseInt(request.getParameter("chat_id"));
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            List<Comment> list = ComService.getCommentListByChat(chatid);
            res.add("list", CustomResponse.convert2Array(list));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }
}
