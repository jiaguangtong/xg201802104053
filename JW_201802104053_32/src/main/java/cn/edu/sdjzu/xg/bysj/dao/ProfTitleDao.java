package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles;
	static{
		profTitles = new TreeSet<ProfTitle>();
		ProfTitle ProfTitle = new ProfTitle(1,"教授","01","");
		profTitles.add(ProfTitle);
		profTitles.add(new ProfTitle(2,"副教授","02",""));
		profTitles.add(new ProfTitle(3,"讲师","03",""));
		profTitles.add(new ProfTitle(4,"助教","04",""));
	}
	public Collection<ProfTitle> findAll() throws SQLException{
		profTitles = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		while (resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		JdbcHelper.close(statement,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "SELECT * FROM proftitle where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
				resultSet.getString("no"),resultSet.getString("remarks"));
		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "UPDATE proftitle SET description = ?,no = ?,remarks = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
		pstmt.setString(1,profTitle.getDescription());
		pstmt.setInt(4,profTitle.getId());
		pstmt.setString(2,profTitle.getNo());
		pstmt.setString(3,profTitle.getRemarks());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO proftitle "+ "(description, no,remarks)" +" VALUES (?,?,?)");
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException{
		ProfTitle profTitle = this.find(id);
		return this.delete(profTitle);
	}

	public boolean delete(ProfTitle profTitle){
		return profTitles.remove(profTitle);
	}
}

