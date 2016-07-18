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
