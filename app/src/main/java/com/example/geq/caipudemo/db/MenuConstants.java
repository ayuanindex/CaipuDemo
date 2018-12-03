package com.example.geq.caipudemo.db;

public interface MenuConstants {
    //数据库的名称
    public static final String DB_NAME="menu.db";

    //数据库的版本
    public static final int DB_VERSION=1;

    //表的名称
    public static final String TABLE_TYPES="types";
    public static final String TABLE_MEUNS="menus";
    public static final String TABLE_MENUDETAIL="menuDetail";
    public static final String TABLE_SUPPORT="support";
    public static final String TABLE_COMMENT="comment";

    //菜谱的分类编号
    public static final String TYPESID="typesid";
    //菜谱的分类名称
    public static final String TYPENAME="typename";
    //菜谱的分类介绍
    public static final String DESCRIPTION="description";
    //菜谱的分类图片
    public static final String TYPEPIC="typeic";

    //创建types表的sql语句
    public static final String DB_SQL="create table "+TABLE_TYPES+"("+TYPESID+" integer primary key autoincrement," +
            ""+TYPENAME+" varcher(100),"+DESCRIPTION+" varcher(500),"+TYPEPIC+" varcher(100))";

    //创建menus表的sql语句

    //创建menuDetail表的sql语句

    //创建support表的sql语句

    //创建comment表的sql语句








}
