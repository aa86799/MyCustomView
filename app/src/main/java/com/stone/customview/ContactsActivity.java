package com.stone.customview;

import android.app.Activity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import com.stone.customview.adapter.CityListAdapter;
import com.stone.customview.db.CityDao;
import com.stone.customview.db.DBHelper;
import com.stone.customview.model.City;
import com.stone.customview.service.MySectionIndexer;
import com.stone.customview.view.BladeView;
import com.stone.customview.view.PinnedHeaderListView;

public class ContactsActivity extends Activity {

    private static final int COPY_DB_SUCCESS = 10;
    private static final int COPY_DB_FAILED = 11;
    protected static final int QUERY_CITY_FINISH = 12;
    private MySectionIndexer mIndexer;

    private List<City> cityList = new ArrayList<City>();
    public static String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
    private Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case QUERY_CITY_FINISH:

                    if(mAdapter==null){

                        mIndexer = new MySectionIndexer(sections, counts);

                        mAdapter = new CityListAdapter(cityList, mIndexer, getApplicationContext());
                        mListView.setAdapter(mAdapter);

                        mListView.setOnScrollListener(mAdapter);

                        //設置頂部固定頭部
                        mListView.setPinnedHeaderView(LayoutInflater.from(getApplicationContext()).inflate(
                                R.layout.list_group_item, mListView, false));

                    }else if(mAdapter!=null){
                        mAdapter.notifyDataSetChanged();
                    }

                    break;

                case COPY_DB_SUCCESS:
                    requestData();
                    break;
                default:
                    break;
            }
        };
    };
    private DBHelper helper;

    private CityListAdapter mAdapter;
    private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;
    protected static final String TAG = null;

    private String[] sections = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z" };
    private int[] counts;
    private PinnedHeaderListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        helper = new DBHelper();
        System.out.println("APP_DIR=" + APP_DIR);
        copyDBFile();
        findView();
    }

    private void copyDBFile() {

        File file = new File(APP_DIR+"/city.db");
        if(file.exists()){
            requestData();

        }else{	//拷贝文件
            Runnable task = new Runnable() {

                @Override
                public void run() {

                    copyAssetsFile2SDCard("city.db");
                }
            };

            new Thread(task).start();
        }
    }

    /**
     * 拷贝资产目录下的文件到 手机
     */
    private void copyAssetsFile2SDCard(String fileName) {

        File desDir = new File(APP_DIR);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }

        // 拷贝文件
        File file = new File(APP_DIR + fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            InputStream in = getAssets().open(fileName);

            FileOutputStream fos = new FileOutputStream(file);

            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }

            fos.flush();
            fos.close();

            handler.sendEmptyMessage(COPY_DB_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(COPY_DB_FAILED);
        }
    }

    private void requestData() {

        Runnable task = new Runnable() {

            @Override
            public void run() {
                CityDao dao = new CityDao(helper);

                List<City> hot = dao.getHotCities();	//热门城市
                List<City> all = dao.getAllCities();	//全部城市

                if(all!=null){

                    Collections.sort(all, new MyComparator());	//排序

                    cityList.addAll(hot);
                    cityList.addAll(all);

                    //初始化每个字母有多少个item
                    counts = new int[sections.length];

                    counts[0] = hot.size();	//热门城市 个数

                    for(City city : all){	//计算全部城市

                        String firstCharacter = city.getSortKey();
                        int index = ALL_CHARACTER.indexOf(firstCharacter);
                        counts[index]++;
                    }

                    handler.sendEmptyMessage(QUERY_CITY_FINISH);
                }
            }
        };

        new Thread(task).start();
    }

    public class MyComparator implements Comparator<City> {

        @Override
        public int compare(City c1, City c2) {

            return c1.getSortKey().compareTo(c2.getSortKey());
        }

    }

    private void findView() {

        mListView = (PinnedHeaderListView) findViewById(R.id.mListView);
        BladeView mLetterListView = (BladeView) findViewById(R.id.mLetterListView);

//        if (mLetterListView == null) return;
        mLetterListView.setOnItemClickListener(new BladeView.OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                if(s!=null){

                    int section = ALL_CHARACTER.indexOf(s);

                    int position = mIndexer.getPositionForSection(section);

                    Log.i(TAG, "s:"+s+",section:"+section+",position:"+position);

                    if(position!=-1){
                        mListView.setSelection(position);
                    }else{

                    }
                }

            }
        });
    }

}
