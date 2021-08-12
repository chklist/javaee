<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Student List</title>
    <base href="">
    <!--移动端优先的配置-->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!--bootstrap的样式库-->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/bootstrap.min.css">
    <style>
        table {
            border: 3px solid darkgreen;
        }

        th, td {
            border: 3px solid darkgreen;
        }
    </style>
    <!--jQuery-->
    <script type="application/javascript" src="<%=request.getContextPath()%>/static/js/jquery-3.5.1.min.js"></script>
    <!--bootstrap核心库含popper-->
    <script type="application/javascript"
            src="<%=request.getContextPath()%>/static/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
    <table class="table table-stripe">
        <tr>
            <th>学生编号</th>
            <th>学生姓名</th>
            <th>学生年龄</th>
            <th>学生性别</th>
            <th>照片</th>
            <th>照片类型</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${requestScope.pageBean.data}" var="student">
            <tr>
                <td>${student.stuid}</td>
                <td>${student.stuname}</td>
                <td>${student.stuage}</td>
                <td>${student.stugender}</td>
                <td>${student.filename}</td>
                <td>${student.filetype}</td>
                <td>下载</td>
            </tr>
        </c:forEach>
        <tr style="text-align: center">
            <td colspan="7">
                <a href="javascript:void(0);" onclick="changePage(1)">首页</a>&emsp;
                <a href="javascript:void(0);" onclick="changePage(${requestScope.pageBean.currentPage - 1})">上一页</a>&emsp;
                <a href="javascript:void(0);" onclick="changePage(${requestScope.pageBean.currentPage + 1})">下一页</a>&emsp;
                <a href="javascript:void(0);" onclick="changePage(${requestScope.pageBean.totalPage})">尾页</a>&emsp;
                每页
                <select name="pageSize" id="pageSize" onchange="changePageSize(this.value)">
                    <option value="10" selected>10</option>
                    <option value="20">20</option>
                    <option value="30">30</option>
                    <option value="50">50</option>
                </select>
                条&emsp;
                当前第${requestScope.pageBean.currentPage}页&emsp;
                共${requestScope.pageBean.totalPage}页&emsp;
                共${requestScope.pageBean.totalSize}条记录
            </td>
        </tr>
    </table>
</div>
<script>
    $(function () {
       $("#pageSize").val(${requestScope.pageBean.pageSize})
    });
    function changePageSize(val) {
        location.href = "<%=request.getContextPath()%>/student/list?currentPage=${requestScope.pageBean.currentPage}&pageSize="+val
    }
    function changePage(currentPage) {
        if (currentPage < 1) {
            return
        }
        if (currentPage > ${requestScope.pageBean.totalPage}) {
            return
        }
        location.href = "<%=request.getContextPath()%>/student/list?currentPage=" + currentPage + "&pageSize="+$("#pageSize").val()
    }
</script>
</body>
</html>
