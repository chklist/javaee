package com.mega.core.servlet;

import com.mega.core.util.ConstUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionName = getActionNameByServletRequest(req.getRequestURI());
        if (StringUtils.isNotEmpty(actionName)) {
            try {
                Method method = getActionMethodByActionName(actionName);
                Object[] args = actionMethodParam(method, req, resp);
                Object invoke = method.invoke(this, args);
                if (invoke != null) {
                    response(invoke.toString(), req, resp);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 响应页面给浏览器
     *
     * @param result   响应页面字符串
     * @param request  请求
     * @param response 响应
     * @throws ServletException Servlet异常
     * @throws IOException      IQ异常
     */
    private void response(String result, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (result.startsWith(ConstUtils.FORWARD)) {
            request.getRequestDispatcher("/" + result.split(":")[1]).forward(request, response);
        } else if (result.startsWith(ConstUtils.REDIRECT)) {
            response.sendRedirect(request.getContextPath() + "/" + result.split(":")[1]);
        }
    }

    /**
     * 获取方法参数对象数组
     *
     * @param method   方法
     * @param request  请求
     * @param response 响应
     * @return 参数对象数组
     */
    private Object[] actionMethodParam(Method method, HttpServletRequest request, HttpServletResponse response) {
        Object[] args = new Object[method.getParameterCount()];
        // 编译选项需要开启-parameters
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value;
            if ("HttpServletRequest".equals(parameter.getType().getSimpleName())) {
                value = request;
            } else if ("HttpServletResponse".equals(parameter.getType().getSimpleName())) {
                value = response;
            } else {
                value = parameterType(request.getParameter(parameter.getName()), parameter.getType());
                if (value == null) {
                    try {
                        Class<?> parameterType = parameter.getType();
                        Object instance = parameterType.newInstance();

                        Map<String, String[]> parameterMap = request.getParameterMap();
                        Set<String> keySet = parameterMap.keySet();
                        for (String key : keySet) {
                            Field field = parameterType.getDeclaredField(key);
                            field.setAccessible(true);
                            String type = field.getType().getSimpleName();
                            switch (type) {
                                case ConstUtils.STRING:
                                    field.set(instance, parameterMap.get(key)[0]);
                                    break;
                                case ConstUtils.INTEGER:
                                    field.set(instance, Integer.parseInt(parameterMap.get(key)[0]));
                                    break;
                                case ConstUtils.DATE:
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    field.set(instance, format.parse(parameterMap.get(key)[0]));
                                    break;
                            }
                        }
                        value = instance;
                    } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            args[i] = value;
        }
        return args;
    }

    /**
     * 根据方法参数类型获取参数对象
     *
     * @param parameter 请求参数
     * @param type      方法参数类型
     * @return 参数对象
     */
    private Object parameterType(String parameter, Class<?> type) {
        Object value = parameter;
        if (ConstUtils.INTEGER.equals(type.getSimpleName())) {
            value = Integer.parseInt(parameter);
        } else if (ConstUtils.DOUBLE.equals(type.getSimpleName())) {
            value = Double.parseDouble(parameter);
        }
        return value;
    }

    private String getActionNameByServletRequest(String requestURI) {
        return requestURI.substring(requestURI.lastIndexOf("/") + 1);
    }

    private Method getActionMethodByActionName(String actionName) throws NoSuchMethodException {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(actionName)) {
                return method;
            }
        }
        throw new NoSuchMethodException("the requested method does not exist.");
    }
}
