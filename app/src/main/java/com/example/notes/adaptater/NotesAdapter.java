package com.example.notes.adaptater;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.activity.MainActivity;
import com.example.notes.model.Note;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends ArrayAdapter<Note> {

    private static final int DELETE_BUTTON_DELAY = 5000; // 5 secondes
    private Handler deleteItemHandler;
    private Runnable deleteItemRunnable;

    public NotesAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Récupérer ou créer la vue
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        // Récupérer la note à la position donnée
        Note note = getItem(position);

        // Mettre à jour la vue avec les données de la note
        EditText titleEditText = convertView.findViewById(R.id.titleEditText);
        EditText descriptionEditText = convertView.findViewById(R.id.descriptionEditText);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);

        Locale locale = new Locale("fr","FR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT,locale);

        titleEditText.setText(note.getTitre());
        descriptionEditText.setText(note.getDescription());
        dateTextView.setText(dateFormat.format(note.getDate()));

        ImageButton deleteItem = convertView.findViewById(R.id.deleteItem);

        // Lors d'un long clic sur l'item
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Afficher le bouton de suppression
                deleteItem.setVisibility(View.VISIBLE);

                // Planifier la disparition du bouton après 5 secondes
                deleteItemHandler = new Handler();
                deleteItemRunnable = new Runnable() {
                    @Override
                    public void run() {
                        deleteItem.setVisibility(View.GONE);
                    }
                };
                deleteItemHandler.postDelayed(deleteItemRunnable, DELETE_BUTTON_DELAY);

                return true;
            }
        });

        // Ajout de l'event sur les éléments contenus dans un item // propagation aux enfants
        titleEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Afficher le bouton de suppression
                deleteItem.setVisibility(View.VISIBLE);

                // Planifier la disparition du bouton après 5 secondes
                deleteItemHandler = new Handler();
                deleteItemRunnable = new Runnable() {
                    @Override
                    public void run() {
                        deleteItem.setVisibility(View.GONE);
                    }
                };
                deleteItemHandler.postDelayed(deleteItemRunnable, DELETE_BUTTON_DELAY);
                return true;
            }
        });

        descriptionEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Afficher le bouton de suppression
                deleteItem.setVisibility(View.VISIBLE);

                // Planifier la disparition du bouton après 5 secondes
                deleteItemHandler = new Handler();
                deleteItemRunnable = new Runnable() {
                    @Override
                    public void run() {
                        deleteItem.setVisibility(View.GONE);
                    }
                };
                deleteItemHandler.postDelayed(deleteItemRunnable, DELETE_BUTTON_DELAY);
                return true;
            }
        });

        // Au clic du bouton de suppression
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity context = (MainActivity) getContext();
                // Supprimer l'item de la ListView
                Note noteToDelete = getItem(position);
                remove(noteToDelete);
                // Supprimer de la base de donnée
                context.getNotesDbHelper().deleteNote(noteToDelete);
                notifyDataSetChanged();
                try {
                    context.getNotesDbHelper().deleteNote(noteToDelete);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // On change le layout si aucunes notes
                context.updateMainLayout();

                // Annuler la disparition du bouton
                deleteItemHandler.removeCallbacks(deleteItemRunnable);
            }
        });

        // Retourner la vue mise à jour
        return convertView;
    }
}