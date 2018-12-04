package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.geq.caipudemo.db.Constants;
import com.example.geq.caipudemo.db.InternetUtils;
import com.example.geq.caipudemo.db.Recipedao;
import com.example.geq.caipudemo.db.SharedPreferencesUtils;
import com.example.geq.caipudemo.tool.GetDrawable;
import com.example.geq.caipudemo.tool.Http_menuDetail;
import com.example.geq.caipudemo.tool.Http_support;
import com.example.geq.caipudemo.vo.MenuDetail;
import com.example.geq.caipudemo.vo.Step;

import java.util.List;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;

public class MyCollectInfoActivity extends Activity implements View.OnClickListener {

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
    private List<Step> stepList;

    private Recipedao recipedao;
    private MenuDetail menuDetai2;
    private MyAdpater myAdpater;
    private String spic;
    private boolean flag;
    private MenuDetail menuDetail;
    private String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_infos);
        menuId = getMenuId();
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
        //收藏的点击事件
        mCollect.setOnClickListener(this);
    }

    //初始化数据，展示数据
    private void initData() {

        //收藏按钮初始化图标
        setCollectIcon(menuId);
        //获取传递管理的菜品id
        final String menuId = getMenuId();
        Log.e(TAG, "menuId: "+menuId );
        if (!TextUtils.isEmpty(menuId)) {
            new Thread() {
                @Override
                public void run() {
                    recipedao = Recipedao.getRecipedao(MyCollectInfoActivity.this);
                    menuDetail = recipedao.select_collect(menuId);
                    if (menuDetail != null) {
                        //菜谱简介
                        showCollectData();
                        //条目步骤
                        stepList = menuDetail.getSteps();
                        if (stepList!=null){
                            mLstview.setAdapter(new MyAdpater());
                        }
                    }
                }
            }.start();
        }

    }
    //获取菜品id
    private String  getMenuId() {
        Intent intent1 = getIntent();
        return intent1.getStringExtra("menuid");
    }




    //加载数据库操作
    public void showCollectData() {
        mName.setText(menuDetail.getMenuname());
        String spic = menuDetail.getSpic();
        //getdrawable getdrawable = new getdrawable();
        //Drawable drawable = getdrawable.getdrawable(spic, MyCollectInfoActivity.this);
        //mIcon.setImageDrawable(drawable);
        mType.setText("ZXS");
        mInfo.setText(menuDetail.getAbstracts());
        mFoods.setText(menuDetail.getMainmaterial());
    }




    //菜单详情设置
    private void setMenuInfo() {
        mName.setText(menuDetail.getMenuname());

        spic = menuDetail.getSpic();
        GetDrawable getdrawable = new GetDrawable();
        Drawable drawable = getdrawable.getdrawable(spic, MyCollectInfoActivity.this);
        mIcon.setImageDrawable(drawable);
        mType.setText("www");
        mInfo.setText(menuDetail.getAbstracts());
        mFoods.setText(menuDetail.getMainmaterial());
    }


    //评论点击事件

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dishesinfos_iv_evaluate:
                //评论跳转
                Intent intent = new Intent(this, CommentPageActivity.class);
                Intent intent2 = getIntent();
                String menuid = intent2.getStringExtra("menuid");
                Log.e(TAG, "onClick: ===============" + menuid);
                intent.putExtra("spic", spic);
                intent.putExtra("menuid", menuid);
                startActivity(intent);
                break;
            case R.id.dishesinfos_iv_like:
                //喜欢
                boolean like = SharedPreferencesUtils.getBoolean(getApplicationContext(), Constants.LIKE, true);
                if (like) {
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(), Constants.LIKE, false);
                    new Thread() {
                        @Override
                        public void run() {
                            String menuId = getMenuId();
                            final String result = Http_support.support(Integer.parseInt(menuId), "yes");
                            Log.e("----------", "onClick: " + result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.equals("ok")) {
                                        //mLike.setImageResource(R.drawable.like);
                                        mLike.setBackgroundResource(R.drawable.like);
                                        mUnlike.setBackgroundResource(R.drawable.notlike);
                                    }
                                }
                            });
                        }
                    }.start();
                } else {
                    mLike.setBackgroundResource(R.drawable.notlike);
                    mUnlike.setBackgroundResource(R.drawable.notlike);
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(), Constants.LIKE, true);
                }


                break;
            case R.id.dishesinfos_iv_unlike:
                //不喜欢
                boolean notlike = SharedPreferencesUtils.getBoolean(getApplicationContext(), Constants.UNLIKE, true);
                if (notlike) {
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(), Constants.UNLIKE, false);
                    new Thread() {
                        @Override
                        public void run() {
                            String menuId = getMenuId();
                            final String result = Http_support.support(Integer.parseInt(menuId), "no");
                            Log.e("----------", "onClick: " + result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.equals("ok")) {
                                        mUnlike.setBackgroundResource(R.drawable.like);
                                        mLike.setBackgroundResource(R.drawable.notlike);
                                    }
                                }
                            });
                        }
                    }.start();
                } else {
                    SharedPreferencesUtils.saveBoolean(getApplicationContext(), Constants.UNLIKE, true);
                    mLike.setBackgroundResource(R.drawable.notlike);
                    mUnlike.setBackgroundResource(R.drawable.notlike);
                }
                break;
            case R.id.dishesinfos_iv_collect://收藏
                //判此菜品是否收藏
                // setCollectIcon(menuid1);
                if (flag) {
                    //收藏 -- 不收藏
                    //删除收藏菜品信息
                    Log.e(TAG, "onClick: " + "取消收藏操作");
                    new Thread() {
                        @Override
                        public void run() {

                            Boolean aBoolean = recipedao.delete_collect(menuId);
                            Log.e(TAG, "onClick: 菜品编号是：" + menuId + "   数据库删除: " + aBoolean);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    flag = false;
                                    mCollect.setImageResource(R.drawable.notlike);
                                    Toast.makeText(MyCollectInfoActivity.this, "已取消收藏！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.start();
                } else {
                    //不收藏 ---收藏
                    //添加菜品信息到收藏
                    Log.e(TAG, "onClick: " + "开始收藏操作");

                    new Thread() {
                        @Override
                        public void run() {
                            final boolean innest_collect = recipedao.innest_collect(menuDetail);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (innest_collect) {
                                        mCollect.setImageResource(R.drawable.like);
                                        flag = true;
                                        Toast.makeText(MyCollectInfoActivity.this, "数据收藏成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mCollect.setImageResource(R.drawable.notlike);
                                        Toast.makeText(MyCollectInfoActivity.this, "数据收藏失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }.start();
                }
                break;
        }
    }


    //收藏按钮初始化操作
    private void setCollectIcon(final String menuid) {
        //dao数据操作
        recipedao = Recipedao.getRecipedao(MyCollectInfoActivity.this);
        //询collect收藏表其中的一条信息
        new Thread() {
            @Override
            public void run() {
                menuDetai2 = recipedao.select_collect(menuid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (menuDetai2 == null) {
                            //空，数据库没有查询到收藏信息
                            mCollect.setImageResource(R.drawable.notlike);
                            Toast.makeText(MyCollectInfoActivity.this, "未收藏！", Toast.LENGTH_SHORT).show();
                            flag = false;
                        } else {
                            //查询到收藏信息
                            mCollect.setImageResource(R.drawable.like);
                            Toast.makeText(MyCollectInfoActivity.this, "已收藏！", Toast.LENGTH_SHORT).show();
                            flag = true;
                        }
                    }
                });
            }
        }.start();
    }


    public class MyAdpater extends BaseAdapter {

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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.dishes_infos_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mCode = convertView.findViewById(R.id.item_tv_code);
                viewHolder.mText = convertView.findViewById(R.id.item_tv_text);
                viewHolder.mTime = convertView.findViewById(R.id.item_tv_time2);
                viewHolder.mIcon = convertView.findViewById(R.id.item_lv_icon2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyCollectInfoActivity.ViewHolder) convertView.getTag();
            }
            //步骤信息设置展示
            Step step = (Step) getItem(position);
            viewHolder.mCode.setText("步骤" + step.getStepid());
            viewHolder.mText.setText(step.getDescription());
            viewHolder.mTime.setText("8:33 TM");
            String pic = step.getPic();
//            getdrawable getdrawable = new getdrawable();
//            Drawable drawable = getdrawable.getdrawable(pic, MyCollectInfoActivity.this);
//            viewHolder.mIcon.setImageDrawable(drawable);
            return convertView;
        }
    }

    public class ViewHolder {
        TextView mCode, mText, mTime;
        ImageView mIcon;
    }

    @Override
    protected void onDestroy() {
        setResult(1);
        super.onDestroy();
    }
}
