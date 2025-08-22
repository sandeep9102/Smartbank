package com.smartbank.db;
import com.zaxxer.hikari.HikariConfig; import com.zaxxer.hikari.HikariDataSource; import io.github.cdimascio.dotenv.Dotenv; import javax.sql.DataSource;
public class Database {
  private static HikariDataSource ds;
  public static DataSource get(){
    if(ds==null){
      Dotenv env=Dotenv.configure().ignoreIfMissing().load();
      String url=env.get("DB_URL","jdbc:mysql://localhost:3306/smartbank?useSSL=false");
      String user=env.get("DB_USER","root"); String pass=env.get("DB_PASSWORD","");
      HikariConfig cfg=new HikariConfig(); cfg.setJdbcUrl(url); cfg.setUsername(user); cfg.setPassword(pass);
      cfg.setMaximumPoolSize(Integer.parseInt(env.get("DB_POOL_SIZE","10"))); cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
      ds=new HikariDataSource(cfg);
    } return ds;
  }
}
