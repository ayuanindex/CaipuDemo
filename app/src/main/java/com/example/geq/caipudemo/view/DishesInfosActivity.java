package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.db.Constants;
import com.example.geq.caipudemo.db.InternetUtils;
import com.example.geq.caipudemo.db.Recipedao;
import com.example.geq.caipudemo.db.SharedPreferencesUtils;
import com.example.geq.caipudemo.tool.Http_menuDetail;
import com.example.geq.caipudemo.tool.Http_support;
import com.example.geq.caipudemo.tool.getdrawable;
import com.example.geq.caipudemo.vo.MenuDetail;
import com.example.geq.caipudemo.vo.Step;


import java.util.List;

public class DishesInfosActivity extends Activity  implements View.OnClickListener {

    private TextView mName;
    private ImageView mCollect;
    private ImageView mIcon;
    private TextView mType;
    private TextView mInfo;
    private TextView mFoods;
    private ListView mLstview;
    private Button mEvaluate;
    private Button mLike;
    private Button mUnlike;
    private MenuDetail menuDetail;
    private List<Step>  stepList ;
    private  String menuid1;
    private  Recipedao recipedao;
    private MenuDetail menuDetai2;
    private  MyAdpater myAdpater;

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
        final int menuid = intent.getIntExtra("menuid", 1);
        String menuName = intent.getStringExtra("menuname");
        //收藏按钮初始化图标
        setCollectIcon(menuid1);
        if (menuid>=1){
            //是否联网
            boolean netWorkAvailable = InternetUtils.isNetWorkAvailable(DishesInfosActivity.this);
            Log.e("----------", "网络: "+netWorkAvailable );
            if (netWorkAvailable){
                new Thread(){
                    @Override
                    public void run() {
                        //根据菜品id,获取菜谱信息
                        menuDetail = Http_menuDetail.getmenus(1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (menuDetail!=null){
                                    menuid1 = menuDetail.getMenuid();
                                    stepList = menuDetail.getSteps();
                                    myAdpater = new MyAdpater();
                                    //菜单详情设置
                                    setMenuInfo();
                                    //展示菜品步骤
                                    mLstview.setAdapter(myAdpater);
                                }
                            }
                        });
                    }
                }.start();
            }
        }

        boolean like = SharedPreferencesUtils.getBoolean(getApplicationContext(), Constants.LIKE, true);
        if (like){
            mLike.setBackgroundResource(R.drawable.like);
            mUnlike.setBackgroundResource(R.drawable.notlike);
        }else{
            mLike.setBackgroundResource(R.drawable.notlike);
            mUnlike.setBackgroundResource(R.drawable.like);
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

                boolean like = SharedPreferencesUtils.getBoolean(getApplicationContext(), Constants.LIKE, true);
                if (like){
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(),Constants.LIKE,false);
                    new Thread(){
                        @Override
                        public void run() {
                            final String result = Http_support.support(Integer.parseInt(menuid1), "yes");
                            Log.e("----------","onClick: "+result );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.equals("ok")){
                                        //mLike.setImageResource(R.drawable.like);
                                        mLike.setBackgroundResource(R.drawable.like);
                                        mUnlike.setBackgroundResource(R.drawable.notlike);
                                    }
                                }
                            });
                        }
                    }.start();
                }else{
                    mLike.setBackgroundResource(R.drawable.notlike);
                    mUnlike.setBackgroundResource(R.drawable.notlike);
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(),Constants.LIKE,true);
                }


                break;
            case R.id.dishesinfos_iv_unlike:
                //不喜欢
                boolean notlike = SharedPreferencesUtils.getBoolean(getApplicationContext(), Constants.LIKE, true);
                if (notlike){
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(),Constants.LIKE,false);
                    mLike.setBackgroundResource(R.drawable.notlike);
                    mUnlike.setBackgroundResource(R.drawable.notlike);
                }else{
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(),Constants.LIKE,true);
                    new Thread(){
                        @Override
                        public void run() {
                            final String result = Http_support.support(Integer.parseInt(menuid1), "no");
                            Log.e("----------","onClick: "+result );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.equals("ok")){
                                        mUnlike.setBackgroundResource(R.drawable.like);
                                        mLike.setBackgroundResource(R.drawable.notlike);
                                     // mLike.setImageResource(R.drawable.notlike);

                                    }
                                }
                            });
                        }
                    }.start();
                }
                break;
            case R.id.dishesinfos_iv_collect://收藏
                //判此菜品是否收藏
                boolean isCollect = setCollectIcon(menuid1);
                if (isCollect){
                    //收藏 -- 不收藏
                    //删除收藏菜品信息
                    recipedao.delete_collect(menuid1);
                }else{
                    //不收藏 ---收藏
                    //添加菜品信息到收藏
                    recipedao.innest_collect(menuDetail);
                }
                break;
        }
    }


    //收藏按钮初始化操作
    private boolean setCollectIcon(String menuid) {
        //dao数据操作
        recipedao = Recipedao.getRecipedao(DishesInfosActivity.this);
        //询collect收藏表其中的一条信息
        menuDetai2 = recipedao.select_collect(menuid);
        if (menuDetail==null){
            //空，数据库灭有查询到收藏信息
            mCollect.setImageResource(R.drawable.notlike);
            return  false;
        }else{
            //查询到收藏信息
            mCollect.setImageResource(R.drawable.like);
            return  true;
        }
    }


    //加载数据库操作
    public void showCollectData(){
        stepList = menuDetai2.getSteps();
        mName.setText(menuDetai2.getMenuname());
        String spic = menuDetai2.getSpic();
        getdrawable getdrawable = new getdrawable();
        Drawable drawable = getdrawable.getdrawable(spic, DishesInfosActivity.this);
        mIcon.setImageDrawable(drawable);
        mType.setText("");
        mInfo.setText(menuDetai2.getAbstracts());
        mFoods.setText(menuDetai2.getMainmaterial());

        myAdpater.notifyDataSetChanged();
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
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //步骤信息设置展示
            Step step = (Step) getItem(position);
            viewHolder.mCode.setText("步骤"+step.getStepid());
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
