package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.School;
import java.sql.SQLException;

public class SchoolDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        //获取school对象，以便为schoolToAdd的
        School schoolToAdd = new School("管理工程","01","最好的");
        //创建Dao对象
        SchoolDao schoolDao = new SchoolDao();
        //执行Dao对象的方法
        School addedSchool = schoolDao.addWithSP(schoolToAdd);
        //打印添加后的对象
        System.out.println(addedSchool);
        System.out.println("添加成功");
    }
}
