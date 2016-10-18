package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

//import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tadakazu on 2016/07/12.
 */
public class KinentaiActivity extends AppCompatActivity {
    protected KinentaiActivity mActivity = null;
    protected View mView = null;
    //連絡網データ操作用変数
    protected ListView mListView = null;
    protected DBHelper mDBHelper = null;
    protected SQLiteDatabase db = null;
    protected SimpleCursorAdapter mAdapter = null;
    protected CustomCursorAdapter mAdapter2 = null;
    //連絡網データ入力用　親所属スピナー文字列保存用
    private static String mSelected;
    private static String[] mArray;
    //まとめてメール送信用
    private ArrayList<String> mailArray;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_kinentai);
        initButtons();
        //連絡網データ作成
        mListView = new ListView(this);
        mDBHelper = new DBHelper(this);
        SQLiteDatabase.loadLibs(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String mKey = sp.getString("key", null);
        db = mDBHelper.getWritableDatabase(mKey);
        mailArray = new ArrayList<String>();
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
        mView.findViewById(R.id.btnTyphoon).setOnClickListener(new OnClickListener(){
           @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, TyphoonActivity.class);
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
        mView.findViewById(R.id.btnKinentai1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showKinentai1();
            }
        });
        mView.findViewById(R.id.btnKinentai2).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showKinentai2();
            }
        });
        mView.findViewById(R.id.btnKinentai3).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showKinentai3();
            }
        });
        mView.findViewById(R.id.btnKinentai4).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showKinentai4();
            }
        });
        mView.findViewById(R.id.btnKinentai5).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showKinentai5();
            }
        });
        mView.findViewById(R.id.btnKinentaiEarthquake).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showEarthquake();
            }
        });
        mView.findViewById(R.id.btnKinentaiBlackout).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showBlackout();
            }
        });
        mView.findViewById(R.id.btnKinentaiRoad).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showRoad();
            }
        });
        mView.findViewById(R.id.btnKinentaiTel).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showCheck();
            }
        });
        mView.findViewById(R.id.btnKinentaiRiver).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showRiver();
            }
        });
        mView.findViewById(R.id.btnKinentaiWeather).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showWeather();
            }
        });
    }

    //震央「陸」
    private void showKinentai1(){
        final CharSequence[] actions = {"■震度７(特別区６強)","■震度６強(特別区６弱)","■震度６弱(特別区５強、政令市５強又は６弱)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最大震度は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showRiku7();
                        break;
                    case 1:
                        showRiku6strong();
                        break;
                    case 2:
                        showRiku6weak();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度７
    private void showRiku7(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■最大震度７(特別区６強)\n   震央管轄都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int which){
               showCSV(which, "■最大震度７(特別区６強)", "riku7.csv");
           }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度６強
    private void showRiku6strong(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■最大震度６強(特別区６弱)\n   震央管轄都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■最大震度６強(特別区６弱)","riku6strong.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度６弱
    private void showRiku6weak(){
        final CharSequence[] actions = {"北海道(札幌市)","青森県","岩手県","宮城県(仙台市)","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県(さいたま市)","千葉県(千葉市)","東京都","神奈川県(横浜市、川崎市、相模原市)","新潟県(新潟市)","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県(静岡市、浜松市)","愛知県(名古屋市)","三重県","滋賀県","京都府(京都市)","大阪府(大阪市、堺市)","兵庫県(神戸市)","奈良県","和歌山県","鳥取県","島根県","岡山県(岡山市)","広島県(広島市)","山口県","徳島県","香川県","愛媛県","高知県","福岡県(北九州市、福岡市)","佐賀県","長崎県","熊本県(熊本市)","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■最大震度６弱(特別区５強、政令市５強又は６弱)   震央管轄都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■最大震度６弱(特別区５強、政令市５強又は６弱)", "riku6weak.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震央「海域」
    private void showKinentai2(){
        final CharSequence[] actions = {"■震度７(特別区６強)","■震度６強(特別区６弱)","■震度６弱(特別区５強、政令市５強又は６弱)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最大震度は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showKaiiki7();
                        break;
                    case 1:
                        showKaiiki6strong();
                        break;
                    case 2:
                        showKaiiki6weak();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度７
    private void showKaiiki7(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■最大震度７(特別区６強)\n   最大震度都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■最大震度７(特別区６強)","kaiiki7.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度６強
    private void showKaiiki6strong(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■最大震度６強(特別区６弱)\n   最大震度都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■最大震度６強(特別区６弱)","kaiiki6strong.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //震度６弱
    private void showKaiiki6weak(){
        final CharSequence[] actions = {"北海道(札幌市)","青森県","岩手県","宮城県(仙台市)","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県(さいたま市)","千葉県(千葉市)","東京都","神奈川県(横浜市、川崎市、相模原市)","新潟県(新潟市)","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県(静岡市、浜松市)","愛知県(名古屋市)","三重県","滋賀県","京都府(京都市)","大阪府(大阪市、堺市)","兵庫県(神戸市)","奈良県","和歌山県","鳥取県","島根県","岡山県(岡山市)","広島県(広島市)","山口県","徳島県","香川県","愛媛県","高知県","福岡県(北九州市、福岡市)","佐賀県","長崎県","熊本県(熊本市)","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■最大震度６弱(特別区５強、政令市５強又は６弱)   最大震度都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■最大震度６弱(特別区５強、政令市５強又は６弱)","kaiiki6weak.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //アクションプラン
    private void showKinentai3(){
        final CharSequence[] actions = {"東海地震","首都直下地震","東南海・南海地震","南海トラフ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("アクションプラン");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showActionPlan((String)actions[which],"kinentai_toukai.txt");
                        break;
                    case 1:
                        showActionPlan((String)actions[which],"kinentai_syutochokka.txt");
                        break;
                    case 2:
                        showActionPlan((String)actions[which],"kinentai_tounankai.txt");
                        break;
                    case 3:
                        showNankaitraf();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //アクションプラン表示
    public void showActionPlan(String title, String filename){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        //テキストファイル読み込み
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                //assetsフォルダ内のテキスト読み込み
                is = getAssets().open(filename);
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
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //南海トラフ
    private void showNankaitraf(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("南海トラフ　次の①②が合致した場合にアクションプランを適用する");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.nankaitraf, (ViewGroup)findViewById(R.id.nankaitraf));
        //判定準備
        final Spinner nankaitraf1 = (Spinner)layout.findViewById(R.id.spnNankaitraf1);
        final Spinner nankaitraf21 = (Spinner)layout.findViewById(R.id.spnNankaitraf21);
        final Spinner nankaitraf22 = (Spinner)layout.findViewById(R.id.spnNankaitraf22);
        final Spinner nankaitraf23 = (Spinner)layout.findViewById(R.id.spnNankaitraf23);
        builder.setView(layout);
        builder.setPositiveButton("判定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String check1 = (String)nankaitraf1.getSelectedItem();
                String check21 = (String)nankaitraf21.getSelectedItem();
                String check22 = (String)nankaitraf22.getSelectedItem();
                String check23 = (String)nankaitraf23.getSelectedItem();
                //いざ、判定
                if (!check1.equals("その他")&&!check21.equals("その他")&&!check22.equals("その他")&&!check23.equals("その他")){
                    showActionPlan("南海トラフ","kinentai_nankaitraf.txt");
                } else {
                    showActionPlan("南海トラフ","kinentai_nankaitraf2.txt");
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //大津波・噴火
    private void showKinentai4(){
        final CharSequence[] actions = {"大津波","噴火"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("大津波・噴火");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showKinentai41();
                        break;
                    case 1:
                        showKinentai42();
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //大津波　都道府県選択
    private void showKinentai41(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■大津波警報\n   都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■大津波警報","otsunami.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //噴火　都道府県選択
    private void showKinentai42(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■噴火\n   都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■噴火","hunka.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //特殊災害(NBC含む)
    private void showKinentai5(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■特殊災害(NBC含む)\n   都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showCSV(which, "■特殊災害(NBC含む)","nbc.csv");
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //CSVファイル表示ダイアログ
    public void showCSV(int which, String title, String filename){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //csvファイル読み込み
        InputStream is = null;
        String pref = ""; //都道府県
        String data1 = ""; //指揮支援隊
        String data2 = ""; //大阪府大隊(陸上)
        String data3 = ""; //大阪府隊(航空小隊)
        try {
            try {
                //assetsフォルダ内のcsvファイル読み込み
                is = getAssets().open(filename);
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1]; data1 = data1.replaceAll("、","\n     "); //２行になる答えなので改行とスペースを挿入
                data2 = data[2]; data2 = data2.replaceAll("、","\n     "); //２行になる答えなので改行とスペースを挿入
                data3 = data[3]; data3 = data3.replace("]","").replaceAll("、","\n     "); //２行になる答えなので改行とスペースを挿入;
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle(title+"　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府大隊(航空)\n\n　"+data3);
        builder.setNegativeButton("キャンセル", null);
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
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //情報（停電）
    private void showBlackout(){
        final CharSequence[] actions = {"■関西電力","■四国電力","■中国電力","■九州電力","■中部電力","■北陸電力","■東京電力","■東北電力"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("電力会社を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showURL(R.layout.info_kanden, R.id.infoKanden);
                        break;
                    case 1:
                        showURL(R.layout.info_yonden, R.id.infoYonden);
                        break;
                    case 2:
                        showURL(R.layout.info_energia, R.id.infoEnergia);
                        break;
                    case 3:
                        showURL(R.layout.info_kyuden, R.id.infoKyuden);
                        break;
                    case 4:
                        showURL(R.layout.info_chuden, R.id.infoChuden);
                        break;
                    case 5:
                        showURL(R.layout.info_rikuden, R.id.infoRikuden);
                        break;
                    case 6:
                        showURL(R.layout.info_touden, R.id.infoTouden);
                        break;
                    case 7:
                        showURL(R.layout.info_touhokuden, R.id.infoTouhokuden);
                        break;
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showURL(int xml, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(xml, (ViewGroup)findViewById(id));
        builder.setView(layout);
        builder.setNegativeButton("キャンセル", null);
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
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //連絡網データ表示
    private void showCheck(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.checkTitle);
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.check, (ViewGroup)findViewById(R.id.telCheck));
        //データ取得準備
        final EditText edit1 = (EditText)layout.findViewById(R.id.editCheck);
        builder.setView(layout);
        builder.setPositiveButton("入力", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String checked = edit1.getText().toString();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
                String uuid = UUID.randomUUID().toString(); //念のためpasswordが空の時に返すダミーデータ生成。空の時にそのままエンター押して通過されるのを防止
                String word = sp.getString("password",uuid);
                if (checked.equals(word)){
                    checked = null; //これを入れて明示的に閉じないと次の画面でEditTextのインスタンスに反応してソフトキーボードが立ち上がり続ける端末あり
                    dialog.dismiss(); //これを入れて明示的に閉じないと次の画面でEditTextのインスタンスに反応してソフトキーボードが立ち上がり続ける端末あり
                    showTel();
                }
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showTel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("連絡網");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.tel_show2, (ViewGroup)findViewById(R.id.telShow2));
        //全件表示ボタン設定
        final Button btnAll = (Button)layout.findViewById(R.id.btnTel);
        btnAll.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showTelAll();
            }
        });
        //検索条件取得準備
        final Spinner  editKubun = (Spinner)layout.findViewById(R.id.editKubun);
        final Spinner  editSyozoku = (Spinner)layout.findViewById(R.id.editSyozoku);
        final Spinner  editSyozoku2 = (Spinner)layout.findViewById(R.id.editSyozoku2);
        final Spinner  editKinmu = (Spinner)layout.findViewById(R.id.editKinmu);
        //親所属スピナー選択時の処理
        editSyozoku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //親所属スピナーの選択した位置をint取得
                int i = parent.getSelectedItemPosition();
                //Toast.makeText(mActivity, String.valueOf(i)+"番目を選択", Toast.LENGTH_SHORT).show();
                //取得したintを配列リソース名に変換し、配列リソースIDを取得（なぜか日本語ではエラーが出るのでアルファベットと数字で対応））
                mSelected = "firestationB"+ String.valueOf(i);
                int resourceId = getResources().getIdentifier(mSelected, "array", getPackageName());
                //Toast.makeText(mActivity, "resourceID="+String.valueOf(resourceId), Toast.LENGTH_SHORT).show();
                //取得した配列リソースIDを文字列配列に格納
                mArray = getResources().getStringArray(resourceId);
                //配列リソースIDから取得した文字列配列をアダプタに入れる
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item);
                for (String aMArray : mArray) {
                    mAdapter.add(aMArray);
                }
                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //アダプタを子スピナーにセット
                editSyozoku2.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //nothing to do
            }
        });
        builder.setView(layout);
        builder.setPositiveButton("検索", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String kubun = (String)editKubun.getSelectedItem();
                String syozoku = (String)editSyozoku2.getSelectedItem();
                String kinmu = (String)editKinmu.getSelectedItem();
                showTelResult(kubun, syozoku, kinmu);
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showTelAll(){
        //データ準備
        mailArray.clear(); //前回の残りを消去しておく
        final String order = "select * from records order by name desc";
        final Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel","mail","kubun","syozoku","kinmu"};
        int[] to = {R.id.record_name,R.id.record_tel,R.id.record_mail,R.id.record_kubun,R.id.record_syozoku,R.id.record_kinmu};
        //初回のみ起動。そうしないと、すべて選択した後の２回目がまたnewされて意味ない
        if (mAdapter2 == null) {
            mActivity.mAdapter2 = new CustomCursorAdapter(mActivity,R.layout.record_view2,c,from,to,0);
        }
        mListView.setAdapter(mActivity.mAdapter2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //タップした位置のデータをチェック処理
                mActivity.mAdapter2.clickData(position, view);
            }
        });
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("連絡網データ\n(メールはチェックしてから送信)");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
        builder.setPositiveButton("メール送信", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //第一段階　メール送信対象リストに格納
                //CustomCursorAdapterのメンバ変数であるitemCheckedを見に行く
                c.moveToFirst(); //カーソルを先頭に
                for (int i=0; i < mAdapter2.itemChecked.size(); i++){
                    //チェックされていたら対応するカーソルのmailアドレス文字列をメール送信対象文字列に格納
                    if (mAdapter2.itemChecked.get(i)){
                        mailArray.add(c.getString(c.getColumnIndex("mail")));
                    }
                    c.moveToNext();
                }
                //第二段階　メールアプリのsendに渡す文字列にArrayListに格納した各アドレスを格納
                final String[] sendMails = new String[mailArray.size()];
                for (int i=0; i < mailArray.size(); i++){
                    sendMails[i] = mailArray.get(i);
                }
                //メール立ち上げ
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, sendMails);
                intent.putExtra(Intent.EXTRA_SUBJECT, "参集アプリ　一斉送信メール");
                intent.putExtra(Intent.EXTRA_TEXT, "緊急連絡");
                try {
                    startActivity(Intent.createChooser(intent, "メールアプリを選択"));
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(mActivity, "メールアプリが見つかりません", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNeutralButton("すべて選択/解除", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                c.moveToFirst();
                for (int i=0; i < mAdapter2.itemChecked.size(); i++){
                    if (!mAdapter2.itemChecked.get(i)) {
                        mActivity.mAdapter2.itemChecked.set(i, true);
                    } else {
                        mActivity.mAdapter2.itemChecked.set(i, false);
                    }
                }
                //再帰しないとsetNeutralButtonを押すとダイアログが自動で消えてしまって意味がないので・・・
                showTelAll();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                mailArray.clear(); //きちんと後片付け
                mAdapter2 = null;
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void showTelResult(String _kubun, String _syozoku, String _kinmu){
        //データ準備
        mailArray.clear(); //前回の残りを消去
        final String kubun;
        if (_kubun.equals("すべて")){
            kubun = "is not null";
        } else {
            kubun = "='" + _kubun + "'";
        }
        final String syozoku;
        if (_syozoku.equals("すべて")){
            syozoku = "is not null";
        } else {
            syozoku = "='" + _syozoku + "'";
        }
        final String kinmu;
        if (_kinmu.equals("すべて")){
            kinmu = "is not null";
        } else {
            kinmu = "='" + _kinmu + "'";
        }
        final String order = "select * from records where kubun " + kubun + " and syozoku " + syozoku + " and kinmu " + kinmu + " order by name desc";
        final Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel","mail","kubun","syozoku","kinmu"};
        int[] to = {R.id.record_name,R.id.record_tel,R.id.record_mail,R.id.record_kubun,R.id.record_syozoku,R.id.record_kinmu};
        //初回のみ起動。そうしないと、すべて選択した後の２回目がまたnewされて意味ない
        if (mAdapter2 == null) {
            mActivity.mAdapter2 = new CustomCursorAdapter(mActivity, R.layout.record_view2, c, from, to, 0);
        }
        mListView.setAdapter(mActivity.mAdapter2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //タップした位置のデータをチェック処理
                mActivity.mAdapter2.clickData(position, view);
            }
        });
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("連絡網データ\n (メールはチェックしてから送信)");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
        builder.setPositiveButton("メール送信", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //第一段階　メール送信対象リストに格納
                //CustomCursorAdapterのメンバ変数であるitemCheckedを見に行く
                c.moveToFirst(); //カーソルを先頭に
                for (int i=0; i < mAdapter2.itemChecked.size(); i++){
                    //チェックされていたら対応するカーソルのmailアドレス文字列をメール送信対象文字列に格納
                    if (mAdapter2.itemChecked.get(i)){
                        mailArray.add(c.getString(c.getColumnIndex("mail")));
                    }
                    c.moveToNext();
                }
                //第二段階　メールアプリのsendに渡す文字列にArrayListに格納した各アドレスを格納
                final String[] sendMails = new String[mailArray.size()];
                for (int i=0; i < mailArray.size(); i++){
                    sendMails[i] = mailArray.get(i);
                }
                //メール立ち上げ
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, sendMails);
                intent.putExtra(Intent.EXTRA_SUBJECT, "参集アプリ　一斉送信メール");
                intent.putExtra(Intent.EXTRA_TEXT, "緊急連絡");
                try {
                    startActivity(Intent.createChooser(intent, "メールアプリを選択"));
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(mActivity, "メールアプリが見つかりません", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNeutralButton("すべて選択/解除", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                c.moveToFirst();
                for (int i=0; i < mAdapter2.itemChecked.size(); i++){
                    if (!mAdapter2.itemChecked.get(i)) {
                        mActivity.mAdapter2.itemChecked.set(i, true);
                    } else {
                        mActivity.mAdapter2.itemChecked.set(i, false);
                    }
                }
                //再帰しないとsetNeutralButtonを押すとダイアログが自動で消えてしまって意味がないので・・・
                showTelResult(kubun, syozoku, kinmu);
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                mailArray.clear(); //きちんと後片付け
                mAdapter2 = null;
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
        builder.setNegativeButton("キャンセル", null);
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
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }
}
