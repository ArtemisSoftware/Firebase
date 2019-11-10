package com.titan.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.titan.firebase.models.Note;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {


    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("Notebook/My First Note");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);

        ((Button)findViewById(R.id.btn_save_note)).setOnClickListener(btn_save_note__OnClickListener);
        ((Button)findViewById(R.id.btn_load_note)).setOnClickListener(btn_load_note__OnClickListener);
        ((Button)findViewById(R.id.btn_update_description)).setOnClickListener(btn_update_description__OnClickListener);
        ((Button)findViewById(R.id.btn_delete_description)).setOnClickListener(btn_delete_description__OnClickListener);
        ((Button)findViewById(R.id.btn_delete_note)).setOnClickListener(btn_delete_note__OnClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Timber.d("onStart...");

        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {

                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Timber.e("Error while loading: " + e.toString());
                    return;
                }

                if (documentSnapshot.exists()) {

                    Note note = documentSnapshot.toObject(Note.class);
                    textViewData.setText("Title: " + note.getTitle() + "\n" + "Description: " + note.getDescription());

                    Timber.d("Loaded document");
                }
                else{
                    textViewData.setText("");
                }
            }
        });
    }


    public void saveNote(View v) {

        Note note = new Note(editTextTitle.getText().toString(), editTextDescription.getText().toString());

        db.collection("Notebook").document("My First Note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
                        Timber.d("Note saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Timber.e("Failure error: " + e.toString());
                    }
                });
    }


    public void loadNote(View v) {

        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            Note note = documentSnapshot.toObject(Note.class);

                            textViewData.setText("Title: " + note.getTitle() + "\n" + "Description: " + note.getDescription());
                            Timber.d("Received: " + documentSnapshot);
                        } else {
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Timber.d("OnFailure: " + e.toString());
                    }
                });
    }


    public void updateDescription(View v) {
        String description = editTextDescription.getText().toString();

        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, description);
        //noteRef.set(note, SetOptions.merge());

        noteRef.update(KEY_DESCRIPTION, description);
    }


    public void deleteDescription(View v) {
        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, FieldValue.delete());
        //noteRef.update(note);

        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View v) {
        noteRef.delete();
    }





    Button.OnClickListener btn_save_note__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Sending note...");
            saveNote(v);
        }
    };

    Button.OnClickListener btn_load_note__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Loading note...");
            loadNote(v);
        }
    };

    Button.OnClickListener btn_update_description__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Updating description...");
            updateDescription(v);
        }
    };

    Button.OnClickListener btn_delete_description__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Deleting description...");
            deleteDescription(v);
        }
    };

    Button.OnClickListener btn_delete_note__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Deleting note...");
            deleteNote(v);
        }
    };

}
