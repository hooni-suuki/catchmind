package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import model.User;

public class UserDao {
	private SqlSessionFactory sqlSessionFactory = null;
	
	public UserDao(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory=sqlSessionFactory;
	}
	
	public List<User> selectAll(){
		List<User> list = null;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			list = session.selectList("User.selectAll");
		}finally {
			session.close();
		}
		return list;
	}
	
	public User selectByID(int id) {
		User user = null;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			user= session.selectOne("User.selectById", id);
		} finally {
			session.close();
		} 
		return user;
	}
}
