package AIchat.controller;

import AIchat.service.ComService;
import AIchat.util.CustomResponse;
import AIchat.entity.Comment;
import AIchat.util.CustomException;
import com.fusesource.examples.activemq.SimpleConsumer;
import com.fusesource.examples.activemq.SimpleProducer;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CommentServlet", value = "/comment")
public class ComServlet extends HttpServlet {
    private SimpleConsumer com;
    private SimpleProducer pro;



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Integer id = Integer.parseInt(request.getParameter("id"));
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            Comment cmt = ComService.getComment(id);
            res.add("comment", CustomResponse.convert2Object(cmt));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        com=new SimpleConsumer();
        pro=new SimpleProducer();
        System.out.println("comment post");
        response.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        JsonObject res = new JsonObject();
        PrintWriter out = response.getWriter();
        if (method != null && method.equals("put")) {
            this.doPut(request, response);
            return;
        }
        if (method != null && method.equals("delete")) {
            this.doDelete(request, response);
            return;
        }


        Integer chatid = Integer.parseInt(request.getParameter("chat_id"));
        Integer userid = Integer.parseInt(request.getParameter("user_id"));
        String content = request.getParameter("content");


        pro.send("this is USer`s content: "+content);
        com.receive();


        try{

            Comment com=new Comment();
            com.setContent(content);
            Comment cmt= ComService.createComment(userid,chatid,com);
            res.add("comment", CustomResponse.convert2Object(cmt));
            out.print(CustomResponse.success(res).toString());

        }catch (Exception e){
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Integer id = Integer.parseInt(request.getParameter("id"));
        String content = request.getParameter("content");
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            Comment data = ComService.getComment(id);
            data.setContent(content);
            Comment cmt = ComService.updateComment(id, data);
            res.add("comment", CustomResponse.convert2Object(cmt));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Integer id = Integer.parseInt(request.getParameter("id"));
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            ComService.deleteComment(id);
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }
}
