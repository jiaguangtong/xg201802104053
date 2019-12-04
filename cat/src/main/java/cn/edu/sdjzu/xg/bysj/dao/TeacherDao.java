package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.*;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Date;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers;
	static{
//		ProfTitle assProf = ProfTitleDao.getInstance().find(2);
//		ProfTitle lecture = ProfTitleDao.getInstance().find(3);
//
//		Degree phd = DegreeDao.getInstance().find(1);
//		Degree master = DegreeDao.getInstance().find(2);
//
//
//		Department misDept = DepartmentService.getInstance().find(2);
//
//		teachers = new TreeSet<Teacher>();
//		Teacher teacher = new Teacher(1,"苏同",assProf,phd,misDept);
//		teachers.add(teacher);
//		teachers.add(new Teacher(2,"刘霞",lecture,phd,misDept));
//		teachers.add(new Teacher(3,"王方",assProf,phd,misDept));
//		teachers.add(new Teacher(4,"刘峰",assProf,master,misDept));
	}
	public Collection<Teacher> findAll() throws SQLException {
		teachers = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		while (resultSet.next()){
			teachers.add(new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					resultSet.getString("no"),
					ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id"))));
//
		}
		JdbcHelper.close(statement,connection);
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teacher where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		Teacher teacher = new Teacher(resultSet.getInt("id"),
				resultSet.getString("name"),
				resultSet.getString("no"),
				ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
				DegreeDao.getInstance().find(resultSet.getInt("degree_id")),DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		return teacher;

	}
	
	public boolean update(Teacher teacher) throws SQLException{
		Connection connection = JdbcHelper.getConn();

		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE teacher SET  proftitle_id = ?,no=?,name  = ?,department_id = ?,degree_id = ? where id = ?");

		preparedStatement.setString(3,teacher.getName());
		preparedStatement.setString(2,teacher.getNo());

		preparedStatement.setInt(4,teacher.getDepartment().getId());

		preparedStatement.setInt(5,teacher.getDegree().getId());
		preparedStatement.setInt(1,teacher.getTitle().getId());
		preparedStatement.setInt(6,teacher.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
	
	public void add(Teacher teacher) throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement= null;
		try {
			connection = JdbcHelper.getConn();
			String addTeacher_sql = "INSERT INTO teacher(name,no,proftitle_id,degree_id,department_id) VALUES" +
					" (?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(addTeacher_sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,teacher.getName());
			preparedStatement.setString(2,teacher.getNo());
			preparedStatement.setInt(3,teacher.getTitle().getId());
			preparedStatement.setInt(4,teacher.getDegree().getId());
			preparedStatement.setInt(5,teacher.getDepartment().getId());
			int affectedRowNum = preparedStatement.executeUpdate();
			System.out.println("添加了 " + affectedRowNum +" 行记录");
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			int teacherId = resultSet.getInt(1);
			teacher.setId(teacherId);
			Date date_util = new Date();
			Long date_long = date_util.getTime();
			java.sql.Date date_sql = new java.sql.Date(date_long);

			User user = new User(
					teacher.getNo(),
					teacher.getNo(),
					date_sql,
					teacher
			);
			UserService.getInstance().add(connection,user);

		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
			try {
				if (connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e.printStackTrace();
			}

		} finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement,connection);
		}

	}

	public void delete(Integer id) throws SQLException {
		Teacher teacher = this.find(id);
		this.delete(teacher);
	}
	
	public void delete(Teacher teacher) throws SQLException{
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {

			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			String deleteUser_sql = "DELETE FROM user WHERE teacher_id = ?";
			pstmt = connection.prepareStatement(deleteUser_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum +" 行记录");

			String deleteTeacher_sql = "DELETE FROM teacher WHERE id = ?";
			pstmt = connection.prepareStatement(deleteTeacher_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum1 = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum1 +" 行记录");


		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
			try {
				if (connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e.printStackTrace();
			}

		} finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}

	}
	
	
	
}
