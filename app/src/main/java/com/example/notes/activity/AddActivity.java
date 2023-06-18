package com.example.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import com.example.notes.model.Note;
import com.example.notes.sqLite.NotesDbHelper;

import java.util.Date;

public class AddActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_DATE = "extra_date";

    private static final int RES_CODE = 123;



    private NotesDbHelper notesDbHelper;

    private EditText titleEditText;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        this.notesDbHelper = new NotesDbHelper(this);
        titleEditText = findViewById(R.id.addTitleEditText);
        descriptionEditText = findViewById(R.id.addDescriptionEditText);
    }

    public void onClickSaveBtn(View view){
        saveNote();
    }

    public void onCancelClick(View view){
        cancel();
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        Date date = new Date(); // Date actuelle

        // Créer un Intent pour renvoyer les données de la note à MainActivity
        if(!title.equals("") && !description.equals("")){
            // Création et envoi de l'intent
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_TITLE, title);
            resultIntent.putExtra(EXTRA_DESCRIPTION, description);
            resultIntent.putExtra(EXTRA_DATE, date);
            setResult(RES_CODE, resultIntent);

            // Ajout dans la bdd avant de retourner sur l'activite principale
            Note note = new Note(title,description,date);
            this.notesDbHelper.addNote(note);
            finish();
            Log.i("ADDNOTE","note added to database.");
        }else{
            Toast.makeText(getApplicationContext(),"Veuillez remplir tous les champs!",Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(){
        Intent cancelIntent = new Intent();
        setResult(RESULT_CANCELED,cancelIntent);
        finish();
        Log.i("ADDNOTE","adding note canceled");
    }
}
