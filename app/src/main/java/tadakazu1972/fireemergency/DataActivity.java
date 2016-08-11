package tadakazu1972.fireemergency;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by tadakazu on 2016/07/18.
 */
public class DataActivity extends AppCompatActivity {
    protected DataActivity mActivity = null;
    protected View mView = null;
    private Spinner mSpn1 = null;
    private Spinner mSpn2 = null;
    private Spinner mSpn3 = null;
    //連絡網データ操作用変数
    protected ListView mListView = null;
    protected DBHelper mDBHelper = null;
    protected SQLiteDatabase db = null;
    protected SimpleCursorAdapter mAdapter = null;
    //連絡網データ入力用　親所属スピナー文字列保存用
    private static String mSelected;
    private static String[] mArray;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_data);

        mSpn1 = (Spinner)findViewById(R.id.spnData1);
        mSpn2 = (Spinner)findViewById(R.id.spnData2);
        mSpn3 = (Spinner)findViewById(R.id.spnData3);

        //保存している基礎データを呼び出してスピナーにセット
        //勤務消防署
        SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        String compareValue1 = sp1.getString("mainStation","消防局"); // 第２引数はkeyが存在しない時に返す初期値
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.firestation, R.layout.custom_spinner_item);
        mSpn1.setAdapter(adapter1);
        if (!compareValue1.equals(null)){
            int spinnerPosition = adapter1.getPosition(compareValue1);
            mSpn1.setSelection(spinnerPosition);
        }
        //大津波・津波警報時指定署
        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(this);
        String compareValue2 = sp2.getString("tsunamiStation", "消防局"); // 第２引数はkeyが存在しない時に返す初期値
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.firestation, R.layout.custom_spinner_item);
        mSpn2.setAdapter(adapter2);
        if (!compareValue2.equals(null)){
            int spinnerPosition = adapter2.getPosition(compareValue2);
            mSpn2.setSelection(spinnerPosition);
        }
        //招集区分
        SharedPreferences sp3 = PreferenceManager.getDefaultSharedPreferences(this);
        String compareValue3 = sp3.getString("kubun", "１"); // 第２引数はkeyが存在しない時に返す初期値
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.kubun, R.layout.custom_spinner_item);
        mSpn3.setAdapter(adapter3);
        if (!compareValue3.equals(null)){
            int spinnerPosition = adapter3.getPosition(compareValue3);
            mSpn3.setSelection(spinnerPosition);
        }
        //スピナーの選択リスナー設定
        mSpn1.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String item = (String)mSpn1.getSelectedItem();
                //保存
                SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(mActivity);
                sp1.edit().putString("mainStation",item).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //何もしない
            }
        });
        mSpn2.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String item = (String)mSpn2.getSelectedItem();
                //保存
                SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(mActivity);
                sp1.edit().putString("tsunamiStation",item).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //何もしない
            }
        });
        mSpn3.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String item = (String)mSpn3.getSelectedItem();
                //保存
                SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(mActivity);
                sp1.edit().putString("kubun",item).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //何もしない
            }
        });

        //連絡網データ作成
        mListView = new ListView(this);
        mDBHelper = new DBHelper(this);
        db = mDBHelper.getWritableDatabase();
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
        //連絡網データ修正/削除ボタン
        mView.findViewById(R.id.btnTelUpdate).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTel2();
            }
        });
        mView.findViewById(R.id.btnImport).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showImport();
            }
        });

        //復帰用ボタン
        mView.findViewById(R.id.btnEarthquake).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, EarthquakeActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnTyphoon).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, TyphoonActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnKokuminhogo).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, KokuminhogoActivity.class);
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btnKinentai).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, KinentaiActivity.class);
                startActivity(intent);
            }
        });
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
                ListView listView = (ListView)parent;
                Cursor i = (Cursor)listView.getItemAtPosition(position);
                String _id = i.getString(i.getColumnIndex("_id"));
                String _name = i.getString(i.getColumnIndex("name"));
                String _tel = i.getString(i.getColumnIndex("tel"));
                String _mail = i.getString(i.getColumnIndex("mail"));
                String _kubun = i.getString(i.getColumnIndex("kubun"));
                String _syozoku = i.getString(i.getColumnIndex("syozoku"));
                String _kinmu = i.getString(i.getColumnIndex("kinmu"));
                showUpdateTel(_id, _name, _tel, _mail, _kubun, _syozoku, _kinmu);
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

    //連絡網データ表示　修正用
    private void showTel2(){
        //データ準備
        String order;
        order = "select * from records order by name desc";
        Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"name","tel"};
        int[] to = {R.id.record_name,R.id.record_tel};
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
                String _syozoku = i.getString(i.getColumnIndex("syozoku"));
                String _kinmu = i.getString(i.getColumnIndex("kinmu"));
                showUpdateTel(_id, _name, _tel, _mail, _kubun, _syozoku, _kinmu);

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
                String syozoku = (String)editSyozoku2.getSelectedItem();
                String kinmu = (String)editKinmu.getSelectedItem();
                mActivity.mDBHelper.insert(db, name, tel, mail, kubun, syozoku, kinmu);
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    //連絡網データ修正・削除
    private void showUpdateTel(String _id, String _name, String _tel, String _mail, String _kubun, String _syozoku, String _kinmu){
        final String id = _id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("データ修正/削除");
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
        //送られてきたデータをはめこむ
        editName.setText(_name);
        editTel.setText(_tel);
        editMail.setText(_mail);
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
        builder.setPositiveButton("修正", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String name = editName.getText().toString();
                String tel  = editTel.getText().toString();
                String mail = editMail.getText().toString();
                String kubun = (String)editKubun.getSelectedItem();
                String syozoku = (String)editSyozoku2.getSelectedItem();
                String kinmu = (String)editKinmu.getSelectedItem();
                mActivity.mDBHelper.update(db, id, name, tel, mail, kubun, syozoku, kinmu);
                Toast.makeText(mActivity, "データを修正しました。", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("削除", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                mActivity.mDBHelper.delete(db, id);
                Toast.makeText(mActivity, "データを削除しました。", Toast.LENGTH_SHORT).show();
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
        try {
            if (requestCode == CHOSE_FILE_CODE && resultCode == RESULT_OK) {
                String filePath = data.getDataString().replace("file://", "");
                String decodedfilePath = URLDecoder.decode(filePath, "utf-8");
                Toast.makeText(mActivity, "filePath=" + decodedfilePath, Toast.LENGTH_LONG).show();
            }
        } catch (UnsupportedEncodingException e) {
                //nothing to do
        }
    }
}
