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
        builder.setMessage("１号招集\n\n全署");
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
        builder.setMessage("４号招集\n\n全署");
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
        builder.setMessage("第５非常警備\n\n全署");
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
        builder.setMessage("第５非常警備\n\n全署");
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
        builder.setMessage("第５非常警備\n\n北、都島、福島、此花、中央、西淀川、淀川、東淀川、東成、生野、旭、城東、鶴見、住之江、住吉、東住吉、平野");
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
        builder.setMessage("第５非常警備\n\n此花、港、大正、西淀川、住之江、水上");
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
        builder.setMessage("第５非常警備\n\n北、都島、福島、此花、中央、西、港、大正、浪速、西淀川、淀川、住之江、西成、水上");
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
        builder.setMessage("第５非常警備\n\n北、都島、福島、此花、中央、西、港、大正、浪速、西淀川、淀川、住之江、西成、水上");
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
        final CharSequence[] actions = {"■氾濫注意水位　2.7→第5非常警備","■準備情報発令の見込み（1時間以内に5.4に到達）→4号招集","■避難準備情報　5.4→3号招集、4号招集","■避難勧告　5.5→2号招集、3号招集、4号招集","■避難指示8.3→2号招集"};
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
        builder.setMessage("第５非常警備\n\n北、都島、福島、此花、西淀川、淀川、東淀川、旭");
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
        builder.setTitle("■淀川（枚方） 準備情報発令の見込み（1時間以内に5.4に到達）");
        builder.setMessage("４号招集\n\n北、都島、福島、此花、西淀川、淀川、東淀川、旭");
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
        builder.setMessage("３号招集、４号招集\n\n流域署3号：北、都島、福島、此花、西淀川、淀川、東淀川、旭\n\n流域周辺署4号：中央、西、浪速、東成、生野、城東、鶴見、西成");
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
        builder.setTitle("■淀川（枚方） 避難韓国　5.5");
        builder.setMessage("２号招集、３号招集、４号招集\n\n流域署2号：北、都島、福島、此花、西淀川、淀川、東淀川、旭\n\n流域周辺署3号：中央、西、浪速、東成、生野、城東、鶴見、西成\n\nその他の署4号：港、大正、天王寺、阿倍野、住之江、住吉、東住吉、平野、水上");
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
        builder.setMessage("２号招集\n\n全署");
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
