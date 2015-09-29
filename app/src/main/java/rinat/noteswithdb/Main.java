package rinat.noteswithdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Main extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    DB db;
    SimpleCursorAdapter scAdapter;
    ListView listMain;
    Button addNote;
    final int CM_DELETE_ID =3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new DB(this);
        db.open();

        String[] from = new String[]{DB.COLUMN_TOPIC, DB.COLUMN_NOTE};
        int[] to = new int[]{R.id.topicText, R.id.noteText};

        scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        listMain = (ListView) findViewById(R.id.listMain);
        listMain.setAdapter(scAdapter);

        addNote = (Button) findViewById(R.id.addNoteButton);
        addNote.setOnClickListener(this);
        registerForContextMenu(listMain);

        getSupportLoaderManager().initLoader(0, null, this);


        listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(Main.this, ViewNote.class);
                TextView getTopic = (TextView) view.findViewById(R.id.topicText);
                TextView getNote = (TextView) view.findViewById(R.id.noteText);
                intent.putExtra("Id",id);
                Log.d("myLog","id="+id);
                intent.putExtra("Topic", getTopic.getText());
                intent.putExtra("Note", getNote.getText());
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Delete note"); // Context menu(button DELETE)
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {// action -> when pressed context button Delete
        if (item.getItemId() == CM_DELETE_ID) {
            AlertDialog.Builder adb=new AlertDialog.Builder(this);
            adb.setCancelable(false);  // чтобы пользователь не смог нажать назад
            adb.setTitle("Confirm delete");
            adb.setMessage("Are you sure you want to delete");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                    // получаем инфу о пункте списка
                    AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    // извлекаем id записи и удаляем соответствующую запись в БД
                    db.delRec(acmi.id);
                    // получаем новый курсор с данными
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(Main.this, " Delete canceled", Toast.LENGTH_SHORT).show();
                }
            });
            adb.show();
            return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNoteButton:
                Intent intent = new Intent(this, AddNote.class);
                intent.setAction("Main");
                startActivity(intent);
                break;
            case R.id.settingsButton:
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
          //  try {
         //       TimeUnit.SECONDS.sleep(3);
         //   } catch (InterruptedException e) {
         //       e.printStackTrace();
         //   }
            return cursor;
        }
    }
}