package com.example.notes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.notes.adaptater.NotesAdapter;
import com.example.notes.model.Note;

import com.example.notes.R;
import com.example.notes.sqLite.NotesDbHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Code d'envoi de la requête d'ajout d'une note
    private static final int ADD_NOTE_REQUEST_CODE = 1;

    private ListView listView;
    private NotesDbHelper notesDbHelper;
    private Button addBtn;
    private List<Note> notes;

    private NotesAdapter listAdaptater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.notesDbHelper == null){
            this.notesDbHelper = new NotesDbHelper(this);
        }
        this.setNotes();
        //this.displayListonLog(this.notes);
        if (this.listAdaptater == null){
            this.listAdaptater = new NotesAdapter(this, this.notes);
        }
        this.updateMainLayout();
        this.addBtn= findViewById(R.id.addBtn);
    }

    public void openAddActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
    }

    public void displayListonLog(List<Note> listNotes){
        if (listNotes != null){
            for (Note note: listNotes) {
                Log.i("NOTESLIST",note.getTitre()+" | "+note.getDescription()+" | "+note.getDate().toString());
            }
        }else if (listNotes.size()==0){
            Log.i("NOTESLIST","La liste est vide");
        }

        else{
            Log.i("NOTESLIST","La méthode getAllnotes retourne null!");
        }
    }

    // Gérer le layout quand il n'y a aucunes notes
    public void updateMainLayout(){
        if (this.notes.size() == 0 ){
            setContentView(R.layout.empty_list_layout);
        }else{
            setContentView(R.layout.activity_main);
            this.listView =  findViewById(R.id.mainListView);
            this.listView.setAdapter(this.listAdaptater);
        }
    }

    public void setNotes() {
        try {
            this.notes = this.notesDbHelper.getAllNotes();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == 123) {
            //this.setNotes();
            // Récupérer les données de la note ajoutée depuis AddActivity
            String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddActivity.EXTRA_DESCRIPTION)+"\n";
            Date date = (Date) data.getSerializableExtra(AddActivity.EXTRA_DATE);

            // Créer une instance de Note avec les données
            Note note = new Note(title, description, date);

            // Ajout à la liste de l'addaptater
            this.notes.add(0,note);
            this.listAdaptater.notifyDataSetChanged();
            this.updateMainLayout();

            // Mettre à jour l'adaptateur de la ListView pour refléter les changements

        }
    }

    public NotesDbHelper getNotesDbHelper() {
        return notesDbHelper;
    }
}