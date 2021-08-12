<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Emp List</title>
    <style>
        table {
            margin: 0 auto;
            border: 1px solid burlywood;
            border-collapse: collapse;
            border-spacing: 0;
        }
        th, td {
            border: 1px solid burlywood;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>员工编号</th>
        <th>员工姓名</th>
        <th>职务</th>
        <th>上级编号</th>
        <th>入职日期</th>
        <th>薪资</th>
        <th>补助</th>
        <th>部门号</th>
        <th>薪资等级</th>
    </tr>
    <c:forEach items="${requestScope.emps}" var="emp">
        <tr>
            <td>${emp.empNo}</td>
            <td>${emp.ename}</td>
            <td>${emp.job}</td>
            <td>${emp.mgr}</td>
            <td><fmt:formatDate value="${emp.hiredate}" pattern="yyyy年MM月dd日" /> </td>
            <td>${emp.sal}</td>
            <td>${emp.comm}</td>
            <td>${emp.deptNo}</td>
            <td>
                <c:choose>
                    <c:when test="${emp.sal le 500}">A</c:when>
                    <c:when test="${emp.sal le 1000}">B</c:when>
                    <c:when test="${emp.sal le 1500}">C</c:when>
                    <c:when test="${emp.sal le 2000}">D</c:when>
                    <c:when test="${emp.sal le 3000}">E</c:when>
                    <c:when test="${emp.sal le 4000}">F</c:when>
                    <c:otherwise>G</c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>