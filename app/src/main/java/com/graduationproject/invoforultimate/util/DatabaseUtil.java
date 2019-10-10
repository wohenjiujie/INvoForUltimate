package com.graduationproject.invoforultimate.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by INvo
 * on 2019-09-25.
 */
public class DatabaseUtil {
   private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static String tid;

    public DatabaseUtil(Context context) {
        this.context = context;
    }
    public DatabaseUtil() {
//        this.context = context;
    }


    public boolean isRegistration() {
    sharedPreferences=context.getSharedPreferences("Terminal", MODE_PRIVATE);
        tid = sharedPreferences.getString("Tid", "");
        if (tid == "") {
            return false;
        } else {
            return true;
        }
    }

    public String getTerminalID() {
        return sharedPreferences.getString("tid", "");
    }

    public String test() {
        String ano=null;
        String code=null;
        Connection con;
        String driver="com.mysql.cj.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/testxx?"
                +"characterEncoding=utf8&useSSL=true";
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url, "root", "12345678");
            Statement statement=con.createStatement();
            String sql = "select * from info";
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                ano = rs.getString("account");
                code = rs.getString("password");
            }
            rs.close();
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ano;
    }

}
