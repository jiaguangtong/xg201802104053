package cn.edu.sdjzu.xg.bysj.controller.basic.department;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
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

@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    /**
     * POST, http://116.62.205.188:8080/JW_201802104053_24_war_exploded/teacher1.ctl,
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json,Department.class);
        JSONObject jsonObject = JSON.parseObject(department_json);
        int schoolId = Integer.parseInt(jsonObject.getString("school_id"));
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        School school = null;
        try {
            school = SchoolService.getInstance().find(schoolId);
            departmentToAdd.setSchool(school);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        //在数据库表中增加School对象
        try {
            DepartmentService.getInstance().add(departmentToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        Collection<Department> departments = null;
//        try {
//            departments = DepartmentService.getInstance().getAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String department_sql = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
//        response.getWriter().println(department_sql);
        //读取参数id
        String id_str = request.getParameter("id");
        System.out.println(id_str);
        String paraType = request.getParameter("paraType");
        System.out.println(paraType);
        JSONObject message = new JSONObject();
        try {
            if (id_str == null&&paraType ==null){
                responseDepartments(response);
            }else if (id_str != null&&paraType==null){
                int id = Integer.parseInt(id_str);
                responseDepartment(id,response);
            }else if (id_str !=null &&paraType.equals("school"))
            {
                int schoolId = Integer.parseInt(id_str);
                responseDepartmentBySchoolId(schoolId,response);
            }else {response.getWriter().println("不存在这种情况");}
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    /**
     * DELETE, http://116.62.205.188:8080/JW_201802104053_24_war_exploded/teacher1.ctl,
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //读取参数id,类型为str类型
        String departmentId_str = request.getParameter("id");
        //解析字符串，将id类型返回为整数
        int id = Integer.parseInt(departmentId_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的学院
        try {
            DepartmentService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改School对象对应的记录
        try {
            DepartmentService.getInstance().update(departmentToAdd);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        response.getWriter().println(department_json);
    }
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Department> departments = DepartmentService.getInstance().getAll();
        String departments_json = JSON.toJSONString(departments);
        response.getWriter().println(departments_json);
    }
    private void responseDepartmentBySchoolId(int schoolId,HttpServletResponse response)
        throws ServletException,IOException,SQLException{
                Collection<Department> departments = DepartmentService.getInstance().findAllBySchoolId(schoolId);
                 String department_json = JSON.toJSONString(departments);
                 response.getWriter().println(department_json);
             }
}
