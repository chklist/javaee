package com.mega.admin.dao;

import com.mega.admin.pojo.Student;

import java.util.List;

public interface StudentDao {
    List<Student> findStudentsByPage(String stuname, String stuage, int currentPage, int pageSize) throws Exception;

    int findStudentsTotalSize(String stuname, String stuage) throws Exception;
}
