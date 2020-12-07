package cn.krl.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//处理CustomizeExceptionHandler 没处理的异常
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest request,
                                 // HttpServletResponse response,
                                  Model model) {
        HttpStatus status = this.getStatus(request);
        //前端异常
        if (status.is4xxClientError()) {
            model.addAttribute("message", "前端异常！");
        }
        //后端异常
        if (status.is5xxServerError()) {
            model.addAttribute("message", "后端异常！");
        }
        return new ModelAndView("error");
    }

    //获取状态
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
