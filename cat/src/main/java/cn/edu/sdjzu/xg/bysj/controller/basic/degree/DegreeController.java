package cn.edu.sdjzu.xg.bysj.controller.basic.degree;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {
    /**
     * POST, http://116.62.205.188:8080/JW_201802104053_24_war_exploded/teacher1.ctl,
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        JSONObject message = new JSONObject();
        try {
            DegreeService.getInstance().add(degreeToAdd);
            message.put("message", "增加成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    /**
     * GET, http://116.62.205.188:8080/JW_201802104053_24_war_exploded/teacher1.ctl,
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        //获得所有学院
//        Collection<Degree> degrees = null;
//        try {
//            degrees = DegreeService.getInstance().findAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String degrees_str = JSON.toJSONString(degrees);
////        JSON.to
//        //向客户端响应数据
//        response.getWriter().println(degrees_str);
        //设置响应字符编码为UTF-8
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseDegrees(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseDegree(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        response.getWriter().println(degree_json);
    }

    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);

        response.getWriter().println(degrees_json);
    }
    /**
     * DELETE, http://116.62.205.188:8080/JW_201802104053_24_war_exploded/teacher1.ctl,
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的学院
        try {
            DegreeService.getInstance().delete(id);
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    /**
     * PUT, http://116.62.205.188:8080/JW_201802104053_24_war_exploded/teacher1.ctl,
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToPut = JSON.parseObject(degree_json, Degree.class);
          //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改School对象对应的记录
        try {
            DegreeService.getInstance().update(degreeToPut);
            message.put("message", "修改成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
}
