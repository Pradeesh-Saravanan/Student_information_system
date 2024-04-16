/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author WINDOWS 10
 * SUBSCRIBE OUR YOUTUBE CHANNEL -> https://www.youtube.com/channel/UCPgcmw0LXToDn49akUEJBkQ
 * THANKS FOR YOUR SUPPORT : )
 */
public class database {

    public static Connection connectDb(){

        try{

            Class.forName("com.mysql.jdbc.Driver");

            Connection connect;
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/memory", "root", "Mysql@42");
            return connect;
        }catch(Exception e){e.printStackTrace();}
        return null;
    }

}
