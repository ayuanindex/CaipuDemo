package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.utils.Http_comments;
import com.example.geq.caipudemo.utils.Http_menuDetail;
import com.example.geq.caipudemo.utils.getdrawable;
import com.example.geq.caipudemo.vo.MenuDetail;
import com.example.geq.caipudemo.vo.Step;
import com.example.geq.caipudemo.vo.comment;

import java.util.List;

public class DishesInfosActivity extends Activity  implements View.OnClickListener {

    private TextView mName;
    private ImageView mCollect;
    private ImageView mIcon;
    private TextView mType;
    private TextView mInfo;
    private TextView mFoods;
    private ListView mLstview;
    private ImageView mEvaluate;
    private ImageView mLike;
    private ImageView mUnlike;
    private MenuDetail menuDetail;
    private List<Step>  stepList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_infos);
        initView();
        initData();
    }

    //初始化控件
    private void initView() {
        mName = findViewById(R.id.dishesinfos_tv_name);
        mCollect = findViewById(R.id.dishesinfos_iv_collect);
        mIcon = findViewById(R.id.dishesinfos_iv_icon);
        mType = findViewById(R.id.dishesinfos_tv_type);
        mInfo = findViewById(R.id.dishesinfos_tv_info);
        mFoods = findViewById(R.id.dishesinfos_tv_foods);
        mLstview = findViewById(R.id.dishesinfos_lv_listview);
        mEvaluate = findViewById(R.id.dishesinfos_iv_evaluate);
        mLike = findViewById(R.id.dishesinfos_iv_like);
        mUnlike = findViewById(R.id.dishesinfos_iv_unlike);
        //评论的点击事件
        mEvaluate.setOnClickListener(this);
        //喜欢的点击事件
        mLike.setOnClickListener(this);
        //不喜欢的点击事件
        mUnlike.setOnClickListener(this);
    }

    //初始化数据，展示数据
    private void initData() {
        //获取传递管理的菜品id
        Intent intent = getIntent();
        int menuid = intent.getIntExtra("menuid", 0);
        if (1>0){
            //根据菜品id,获取菜谱信息
            menuDetail = Http_menuDetail.getmenus(menuid);
            stepList = menuDetail.getSteps();
            MyAdpater myAdpater = new MyAdpater();
            //菜单详情设置
            setMenuInfo();
            //展示菜品步骤
            mLstview.setAdapter(myAdpater);
        }
    }

    //菜单详情设置
    private void setMenuInfo() {
        mName.setText(menuDetail.getMenuname());
        String spic = menuDetail.getSpic();
        getdrawable getdrawable = new getdrawable();
        Drawable drawable = getdrawable.getdrawable(spic, DishesInfosActivity.this);
        mIcon.setImageDrawable(drawable);
        mType.setText("");
        mInfo.setText(menuDetail.getAbstracts());
        mFoods.setText(menuDetail.getMainmaterial());
    }


    //评论点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dishesinfos_iv_evaluate:
                //评论跳转
                Intent intent = new Intent(this,CommentPageActivity.class);
                startActivity(intent);
                break;
            case R.id.dishesinfos_iv_like:
                //喜欢
                break;
            case R.id.dishesinfos_iv_unlike:
                //不喜欢
                break;
        }

    }



    public class MyAdpater extends BaseAdapter{

        @Override
        public int getCount() {
            return stepList.size();
        }

        @Override
        public Object getItem(int position) {
            return stepList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder ;
            if (convertView==null){
                convertView  = View.inflate(getApplicationContext(), R.layout.dishes_infos_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mCode = convertView.findViewById(R.id.item_tv_code);
                viewHolder.mText = convertView.findViewById(R.id.item_tv_text);
                viewHolder.mTime = convertView.findViewById(R.id.item_tv_time2);
                viewHolder.mIcon = convertView.findViewById(R.id.item_lv_icon2);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //步骤信息设置展示
            Step step = (Step) getItem(position);
            viewHolder.mCode.setText(step.getStepid());
            viewHolder.mText.setText(step.getDescription());
            viewHolder.mTime.setText("8:33 TM");
            String pic = step.getPic();
            getdrawable getdrawable = new getdrawable();
            Drawable drawable = getdrawable.getdrawable(pic, DishesInfosActivity.this);
            viewHolder.mIcon.setImageDrawable(drawable);
            return convertView;
        }
    }

    public class ViewHolder {
        TextView mCode ,mText,mTime;
        ImageView mIcon;
    }

}
