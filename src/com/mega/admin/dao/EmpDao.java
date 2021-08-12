package com.mega.admin.dao;

import com.mega.admin.pojo.Emp;

import java.util.List;

public interface EmpDao {
    List<Emp> findAll() throws Exception;
}
