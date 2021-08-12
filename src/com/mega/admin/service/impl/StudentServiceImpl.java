package com.mega.admin.service.impl;

import com.mega.admin.dao.StudentDao;
import com.mega.admin.dao.impl.StudentDaoImpl;
import com.mega.admin.pojo.Student;
import com.mega.admin.service.StudentService;
import com.mega.core.jdbc.page.PageBean;

import java.util.List;

public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;

    public StudentServiceImpl() {
        studentDao = new StudentDaoImpl();
    }

    @Override
    public PageBean<Student> findStudentsByPage(String stuname, String stuage, int currentPage, int pageSize) throws Exception {
        List<Student> students = studentDao.findStudentsByPage(stuname, stuage, currentPage, pageSize);
        int totalSize = studentDao.findStudentsTotalSize(stuname, stuage);
        int totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        PageBean<Student> pageBean = new PageBean<>();
        pageBean.setData(students);
        pageBean.setTotalSize(totalSize);
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalPage(totalPage);
        return pageBean;
    }
}
