package com.example.dzy_application;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dzy_application.database.MySQLConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShoesActivity extends AppCompatActivity {
    private static Connection con = null;
    private static PreparedStatement pst = null;
    private int space=5;//设置RecyclerView控件的item的上下间距
    //要展示的对应item的数据，imgs是上方的图片/视图
    //titles是标题，headsIcon是头像，username是用户名
    List<commodity> comList = new ArrayList<commodity>();
    HomeAdapter homeAdapter=new HomeAdapter();//创建适配器对象
    private int[] com_imgs={R.drawable.shoes_1,R.drawable.shoes_2,R.drawable.shoes_3,R.drawable.shoes_4,R.drawable.shoes_5,R.drawable.shoes_6};
    //private String[] com_name={"高领打底衫男潮流ins港","毛呢大衣男2021年秋冬","雾霾蓝马里奥卫衣男秋","禾子先生秋季棒球服日","2021秋冬加绒卫衣男","牛仔外套男生秋冬季大"};
    //private String[] com_price={"￥68","￥128","￥88.88","￥168 ","￥65 ","￥108 "};
    //private String[] com_manj={" 100减15 "," 50减10 "," 50减10 "," 100减10 "," 200减15 "," 100减10 "};
    //private String[] com_pay={"1000+付款","2000+付款","3000+付款","500+付款","7000+付款","100+付款"};
    //private String[] com_dpname={"铁尚旗舰店 >","vesibo旗舰店 >","铁尚旗舰店 >","铁尚旗舰店 >","铁尚旗舰店 >","卡迪兽旗舰店 >"};
    private OnItemClickListener onItemClickListener;

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
        setContentView(R.layout.shoes);//设置刚写的xml文件页面

        ShoesActivity.Threads_readSQL threads_readSQL = new ShoesActivity.Threads_readSQL();
        threads_readSQL.start();
        
        RecyclerView recyclerView1=findViewById(R.id.com_item);//创建recyclerView对象，并初始化其xml文件
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));//设置为表格布局，列数为2（这个是最主要的，就是这个来展示陈列式布局）
        recyclerView1.addItemDecoration(new space_item(space));//给recycleView添加item的间距
        recyclerView1.setAdapter(homeAdapter);//将适配器添加到recyclerView

        Button btn = (Button) findViewById(R.id.fanh);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoesActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    class Threads_readSQL extends Thread {
        @Override
        public void run() {

                try {
                    con = MySQLConnections.getConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String sql ="Select store_name,com_name,com_price,com_pay,com_manj from commodity where com_type = 'shoes'";
                    if (con!=null) {

                        pst = con.prepareStatement(sql);
                        // 关闭事务自动提交 ,这一行必须加上
                        con.setAutoCommit(false);
                        ResultSet rs = pst.executeQuery();//创建数据对象
                        //清空上次发送的信息
                        List<commodity> comList1 = new ArrayList<commodity>();
                        while (rs.next()) {
                            commodity com = new commodity();
                            com.setCom_name(rs.getString(2));
                            com.setStore_name(rs.getString(1));
                            com.setCom_price(rs.getString(3));
                            com.setCom_pay(rs.getString(4));
                            com.setManj(rs.getString(5));
                            comList1.add(com);
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

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
        private View itemView;
        @NonNull
        @Override
        //加载布局文件并返回MyViewHolder对象
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //创建view对象
            itemView = LayoutInflater.from(ShoesActivity.this).inflate(R.layout.commodity_item,parent,false);
            //创建MyViewHolder对象
            MyViewHolder myViewHolder=new MyViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        //获取数据并显示到对应控件
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //给我的四个控件获取一下数据，注意不同类型调用不同的方法，设置图片用setImageResource（），设置文字用setText（）
            holder.com_img.setImageResource(com_imgs[position]);
            holder.com_name.setText(comList.get(position).getCom_name());
            holder.com_price.setText(comList.get(position).getCom_price());
            holder.com_pay.setText(comList.get(position).getCom_pay());
            holder.com_manj.setText(comList.get(position).getManj());
            holder.dpname.setText(comList.get(position).getStore_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShoesActivity.this,com_detail.class);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            //获取列表条目总数
            return comList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            //初始化控件
            ImageView com_img;
            TextView com_name,com_price,com_pay,com_manj,dpname;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                com_img=itemView.findViewById(R.id.com_img);
                com_name=itemView.findViewById(R.id.com_name);
                com_price=itemView.findViewById(R.id.com_price);
                com_pay=itemView.findViewById(R.id.pay_num);
                com_manj=itemView.findViewById(R.id.manj);
                dpname=itemView.findViewById(R.id.dpname);
            }
        }


    }
    class space_item extends RecyclerView.ItemDecoration{
        //设置item的间距
        private int space=5;
        public space_item(int space){
            this.space=space;
        }
        public void getItemOffsets(Rect outRect,View view,RecyclerView parent,RecyclerView.State state){
            outRect.bottom=space;
            outRect.top=space;
        }
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
