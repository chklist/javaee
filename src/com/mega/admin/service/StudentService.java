package com.mega.admin.service;

import com.mega.admin.pojo.Student;
import com.mega.core.jdbc.page.PageBean;

public interface StudentService {
    PageBean<Student> findStudentsByPage(String stuname, String stuage, int currentPage, int pageSize) throws Exception;
}
