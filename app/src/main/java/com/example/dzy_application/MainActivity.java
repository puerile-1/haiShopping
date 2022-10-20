package com.example.dzy_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dzy_application.database.MySQLConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private RadioButton tab1,tab2,tab3,tab4;  //四个单选按钮
    private List<View> mViews;   //存放视图
    List<msg> msgList = new ArrayList<msg>();

    private Map<Integer, Boolean> map = new HashMap<>();
    private static Connection con = null;
    private static PreparedStatement pst = null;
    private int space=5;
    List<gwc> comList = new ArrayList<gwc>();
    commodity selectgwc = new commodity();
    MainActivity.HomeAdapter homeAdapter=new MainActivity.HomeAdapter();//创建适配器对象
    private int[] com_imgs={R.drawable.com_2,R.drawable.com_4,R.drawable.gwc4,R.drawable.gwc3,R.drawable.com_3};
    private OrdersActivity.OnItemClickListener onItemClickListener;
    private boolean[] checked1 = {false,false,false,false};
    private boolean[] checked2 = {false,false,false,false};
    private Handler handler = new Handler() {
        @Override
        //书写handleMessage方法，接受信息
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                comList.addAll((List) msg.obj);
                homeAdapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();//初始化数据
        //对单选按钮进行监听，选中、未选中
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_sy:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_msg:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_gwc:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_me:
                        mViewPager.setCurrentItem(3);
                        break;
                }
            }
        });

    }

    private void initView() {
        //初始化控件
        mViewPager=findViewById(R.id.viewpager);
        mRadioGroup=findViewById(R.id.rg_tab);
        //初始化导航栏按钮
        tab1=findViewById(R.id.rb_sy);
        tab2=findViewById(R.id.rb_msg);
        tab3=findViewById(R.id.rb_gwc);
        tab4=findViewById(R.id.rb_me);
        //导航栏图标大小控制
        final Drawable drawable1 = getResources().getDrawable(R.drawable.sy_drawable);
        drawable1.setBounds(0, 0, 60, 60);
        tab1.setCompoundDrawables(null, drawable1, null, null);
        final Drawable drawable2 = getResources().getDrawable(R.drawable.msg_drawable);
        drawable2.setBounds(0, 0, 60, 60);
        tab2.setCompoundDrawables(null, drawable2, null, null);
        final Drawable drawable3 = getResources().getDrawable(R.drawable.gwc_drawable);
        drawable3.setBounds(0, 0, 60, 60);
        tab3.setCompoundDrawables(null, drawable3, null, null);
        final Drawable drawable4 = getResources().getDrawable(R.drawable.me_drawable);
        drawable4.setBounds(0, 0, 60, 60);
        tab4.setCompoundDrawables(null, drawable4, null, null);

        mViews=new ArrayList<View>();//加载，添加视图
        //首页视图
        View view = getLayoutInflater().inflate(R.layout.sy,null);
        mViews.add(view);
        EditText editText1 = (EditText) view.findViewById(R.id.edit1);
        editText1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,searchActivity.class);
                startActivity(intent);
            }
            });
        Button btn = (Button)view.findViewById(R.id.cloth_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ClothActivity.class);
                startActivity(intent);
            }
        });
        Button shoes_btn = (Button)view.findViewById(R.id.shoes_btn);
        shoes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ShoesActivity.class);
                startActivity(intent);
            }
        });
        Button watch_btn = (Button)view.findViewById(R.id.watch_btn);
        watch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, WatchActivity.class);
                startActivity(intent);
            }
        });
        Button bag_btn = (Button)view.findViewById(R.id.bag_btn);
        bag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, BagActivity.class);
                startActivity(intent);
            }
        });
        //消息视图
        View view1 = getLayoutInflater().inflate(R.layout.msg,null);
        mViews.add(view1);
        //添加listview
        ListView listView1 = (ListView) view1.findViewById(R.id.msg_listview);
        initFruits();//传入listview数据
        msg_adapter adapter = new msg_adapter(this, R.layout.msg_item, msgList);//自定义listview适配器
        listView1.setAdapter(adapter);
        //mViews.add(LayoutInflater.from(this).inflate(R.layout.msg,null));
        //购物车视图
        View view2 = getLayoutInflater().inflate(R.layout.gwc,null);
        mViews.add(view2);


        MainActivity.Threads_readSQL threads_readSQL = new MainActivity.Threads_readSQL();
        threads_readSQL.start();

        RecyclerView recyclerView1=view2.findViewById(R.id.com_item);//创建recyclerView对象，并初始化其xml文件
        recyclerView1.setLayoutManager(new GridLayoutManager(this,1));//设置为表格布局，列数为2（这个是最主要的，就是这个来展示陈列式布局）
        recyclerView1.addItemDecoration(new MainActivity.space_item(space));//给recycleView添加item的间距
        recyclerView1.setAdapter(homeAdapter);//将适配器添加到recyclerView

        TextView money = (TextView) view2.findViewById(R.id.money);
        money.setText(selectgwc.getCom_price());

        double[] allmoney = new double[1];
        Button jies = (Button) view2.findViewById(R.id.jies);
        jies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, purchase_Activity.class);
                startActivity(intent);

            }
        });
        Button button1 = (Button) view2.findViewById(R.id.quanx);
        int[] flag = {0};
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if(flag[0] == 0 ){
                            for (int i = 0; i <comList.size() ; i++) {
                                comList.get(i).setChecked1(true);
                                comList.get(i).setChecked2(true);
                                allmoney[0] = comList.get(i).getPrice()+ allmoney[0];
                            }
                            money.setText(String.valueOf(allmoney[0]));
                            flag[0] = 1;
                            homeAdapter.notifyDataSetChanged();
                        }
                        else if(flag[0] == 1 ){
                            for (int i = 0; i <comList.size() ; i++) {
                                comList.get(i).setChecked1(false);
                                comList.get(i).setChecked2(false);

                            }
                            allmoney[0]=0;
                            money.setText(String.valueOf(allmoney[0]));
                            flag[0] = 0;
                            homeAdapter.notifyDataSetChanged();
                        }



            }
        });

        /*ListView listView2 = (ListView) view2.findViewById(R.id.gwc_listview);
        initgwcs();//传入listview数据
        gwc_adapter gwc_adapter = new gwc_adapter(this, R.layout.gwc_item, gwcList);//自定义listview适配器
        listView2.setAdapter(gwc_adapter);
        TextView money = (TextView) view2.findViewById(R.id.money);
        final double[] allmoney = new double[1];
        Button button1 = (Button) view2.findViewById(R.id.quanx);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //设置全部选中
                    case R.id.quanx:
                        for (int i = 0; i <gwcList.size() ; i++) {
                            gwcList.get(i).checked1=true;
                            gwcList.get(i).checked2=true;
                            allmoney[0] = gwcList.get(i).getPrice()+ allmoney[0];
                        }
                        money.setText(String.valueOf(allmoney[0]));
                        gwc_adapter.notifyDataSetChanged();
                        break;
                    //取消全部选中
                }

            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录选中item
                boolean checked = gwcList.get(position).checked1;
                if (!checked) {
                    gwcList.get(position).checked1 = true;
                }else {
                    gwcList.get(position).checked1 = false;
                }
                gwc_adapter.notifyDataSetChanged();
            }
        });

*/
        //我的视图
        View view3 = getLayoutInflater().inflate(R.layout.me,null);
        mViews.add(view3);
        Button orders = (Button) view3.findViewById(R.id.see_dd);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });
        //mViews.add(LayoutInflater.from(this).inflate(R.layout.me,null));

        mViewPager.setAdapter(new MyViewPagerAdapter());//设置一个适配器
        //对viewpager监听，让分页和底部图标保持一起滑动
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override   //让viewpager滑动的时候，下面的图标跟着变动
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tab1.setChecked(true);
                        tab2.setChecked(false);
                        tab3.setChecked(false);
                        tab4.setChecked(false);
                        break;
                    case 1:
                        tab1.setChecked(false);
                        tab2.setChecked(true);
                        tab3.setChecked(false);
                        tab4.setChecked(false);
                        break;
                    case 2:
                        tab1.setChecked(false);
                        tab2.setChecked(false);
                        tab3.setChecked(true);
                        tab4.setChecked(false);
                        break;
                    case 3:
                        tab1.setChecked(false);
                        tab2.setChecked(false);
                        tab3.setChecked(false);
                        tab4.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initFruits() {
        msg logistics = new msg(R.drawable.logistics,"交易物流","您的包裹开始运输","17:13");
        msgList.add(logistics);
        msg activity = new msg(R.drawable.activity,"活动优惠","好货限时买二免一，全场低至一元","12:15");
        msgList.add(activity);
        msg milk = new msg(R.drawable.milk,"新农旗舰店","好的，谢谢","9:15");
        msgList.add(milk);
        msg move = new msg(R.drawable.zgmove,"中国移动官方旗舰店","您已成功充值100元","7:15");
        msgList.add(move);
        msg server = new msg(R.drawable.server,"服务通知","您有28个淘金币可领取","6:15");
        msgList.add(server);
        msg msg = new msg(R.drawable.xxtz,"消息号通知","科颜氏必囤好物抢购中","昨日");
        msgList.add(msg);
    }
    /*private void initgwcs(){
        gwc gwc1 = new gwc("简佩旗舰店","雾霾蓝马里奥卫衣男秋",R.drawable.syt3,88.88,"* 1",false,false);
        gwcList.add(gwc1);
        gwc gwc2 = new gwc("禾子先生男装","禾子先生套头衫ins情侣",R.drawable.gwc1,108,"* 2",false,false);
        gwcList.add(gwc2);
        gwc gwc3 = new gwc("珂润官方旗舰店","珂润 润浸保湿洁颜 泡沫",R.drawable.gwc4,108,"* 1",false,false);
        gwcList.add(gwc3);
        gwc gwc4 = new gwc("浪漫浅水湾","加油鸭 书包挂件 毛绒小",R.drawable.gwc3,12.8,"* 1",false,false);
        gwcList.add(gwc4);
    }*/




    class Threads_readSQL extends Thread {
        @Override
        public void run() {

            try {
                con = MySQLConnections.getConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String sql ="Select store_name,com_name,com_num,com_price,checked1*1 AS checked1,checked2*1 as checked2 from gwc";
                if (con!=null) {

                    pst = con.prepareStatement(sql);
                    // 关闭事务自动提交 ,这一行必须加上
                    con.setAutoCommit(false);
                    ResultSet rs = pst.executeQuery();//创建数据对象
                    //清空上次发送的信息
                    List<gwc> comList1 = new ArrayList<gwc>();
                    while (rs.next()) {
                        gwc gwc = new gwc();
                        gwc.setDpname(rs.getString(1));
                        gwc.setSpname(rs.getString(2));
                        gwc.setNum(rs.getInt(3));
                        gwc.setPrice(rs.getDouble(4));
                        gwc.setChecked1(rs.getBoolean(5));
                        gwc.setChecked2(rs.getBoolean(6));
                        comList1.add(gwc);
                    }
                    pst.execute();
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = comList1;
                    handler.sendMessage(message);

                    con.commit();
                    rs.close();
                    pst.close();

                }

            }catch (Exception e){
                System.out.println(e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this,"请输入正确的语句",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<MainActivity.HomeAdapter.MyViewHolder>  {
        private View itemView;
        @NonNull
        @Override
        //加载布局文件并返回MyViewHolder对象
        public MainActivity.HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //创建view对象
            itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.gwc_item,parent,false);
            //创建MyViewHolder对象
            MainActivity.HomeAdapter.MyViewHolder myViewHolder=new MainActivity.HomeAdapter.MyViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        //获取数据并显示到对应控件
        public void onBindViewHolder(final MainActivity.HomeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            //给我的四个控件获取一下数据，注意不同类型调用不同的方法，设置图片用setImageResource（），设置文字用setText（）
            holder.com_img.setImageResource(com_imgs[position]);
            holder.com_name.setText(comList.get(position).getSpname());
            holder.com_num.setText(String.valueOf(comList.get(position).getNum()));
            holder.com_price.setText(String.valueOf(comList.get(position).getPrice()));
            holder.dpname.setText(comList.get(position).getDpname());

            holder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //用集合保存当前的状态
                    if(isChecked){
                        holder.checkBox1.setChecked(true);
                        holder.checkBox2.setChecked(true);
                        selectgwc.setStore_name(comList.get(position).getDpname());
                        selectgwc.setCom_name(comList.get(position).getSpname());
                        selectgwc.setCom_price(String.valueOf(comList.get(position).getPrice()));
                        selectgwc.setCom_num(comList.get(position).getNum());



                    }

                    else{
                        holder.checkBox1.setChecked(false);
                        holder.checkBox2.setChecked(false);
                    }
                }
            });
            holder.checkBox1.setChecked(comList.get(position).getChecked1());
            holder.checkBox2.setChecked(comList.get(position).getChecked2());
            holder.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //用集合保存当前的状态
                    if(isChecked){
                        comList.get(position).setChecked2(false);
                    }

                    else{
                        comList.get(position).setChecked2(true);
                    }
                }
            });
            holder.checkBox2.setChecked(comList.get(position).getChecked2());

        }

        @Override
        public int getItemCount() {
            //获取列表条目总数
            return comList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            //初始化控件
            ImageView com_img;
            TextView com_name,com_price,com_num,dpname;
            CheckBox checkBox1,checkBox2;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                com_img=itemView.findViewById(R.id.gwc_img);
                com_name=itemView.findViewById(R.id.gwc_spname);
                com_price=itemView.findViewById(R.id.gwc_price);
                com_num=itemView.findViewById(R.id.gwc_num);
                dpname=itemView.findViewById(R.id.gwc_dpname);
                checkBox1=itemView.findViewById(R.id.select_btn);
                checkBox2=itemView.findViewById(R.id.select_btn2);
            }
        }


    }
    class space_item extends RecyclerView.ItemDecoration{
        //设置item的间距
        private int space=5;
        public space_item(int space){
            this.space=space;
        }
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            outRect.bottom=space;
            outRect.top=space;
        }
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OrdersActivity.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }






    //ViewPager适配器
    private class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }
    }
}
