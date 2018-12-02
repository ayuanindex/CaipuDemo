package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.utils.Http_comments;
import com.example.geq.caipudemo.vo.comment;

import java.util.List;


public class CommentPageActivity extends Activity implements View.OnClickListener {
    private TextView mName;
    private ImageView  mIcon;
    private ListView mListView;
    private TextView mComment;
    private  Button mSend;
    private List<comment> commentList;
    //评论结合
    private List list;


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
    }

    //初始化数据，展示数据
    private void initData() {
        //获取传递管理的菜品id
        Intent intent = getIntent();
        int menuid = intent.getIntExtra("menuid", 0);
        if (1>0){
            commentList = Http_comments.getcomments(menuid);
        }
    }

    @Override
    public void onClick(View v) {
        //获取评论文本框内容
        String comment = mComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)){
            Toast.makeText(this, "请输入评论！", Toast.LENGTH_SHORT).show();
        }else{
            // sendComment发送评论 ,true 成功 ，false 失败
            //menuid  菜谱编号
            //comment  评论内容
            boolean result = sendComment(1, comment);
            if(result){
                Toast.makeText(this, "评论成功！", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "评论失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean sendComment(int menuid, String comment) {

        return true;
    }



    public class MyAdpater extends BaseAdapter{

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
            ViewHolder viewHolder ;
            if (convertView==null){
                convertView  = View.inflate(getApplicationContext(), R.layout.comment_page_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mDate = convertView.findViewById(R.id.item_tv_date);
                viewHolder.mTime = convertView.findViewById(R.id.item_tv_time);
                viewHolder.mName = convertView.findViewById(R.id.item_tv_name);
                viewHolder.mComment = convertView.findViewById(R.id.item_tv_comment);
                viewHolder.mIcon = convertView.findViewById(R.id.item_lv_icon);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Object item = getItem(position);
            viewHolder.mDate.setText(item.toString());
            viewHolder.mTime.setText(item.toString());
            viewHolder.mName.setText(item.toString());
            viewHolder.mDate.setText(item.toString());


            return convertView;
        }
    }

    public class ViewHolder {
        TextView mDate ,mTime,mName,mComment;
        ImageView mIcon;
    }

}
