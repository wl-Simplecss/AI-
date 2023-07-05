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

@WebServlet(name = "PasswordServlet", value = "/password")
public class PassServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String name = request.getParameter("username");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();
        try {
            if (name == null || phone == null || password == null) {
                throw new CustomException("参数错误");
            }
            User u = UserService.getUser(name);
            if (u == null) {
                throw new CustomException("用户不存在");
            }
            if (!u.getPhone().equals(phone)) {
                throw new CustomException("手机号错误");
            }
            u.setPassword(password);//？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？MD5
            User nu = UserService.updateUser(u.getId(), u);
            res.add("user", CustomResponse.convert2Object(nu));
            out.print(CustomResponse.success(res).toString());
        } catch (CustomException e) {
            out.print(CustomResponse.error(res, e.getMessage()).toString());
        }
    }
}
