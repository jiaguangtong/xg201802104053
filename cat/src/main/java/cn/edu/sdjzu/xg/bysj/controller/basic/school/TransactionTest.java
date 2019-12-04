package cn.edu.sdjzu.xg.bysj.controller.basic.school;

import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionTest {
    public static void main(String[] args) {
        //为了更好的模拟insert的操作异常，将school表的no字段添加唯一约束
        //alter table school add unique（no）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("INSERT INTO school(description,no)VALUE (?,?)");
            preparedStatement.setString(1,"管理学院");
            preparedStatement.setString(2,"22");
            //执行第一条insert语句
            preparedStatement.executeLargeUpdate();
            preparedStatement = connection.prepareStatement("INSERT INTO school(description,no)VALUE (?,?)");
            preparedStatement.setString(1,"土木学院");
            preparedStatement.setString(2,"02");
            //执行第一条insert语句
            preparedStatement.executeUpdate();
            //提交当前连接所作的操作
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
            try {
                //回滚当前连接所做的操作
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }finally {
            try{
                //恢复自动提交
                if(connection != null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(preparedStatement,connection);
        }
    }
}
