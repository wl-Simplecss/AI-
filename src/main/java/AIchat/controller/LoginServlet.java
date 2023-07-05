package AIchat.controller;

import AIchat.entity.User;
import AIchat.service.UserService;
import AIchat.util.CustomException;
import AIchat.util.CustomResponse;
import com.google.gson.JsonObject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;





@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {





    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("command");
        response.setCharacterEncoding("UTF-8");
        String name = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        System.out.println("name:"+name);
        try {

            User u = UserService.login(name, password);

            res.add("user", CustomResponse.convert2Object(u));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }




}
