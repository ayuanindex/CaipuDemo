package com.example.geq.caipudemo.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;

import com.example.geq.caipudemo.vo.MenuDetail;
import com.example.geq.caipudemo.vo.Step;
import com.example.geq.caipudemo.vo.Vegetableinfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public  class Recipedao {
    private static Recipedao recipedao;
    private RecipeOpenhelp recipeOpenhelp;
    private List<Vegetableinfo> vegetableinfoList=null;
    private List<MenuDetail> menuDetailList=null;
    private List<Step> stepList=null;
    private MenuDetail menuDetail=null;

    public Recipedao(Context context){
        recipeOpenhelp=new RecipeOpenhelp(context);
    }
    public static Recipedao getRecipedao(Context context){
        if(recipedao==null){
            recipedao=new Recipedao(context);
        }
        return recipedao;
    }
    //添加菜谱分类列表信息
    public boolean innest_type(Vegetableinfo vegetableinfo){
        boolean flag=false;
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(vegetableinfo!=null){
            values.put("typeid", vegetableinfo.getTypeid());
            values.put("typename",vegetableinfo.getTypename());
            values.put("description", vegetableinfo.getDescription());
            values.put("typeic", vegetableinfo.getTypepic());
            long types = db.insert("types", null, values);
            flag=(types>0);
        }
        if(db!=null)
            db.close();
        return flag;
    }

    //查询菜谱分类列表信息
    public List<Vegetableinfo> select_types(){
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        Cursor types = db.query("types", new String[]{"typeid", "typename", "description", "typeic"}, null, null, null, null, null);
        vegetableinfoList=new ArrayList<Vegetableinfo>();
        if(types.getCount()!=0){
            while (types.moveToNext()){
                Vegetableinfo vegetableinfo = new Vegetableinfo(types.getString(3), types.getString(2), types.getString(0), types.getString(1));
                vegetableinfoList.add(vegetableinfo);
            }
        }
        if(db!=null)
            db.close();
        return vegetableinfoList;
    }

    //查询菜谱分类列表信息
    public boolean issavetypes(){
        boolean flag;
        int count=0;
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        Cursor types = db.query("types", new String[]{"typeid"}, null, null, null, null, null);
        count = types.getCount();
        if(count==6){
            return true;
        }
        if(db!=null) {
            db.close();
        }
        return false;
    }

    //添加collect收藏表信息
    public boolean innest_collect(MenuDetail menuDetail){
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        if(menuDetail!=null){
            ContentValues values = new ContentValues();
            values.put("menuid", menuDetail.getMenuid());
            values.put("typeid", menuDetail.getTypeid());
            values.put("spic",menuDetail.getSpic());
            values.put("assistmaterial", menuDetail.getAssistmaterial());
            values.put("notlikes", menuDetail.getNotlikes());
            values.put("menuname", menuDetail.getMenuname());
            values.put("abstracts", menuDetail.getAbstracts());
            values.put("mainmaterial", menuDetail.getMainmaterial());
            values.put("likes", menuDetail.getLikes());
            long types = db.insert("collect", null, values);
            if(types<0){
                if(db!=null)
                    db.close();
                return false;
            }
            List<Step> steps = menuDetail.getSteps();
            for(Step step:steps){
                ContentValues values1 = new ContentValues();
                values1.put("menuid",step.getMenuid());
                values1.put("stepid",step.getStepid());
                values1.put("description",step.getDescription());
                values1.put("pic",step.getPic());
                long step1 = db.insert("step", null, values1);
                if(step1<0){
                    if(db!=null)
                        db.close();
                    return false;
                }
            }
            if(db!=null)
                db.close();
            return true;
        }
        if(db!=null)
            db.close();
        return false;
    }

    //查询collect收藏表所有信息
    public List<MenuDetail> selectall_collect(){
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        Cursor collect = db.query("collect", new String[]{"menuid", "typeid", "spic", "assistmaterial","notlikes","menuname","abstracts","mainmaterial","likes"}, null, null, null, null, null);
        menuDetailList=new ArrayList<MenuDetail>();
        if(collect.getCount()>0){
            while (collect.moveToNext()){
                Cursor step = db.query("step", new String[]{"menuid","stepid","description","pic"}, "menuid = ?", new String[]{collect.getString(0)}, null, null, null);
                stepList=new ArrayList<Step>();
                while (step.moveToNext()){
                    Step stepinfo=new Step(step.getString(1),step.getString(2),step.getString(0),step.getString(3));
                    stepList.add(stepinfo);
                }
                MenuDetail menuDetail = new MenuDetail(collect.getString(2),collect.getString(3),collect.getString(4),collect.getString(5),collect.getString(6),collect.getString(7),collect.getString(0),collect.getString(1),collect.getString(8),stepList);
                menuDetailList.add(menuDetail);
            }
        }
        if(db!=null)
            db.close();
        return menuDetailList;
    }

    //查询collect收藏表其中的一条信息
    public MenuDetail select_collect(String menuid){
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        Cursor collect = db.query("collect", new String[]{"menuid", "typeid", "spic", "assistmaterial","notlikes","menuname","abstracts","mainmaterial","likes"}, "menuid = ?", new String[]{menuid}, null, null, null);
        if(collect.getCount()>0){
            collect.moveToNext();
            Cursor step = db.query("step", new String[]{"menuid","stepid","description","pic"}, "menuid = ?", new String[]{collect.getString(0)}, null, null, null);
            stepList=new ArrayList<Step>();
            while (step.moveToNext()){
                Step stepinfo=new Step(step.getString(1),step.getString(2),step.getString(0),step.getString(3));
                stepList.add(stepinfo);
            }
            menuDetail = new MenuDetail(collect.getString(2),collect.getString(3),collect.getString(4),collect.getString(5),collect.getString(6),collect.getString(7),collect.getString(0),collect.getString(1),collect.getString(8),stepList);
        }
//        if(db!=null)
//            db.close();
        return menuDetail;
    }

    //取消收藏删除collect收藏表其中的一条信息
    public Boolean delete_collect(String menuid){
        SQLiteDatabase db = recipeOpenhelp.getWritableDatabase();
        int step=0;
        int collect = db.delete("collect", "menuid = ?", new String[]{menuid});
        if(collect>0){
            step = db.delete("step", "menuid = ?", new String[]{menuid});
        }else {
            return false ;
        }
        if(step>0){
            return true;
        }else {
            return false;
        }
    }

}
