package com.astro.Util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by astro on 2018/2/22.
 */
public class DBUtil {

    private static Properties props;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.yml");
            props = new Properties();
            props.load(in);
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception{
        String url = props.getProperty("jdbc:mysql://localhost:3306/seckilling?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false");
        String username = props.getProperty("root");
        String password = props.getProperty("");
        String driver = props.getProperty("com.mysql.jdbc.Driver");
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/seckilling?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false"
                ,"root", "");
    }
}
