package com.mega.admin.controller;

import com.mega.admin.pojo.Emp;
import com.mega.admin.service.EmpService;
import com.mega.admin.service.impl.EmpServiceImpl;
import com.mega.core.servlet.DispatcherServlet;
import com.mega.core.util.ConstUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/emp/*")
public class EmpController extends DispatcherServlet {
    private EmpService empService;

    public String list(HttpServletRequest request, HttpServletResponse response) {

        System.out.println(request.getContextPath());
        List<Emp> emps = empService.findAll();
        request.setAttribute("emps", emps);
        return ConstUtils.FORWARD + "list.jsp";
    }

    @Override
    public void init() throws ServletException {
        empService = new EmpServiceImpl();
    }
}
