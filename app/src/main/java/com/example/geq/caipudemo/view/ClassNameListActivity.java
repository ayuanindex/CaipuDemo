package com.example.geq.caipudemo.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.db.InternetUtils;
import com.example.geq.caipudemo.db.Recipedao;
import com.example.geq.caipudemo.tool.GetDrawable;
import com.example.geq.caipudemo.tool.Http_menus;
import com.example.geq.caipudemo.tool.LogUtil;
import com.example.geq.caipudemo.vo.Menuinfo;
import com.example.geq.caipudemo.vo.Request_menu;

import java.util.ArrayList;
import java.util.List;

public class ClassNameListActivity extends AppCompatActivity {

	private static final String TAG = "ClassNameListActivity";
	private TextView tv_class_name;
	private ListView lv_menu_item;
	private String typename;
	private String typeid;
	private List<Menuinfo> getmenus = null;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
				case 1:
					MyMenuItemAdapter myMenuItemAdapter = new MyMenuItemAdapter();
					lv_menu_item.setAdapter(myMenuItemAdapter);
					break;
			}
		}
	};
	private Request_menu request_menu;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class);

		initUI();

		boolean netWorkAvailable = InternetUtils.isNetWorkAvailable(this);
		if (netWorkAvailable) {
			initData();
		} else {
			//从数据里面获取数据
			Recipedao recipedao = new Recipedao(this);
			recipedao.issavetypes();
		}
	}


	private void initUI() {
		getmenus = new ArrayList<Menuinfo>();
		//取得从上一个页面传过来的参数(用于数据获取和标题显示)
		typeid = getIntent().getStringExtra("typeid");
		typename = getIntent().getStringExtra("typename");
		LogUtil.e(typeid);
		tv_class_name = (TextView) findViewById(R.id.tv_class_name);
		lv_menu_item = (ListView) findViewById(R.id.lv_menu_item);

		tv_class_name.setText(typename);

		initListener();
	}

	private void initListener() {
		if (lv_menu_item != null) {
			lv_menu_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getApplicationContext(), DishesInfosActivity.class);
					if (getmenus != null) {
						intent.putExtra("menuid", getmenus.get(position).getMenuid());
						Log.e(TAG, "onClick: ========9=======" + getmenus.get(position).getMenuid());
						intent.putExtra("menuname", getmenus.get(position).getMenuname());
					}
					startActivity(intent);
				}
			});
		}
	}

	/**
	 * 从网络上获取数据，放到数据库中，图片加载到
	 */
	private void initData() {
		/*getmenus.clear();*/
		if (typeid != null) {
			request_menu = new Request_menu(Integer.parseInt(typeid), 1, 20);
			new Thread() {
				@Override
				public void run() {
					super.run();
					getmenus = Http_menus.getmenus(request_menu);
					Message message = Message.obtain();
					message.what = 1;
					mHandler.sendMessage(message);
				}
			}.start();
		}
	}

	private class MyMenuItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (getmenus == null) {
				return 10;
			}
			return getmenus.size();
		}

		@Override
		public Menuinfo getItem(int position) {
			return getmenus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final View view;
			if (convertView == null) {
				view = View.inflate(ClassNameListActivity.this, R.layout.menu_item, null);
			} else {
				view = convertView;
			}

			if (getmenus == null) {
				return view;
			}

			ImageView iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			RatingBar rb_pinfen = (RatingBar) view.findViewById(R.id.rb_pinfen);

			int likes = Integer.parseInt(getItem(position).getLikes());
			int notLikes = Integer.parseInt(getItem(position).getNotlikes());
			rb_pinfen.setRating((float) ((float) (likes / notLikes * 1.5) - 1.23 - 2));

			GetDrawable getDrawable = new GetDrawable();
			String spic = getItem(position).getSpic();
			Drawable getdrawable = getDrawable.getdrawable(spic, ClassNameListActivity.this);
			iv_logo.setImageDrawable(getdrawable);

			String menuname = getItem(position).getMenuname();
			tv_name.setText(menuname);

			Animation animation = AnimationUtils.loadAnimation(ClassNameListActivity.this, R.anim.loding);
			view.startAnimation(animation);
			return view;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(1);
	}
}
