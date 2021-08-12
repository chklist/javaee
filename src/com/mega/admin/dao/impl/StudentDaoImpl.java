package com.mega.admin.dao.impl;

import com.mega.admin.dao.StudentDao;
import com.mega.admin.pojo.Student;
import com.mega.core.jdbc.DBManager;

import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    @Override
    public List<Student> findStudentsByPage(String stuname, String stuage, int currentPage, int pageSize) throws Exception {
        DBManager dbManager = DBManager.getInstance();

        StringBuilder sql = new StringBuilder("select * from student where 1=1 ");
        List<String> params = new ArrayList<>();
        if (stuname != null && !stuname.equals("")) {
            sql.append("and stuname like ? ");
            params.add("%" + stuname + "%");
        }
        if (stuage != null && !stuage.equals("")) {
            sql.append("and stuage >= ? ");
            params.add(stuage);
        }
        sql.append("limit ?,?");

        return dbManager.execMultiQueryByPage((rs, rowNum) -> {
            Student student = new Student();
            student.setStuid(rs.getInt("stuid"));
            student.setStuname(rs.getString("stuname"));
            student.setStuage(rs.getInt("stuage"));
            student.setStugender(rs.getString("stugender"));
            student.setFilename(rs.getString("filename"));
            student.setFiletype(rs.getString("filetype"));
            return student;
        }, sql.toString(), currentPage, pageSize, params.toArray());
    }

    @Override
    public int findStudentsTotalSize(String stuname, String stuage) throws Exception {
        DBManager dbManager = DBManager.getInstance();

        StringBuilder countSql = new StringBuilder("select count(1) from student where 1=1 ");
        List<String> params = new ArrayList<>();
        if (stuname != null && !stuname.equals("")) {
            countSql.append("and stuname like ? ");
            params.add("%" + stuname + "%");
        }
        if (stuage != null && !stuage.equals("")) {
            countSql.append("and stuage >= ?");
            params.add(stuage);
        }
        return dbManager.execQueryCount(countSql.toString(), params.toArray());
    }
}
