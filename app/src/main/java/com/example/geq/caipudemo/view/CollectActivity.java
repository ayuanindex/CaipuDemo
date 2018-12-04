package com.example.geq.caipudemo.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.db.Recipedao;
import com.example.geq.caipudemo.vo.MenuDetail;

import java.util.ArrayList;
import java.util.List;

import static com.example.geq.caipudemo.tool.GetDrawable.getBitmap;

public class CollectActivity extends Activity {

    private ListView lv_collectlist;
    private List<MenuDetail> menuDetails;
    private Myadapter myadapter;
    private Recipedao recipedao;
    Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             switch (msg.what){
                 case 0:
                     if(menuDetails!=null){
                         myadapter=new Myadapter();
                         System.out.println(menuDetails.size());
                         lv_collectlist.setAdapter(myadapter);
                     }
                     break;
                 case 1:
                     if(myadapter!=null){
                         myadapter.notifyDataSetChanged();
                     }
                     break;
             }
             }
     };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_list);
        init_view();
        init_date();
    }

    private void init_view() {
        lv_collectlist = findViewById(R.id.lv_collectlist);
    }

    private void init_date() {
        new Thread() {
            @Override
            public void run() {
                recipedao = Recipedao.getRecipedao(CollectActivity.this);
                menuDetails = new ArrayList<MenuDetail>();
                menuDetails = recipedao.selectall_collect();
                Message message=new Message();
                message.what=0;
                handler.sendMessage(message);
            }
        }.start();

    lv_collectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(CollectActivity.this,MyCollectInfoActivity.class);
            intent.putExtra("menuid",menuDetails.get(position).getMenuid());
            startActivityForResult(intent,1);
        }
    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        init_date();
    }

    class Myadapter extends BaseAdapter{
        private ViewHolder holder;
        private MenuDetail item;

        @Override
        public int getCount() {
            return menuDetails.size();
        }

        @Override
        public MenuDetail getItem(int position) {
            return menuDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            item = menuDetails.get(position);
            if(convertView==null){
                convertView=View.inflate(getApplicationContext(),R.layout.collect_item,null);
                holder = new ViewHolder();
                holder.iv_icon = convertView.findViewById(R.id.iv_icon);
                holder.iv_star = convertView.findViewById(R.id.iv_star);
                holder.tv_mainmaterial = convertView.findViewById(R.id.tv_mainmaterial);
                holder.tv_abstracts = convertView.findViewById(R.id.tv_abstracts);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }
             holder = (ViewHolder)convertView.getTag();
             holder.tv_name.setText(item.getMenuname());
             holder.tv_abstracts.setText("简介："+item.getAbstracts());
             holder.tv_mainmaterial.setText("主要材料："+item.getMainmaterial());
            Bitmap bitmap = getBitmap(item.getSpic(), getApplicationContext());
            if(bitmap!=null){
             //   getdrawable=getDrawable(R.drawable.logo);
                holder.iv_icon.setImageBitmap(bitmap);
            }else {
                holder.iv_icon.setBackgroundResource(R.drawable.hsr);
            }
            holder.iv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipedao.delete_collect(item.getMenuid());
                    Toast.makeText(getApplicationContext(),"取消收藏",Toast.LENGTH_SHORT).show();
                    menuDetails.remove(position);
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            });
            return convertView;
        }
        class ViewHolder{
            ImageView iv_icon;
            ImageView iv_star;
            TextView tv_mainmaterial;
            TextView tv_abstracts;
            TextView tv_name;
        }
    }
}
