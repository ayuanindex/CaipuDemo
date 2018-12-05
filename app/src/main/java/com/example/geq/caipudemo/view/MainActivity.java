package com.example.geq.caipudemo.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geq.caipudemo.R;
import com.example.geq.caipudemo.db.InternetUtils;
import com.example.geq.caipudemo.db.Recipedao;
import com.example.geq.caipudemo.tool.GetDrawable;
import com.example.geq.caipudemo.tool.Http_Vegetable;
import com.example.geq.caipudemo.vo.Vegetableinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
	private ProgressDialog progressDialog = null;
	private GridView gv_class;
	private List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
	private ConnectivityManager connectivityManager;
	private NetworkInfo activeNetworkInfo;
	private String TAG = "MainActivity";
	private List<Vegetableinfo> vegetableinfoList;
	private SQLiteDatabase readableDatabase;
	private Recipedao recipedao;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
				case 0:
					if (vegetableinfoList == null) {
						if(recipedao==null){
							recipedao=new Recipedao(getApplicationContext());
						}
						vegetableinfoList=recipedao.select_types();
						if(vegetableinfoList == null) {
							Toast.makeText(MainActivity.this, "获取数据不成功", Toast.LENGTH_SHORT).show();
						}
					}
					MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter();
					gv_class.setAdapter(myGridViewAdapter);

					break;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		/*CustomDialog dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
		//屏幕外点击无效
		dialog.setCancelable(false);
		dialog.show();*/

		initUI();
		connectionJudgment();
	}

	private void connectionJudgment() {
		boolean flag = InternetUtils.isNetWorkAvailable(this);
		if (flag) {
			initData();
		} else {
			Toast.makeText(this, "亲，您的网络没有连接哦", Toast.LENGTH_SHORT).show();
			vegetableinfoList = inDatabase();
			Message message = Message.obtain();
			message.what = 0;
			mHandler.sendMessage(message);
		}
	}


	/**
	 * 从网络中访问数据
	 */
	private void initData() {
		progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "获取数据中...", true);
		new Thread() {
			@Override
			public void run() {
				vegetableinfoList = Http_Vegetable.getVegetable();
				Message message = Message.obtain();
				progressDialog.dismiss();
				message.what = 0;
				mHandler.sendMessage(message);
			}
		}.start();
	}

	private void initListener() {

		if (gv_class != null) {
			gv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (InternetUtils.isNetWorkAvailable(MainActivity.this)) {
						//将id和分类名称传入到下一个页面
						if (vegetableinfoList != null) {
							Intent intent = new Intent(MainActivity.this, ClassNameListActivity.class);
							Log.i(TAG, "集合:" + vegetableinfoList.toString());
							String typeid = vegetableinfoList.get(position).getTypeid();
							String typename = vegetableinfoList.get(position).getTypename();
							intent.putExtra("typeid", typeid);
							intent.putExtra("typename", typename);
							startActivityForResult(intent, 1);
						}
					} else {
						Toast.makeText(MainActivity.this, "亲，您的网络没有连接哦", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:
				initData();
				break;
		}
	}

	private void initUI() {
		recipedao = new Recipedao(this);
		vegetableinfoList = new ArrayList<Vegetableinfo>();

		gv_class = (GridView) findViewById(R.id.gv_class);
		initListener();
	}

	private class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (vegetableinfoList == null) {
				return 10;
			}
			return vegetableinfoList.size();
		}

		@Override
		public Vegetableinfo getItem(int position) {
			return vegetableinfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(MainActivity.this, R.layout.gridview_list, null);
			} else {
				view = convertView;
			}
			TextView tv_des = (TextView) view.findViewById(R.id.tv_des);
			ImageView iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
			if (vegetableinfoList == null) {
				//从数据库中获取数据来显示页面
					anim(view);
					return view;
			}
			onView(position, view, tv_des, iv_logo);
			return view;
		}

		private void onView(int position, final View view, TextView tv_des, final ImageView iv_logo) {
			if (!recipedao.issavetypes()) {
				insertData(position);
			}
			String typename = getItem(position).getTypename();
			tv_des.setText(typename);

			String typepic = getItem(position).getTypepic();
			final Bitmap logo = GetDrawable.getBitmap(typepic, MainActivity.this);

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (logo != null) {
						iv_logo.setImageBitmap(logo);
					} else {
						iv_logo.setImageResource(R.drawable.image_haha);
					}
					anim(view);
				}
			});
		}

		private boolean insertData(int position) {
			if (recipedao != null) {
				//单独将图片文件写道缓存里
				boolean b = recipedao.innest_type(vegetableinfoList.get(position));
				return b;
			}
			return false;
		}
	}

	private void anim(View view) {
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.loding);
		view.startAnimation(animation);
	}

	/**
	 * 在没有网络的情况下，使用数据库里面的数据进行数据展示
	 */
	private List<Vegetableinfo> inDatabase() {
	List<Vegetableinfo> vegetableinfos=new ArrayList<Vegetableinfo>();
				if (recipedao != null) {
					 vegetableinfos = recipedao.select_types();
				} else {
					recipedao = new Recipedao(MainActivity.this);
					vegetableinfos = recipedao.select_types();
				}
		return vegetableinfos;
	}
}