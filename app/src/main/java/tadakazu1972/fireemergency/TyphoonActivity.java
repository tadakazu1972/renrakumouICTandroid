package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TyphoonActivity extends AppCompatActivity {
    protected View mView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_typhoon);
        initButtons();
    }

    //ボタン設定
    private void initButtons(){
        mView.findViewById(R.id.btnTyphoon1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showTyphoon1();
            }
        });
        mView.findViewById(R.id.btnTyphoonRiver).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showRiver();
            }
        });
        mView.findViewById(R.id.btnTyphoonWeather).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showWeather();
            }
        });
        mView.findViewById(R.id.btnTyphoonSea).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showSea();
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

    //情報（河川）
    private void showRiver(){
        final CharSequence[] actions = {"■国交省　川の防災情報","http://www.river.go.jp/kawabou/ipRadar.do?areaCd=86&prefCd=&townCd=&gamenId=01-0706&fldCtlParty=no","■大阪府　河川情報","http://www.osaka-kasen-portal.net/suibou/index.html","■気象庁　洪水予報","http://www.jma.go.jp/jp/flood/"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("タップすると各サイトを表示します");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    //{}スコープを切るのはuri,iが同じ変数を使うため
                    //国交省などタイトルの文字をクリックしてもリンク先に飛ぶようにしている
                    case 0: {
                        Uri uri = Uri.parse(actions[1].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                        break;
                    case 1: {
                        Uri uri = Uri.parse(actions[1].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                        break;
                    case 2:{
                        Uri uri = Uri.parse(actions[3].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                        break;
                    case 3:{
                        Uri uri = Uri.parse(actions[3].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                        break;
                    case 4:{
                        Uri uri = Uri.parse(actions[5].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                        break;
                    case 5:{
                        Uri uri = Uri.parse(actions[5].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
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

    //情報（気象）
    public void showWeather(){
        final CharSequence[] actions = {"■気象庁","http://www.jma.go.jp/jma/index.html","■建設局　降雨情報","http://www.ame.city.osaka.lg.jp/pweb/"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("タップすると各サイトを表示します");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    //{}スコープを切るのはuri,iが同じ変数を使うため
                    //国交省などタイトルの文字をクリックしてもリンク先に飛ぶようにしている
                    case 0: {
                        Uri uri = Uri.parse(actions[1].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                    break;
                    case 1: {
                        Uri uri = Uri.parse(actions[1].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                    break;
                    case 2:{
                        Uri uri = Uri.parse(actions[3].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                    break;
                    case 3:{
                        Uri uri = Uri.parse(actions[3].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
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

    //情報（潮位）
    public void showSea(){
        final CharSequence[] actions = {"■気象庁　潮位情報","http://www.data.jma.go.jp/gmd/kaiyou/db/tide/suisan/suisan.php?stn=OS"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("タップすると各サイトを表示します");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch(which){
                    //{}スコープを切るのはuri,iが同じ変数を使うため
                    //国交省などタイトルの文字をクリックしてもリンク先に飛ぶようにしている
                    case 0: {
                        Uri uri = Uri.parse(actions[1].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                    break;
                    case 1: {
                        Uri uri = Uri.parse(actions[1].toString());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
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
