package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

//import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tadakazu on 2016/07/18.
 */
public class DataActivity extends AppCompatActivity {
    protected DataActivity mActivity = null;
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
    //showTel2()ダイアログ閉じるための制御用
    private AlertDialog mDialogShowTel2 = null;
    //showUpdateTel()データ呼び出して対応スピナー当てはめ用
    private boolean mInitSpinner = false;
    private int _syozokuPos = 0;
    //削除選択データid格納用
    private ArrayList<String> deleteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_data);

        //ボタンリスナー設定
        initButtons();
    }

    //ボタン設定
    private void initButtons(){

        //ボタン設定
        //復帰用ボタン
        mView.findViewById(R.id.btnEarthquake).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, EarthquakeActivity.class);
                startActivity(intent);
            }
        });

        //連絡網データ作成
        mListView = new ListView(this);
        mDBHelper = new DBHelper(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String mKey = sp.getString("key", null);
        db = mDBHelper.getWritableDatabase(mKey);
        //データまとめて削除用ArrayList初期化
        deleteArray = new ArrayList<String>();
        //連絡網データ確認ボタン
        mView.findViewById(R.id.btnTel).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTel();
            }
        });
        //連絡網データ入力ボタン
        mView.findViewById(R.id.btnTelEdit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showEditTel();
            }
        });
        //連絡網データ修正ボタン
        mView.findViewById(R.id.btnTelUpdate).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTel2();
            }
        });
        //連絡網データ削除ボタン
        mView.findViewById(R.id.btnTelDelete).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTel3();
            }
        });
        //CSV読み込みボタン
        mView.findViewById(R.id.btnImport).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showImport();
            }
        });

        //CSV読み込ませ説明書遷移
        mView.findViewById(R.id.btnGuide).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showGuide22();
            }
        });

        //情報ボタン生成
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
        mView.findViewById(R.id.btnWeather).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showWeather();
            }
        });
        mView.findViewById(R.id.btnEarthquakeCaution).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showCaution();
            }
        });
        mView.findViewById(R.id.btnEarthquakeBousaiNet).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showBousaiNet();
            }
        });
    }

    //連絡網データ表示
    private void showTel(){
        //データ準備
        String order;
        order = "select * from records order by _id";
        Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel","mail","kubun","syozoku0","syozoku","kinmu"};
        int[] to = {R.id.record_name,R.id.record_tel,R.id.record_mail,R.id.record_kubun,R.id.record_syozoku0,R.id.record_syozoku,R.id.record_kinmu};
        mActivity.mAdapter = new SimpleCursorAdapter(mActivity,R.layout.record_view,c,from,to,0);
        mListView.setAdapter(mActivity.mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //残念だが以下は機能しない。電話させるautoLink="phone"を優先させる。そのため以下コメントアウトする。
                // ListView listView = (ListView)parent;
                //Cursor i = (Cursor)listView.getItemAtPosition(position);
                //String _id = i.getString(i.getColumnIndex("_id"));
                //String _name = i.getString(i.getColumnIndex("name"));
                //String _tel = i.getString(i.getColumnIndex("tel"));
                //String _mail = i.getString(i.getColumnIndex("mail"));
                //String _kubun = i.getString(i.getColumnIndex("kubun"));
                //String _syozoku = i.getString(i.getColumnIndex("syozoku"));
                //String _kinmu = i.getString(i.getColumnIndex("kinmu"));
                //showUpdateTel(_id, _name, _tel, _mail, _kubun, _syozoku, _kinmu);
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
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //連絡網データ表示　修正用
    private void showTel2(){
        //データ準備
        String order;
        order = "select * from records order by _id";
        Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel","kubun","syozoku0","syozoku","kinmu","mail"};
        int[] to = {R.id.record_name, R.id.record_tel, R.id.record_kubun, R.id.record_syozoku0, R.id.record_syozoku, R.id.record_kinmu, R.id.record_mail};
        mActivity.mAdapter = new SimpleCursorAdapter(mActivity,R.layout.record_view_update,c,from,to,0);
        mListView.setAdapter(mActivity.mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                ListView listView = (ListView)parent;
                Cursor i = (Cursor)listView.getItemAtPosition(position);
                String _id = i.getString(i.getColumnIndex("_id"));
                String _name = i.getString(i.getColumnIndex("name"));
                String _tel = i.getString(i.getColumnIndex("tel"));
                String _mail = i.getString(i.getColumnIndex("mail"));
                String _kubun = i.getString(i.getColumnIndex("kubun"));
                String _syozoku0 = i.getString(i.getColumnIndex("syozoku0"));
                String _syozoku = i.getString(i.getColumnIndex("syozoku"));
                String _kinmu = i.getString(i.getColumnIndex("kinmu"));
                //選択された行のデータを送る
                showUpdateTel(_id, _name, _tel, _mail, _kubun, _syozoku0, _syozoku, _kinmu);
                //明示的に消さないとあとで空のダイアログが残る
                mDialogShowTel2.dismiss();
            }
        });
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修正するデータを選択");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        mDialogShowTel2 = builder.show(); //このやり方は知らなかった
    }

    //連絡網データ表示　削除用
    private void showTel3(){
        //データ準備
        final String order = "select * from records order by _id";
        final Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel","kubun","syozoku0","syozoku","kinmu","mail"};
        int[] to = {R.id.record_name,R.id.record_tel, R.id.record_kubun, R.id.record_syozoku0, R.id.record_syozoku, R.id.record_kinmu, R.id.record_mail};
        //初回のみ起動。そうしないと、すべて選択した後の２回目がまたnewされて意味ない
        if (mAdapter2 == null) {
            mActivity.mAdapter2 = new CustomCursorAdapter(mActivity, R.layout.record_view_delete2, c, from, to, 0);
        }
        mListView.setAdapter(mActivity.mAdapter2);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Integer itemcount = mListView.getCount();
        Toast.makeText(mActivity, "データ件数："+String.valueOf(itemcount), Toast.LENGTH_SHORT).show();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //タップした位置のデータをチェック処理
                mActivity.mAdapter2.clickData(position, view);
            }
        });
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("削除するデータを選択");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
        builder.setPositiveButton("削除", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //第一段階　削除対象リストに格納
                //CustomCursorAdapterのメンバ変数であるitemCheckedを見に行く
                c.moveToFirst(); //カーソルを先頭に
                for (int i=0; i < mAdapter2.itemChecked.size(); i++){
                    //チェックされていたら対応するカーソルの_idを削除対象文字列に格納
                    if (mAdapter2.itemChecked.get(i)){
                        deleteArray.add(c.getString(c.getColumnIndex("_id")));
                    }
                    c.moveToNext();
                }
                //第二段階　削除実行
                if ( deleteArray.get(0) != null ) {
                    for(int i=0;i< deleteArray.size(); i++) {
                        mActivity.mDBHelper.delete(db, deleteArray.get(i));
                    }
                    Toast.makeText(mActivity, "データを削除しました。", Toast.LENGTH_SHORT).show();
                    //削除結果を見せるため再度呼び出し
                    mAdapter2 = null;
                    showTel3();
                } else {
                    Toast.makeText(mActivity, "データが選択されていません。", Toast.LENGTH_SHORT).show();
                    //再度呼び出し
                    showTel3();
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
                showTel3();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                mAdapter2 = null;
            }
        });
        builder.setCancelable(true);
        builder.create();
        mDialogShowTel2 = builder.show(); //このやり方は知らなかった
    }

    //連絡網データ入力
    private void showEditTel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新規データ入力");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.tel_edit, (ViewGroup)findViewById(R.id.telEdit));
        //データ取得準備
        final EditText editName = (EditText)layout.findViewById(R.id.editName);
        final EditText editTel  = (EditText)layout.findViewById(R.id.editTel);
        final EditText editMail = (EditText)layout.findViewById(R.id.editMail);
        final Spinner  editKubun = (Spinner)layout.findViewById(R.id.editKubun);
        final Spinner  editSyozoku = (Spinner)layout.findViewById(R.id.editSyozoku);
        final Spinner  editSyozoku2 = (Spinner)layout.findViewById(R.id.editSyozoku2);
        //親所属スピナー選択時の処理
        editSyozoku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //親所属スピナーの選択した位置をint取得
                int i = parent.getSelectedItemPosition();
                //Toast.makeText(mActivity, String.valueOf(i)+"番目を選択", Toast.LENGTH_SHORT).show();
                //取得したintを配列リソース名に変換し、配列リソースIDを取得（なぜか日本語ではエラーが出るのでアルファベットと数字で対応））
                mSelected = "firestation"+ String.valueOf(i);
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
        final Spinner  editKinmu = (Spinner)layout.findViewById(R.id.editKinmu);
        builder.setView(layout);
        builder.setPositiveButton("登録", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String name = editName.getText().toString();
                String tel  = editTel.getText().toString();
                String mail = editMail.getText().toString();
                String kubun = (String)editKubun.getSelectedItem();
                String syozoku0 = (String)editSyozoku.getSelectedItem();
                String syozoku = (String)editSyozoku2.getSelectedItem();
                String kinmu = (String)editKinmu.getSelectedItem();
                mActivity.mDBHelper.insert(db, name, tel, mail, kubun, syozoku0, syozoku, kinmu);
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //連絡網データ修正
    private void showUpdateTel(String _id, String _name, String _tel, String _mail, String _kubun, String _syozoku0, String _syozoku, String _kinmu){
        final String id = _id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("データ修正");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.tel_edit, (ViewGroup)findViewById(R.id.telEdit));
        //データ取得準備
        final EditText editName = (EditText)layout.findViewById(R.id.editName);
        final EditText editTel  = (EditText)layout.findViewById(R.id.editTel);
        final EditText editMail = (EditText)layout.findViewById(R.id.editMail);
        final Spinner  editKubun = (Spinner)layout.findViewById(R.id.editKubun);
        final Spinner  editSyozoku = (Spinner)layout.findViewById(R.id.editSyozoku);
        final Spinner  editSyozoku2 = (Spinner)layout.findViewById(R.id.editSyozoku2);
        final Spinner  editKinmu = (Spinner)layout.findViewById(R.id.editKinmu);
        //送られてきたデータをはめこむ
        editName.setText(_name);
        editTel.setText(_tel);
        editMail.setText(_mail);
        //非常招集区分のスピナー初期値設定
        //tel_editのレイアウトでエントリーを設定しており、以下、不要と思われるので複数行コメントアウト
        /*int _kubunPos=0;
        if (_kubun.equals("１号招集")){ _kubunPos =0; }
        if (_kubun.equals("２号招集")){ _kubunPos =1; }
        if (_kubun.equals("３号招集")){ _kubunPos =2; }
        if (_kubun.equals("４号招集")){ _kubunPos =3; }
        editKubun.setSelection(_kubunPos);
        //所属のスピナー初期設定
        int _syozokuParentPos=0;
        _syozokuPos=0;
        mInitSpinner = true;
        if (_syozoku.equals("総務課")){ _syozokuParentPos=0; _syozokuPos=0;}
        if (_syozoku.equals("人事課")){ _syozokuParentPos=0; _syozokuPos=1;}
        if (_syozoku.equals("施設課")){ _syozokuParentPos=0; _syozokuPos=2;}
        if (_syozoku.equals("予防課")){ _syozokuParentPos=0; _syozokuPos=3;}
        if (_syozoku.equals("規制課")){ _syozokuParentPos=0; _syozokuPos=4;}
        if (_syozoku.equals("警防課")){ _syozokuParentPos=0; _syozokuPos=5;}
        if (_syozoku.equals("司令課")){ _syozokuParentPos=0; _syozokuPos=6;}
        if (_syozoku.equals("救急課")){ _syozokuParentPos=0; _syozokuPos=7;}
        if (_syozoku.equals("北本署")){ _syozokuParentPos=1; _syozokuPos=0;}
        if (_syozoku.equals("梅田"))  { _syozokuParentPos=1; _syozokuPos=1;}
        if (_syozoku.equals("浮田"))  { _syozokuParentPos=1; _syozokuPos=2;}
        if (_syozoku.equals("南森町")){ _syozokuParentPos=1; _syozokuPos=3;}
        if (_syozoku.equals("与力"))  { _syozokuParentPos=1; _syozokuPos=4;}
        if (_syozoku.equals("大淀町")){ _syozokuParentPos=1; _syozokuPos=5;}
        if (_syozoku.equals("本庄"))  { _syozokuParentPos=1; _syozokuPos=6;}
        if (_syozoku.equals("都島本署")){ _syozokuParentPos=2; _syozokuPos=0;}
        if (_syozoku.equals("高倉"))  { _syozokuParentPos=2; _syozokuPos=1;}
        if (_syozoku.equals("東野田")){ _syozokuParentPos=2; _syozokuPos=2;}
        if (_syozoku.equals("福島本署")){ _syozokuParentPos=3; _syozokuPos=0;}
        if (_syozoku.equals("上福島")){ _syozokuParentPos=3; _syozokuPos=1;}
        if (_syozoku.equals("海老江")){ _syozokuParentPos=3; _syozokuPos=2;}
        if (_syozoku.equals("此花本署")){ _syozokuParentPos=4; _syozokuPos=0;}
        if (_syozoku.equals("桜島")){ _syozokuParentPos=4; _syozokuPos=1;}
        if (_syozoku.equals("西九条")){ _syozokuParentPos=4; _syozokuPos=2;}
        if (_syozoku.equals("中央本署")){ _syozokuParentPos=5; _syozokuPos=0;}
        if (_syozoku.equals("東雲")){ _syozokuParentPos=5; _syozokuPos=1;}
        if (_syozoku.equals("道頓堀")){ _syozokuParentPos=5; _syozokuPos=2;}
        if (_syozoku.equals("南坂町")){ _syozokuParentPos=5; _syozokuPos=3;}
        if (_syozoku.equals("上町")){ _syozokuParentPos=5; _syozokuPos=4;}
        if (_syozoku.equals("西本署")){ _syozokuParentPos=6; _syozokuPos=0;}
        if (_syozoku.equals("江戸堀")){ _syozokuParentPos=6; _syozokuPos=1;}
        if (_syozoku.equals("新町")){ _syozokuParentPos=6; _syozokuPos=2;}
        if (_syozoku.equals("港本署")){ _syozokuParentPos=7; _syozokuPos=0;}
        if (_syozoku.equals("田中")){ _syozokuParentPos=7; _syozokuPos=1;}
        if (_syozoku.equals("大正本署")){ _syozokuParentPos=8; _syozokuPos=0;}
        if (_syozoku.equals("泉尾")){ _syozokuParentPos=8; _syozokuPos=1;}
        if (_syozoku.equals("鶴町")){ _syozokuParentPos=8; _syozokuPos=2;}
        if (_syozoku.equals("天王寺本署")){ _syozokuParentPos=9; _syozokuPos=0;}
        if (_syozoku.equals("元町")){ _syozokuParentPos=9; _syozokuPos=1;}
        if (_syozoku.equals("浪速本署")){ _syozokuParentPos=10; _syozokuPos=0;}
        if (_syozoku.equals("恵美須")){ _syozokuParentPos=10; _syozokuPos=1;}
        if (_syozoku.equals("立葉")){ _syozokuParentPos=10; _syozokuPos=2;}
        if (_syozoku.equals("浪速出張所")){ _syozokuParentPos=10; _syozokuPos=3;}
        if (_syozoku.equals("西淀川本署")){ _syozokuParentPos=11; _syozokuPos=0;}
        if (_syozoku.equals("佃")){ _syozokuParentPos=11; _syozokuPos=1;}
        if (_syozoku.equals("大和田")){ _syozokuParentPos=11; _syozokuPos=2;}
        if (_syozoku.equals("竹島")){ _syozokuParentPos=11; _syozokuPos=3;}
        if (_syozoku.equals("淀川本署")){ _syozokuParentPos=12; _syozokuPos=0;}
        if (_syozoku.equals("十三橋")){ _syozokuParentPos=12; _syozokuPos=1;}
        if (_syozoku.equals("加島")){ _syozokuParentPos=12; _syozokuPos=2;}
        if (_syozoku.equals("東三国")){ _syozokuParentPos=12; _syozokuPos=3;}
        if (_syozoku.equals("東淀川本署")){ _syozokuParentPos=13; _syozokuPos=0;}
        if (_syozoku.equals("豊里")){ _syozokuParentPos=13; _syozokuPos=1;}
        if (_syozoku.equals("小松")){ _syozokuParentPos=13; _syozokuPos=2;}
        if (_syozoku.equals("井高野")){ _syozokuParentPos=13; _syozokuPos=3;}
        if (_syozoku.equals("柴島")){ _syozokuParentPos=13; _syozokuPos=4;}
        if (_syozoku.equals("西淡路")){ _syozokuParentPos=13; _syozokuPos=5;}
        if (_syozoku.equals("東成本署")){ _syozokuParentPos=14; _syozokuPos=0;}
        if (_syozoku.equals("中本")){ _syozokuParentPos=14; _syozokuPos=1;}
        if (_syozoku.equals("深江")){ _syozokuParentPos=14; _syozokuPos=2;}
        if (_syozoku.equals("生野本署")){ _syozokuParentPos=15; _syozokuPos=0;}
        if (_syozoku.equals("勝山")){ _syozokuParentPos=15; _syozokuPos=1;}
        if (_syozoku.equals("中川")){ _syozokuParentPos=15; _syozokuPos=2;}
        if (_syozoku.equals("巽")){ _syozokuParentPos=15; _syozokuPos=3;}
        if (_syozoku.equals("旭本署")){ _syozokuParentPos=16; _syozokuPos=0;}
        if (_syozoku.equals("新森")){ _syozokuParentPos=16; _syozokuPos=1;}
        if (_syozoku.equals("赤川")){ _syozokuParentPos=16; _syozokuPos=2;}
        if (_syozoku.equals("城東本署")){ _syozokuParentPos=17; _syozokuPos=0;}
        if (_syozoku.equals("放出")){ _syozokuParentPos=17; _syozokuPos=1;}
        if (_syozoku.equals("中浜")){ _syozokuParentPos=17; _syozokuPos=2;}
        if (_syozoku.equals("関目")){ _syozokuParentPos=17; _syozokuPos=3;}
        if (_syozoku.equals("鶴見本署")){ _syozokuParentPos=18; _syozokuPos=0;}
        if (_syozoku.equals("今津")){ _syozokuParentPos=18; _syozokuPos=1;}
        if (_syozoku.equals("茨田")){ _syozokuParentPos=18; _syozokuPos=2;}
        if (_syozoku.equals("住之江本署")){ _syozokuParentPos=19; _syozokuPos=0;}
        if (_syozoku.equals("平林")){ _syozokuParentPos=19; _syozokuPos=1;}
        if (_syozoku.equals("加賀屋")){ _syozokuParentPos=19; _syozokuPos=2;}
        if (_syozoku.equals("南港")){ _syozokuParentPos=19; _syozokuPos=3;}
        if (_syozoku.equals("阿倍野本署")){ _syozokuParentPos=20; _syozokuPos=0;}
        if (_syozoku.equals("清明通")){ _syozokuParentPos=20; _syozokuPos=1;}
        if (_syozoku.equals("阪南")){ _syozokuParentPos=20; _syozokuPos=2;}
        if (_syozoku.equals("住吉本署")){ _syozokuParentPos=21; _syozokuPos=0;}
        if (_syozoku.equals("苅田")){ _syozokuParentPos=21; _syozokuPos=1;}
        if (_syozoku.equals("万代")){ _syozokuParentPos=21; _syozokuPos=2;}
        if (_syozoku.equals("東住吉本署")){ _syozokuParentPos=22; _syozokuPos=0;}
        if (_syozoku.equals("北田辺")){ _syozokuParentPos=22; _syozokuPos=1;}
        if (_syozoku.equals("杭全")){ _syozokuParentPos=22; _syozokuPos=2;}
        if (_syozoku.equals("矢田")){ _syozokuParentPos=22; _syozokuPos=3;}
        if (_syozoku.equals("平野本署")){ _syozokuParentPos=23; _syozokuPos=0;}
        if (_syozoku.equals("加美")){ _syozokuParentPos=23; _syozokuPos=1;}
        if (_syozoku.equals("長吉")){ _syozokuParentPos=23; _syozokuPos=2;}
        if (_syozoku.equals("喜連")){ _syozokuParentPos=23; _syozokuPos=3;}
        if (_syozoku.equals("加美正覚寺")){ _syozokuParentPos=23; _syozokuPos=4;}
        if (_syozoku.equals("西成本署")){ _syozokuParentPos=24; _syozokuPos=0;}
        if (_syozoku.equals("海道")){ _syozokuParentPos=24; _syozokuPos=1;}
        if (_syozoku.equals("津守")){ _syozokuParentPos=24; _syozokuPos=2;}
        if (_syozoku.equals("水上")){ _syozokuParentPos=25; _syozokuPos=0;}
        editSyozoku.setSelection(_syozokuParentPos); //親スピナー設定 これでsetOnItemSelectedListenerが作動するので子スピナーのアイテムは設定される
        editSyozoku2.setSelection(_syozokuPos); //子スピナーのポジション設定
        //勤務区分のスピナー初期値設定
        int _kinmuPos=0;
        if (_kinmu.equals("日勤")){ _kinmuPos =0; }
        if (_kinmu.equals("１部")){ _kinmuPos =1; }
        if (_kinmu.equals("２部")){ _kinmuPos =2; }
        editKinmu.setSelection(_kinmuPos);*/
        //親所属スピナー選択時の処理
        editSyozoku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //親所属スピナーの選択した位置をint取得
                int i = parent.getSelectedItemPosition();
                //Toast.makeText(mActivity, String.valueOf(i)+"番目を選択", Toast.LENGTH_SHORT).show();
                //取得したintを配列リソース名に変換し、配列リソースIDを取得（なぜか日本語ではエラーが出るのでアルファベットと数字で対応））
                mSelected = "firestation"+ String.valueOf(i);
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
                if (mInitSpinner){
                    editSyozoku2.setSelection(_syozokuPos);
                    mInitSpinner = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //nothing to do
            }
        });
        builder.setView(layout);
        builder.setPositiveButton("修正", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String name = editName.getText().toString();
                String tel  = editTel.getText().toString();
                String mail = editMail.getText().toString();
                String kubun = (String)editKubun.getSelectedItem();
                String syozoku0 = (String)editSyozoku.getSelectedItem();
                String syozoku = (String)editSyozoku2.getSelectedItem();
                String kinmu = (String)editKinmu.getSelectedItem();
                mActivity.mDBHelper.update(db, id, name, tel, mail, kubun, syozoku0, syozoku, kinmu);
                Toast.makeText(mActivity, "データを修正しました。", Toast.LENGTH_SHORT).show();
                //修正結果を見せるため再度呼び出し
                showTel2();
            }
        });
        builder.setNeutralButton("削除", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                mActivity.mDBHelper.delete(db, id);
                Toast.makeText(mActivity, "データを削除しました。", Toast.LENGTH_SHORT).show();
                //削除結果を見せるため再度呼び出し
                showTel2();
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //連絡網データ CSVファイルインポート
    private final static int CHOSE_FILE_CODE = 1;
    private void showImport(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CHOSE_FILE_CODE && resultCode == RESULT_OK) {
            String filename = null;
            final Uri uri = data.getData();
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                filename = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                c.close();
            }
            final String filename2 = filename; //以下のダイアログで使うためわざわざfinalで別名で保存
            //本当にファイルを取り込むのか今一度確認する
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("■CSVファイル読込");
            builder.setMessage(filename + "を連絡網に取り込みますか？");
            builder.setPositiveButton("はい", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    importCSV(uri);
                    Toast.makeText(mActivity, "取り込んだファイル："+filename2, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("キャンセル", null);
            builder.setCancelable(true);
            builder.create();
            builder.show();
        }
    }

    private void importCSV(Uri uri){
        if (uri==null)
            return;

        InputStream is = null;
        String name = "";
        String tel = "";
        String mail = "";
        String kubun = "";
        String syozoku0 = "";
        String syozoku = "";
        String kinmu = "";

        try {
            try {
                is = getContentResolver().openInputStream(uri);
                InputStreamReader ir = new InputStreamReader(is,"UTF-8");
                CSVReader csvreader = new CSVReader(ir, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER,0);
                String[] csv;
                while ((csv = csvreader.readNext()) != null) {
                    name = csv[0];
                    tel = csv[1];
                    mail = csv[2];
                    kubun = csv[3];
                    syozoku0 = csv[4];
                    syozoku = csv[5];
                    kinmu = csv[6];
                    mActivity.mDBHelper.insert(db, name, tel, mail, kubun, syozoku0, syozoku, kinmu);
                }
            } finally {
                if (is != null) is.close();
                Toast.makeText(this, "連絡網にデータを読み込みました。", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "CSV読込エラー", Toast.LENGTH_SHORT).show();
        }
    }


    private void showGuide22(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.guide22, (ViewGroup)findViewById(R.id.guide22));
        builder.setView(layout);
        builder.setNegativeButton("閉じる",null);
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

    //情報（気象）
    private void showWeather() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URLをタップしてください");
        //カスタムビュー設定
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.info_weather, (ViewGroup) findViewById(R.id.infoWeather));
        builder.setView(layout);
        builder.setNegativeButton("キャンセル", null);
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
        builder.setNegativeButton("キャンセル", null);
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
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //大阪市防災アプリ
    public void startOsakaBousaiApp() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("jp.ne.goo.bousai.osakaapp");
        try {
            startActivity(intent);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("大阪市防災アプリがありません");
            //カスタムビュー設定
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.info_osakabousaiapp, (ViewGroup) findViewById(R.id.infoOsakaBousai));
            builder.setView(layout);
            builder.setNegativeButton("キャンセル", null);
            builder.setCancelable(true);
            builder.create();
            builder.show();
        }
    }
}
