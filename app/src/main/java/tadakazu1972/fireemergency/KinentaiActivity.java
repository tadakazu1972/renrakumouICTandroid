package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by tadakazu on 2016/07/12.
 */
public class KinentaiActivity extends AppCompatActivity {
    protected KinentaiActivity mActivity = null;
    protected View mView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_kinentai);
        initButtons();
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
        builder.setTitle("震度は？");
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

    //震度７
    private void showRiku7(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","山梨県","新潟県","富山県","石川県","福井県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■震度７(特別区６強)の都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int which){
               showRiku71(which);
               //Toast.makeText(mActivity, String.valueOf(which)+"番目が選択されました", Toast.LENGTH_LONG).show();
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

    public void showRiku71(int which){
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
                is = getAssets().open("riku7.csv");
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1];
                data2 = data[2];
                data3 = data[3]; data3 = data3.replace("]","");
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle("■震度７(特別区６強)　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府隊(航空小隊)\n\n　"+data3);
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

    //震度６強
    private void showRiku6strong(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","山梨県","新潟県","富山県","石川県","福井県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■震度６強(特別区６弱)の都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showRiku6strong1(which);
                //Toast.makeText(mActivity, String.valueOf(which)+"番目が選択されました", Toast.LENGTH_LONG).show();
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

    public void showRiku6strong1(int which){
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
                is = getAssets().open("riku6strong.csv");
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1];
                data2 = data[2]; data2 = data2.replaceAll("、","\n     "); //２行になる答えなので改行とスペースを挿入
                data3 = data[3]; data3 = data3.replace("]","");
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle("■震度６強(特別区６弱)　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府隊(航空小隊)\n\n　"+data3);
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

    //震度６弱
    private void showRiku6weak(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","山梨県","新潟県","富山県","石川県","福井県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■震度６弱(特別区５強、政令市５強又は６弱)の都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showRiku6weak1(which);
                //Toast.makeText(mActivity, String.valueOf(which)+"番目が選択されました", Toast.LENGTH_LONG).show();
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

    public void showRiku6weak1(int which){
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
                is = getAssets().open("riku6weak.csv");
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1];
                data2 = data[2];
                data3 = data[3]; data3 = data3.replace("]","");
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle("■震度６弱(特別区５強、政令市５強又は６弱)　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府隊(航空小隊)\n\n　"+data3);
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

    //震央「海域」
    private void showKinentai2(){
        final CharSequence[] actions = {"■震度７(特別区６強)","■震度６強(特別区６弱)","■震度６弱(特別区５強、政令市５強又は６弱)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("震度は？");
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

    //震度７
    private void showKaiiki7(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","山梨県","新潟県","富山県","石川県","福井県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■震度７(特別区６強)の都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showKaiiki71(which);
                //Toast.makeText(mActivity, String.valueOf(which)+"番目が選択されました", Toast.LENGTH_LONG).show();
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

    public void showKaiiki71(int which){
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
                is = getAssets().open("kaiiki7.csv");
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1];
                data2 = data[2];
                data3 = data[3]; data3 = data3.replace("]","");
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle("■震度７(特別区６強)　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府隊(航空小隊)\n\n　"+data3);
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

    //震度６強
    private void showKaiiki6strong(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","山梨県","新潟県","富山県","石川県","福井県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■震度６強(特別区６弱)の都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showKaiiki6strong1(which);
                //Toast.makeText(mActivity, String.valueOf(which)+"番目が選択されました", Toast.LENGTH_LONG).show();
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

    public void showKaiiki6strong1(int which){
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
                is = getAssets().open("kaiiki6strong.csv");
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1];
                data2 = data[2]; data2 = data2.replaceAll("、","\n     "); //２行になる答えなので改行とスペースを挿入
                data3 = data[3]; data3 = data3.replace("]","");
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle("■震度６強(特別区６弱)　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府隊(航空小隊)\n\n　"+data3);
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

    //震度６弱
    private void showKaiiki6weak(){
        final CharSequence[] actions = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","山梨県","新潟県","富山県","石川県","福井県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("■震度６弱(特別区５強、政令市５強又は６弱)の都道府県は？");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                showKaiiki6weak1(which);
                //Toast.makeText(mActivity, String.valueOf(which)+"番目が選択されました", Toast.LENGTH_LONG).show();
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

    public void showKaiiki6weak1(int which){
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
                is = getAssets().open("kaiiki6weak.csv");
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1); //ヘッダー0行読み込まないため1行から
                List<String[]> csv = csvreader.readAll();
                String line = Arrays.toString(csv.get(which));
                String[] data = line.split(Pattern.quote(","),0);
                //データ代入　先頭と最後に[]がついてくるのでreplaceで削除している
                pref = data[0]; pref = pref.replace("[","");
                data1 = data[1];
                data2 = data[2];
                data3 = data[3]; data3 = data3.replace("]","");
            } finally {
                if (is != null) is.close();
            }
        } catch (Exception e) {
            //エラーメッセージ
            Toast.makeText(this, "テキスト読込エラー", Toast.LENGTH_LONG).show();
        }
        builder.setTitle("■震度６弱(特別区５強、政令市５強又は６弱)　"+pref);
        builder.setMessage("・指揮支援隊\n\n　"+data1+"\n\n・大阪府大隊(陸上)\n\n　"+data2+"\n\n・大阪府隊(航空小隊)\n\n　"+data3);
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
        final CharSequence[] actions = {"■関西電力","■四国電力","■中国電力","■九州電力","■中部電力","■北陸電力","■東京電力","■東北電力"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("電力会社を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    case 0:
                        showKanden();
                        break;
                    case 1:
                        showYonden();
                        break;
                    case 2:
                        showEnergia();
                        break;
                    case 3:
                        showKyuden();
                        break;
                    case 4:
                        showChuden();
                        break;
                    case 5:
                        showRikuden();
                        break;
                    case 6:
                        showTouden();
                        break;
                    case 7:
                        showTouhokuden();
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

    private void showKanden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_kanden, (ViewGroup)findViewById(R.id.infoKanden));
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

    private void showYonden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_yonden, (ViewGroup)findViewById(R.id.infoYonden));
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

    private void showEnergia(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_energia, (ViewGroup)findViewById(R.id.infoEnergia));
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

    private void showKyuden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_kyuden, (ViewGroup)findViewById(R.id.infoKyuden));
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

    private void showChuden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_chuden, (ViewGroup)findViewById(R.id.infoChuden));
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

    private void showRikuden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_rikuden, (ViewGroup)findViewById(R.id.infoRikuden));
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

    private void showTouden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_touden, (ViewGroup)findViewById(R.id.infoTouden));
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

    private void showTouhokuden(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_touhokuden, (ViewGroup)findViewById(R.id.infoTouhokuden));
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
}
