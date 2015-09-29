package rinat.noteswithdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class AddNote extends AppCompatActivity implements View.OnClickListener {
    Button cancelSaveButton;
    Button saveButton;
    SimpleCursorAdapter scAdapter;

    DB db;
    private static final String TAG = "myLogs";
    String state ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        Intent viewIntent = getIntent();
        String action = viewIntent.getAction();
        if(action.equals("View")) {
            EditText topic = (EditText) findViewById(R.id.addTopicForNote);
            EditText note = (EditText) findViewById(R.id.addTextForNote);
            topic.setText(viewIntent.getStringExtra("Topic"));
            note.setText(viewIntent.getStringExtra("Note"));
            state ="View";
        }

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        cancelSaveButton = (Button) findViewById(R.id.cancelSaveButton);
        cancelSaveButton.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        EditText topic = (EditText) findViewById(R.id.addTopicForNote);
        EditText note = (EditText) findViewById(R.id.addTextForNote);
        String aTopic = topic.getText().toString();
        String aNote = note.getText().toString();
        switch (v.getId()){
            case R.id.saveButton:
            if(aTopic.equals("")|| aNote.equals("")){
                AlertDialog.Builder adb=new AlertDialog.Builder(this);
                adb.setCancelable(false);  // чтобы пользователь не смог нажать назад
                adb.setTitle("Error!");
                adb.setMessage("You are not fill all fields");
                adb.setPositiveButton("Ok", null);
                adb.show();
            }

             else {
                Intent catchIntent = getIntent();
                String action = catchIntent.getAction();
                Log.d(TAG,"action="+action);

             //open connection to DB
                db = new DB(this);
                db.open();
                if (action.equals("View"))
                {
                    long id=catchIntent.getLongExtra("Id",0);
                    db.updRec(id,aTopic, aNote);
                    Log.d("myLog", "id=" + id);
                }
                else{
                    db.addRec(aTopic, aNote);
                }
                db.close();

                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
            }
                break;
            case R.id.cancelSaveButton:
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
                break;
        }




    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}


