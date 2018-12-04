package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.db.ImageUtils;
import com.example.geq.caipudemo.tool.GetDrawable;
import com.example.geq.caipudemo.tool.Http_comments;
import com.example.geq.caipudemo.tool.Http_postComment;
import com.example.geq.caipudemo.vo.Comment;


import java.util.List;


public class CommentPageActivity extends Activity implements View.OnClickListener {
    private String menuid;
    private TextView mName;
    private ImageView mIcon;
    private ListView mListView;
    private TextView mComment;
    private Button mSend;
    private List<Comment> commentList;
    private boolean flag;
    private Drawable drawable;
    //评论结合


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);
        initView();
        initData();
    }


    //初始化控件
    private void initView() {
        mName = findViewById(R.id.comment_tv_name);
        mIcon = findViewById(R.id.comment_lv_icon);
        mListView = findViewById(R.id.comment_lv_listview);
        mComment = findViewById(R.id.comment_et_comment);
        mSend = findViewById(R.id.comment_but_send);
        //按钮点击发送操作
        mSend.setOnClickListener(this);
        //图片长按点击事件
       setOnImageLongClickListener();
    }


    //长按点击事件
    private void setOnImageLongClickListener() {
        mIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentPageActivity.this);
                builder.setTitle("保存图片");
                builder.setMessage("是否保存？");
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageUtils.saveCroppedImage(CommentPageActivity.this,drawable);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                builder.show();
                return true;
            }
        });
    }




    //初始化数据，展示数据
    private void initData() {
        //获取传递管理的菜品id
        Intent intent = getIntent();
        //图片路径
        final String spic = intent.getStringExtra("spic");
        menuid = intent.getStringExtra("menuid");
        getMenuId();
        if (menuid != null) {
            Log.e("---pl----", "initData: " + menuid);
            new Thread() {
                @Override
                public void run() {
                    int menuId = getMenuId();
                    Log.e("---pl----", "anInt: " + menuId);
                    commentList = Http_comments.getcomments(menuId);
                    Log.e("---commentList----", "commentList: " + commentList.size());
                    GetDrawable getdrawable = new GetDrawable();
                    drawable = getdrawable.getdrawable(spic, CommentPageActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIcon.setImageDrawable(drawable);
                            mListView.setAdapter(new MyAdpater());
                        }
                    });
                }
            }.start();


        }
    }

    private int getMenuId() {
        //获取传递管理的菜品id
        Intent intent = getIntent();
        String spic = intent.getStringExtra("spic");
        String menuid = intent.getStringExtra("menuid");
        return  Integer.valueOf(menuid);
    }


    @Override
    public void onClick(View v) {
        flag = false;
        //获取评论文本框内容
        String comment = mComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "请输入评论！", Toast.LENGTH_SHORT).show();
        } else {
            // sendComment发送评论 ,true 成功 ，false 失败
            //menuid  菜谱编号
            //comment  评论内容
            int menuId = getMenuId();
            sendComment(menuId, comment);


        }
    }

    private void sendComment(final int menuid, final String comment) {
        new Thread(){
            @Override
            public void run() {
                String result = Http_postComment.support(menuid, comment);
                Log.e("----------", "run: "+result );
                if (result.equals("ok")){
                    Log.e("----------", "-----------run: "+result );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommentPageActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommentPageActivity.this, "评论失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            }
        }.start();
    }


    public class MyAdpater extends BaseAdapter {

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.comment_page_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mDate = convertView.findViewById(R.id.item_tv_date);
                viewHolder.mTime = convertView.findViewById(R.id.item_tv_time);
                viewHolder.mName = convertView.findViewById(R.id.item_tv_name);
                viewHolder.mComment = convertView.findViewById(R.id.item_tv_comment);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //首页评论列表
            Comment comment = (com.example.geq.caipudemo.vo.Comment) getItem(position);
            //评论日期
            String year = (comment.getYear() == null ? "2018" : comment.getYear()+"年");
            String month = (comment.getMonth() == null ? "12" : comment.getMonth()+"月");
            String day = (comment.getDay() == null ? "1" : comment.getDay()+"日");
            viewHolder.mDate.setText(year+month+day);
            //评论时间
            viewHolder.mTime.setText(comment.getHours() == null ? "10:12 TM" : comment.getHours()+" TM");
            //评论id
            viewHolder.mName.setText(comment.getTime()== null ? "Mary Cho" : comment.getTime());
            //评论内容
            viewHolder.mComment.setText(comment.getContent() == null ? "好吃好吃！" : comment.getContent());

            return convertView;
        }
    }

    public class ViewHolder {
        TextView mDate, mTime, mName, mComment;
    }

}
