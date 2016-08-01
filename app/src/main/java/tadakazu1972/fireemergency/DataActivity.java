package tadakazu1972.fireemergency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by tadakazu on 2016/07/18.
 */
public class DataActivity extends AppCompatActivity {
    protected DataActivity mActivity = null;
    protected View mView = null;
    private Spinner mSpn1 = null;
    private Spinner mSpn2 = null;
    private Spinner mSpn3 = null;

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

}
