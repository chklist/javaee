package com.mega.admin.controller;

import com.mega.admin.pojo.Student;
import com.mega.admin.service.StudentService;
import com.mega.admin.service.impl.StudentServiceImpl;
import com.mega.core.jdbc.page.PageBean;
import com.mega.core.servlet.DispatcherServlet;
import com.mega.core.util.ConstUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/student/*")
public class StudentController extends DispatcherServlet {

    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentServiceImpl();
    }

    public String list(HttpServletRequest request, HttpServletResponse response) {
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        } catch (Exception ignored) {
        }

        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        } catch (Exception ignored) {
        }

        String stuname = request.getParameter("stuname");
        String stuage = request.getParameter("stuage");
        try {
            PageBean<Student> pageBean = studentService.findStudentsByPage(stuname, stuage, currentPage, pageSize);
            request.setAttribute("pageBean", pageBean);
            return ConstUtils.FORWARD + "students.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
