package mybatis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Mybatis_User {
	
	private static SqlSessionFactory sqlSessionFactory; //�����ͺ��̽��� ���� SQL ���࿡ ���� ��� ���� ���� ��ü
	static {
		try {
			String resource = "config/config.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			
			if(sqlSessionFactory ==null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		}
		catch(FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	public static SqlSessionFactory getSqlSessionFactory(){
		return sqlSessionFactory;
	}
}
