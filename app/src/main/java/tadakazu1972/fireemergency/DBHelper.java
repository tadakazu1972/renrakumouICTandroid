package tadakazu1972.fireemergency;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tadakazu on 2016/07/31.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, "myrecords.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //Create Table
        sqLiteDatabase.execSQL("create table records(_id integer primary key autoincrement,name text,tel text,mail text,kubun text,syozoku text,kinmu text)");

        //Init
        sqLiteDatabase.execSQL("insert into records(name,tel,mail,kubun,syozoku,kinmu) values('中道　忠和','09066080765','tadakazu1972@gmail.com','１号招集','与力','日勤')");
        sqLiteDatabase.execSQL("insert into records(name,tel,mail,kubun,syozoku,kinmu) values('大阪　太郎','0662087507','ta-nakamichi@city.osaka.lg.jp','３号招集','大淀','２部')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2){
        //delete
        sqLiteDatabase.execSQL("drop table if exist records");
        //create
        onCreate(sqLiteDatabase);
    }

    public void insert(SQLiteDatabase db, String name, String tel, String mail, String kubun, String syozoku, String kinmu){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("tel", tel);
        cv.put("mail", mail);
        cv.put("kubun", kubun);
        cv.put("syozoku", syozoku);
        cv.put("kinmu", kinmu);
        db.insert("records", null, cv);
    }

    public void update(SQLiteDatabase db, String _id, String name,String tel,String mail,String kubun,String syozoku,String kinmu){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("tel", tel);
        cv.put("mail", mail);
        cv.put("kubun", kubun);
        cv.put("syozoku", syozoku);
        cv.put("kinmu", kinmu);
        db.update("records", cv, "_id = " +_id, null);
    }

    public void delete(SQLiteDatabase db, String _id){
        db.delete("records", "_id = " + _id, null);
    }
}
