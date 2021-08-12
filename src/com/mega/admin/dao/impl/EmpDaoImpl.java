package com.mega.admin.dao.impl;

import com.mega.admin.dao.EmpDao;
import com.mega.admin.pojo.Emp;
import com.mega.core.jdbc.DBManager;

import java.util.List;

public class EmpDaoImpl implements EmpDao {
    @Override
    public List<Emp> findAll() throws Exception {
        DBManager dbManager = DBManager.getInstance();
        return dbManager.execMultiQuery((rs, rowNum) -> {
            Emp emp = new Emp();
            emp.setEmpNo(rs.getInt("empno"));
            emp.setEname(rs.getString("ename"));
            emp.setJob(rs.getString("job"));
            emp.setMgr(rs.getInt("mgr"));
            emp.setHiredate(rs.getDate("hiredate"));
            emp.setSal(rs.getDouble("sal"));
            emp.setComm(rs.getDouble("comm"));
            emp.setDeptNo(rs.getInt("deptno"));
            return emp;
        }, "select * from emp");
    }
}
