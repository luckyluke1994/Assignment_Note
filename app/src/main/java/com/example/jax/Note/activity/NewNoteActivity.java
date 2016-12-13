package com.example.jax.Note.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jax.Note.custom.view.GridView.GirdViewAdapter;
import com.example.jax.Note.db.DBHelper;
import com.example.jax.Note.model.ImageItem;
import com.example.jax.Note.model.ItemClickListener;
import com.example.jax.Note.model.NoteInfo;
import com.example.jax.assignment_note.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewNoteActivity extends Activity implements ItemClickListener {

    //photo
    private GridView gridView;
    private GirdViewAdapter grGirdViewAdapter;
    private ArrayList<ImageItem> data = new ArrayList<>();

    //add Item
    private EditText editTitle, editNote;
    private TextView tvTime;
    DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTitle = (EditText)findViewById(R.id.title_textview);
        editNote = (EditText)findViewById(R.id.note_textview);
        tvTime =(TextView)findViewById(R.id.time_textview);

        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        tvTime.setText(currentDateTime);

        gridView = (GridView)findViewById(R.id.grid_image);
        grGirdViewAdapter = new GirdViewAdapter(this,R.layout.grid_item_layout, data);
        gridView.setAdapter(grGirdViewAdapter);



    }

    @Override
    protected void onResume() {
        super.onResume();
        deleteImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                builder.setTitle("Insert Picture");
                final CharSequence[] items ={"Take Photo","Choose Photo"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                cameraIntent();
                                break;
                            case 1:
                                galleryIntent();
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            case R.id.action_choose_color:
                return true;
            case R.id.action_accept:
                addItemNode();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }

    }

    private void addItemNode() {
        NoteInfo noteInfo =new NoteInfo();

        if(!editTitle.getText().toString().isEmpty()){
            noteInfo.title = editTitle.getText().toString();
        } else {
            noteInfo.title = "";
        }

        if(!editNote.getText().toString().isEmpty()){
            noteInfo.note = editNote.getText().toString();
        } else {
            noteInfo.note = "";
        }

        dbHelper.insertNote(noteInfo);

        Intent intent = new Intent(NewNoteActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);

    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                grGirdViewAdapter.addItem(resize);
                grGirdViewAdapter.notifyDataSetChanged();

            } else if (requestCode == 1) {
                Bitmap bitmap = null;
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                getApplicationContext().getContentResolver(), data.getData()
                        );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap resize = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                grGirdViewAdapter.addItem(resize);
                grGirdViewAdapter.notifyDataSetChanged();
            }

        }
    }

    private void deleteImage(){
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure want to delete this ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        grGirdViewAdapter.removeItem(pos);
                        grGirdViewAdapter.notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
                return true;
            }
        });

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void editNote(NoteInfo noteInfo) {

    }
}
