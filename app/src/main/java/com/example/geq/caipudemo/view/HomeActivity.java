package com.example.geq.caipudemo.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geq.caipudemo.R;


public class HomeActivity extends Activity implements View.OnClickListener {

	private GridView gv_home;
	private String[] mtitle;
	private int[] mmipmap;
	private Button bt_menu;
	private PopupWindow window;
	private Intent intent;
	private Toast toast;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home1);
		toast = Toast.makeText(HomeActivity.this, "该功能尚未开放", Toast.LENGTH_SHORT);
		initui();
		initdate();
	}

	private void initui() {
		gv_home = findViewById(R.id.gv_home);
		bt_menu = findViewById(R.id.bt_menu);
		bt_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (window == null) {
					View layout = getLayoutInflater().inflate(R.layout.menuitem, null);
					layout.findViewById(R.id.tv_menu1).setOnClickListener(HomeActivity.this);
					layout.findViewById(R.id.tv_menu2).setOnClickListener(HomeActivity.this);
					layout.findViewById(R.id.tv_menu3).setOnClickListener(HomeActivity.this);
					// 创建PopuWindow显示菜单
					window = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					window.setBackgroundDrawable(new ColorDrawable());
					window.setOutsideTouchable(true);;
					window.setFocusable(true);
					window.showAsDropDown(bt_menu, 0, 0);
				} else {
					window.dismiss();
					window = null;
				}
			}
		});
	}

	private void initdate() {
		mtitle = new String[]{"热门分类", "流行食材", "常见功效", "菜系", "菜品", "加工工艺", "厨房用具", "口味", "场景"};
		mmipmap = new int[]{R.drawable.menu1, R.drawable.menu2, R.drawable.menu3,
				R.drawable.menu4, R.drawable.menu5, R.drawable.menu6,
				R.drawable.menu7, R.drawable.menu8, R.drawable.menu9
		};
		gv_home.setAdapter(new Myadapt());
		gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
                        /*
                        /需要修改到分类的Activity
                         */
						intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
						break;
					case 1:
						if (toast != null) {
							toast.show();
						}
						break;
					case 2:
						if (toast != null) {
							toast.show();
						}
						break;
					case 3:
						if (toast != null) {
							toast.show();
						}
						break;
					case 4:
						if (toast != null) {
							toast.show();
						}
						break;
					case 5:
						if (toast != null) {
							toast.show();
						}
						break;
					case 6:
						if (toast != null) {
							toast.show();
						}
						break;
					case 7:
						if (toast != null) {
							toast.show();
						}
						break;
					case 8:
						if (toast != null) {
							toast.show();
						}
						break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//我的收藏
			case R.id.tv_menu1:
				intent = new Intent(getApplicationContext(), CollectActivity.class);
				startActivity(intent);
				window.dismiss();
				window = null;
				break;
			//使用帮助
			case R.id.tv_menu2:
				intent = new Intent(getApplicationContext(), MyCollectInfoActivity.class);
				startActivity(intent);
				window.dismiss();
				window = null;
				break;
			//关于我们
			case R.id.tv_menu3:
				intent = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(intent);
				window.dismiss();
				window = null;
				break;
		}
	}

	private class Myadapt extends BaseAdapter {
		@Override
		public int getCount() {
			return mtitle.length;
		}

		@Override
		public Object getItem(int position) {
			return mtitle[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.home_mune, null);
			ImageView iv_menu = view.findViewById(R.id.iv_menu);
			iv_menu.setBackgroundResource(mmipmap[position]);
			TextView tv_mettle = view.findViewById(R.id.tv_menutitle);
			tv_mettle.setText(mtitle[position]);
			return view;
		}
	}


}
