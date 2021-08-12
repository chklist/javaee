package com.mega.admin.service.impl;

import com.mega.admin.dao.EmpDao;
import com.mega.admin.dao.impl.EmpDaoImpl;
import com.mega.admin.pojo.Emp;
import com.mega.admin.service.EmpService;

import java.util.List;

public class EmpServiceImpl implements EmpService {

    private EmpDao empDao;

    public EmpServiceImpl() {
        empDao = new EmpDaoImpl();
    }

    @Override
    public List<Emp> findAll() {
        try {
            return empDao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
