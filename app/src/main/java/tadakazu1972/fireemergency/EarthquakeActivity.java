package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by tadakazu on 2016/07/17.
 */
public class EarthquakeActivity extends AppCompatActivity {
    protected EarthquakeActivity mActivity = null;
    protected View mView = null;
    //基礎データ保存用変数
    protected String mMainStation;
    protected String mTsunamiStation;
    protected String mKubun;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_earthquake);
        initButtons();
        //基礎データ読み込み
        loadData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //基礎データを変更してActivity復帰した際に反映させないと前のままなので
        loadData();
    }

    //ボタン設定
    private void initButtons(){
        mView.findViewById(R.id.btnData).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, DataActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnTyphoon).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, TyphoonActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnKinentai).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, KinentaiActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnEarthquake1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showEarthquake1();
            }
        });
        mView.findViewById(R.id.btnEarthquake2).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showEarthquake2();
            }
        });
        mView.findViewById(R.id.btnEarthquake3).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showEarthquake3();
            }
        });
        mView.findViewById(R.id.btnEarthquake4).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showEarthquake4();
            }
        });
        mView.findViewById(R.id.btnEarthquakeEarthquake).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showEarthquake();
            }
        });
        mView.findViewById(R.id.btnEarthquakeBlackout).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showBlackout();
            }
        });
        mView.findViewById(R.id.btnEarthquakeRoad).setOnClickListener(new OnClickListener(){
           @Override
            public void onClick(View v){
               showRoad();
           }
        });
        mView.findViewById(R.id.btnEarthquakeCaution).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showCaution();
            }
        });
        mView.findViewById(R.id.btnEarthquakeOsaka).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showOsaka();
            }
        });
    }

    //基礎データ読み込み
    private void loadData(){
        //勤務消防署
        SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        mMainStation = sp1.getString("mainStation","消防局"); // 第２引数はkeyが存在しない時に返す初期値
        //大津波・津波警報時指定署
        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(this);
        mTsunamiStation = sp2.getString("tsunamiStation", "消防局"); // 第２引数はkeyが存在しない時に返す初期値
        //招集区分
        SharedPreferences sp3 = PreferenceManager.getDefaultSharedPreferences(this);
        mKubun = sp3.getString("kubun", "１"); // 第２引数はkeyが存在しない時に返す初期値
    }

    //震度５強以上
    private void showEarthquake1(){
        final CharSequence[] actions = {"■大津波警報","■津波警報","■警報なし"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("発令されている警報は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showEarthquake11();
                        break;
                    case 1:
                        showEarthquake12();
                        break;
                    case 2:
                        showEarthquake13();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake11(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大津波警報");
        String s;
        if (mTsunamiStation.equals("消防局")){
            s = mTsunamiStation+"へ参集";
        } else {
            s = mTsunamiStation+"消防署へ参集";
        }
        builder.setMessage("１号招集\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake12(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■津波警報");
        String s;
        if (mTsunamiStation.equals("消防局")){
            s = mTsunamiStation+"へ参集";
        } else {
            s = mTsunamiStation+"消防署へ参集";
        }
        builder.setMessage("１号招集\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake13(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■警報なし");
        String s;
        if (mMainStation.equals("消防局")){
            s = mMainStation+"へ参集";
        } else {
            s = mMainStation+"消防署へ参集";
        }
        builder.setMessage("１号招集\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度５弱
    private void showEarthquake2(){
        final CharSequence[] actions = {"■大津波警報","■津波警報","■警報なし"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("発令されている警報は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showEarthquake21();
                        break;
                    case 1:
                        showEarthquake22();
                        break;
                    case 2:
                        showEarthquake23();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake21(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大津波警報");
        String s;
        if (mTsunamiStation.equals("消防局")) {
            s = mTsunamiStation+"へ参集";
        } else {
            s = mTsunamiStation+"消防署へ参集";
        }
        builder.setMessage("１号招集\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake22(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■津波警報");
        String s;
        //２号招集なので、１号は参集なしの判定する
        if (mKubun.equals("１")){
            s = "招集なし";
        } else {
            if (mTsunamiStation.equals("消防局")) {
                s = mTsunamiStation+"へ参集";
            } else {
                s = mTsunamiStation+"消防署へ参集";
            }
        }
        builder.setMessage("２号招集(非番・日勤)\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake23(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■警報なし");
        String s;
        //２号招集なので、１号は参集なしの判定する
        if (mKubun.equals("１")){
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) { //勤務消防署であることに注意!
                s = mMainStation+"へ参集";
            } else {
                s = mMainStation+"消防署へ参集";
            }
        }
        builder.setMessage("２号招集(非番・日勤)\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度４
    private void showEarthquake3(){
        final CharSequence[] actions = {"■大津波警報","■津波警報","■警報なし"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("発令されている警報は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showEarthquake31();
                        break;
                    case 1:
                        showEarthquake32();
                        break;
                    case 2:
                        showEarthquake33();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake31(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大津波警報");
        String s;
        if (mTsunamiStation.equals("消防局")){
            s = mTsunamiStation+"へ参集";
        } else {
            s = mTsunamiStation+"消防署へ参集";
        }
        builder.setMessage("１号招集\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake32(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■津波警報");
        String s;
        //３号招集なので、１号、２号は参集なしの判定する
        if (mKubun.equals("１")||mKubun.equals("２")){
            s = "招集なし";
        } else {
            if (mTsunamiStation.equals("消防局")) {
                s = mTsunamiStation+"へ参集";
            } else {
                s = mTsunamiStation+"消防署へ参集";
            }
        }
        builder.setMessage("３号招集(非番・日勤)\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake33(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■警報なし");
        String s;
        //３号招集なので、１号、２号は参集なしの判定する
        if (mKubun.equals("１")||mKubun.equals("２")){
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) { //勤務消防署であることに注意!
                s = mMainStation+"へ参集";
            } else {
                s = mMainStation+"消防署へ参集";
            }
        }
        builder.setMessage("３号招集(非番・日勤)\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度３以下
    private void showEarthquake4(){
        final CharSequence[] actions = {"■大津波警報","■津波警報","■津波注意報","■警報なし"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("発令されている警報は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showEarthquake41();
                        break;
                    case 1:
                        showEarthquake42();
                        break;
                    case 2:
                        showEarthquake43();
                        break;
                    case 3:
                        showEarthquake44();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake41(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大津波警報");
        String s;
        if (mTsunamiStation.equals("消防局")){
            s = mTsunamiStation+"へ参集";
        } else {
            s = mTsunamiStation+"消防署へ参集";
        }
        builder.setMessage("１号招集\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake42(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■津波警報");
        String s;
        //３号招集なので、１号、２号は参集なしの判定する
        if (mKubun.equals("１")||mKubun.equals("２")){
            s = "招集なし";
        } else {
            if (mTsunamiStation.equals("消防局")) {
                s = mTsunamiStation+"へ参集";
            } else {
                s = mTsunamiStation+"消防署へ参集";
            }
        }
        builder.setMessage("３号招集(非番・日勤)\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake43(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■津波注意報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"此花","港","大正","西淀川","住之江","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            if (mMainStation.equals("消防局")) { //勤務消防署であることに注意!
                s = mMainStation;
            } else {
                s = mMainStation + "消防署";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("第５非常警備\n\n"+s);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showEarthquake44(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■警報なし");
        builder.setMessage("招集なし\n\n");
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //情報（地震）
    private void showEarthquake(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_earthquake, (ViewGroup)findViewById(R.id.infoEarthquake));
        builder.setView(layout);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //情報（停電）
    private void showBlackout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_blackout, (ViewGroup)findViewById(R.id.infoBlackout));
        builder.setView(layout);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //情報（道路）
    private void showRoad(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_road, (ViewGroup)findViewById(R.id.infoRoad));
        builder.setView(layout);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //留意事項
    public void showCaution(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("留意事項");
        //テキストファイル読み込み
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                //assetsフォルダ内のテキスト読み込み
                is = getAssets().open("caution.txt");
                br = new BufferedReader(new InputStreamReader(is));
                //１行づつ読み込み、改行追加
                String str;
                while((str = br.readLine()) !=null){
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setMessage(text);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //参集手段
    public void showEarthquakeGathering(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("参集手段");
        //テキストファイル読み込み
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                //assetsフォルダ内のテキスト読み込み
                is = getAssets().open("earthquake_gathering.txt");
                br = new BufferedReader(new InputStreamReader(is));
                //１行づつ読み込み、改行追加
                String str;
                while((str = br.readLine()) !=null){
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setMessage(text);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //おおさか防災ネット
    private void showOsaka(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_osaka, (ViewGroup)findViewById(R.id.infoOsaka));
        builder.setView(layout);
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }
}
