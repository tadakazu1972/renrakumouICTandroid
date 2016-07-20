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

public class TyphoonActivity extends AppCompatActivity {
    protected TyphoonActivity mActivity = null;
    protected View mView = null;
    //基礎データ保存用変数
    protected String mMainStation;
    protected String mTsunamiStation;
    protected String mKubun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_typhoon);
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
        mView.findViewById(R.id.btnEarthquake).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, EarthquakeActivity.class);
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
        mView.findViewById(R.id.btnTyphoonOsaka).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showOsaka();
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
        mView.findViewById(R.id.btnTyphoonGathering).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showGathering();
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

    private void showTyphoon22(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■暴風（雪）警報");
        //４号招集なので、１号、２号、３号は参集なしの判定する
        String s;
        if (mKubun.equals("１")||mKubun.equals("２")||mKubun.equals("３")){
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) { //勤務消防署であることに注意!
                s = mMainStation+"へ参集";
            } else {
                s = mMainStation+"消防署へ参集";
            }
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

    private void showTyphoon24(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大雪警報");
        String s;
        if (mMainStation.equals("消防局")){
            s = mMainStation;
        } else {
            s = mMainStation+"消防署";
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

    private void showTyphoon25(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■洪水警報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","中央","西淀川","淀川","東淀川","東成","生野","旭","城東","鶴見","住之江","住吉","東住吉","平野"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
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

    private void showTyphoon26(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■波浪警報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"此花","港","大正","西淀川","住之江","水上"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
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

    private void showTyphoon27(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■高潮警報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","中央","西","港","大正","浪速","西淀川","淀川","住之江","西成","水上"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
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
    private void showTyphoon28(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■高潮注意報");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","中央","西","港","大正","浪速","西淀川","淀川","住之江","西成","水上"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
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
                    case 8:
                        showTyphoon28();
                        break;
                    case 9:
                        showTyphoon28();
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

    private void showTyphoon31(){
        final CharSequence[] actions = {"■氾濫注意水位　2.7","■準備情報発令の見込み(1時間以内に5.4に到達)","■避難準備情報　5.4","■避難勧告　5.5","■避難指示8.3"};
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
        builder.setTitle("■淀川（枚方） 氾濫注意水位　2.7");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            s = "招集なし";
        } else {
            if (mMainStation.equals("消防局")) {
                s = mMainStation;
            } else {
                s = mMainStation+"消防署";
            }
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

    private void showTyphoon312(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■淀川（枚方） 準備情報発令の見込み(1時間以内に5.4に到達)");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            s = "招集なし";
        } else {
            //４号招集なので、１号、２号、３号は参集なしの判定する
            if (mKubun.equals("１")||mKubun.equals("２")||mKubun.equals("３")){
                s = "招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            }
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
        builder.setTitle("■淀川（枚方） 避難準備情報　5.4");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭"};
        String[] b = {"中央","西","浪速","東成","生野","城東","鶴見","西成"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            if (!Arrays.asList(b).contains(mMainStation)) {
                s = "招集なし";
            } else {
                //４号招集なので、１号、２号、３号は参集なしの判定する
                if (mKubun.equals("１")||mKubun.equals("２")||mKubun.equals("３")){
                    s = "招集なし";
                } else {
                    if (mMainStation.equals("消防局")) {
                        s = mMainStation+"へ参集";
                    } else {
                        s = mMainStation+"消防署へ参集";
                    }
                }
            }
        } else {
            //３号招集なので、１号、２号は参集なしの判定する
            if (mKubun.equals("１")||mKubun.equals("２")){
                s = "招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = mMainStation+"へ参集";
                } else {
                    s = mMainStation+"消防署へ参集";
                }
            }
        }
        builder.setMessage("３号招集(非番・日勤)、４号招集(非番・日勤)\n\n"+s);
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
        builder.setTitle("■淀川（枚方） 避難勧告　5.5");
        //勤務消防署がリストに該当するか判定
        String s;
        String[] a = {"北","都島","福島","此花","西淀川","淀川","東淀川","旭"};
        String[] b = {"中央","西","浪速","東成","生野","城東","鶴見","西成"};
        String[] c = {"港","大正","天王寺","阿倍野","住之江","住吉","東住吉","平野","水上"};
        if (!Arrays.asList(a).contains(mMainStation)){ //!否定なのに注意
            if (!Arrays.asList(b).contains(mMainStation)) {
                if (!Arrays.asList(c).contains(mMainStation)){
                    s = "４号招集\n\n招集なし";
                } else {
                    //４号招集なので、１号、２号、３号は参集なしの判定する
                    if (mKubun.equals("１")||mKubun.equals("２")||mKubun.equals("３")){
                        s = "４号招集(非番・日勤)\n\n招集なし";
                    } else {
                        if (mMainStation.equals("消防局")) {
                            s = "４号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                        } else {
                            s = "４号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                        }
                    }
                }
            } else {
                //３号招集なので、１号、２号は参集なしの判定する
                if (mKubun.equals("１")||mKubun.equals("２")){
                    s = "３号招集(非番・日勤)\n\n招集なし";
                } else {
                    if (mMainStation.equals("消防局")) {
                        s = "３号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                    } else {
                        s = "３号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                    }
                }
            }
        } else {
            //２号招集なので、１号は参集なしの判定する
            if (mKubun.equals("１")){
                s = "２号招集(非番・日勤)\n\n招集なし";
            } else {
                if (mMainStation.equals("消防局")) {
                    s = "２号招集(非番・日勤)\n\n"+mMainStation+"へ参集";
                } else {
                    s = "２号招集(非番・日勤)\n\n"+mMainStation+"消防署へ参集";
                }
            }
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
        builder.setTitle("■淀川（枚方） 避難指示　8.3");
        //２号招集なので、１号は参集なしの判定する
        String s;
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

    //連絡網
    public void showTel(){
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
    public void showGathering(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("参集手段");
        //テキストファイル読み込み
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                //assetsフォルダ内のテキスト読み込み
                is = getAssets().open("gathering.txt");
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
}
