package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TyphoonActivity extends AppCompatActivity {
    protected TyphoonActivity mActivity = null;
    protected View mView = null;
    //基礎データ保存用変数
    protected String mMainStation;
    protected String mTsunamiStation;
    protected String mKubun;
    //連絡網データ操作用変数
    protected ListView mListView = null;
    protected DBHelper mDBHelper = null;
    protected SQLiteDatabase db = null;
    protected SimpleCursorAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_typhoon);
        initButtons();
        //基礎データ読み込み
        loadData();
        //連絡網データ作成
        mListView = new ListView(this);
        mDBHelper = new DBHelper(this);
        db = mDBHelper.getWritableDatabase();
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
        mView.findViewById(R.id.btnEarthquake).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, EarthquakeActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnKokuminhogo).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, KokuminhogoActivity.class);
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
        mView.findViewById(R.id.btnTyphoon1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showTyphoon1();
            }
        });
        mView.findViewById(R.id.btnTyphoon2).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showTyphoon2();
            }
        });
        mView.findViewById(R.id.btnTyphoon3).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showTyphoon3();
            }
        });
        mView.findViewById(R.id.btnTyphoonWeather).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showWeather();
            }
        });
        mView.findViewById(R.id.btnTyphoonRiver).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showRiver();
            }
        });
        mView.findViewById(R.id.btnTyphoonRoad).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showRoad();
            }
        });
        mView.findViewById(R.id.btnTyphoonTel).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showTel();
            }
        });
        mView.findViewById(R.id.btnTyphoonCaution).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showCaution();
            }
        });
        mView.findViewById(R.id.btnTyphoonBousaiNet).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showBousaiNet();
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

    //非常警備の基準（全て）
    private void showTyphoon1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("非常警備の基準（全て）");
        //テキストファイル読み込み
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                //assetsフォルダ内のテキスト読み込み
                is = getAssets().open("typhoon1.txt");
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

    //気象警報による非常警備
    private void showTyphoon2(){
        final CharSequence[] actions = {"■特別警報","■暴風（雪）警報","■大雨警報","■大雪警報","■洪水警報","■波浪警報","■高潮警報","■高潮注意報"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("発令されている警報は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon21();
                        break;
                    case 1:
                        showTyphoon22();
                        break;
                    case 2:
                        showTyphoon23();
                        break;
                    case 3:
                        showTyphoon24();
                        break;
                    case 4:
                        showTyphoon25();
                        break;
                    case 5:
                        showTyphoon26();
                        break;
                    case 6:
                        showTyphoon27();
                        break;
                    case 7:
                        showTyphoon28();
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

    private void showTyphoon21(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■特別警報");
        String s;
        if (mMainStation.equals("消防局")){
            s = mMainStation+"へ参集　所属担当者に確認すること";
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

    private void showTyphoon22(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■暴風（雪）警報");
        //４号招集なので、１号、２号、３号は参集なしの判定する
        String s;
        if (mKubun.equals("４号招集")){
            if (mMainStation.equals("消防局")) { //勤務消防署であることに注意!
                s = mMainStation+"へ参集　所属担当者に確認すること";
            } else {
                s = mMainStation+"消防署へ参集";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon23(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大雨警報");
        String s;
        if (mMainStation.equals("消防局")){
            s = mMainStation;
        } else {
            s = mMainStation+"消防署";
        }
        builder.setMessage("第５非常警備(全署、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon24(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大雪警報");
        String s;
        if (mMainStation.equals("消防局")){
            s = mMainStation;
        } else {
            s = mMainStation+"消防署";
        }
        builder.setMessage("第５非常警備(全署、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon25(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■洪水警報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","中央","西淀川","淀川","東淀川","東成","生野","旭","城東","鶴見","住之江","住吉","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(北、都島、福島、此花、中央、西淀川、淀川、東淀川、東成、生野、旭、城東、鶴見、住之江、住吉、東住吉、平野、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon26(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■波浪警報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"此花","港","大正","西淀川","住之江","水上","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(此花、港、大正、西淀川、住之江、水上、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon27(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■高潮警報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","中央","西","港","大正","浪速","西淀川","淀川","住之江","西成","水上","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(北、都島、福島、此花、中央、西、港、大正、浪速、西淀川、淀川、住之江、西成、水上、消防局)\n\n"+s+"\n\n招集なし");
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
    private void showTyphoon28(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■高潮注意報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","中央","西","港","大正","浪速","西淀川","淀川","住之江","西成","水上","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(北、都島、福島、此花、中央、西、港、大正、浪速、西淀川、淀川、住之江、西成、水上、消防局)\n\n"+s+"\n\n招集なし");
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

    //河川水位による非常警備
    private void showTyphoon3(){
        final CharSequence[] actions = {"■淀川（枚方）","■大和川（柏原）","■神崎川（三国）","■安威川（千歳橋）","■寝屋川（京橋）","■第二寝屋川（昭明橋）","■平野川（剣橋）","■平野川分水路（今里大橋）","■古川（桑才）","■東除川（大堀上小橋）"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("河川を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon31();
                        break;
                    case 1:
                        showTyphoon32();
                        break;
                    case 2:
                        showTyphoon33();
                        break;
                    case 3:
                        showTyphoon34();
                        break;
                    case 4:
                        showTyphoon35();
                        break;
                    case 5:
                        showTyphoon36();
                        break;
                    case 6:
                        showTyphoon37();
                        break;
                    case 7:
                        showTyphoon38();
                        break;
                    case 8:
                        showTyphoon39();
                        break;
                    case 9:
                        showTyphoon3A();
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

    //淀川（枚方）
    private void showTyphoon31(){
        final CharSequence[] actions = {"■氾濫注意水位(水位2.7m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位5.4mに到達)","■避難準備情報(水位5.4m)","■避難勧告(水位5.5m)","■避難指示(水位8.3m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon311();
                        break;
                    case 1:
                        showTyphoon312();
                        break;
                    case 2:
                        showTyphoon313();
                        break;
                    case 3:
                        showTyphoon314();
                        break;
                    case 4:
                        showTyphoon315();
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

    private void showTyphoon311(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■淀川（枚方） 氾濫注意水位(水位2.7m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(北、都島、福島、此花、西淀川、淀川、東淀川、旭、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon312(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■淀川（枚方） 避難準備情報発令の見込み(1時間以内に水位5.4mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon313(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■淀川（枚方） 避難準備情報(水位5.4m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭","消防局"};
        String[] b = {"中央","西","浪速","東成","生野","城東","鶴見","西成"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon314(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■淀川（枚方） 避難勧告(水位5.5m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭","消防局"};
        String[] b = {"中央","西","浪速","東成","生野","城東","鶴見","西成"};
        String[] c = {"港","大正","天王寺","阿倍野","住之江","住吉","東住吉","平野","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon315(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■淀川（枚方） 避難指示(水位8.3m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //大和川（柏原）
    private void showTyphoon32(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.2m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位4.7mに到達)","■避難準備情報(水位4.7m)","■避難勧告(水位5.3m)","■避難指示(水位6.8m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon321();
                        break;
                    case 1:
                        showTyphoon322();
                        break;
                    case 2:
                        showTyphoon323();
                        break;
                    case 3:
                        showTyphoon324();
                        break;
                    case 4:
                        showTyphoon325();
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

    private void showTyphoon321(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大和川（柏原） 氾濫注意水位(水位3.2m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"住之江","住吉","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(住之江、住吉、東住吉、平野、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon322(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大和川（柏原） 避難準備情報発令の見込み(1時間以内に水位4.7mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"住之江","住吉","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon323(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大和川（柏原） 避難準備情報(水位4.7m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"住之江","住吉","東住吉","平野","消防局"};
        String[] b = {"中央","天王寺","浪速","東成","生野","城東","阿倍野","西成"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon324(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大和川（柏原） 避難勧告(水位5.3m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"住之江","住吉","東住吉","平野","消防局"};
        String[] b = {"中央","天王寺","浪速","東成","生野","城東","阿倍野","西成"};
        String[] c = {"北","都島","福島","此花","西","港","大正","西淀川","淀川","東淀川","旭","鶴見","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon325(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大和川（柏原） 避難指示(水位6.8m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //神崎川（三国）
    private void showTyphoon33(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.8m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位4.8mに到達)","■避難準備情報(水位4.8m)","■避難勧告(水位5m)","■避難指示(水位5.8m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon331();
                        break;
                    case 1:
                        showTyphoon332();
                        break;
                    case 2:
                        showTyphoon333();
                        break;
                    case 3:
                        showTyphoon334();
                        break;
                    case 4:
                        showTyphoon335();
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

    private void showTyphoon331(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■神崎川（三国） 氾濫注意水位(水位3.8m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"淀川","東淀川","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(淀川、東淀川、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon332(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■神崎川（三国） 避難準備情報発令の見込み(1時間以内に水位4.8mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"西淀川","淀川","東淀川","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon333(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■神崎川（三国） 避難準備情報(水位4.8m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"西淀川","淀川","東淀川","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
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

    private void showTyphoon334(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■神崎川（三国） 避難勧告(水位5m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"西淀川","淀川","東淀川","消防局"};
        String[] b = {"北","都島","福島","此花","中央","西","港","大正","天王寺","浪速","東成","生野","旭","城東","鶴見","阿倍野","住之江","住吉","東住吉","平野","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "３号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon335(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■神崎川（三国） 避難指示(水位5.8m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //安威川（千歳橋）
    private void showTyphoon34(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.25m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位3.5mに到達)","■避難準備情報(水位3.5m)","■避難勧告(水位4.25m)","■避難指示(水位5.1m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon341();
                        break;
                    case 1:
                        showTyphoon342();
                        break;
                    case 2:
                        showTyphoon343();
                        break;
                    case 3:
                        showTyphoon344();
                        break;
                    case 4:
                        showTyphoon345();
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

    private void showTyphoon341(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■安威川（千歳橋） 氾濫注意水位(水位3.25m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東淀川","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(東淀川、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon342(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■安威川（千歳橋） 避難準備情報発令の見込み(1時間以内に水位3.5mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東淀川","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon343(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■安威川（千歳橋） 避難準備情報(水位3.5m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東淀川","消防局"};
        String[] b = {"西淀川","淀川"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon344(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■安威川（千歳橋） 避難勧告(水位4.25m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東淀川","消防局"};
        String[] b = {"西淀川","淀川"};
        String[] c = {"北","都島","福島","此花","中央","西","港","大正","天王寺","浪速","東成","生野","旭","城東","鶴見","阿倍野","住之江","住吉","東住吉","平野","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon345(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■安威川（千歳橋） 避難指示(水位5.1m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //寝屋川（京橋）
    private void showTyphoon35(){
        final CharSequence[] actions = {"■氾濫注意水位(水位2.9m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位3.1mに到達)","■避難準備情報(水位3.1m)","■避難勧告(水位3.3m)","■避難指示(水位3.5m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon351();
                        break;
                    case 1:
                        showTyphoon352();
                        break;
                    case 2:
                        showTyphoon353();
                        break;
                    case 3:
                        showTyphoon354();
                        break;
                    case 4:
                        showTyphoon355();
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

    private void showTyphoon351(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■寝屋川（京橋） 氾濫注意水位(水位2.9m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"都島","中央","城東","鶴見","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(都島、中央、城東、鶴見、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon352(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■寝屋川（京橋） 避難準備情報発令の見込み(1時間以内に水位3.1mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"都島","東成","生野","旭","城東","鶴見","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon353(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■寝屋川（京橋） 避難準備情報(水位3.1m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"都島","東成","生野","旭","城東","鶴見","東住吉","平野","消防局"};
        String[] b = {"中央","天王寺","阿倍野","住吉"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon354(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■寝屋川（京橋） 避難勧告(水位3.3m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"都島","東成","生野","旭","城東","鶴見","東住吉","平野","消防局"};
        String[] b = {"中央","天王寺","阿倍野","住吉"};
        String[] c = {"北","福島","此花","西","港","大正","浪速","西淀川","淀川","東淀川","住之江","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon355(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■寝屋川（京橋） 避難指示(水位3.5m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //第二寝屋川（昭明橋）
    private void showTyphoon36(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.4m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位4.25mに到達)","■避難準備情報(水位4.25m)","■避難勧告(水位4.55m)","■避難指示(水位4.85m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon361();
                        break;
                    case 1:
                        showTyphoon362();
                        break;
                    case 2:
                        showTyphoon363();
                        break;
                    case 3:
                        showTyphoon364();
                        break;
                    case 4:
                        showTyphoon365();
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

    private void showTyphoon361(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■第二寝屋川（昭明橋） 氾濫注意水位(水位3.4m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"中央","城東","鶴見","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(中央、城東、鶴見、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon362(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■第二寝屋川（昭明橋） 避難準備情報発令の見込み(1時間以内に水位4.25mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","城東","鶴見","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon363(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■第二寝屋川（昭明橋） 避難準備情報(水位4.25m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","城東","鶴見","消防局"};
        String[] b = {"都島","中央","天王寺","生野","旭","阿倍野","住吉","東住吉","平野"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon364(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■第二寝屋川（昭明橋） 避難勧告(水位4.55m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","城東","鶴見","消防局"};
        String[] b = {"都島","中央","天王寺","生野","旭","阿倍野","住吉","東住吉","平野"};
        String[] c = {"北","福島","此花","西","港","大正","浪速","西淀川","淀川","東淀川","住之江","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon365(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■第二寝屋川（昭明橋） 避難指示(水位4.85m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //平野川（剣橋）
    private void showTyphoon37(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.3m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位3.9mに到達)","■避難準備情報(水位3.9m)","■避難勧告(水位4.15m)","■避難指示(水位4.4m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon371();
                        break;
                    case 1:
                        showTyphoon372();
                        break;
                    case 2:
                        showTyphoon373();
                        break;
                    case 3:
                        showTyphoon374();
                        break;
                    case 4:
                        showTyphoon375();
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

    private void showTyphoon371(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川（剣橋） 氾濫注意水位(水位3.3m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(東成、生野、城東、東住吉、平野、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon372(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川（剣橋） 避難準備情報発令の見込み(1時間以内に水位3.9mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon373(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川（剣橋） 避難準備情報(水位3.9m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        String[] b = {"都島","中央","天王寺","旭","鶴見","阿倍野","住吉"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon374(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川（剣橋） 避難勧告(水位4.15m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        String[] b = {"都島","中央","天王寺","旭","鶴見","阿倍野","住吉"};
        String[] c = {"北","福島","此花","西","港","大正","浪速","西淀川","淀川","東淀川","住之江","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon375(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川（剣橋） 避難指示(水位4.4m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //平野川分水路（今里大橋）
    private void showTyphoon38(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.3m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位3.4mに到達)","■避難準備情報(水位3.4m)","■避難勧告(水位3.8m)","■避難指示(水位4.63m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon381();
                        break;
                    case 1:
                        showTyphoon382();
                        break;
                    case 2:
                        showTyphoon383();
                        break;
                    case 3:
                        showTyphoon384();
                        break;
                    case 4:
                        showTyphoon385();
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

    private void showTyphoon381(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川分水路（今里大橋） 氾濫注意水位(水位3.3m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(東成、生野、城東、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon382(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川分水路（今里大橋） 準備情報発令の見込み(1時間以内に水位3.4mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon383(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川分水路（今里大橋） 避難準備情報(水位3.4m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        String[] b = {"都島","中央","天王寺","旭","鶴見","阿倍野","住吉"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else  if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon384(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川分水路（今里大橋） 避難勧告(水位3.8m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"東成","生野","城東","東住吉","平野","消防局"};
        String[] b = {"都島","中央","天王寺","旭","鶴見","阿倍野","住吉"};
        String[] c = {"北","福島","此花","西","港","大正","浪速","西淀川","淀川","東淀川","住之江","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集") || mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "３号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "３号招集(非番・日勤)\n\n招集なし";
            }
        } else if (Arrays.asList(c).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon385(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■平野川分水路（今里大橋） 避難指示(水位4.63m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //古川（桑才）
    private void showTyphoon39(){
        final CharSequence[] actions = {"■氾濫注意水位(水位3.2m)、水防警報(出動)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon391();
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

    private void showTyphoon391(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■古川（桑才） 氾濫注意水位(水位3.2m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"鶴見","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(鶴見、消防局)\n\n"+s+"\n\n招集なし");
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

    //東除川（大堀上小橋）
    private void showTyphoon3A(){
        final CharSequence[] actions = {"■氾濫注意水位(水位2.9m)、水防警報(出動)","■避難準備情報発令の見込み(1時間以内に水位3.2mに到達)","■避難準備情報(水位3.2m)","■避難勧告(水位3.9m)","■避難指示(水位5.3m)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("水位の状況は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showTyphoon3A1();
                        break;
                    case 1:
                        showTyphoon3A2();
                        break;
                    case 2:
                        showTyphoon3A3();
                        break;
                    case 3:
                        showTyphoon3A4();
                        break;
                    case 4:
                        showTyphoon3A5();
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

    private void showTyphoon3A1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■東除川（大堀上小橋） 氾濫注意水位(水位2.9m)、水防警報(出動)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            if (mMainStation.equals("消防局")) {
                s = mMainStation+"(所属担当者に確認すること)";
            } else {
                s = mMainStation+"消防署";
            }
        } else {
            s = "ー";
        }
        builder.setMessage("第５非常警備(平野、消防局)\n\n"+s+"\n\n招集なし");
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

    private void showTyphoon3A2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■東除川（大堀上小橋） 避難準備情報発令の見込み(1時間以内に水位3.2mに到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
        }
        builder.setMessage("４号招集(非番・日勤)\n\n"+s);
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

    private void showTyphoon3A3(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■東除川（大堀上小橋） 避難準備情報(水位3.2m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"平野","消防局"};
        if (Arrays.asList(a).contains(mMainStation)){
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("３号招集")||mKubun.equals("４号招集")){
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            } else {
                s = "招集なし";
            }
        } else {
            s = "招集なし";
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

    private void showTyphoon3A4(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■東除川（大堀上小橋） 避難勧告(水位3.9m)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"平野","消防局"};
        String[] b = {"北","都島","福島","此花","中央","西","港","大正","天王寺","浪速","西淀川","淀川","東淀川","東成","生野","旭","城東","鶴見","住之江","住吉","東住吉","西成","水上"};
        if (Arrays.asList(a).contains(mMainStation)) {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１号招集")) {
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            }
        } else if (Arrays.asList(b).contains(mMainStation)) {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("４号招集")) {
                if (mMainStation.equals("消防局")) {
                    s = "４号招集(非番・日勤)\n\n" + mMainStation + "へ参集";
                } else {
                    s = "４号招集(非番・日勤)\n\n" + mMainStation + "消防署へ参集";
                }
            } else {
                s = "４号招集(非番・日勤)\n\n招集なし";
            }
        } else {
            s = "４号招集(非番・日勤)\n\n招集なし";
        }
        builder.setMessage(s);
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

    private void showTyphoon3A5(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■東除川（大堀上小橋） 避難指示(水位5.3m)");
        //２号招集なので、１号は参集なしの判定する
        String s;
        if (mKubun.equals("１号招集")){
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

    //情報（気象）
    private void showWeather(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_weather, (ViewGroup)findViewById(R.id.infoWeather));
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

    //情報（河川）
    private void showRiver(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_river, (ViewGroup)findViewById(R.id.infoRiver));
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

    //連絡網
    public void showTel0(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("連絡網");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.tel1, (ViewGroup)findViewById(R.id.tel1));
        //ボタン クリックリスナー設定
        layout.findViewById(R.id.btnHonsyo).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(mActivity, "本署タップされました（作業中）", Toast.LENGTH_LONG).show();
            }
        });
        layout.findViewById(R.id.btnOyodo).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(mActivity, "大淀タップされました（作業中）", Toast.LENGTH_LONG).show();
            }
        });
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

    //連絡網データ表示
    private void showTel(){
        //データ準備
        String order;
        order = "select * from records order by name desc";
        Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel","mail","kubun","syozoku","kinmu"};
        int[] to = {R.id.record_name,R.id.record_tel,R.id.record_mail,R.id.record_kubun,R.id.record_syozoku,R.id.record_kinmu};
        mActivity.mAdapter = new SimpleCursorAdapter(mActivity,R.layout.record_view,c,from,to,0);
        mListView.setAdapter(mActivity.mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //なにもしない　setOnItemClickListenerをいれないと、データアイテムをタップした時にアプリが落ちるのを防ぐため。
            }
        });
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("連絡網データ");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
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
                is = getAssets().open("typhoon_caution.txt");
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

    //防災ネット
    private void showBousaiNet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLまたはボタンをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_osaka, (ViewGroup)findViewById(R.id.infoOsaka));
        //ボタン クリックリスナー設定
        layout.findViewById(R.id.btnOsakaBousaiApp).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                startOsakaBousaiApp();
            }
        });
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

    //大阪市防災アプリ
    public void startOsakaBousaiApp(){
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("jp.ne.goo.bousai.osakaapp");
        try {
            startActivity(intent);
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("大阪市防災アプリがありません");
            //カスタムビュー設定
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.info_osakabousaiapp, (ViewGroup)findViewById(R.id.infoOsakaBousai));
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

}
