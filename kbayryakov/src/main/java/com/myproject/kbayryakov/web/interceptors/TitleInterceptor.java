package com.myproject.kbayryakov.web.interceptors;

import com.myproject.kbayryakov.web.annotations.PageTitle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TitleInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle (HttpServletRequest request, HttpServletResponse response,
                            Object handler, ModelAndView modelAndView) {

        String title = "Cars";

        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        } else {
            if (handler instanceof HandlerMethod) {
                PageTitle methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(PageTitle.class);

                if (methodAnnotation != null) {
                    modelAndView.addObject("title", title + " - " + methodAnnotation.value());
                }
            }
        }
    }
}
