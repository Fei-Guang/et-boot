package glodon.etender;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@EnableTransactionManagement 
public class MyBatisConfig implements TransactionManagementConfigurer{
	
	
	@Autowired 
	private DataSource dataSource;
	
	@Bean(name = "sqlSessionFactory") 
	public SqlSessionFactory sqlSessionFactoryBean() {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean(); 
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		 try { // 根据实际配置“mapper.xml”路径
			 bean.setMapperLocations(resolver.getResources("classpath:/com/web/mapping/etender/*.xml")); 
			 return bean.getObject(); 
		 } catch (Exception e) {
			 e.printStackTrace();
			 throw new RuntimeException(e); 
		 }
	}
	
	@Bean 
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) { 
		return new SqlSessionTemplate(sqlSessionFactory); 
	} 
	
	@Bean
	@Override 
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource); 
	}
		

	
	
}
