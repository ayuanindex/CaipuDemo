package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.db.InternetUtils;
import com.example.geq.caipudemo.db.Recipedao;
import com.example.geq.caipudemo.vo.MenuDetail;
import com.example.geq.caipudemo.vo.Menuinfo;
import com.example.geq.caipudemo.vo.Request_menu;

import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends Activity {

    private static final String TAG = "ClassNameListActivity";
    private Recipedao recipedao;
    private  List<MenuDetail> list;
    private TextView tv_class_name;
    private ListView lv_menu_item;
    private String typename;
    private String typeid;
    private List<Menuinfo> getmenus = null;
    private Request_menu request_menu;
    private  MyAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        initUI();
        initData();
    }


    private void initUI() {
        getmenus = new ArrayList<Menuinfo>();
        //取得从上一个页面传过来的参数(用于数据获取和标题显示)
        typeid = getIntent().getStringExtra("typeid");
        lv_menu_item = (ListView) findViewById(R.id.lv_menu_item);


        //条目点击事件
        onListViewClickListener();
    }

    /**
     *  //条目点击事件，跳转
     */
    private void onListViewClickListener() {
        lv_menu_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCollectActivity.this,MyCollectInfoActivity.class);
                intent.putExtra("menuid",list.get(position).getMenuid());
                startActivityForResult(intent,1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                list.clear();
                initData();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从数据库数据，放到数据库中，图片加载到
     */
    private void initData() {
        new Thread(){
            @Override
            public void run() {
                Recipedao recipedao;
                recipedao = Recipedao.getRecipedao(MyCollectActivity.this);
                list = recipedao.selectall_collect();
                if (list!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter = new MyAdapter();
                            lv_menu_item.setAdapter(myAdapter);
                        }
                    });
                }
            }
        }.start();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          ViewHolder viewHolder;
          if (convertView==null){
              convertView = View.inflate(getApplicationContext(),R.layout.menu_item,null);
              viewHolder= new ViewHolder();
              viewHolder.mName = convertView.findViewById(R.id.tv_name);
              convertView.setTag(viewHolder);
          }else{
              viewHolder = (ViewHolder) convertView.getTag();
          }
            MenuDetail menuDetai = (MenuDetail) getItem(position);
            viewHolder.mName.setText(menuDetai.getMenuname());
          return convertView;
        }
    }


    public class ViewHolder{
        TextView mName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(1);
    }
}




















