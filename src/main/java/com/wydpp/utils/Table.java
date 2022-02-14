package com.wydpp.utils;

import com.wydpp.model.SipDevice;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table {

    public static void main(String[] args) {
        String s = generateSql(SipDevice.class.getName());
        System.out.println(s);
    }

    /**
     * 根据实体类生成建表语句
     * @author
     * @date	2019年1月14日
     * @param className 全类名
     */
    public static String generateSql(String className){
        try {
            Class<?> clz = Class.forName(className);
            className = clz.getSimpleName();
            Field[] fields = clz.getDeclaredFields();
            StringBuilder column = new StringBuilder();
            String varchar = " varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,";
            for (Field f : fields) {
                column.append(" \n `").append(humpToLine(f.getName())).append("`").append(varchar);
            }
            String sql = "\n DROP TABLE IF EXISTS `" + humpToLine(className) + "`; " +
                    " \n CREATE TABLE `" + humpToLine(className) + "`  (" +
                    " \n `id` int(11) NOT NULL AUTO_INCREMENT," +
                    " \n " + column +
                    " \n PRIMARY KEY (`id`) USING BTREE," +
                    "\n INDEX `id`(`id`) USING BTREE" +
                    " \n ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci;";
            return sql;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
