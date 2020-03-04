package com.example.facelogin.Controller;

import com.example.facelogin.Service.LoginService;
import com.example.facelogin.Utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/login")
@SessionAttributes(value = "useinf")
public class LoginController {
    @Autowired
    LoginService loginService=null;
    @RequestMapping("/jumpGetface")
    public String getface(){
        return "getface.html";
    }

    @RequestMapping("/searchface")
    @ResponseBody
    public   String searchface(@RequestBody @RequestParam(name = "imagebast64") StringBuffer imagebast64, Model model,HttpServletRequest request) throws IOException {
        Map<String, Object> searchface = loginService.searchface(imagebast64);
        if(searchface==null||searchface.get("user_id")==null){
            System.out.println("我进来了");
            String flag="fail";
            String s = GsonUtils.toJson(flag);
            return s;
        }
            String user_id = searchface.get("user_id").toString();
            String score=searchface.get("score").toString().substring(0,2);
            int i = Integer.parseInt(score);
            if(i>80){
                model.addAttribute("userinf",user_id);
                HttpSession session = request.getSession();
                session.setAttribute("userinf",user_id);
                System.out.println("存入session");
            }


        System.out.println(searchface);
        String s = GsonUtils.toJson(searchface);
        return s;


    }
    @RequestMapping("/facesucceed")
    public String Faceloginsucceed(){
        System.out.println(1222222);
        return "succeed";
    }

}
