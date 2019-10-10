package com.graduationproject.invoforultimate;

import com.graduationproject.invoforultimate.util.DatabaseUtil;
import com.graduationproject.invoforultimate.util.ToastUtil;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
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

        System.out.println(ano);
        System.out.println(code);

    }
}