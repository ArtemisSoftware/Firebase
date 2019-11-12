package com.titan.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.titan.firebase.models.Note;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {


    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.document("Notebook/My First Note");

    private DocumentSnapshot lastResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
        editTextPriority = findViewById(R.id.edit_text_priority);

        //((Button)findViewById(R.id.btn_save_note)).setOnClickListener(btn_save_note__OnClickListener);
        //((Button)findViewById(R.id.btn_load_note)).setOnClickListener(btn_load_note__OnClickListener);
        //((Button)findViewById(R.id.btn_update_description)).setOnClickListener(btn_update_description__OnClickListener);
        //((Button)findViewById(R.id.btn_delete_description)).setOnClickListener(btn_delete_description__OnClickListener);
        //((Button)findViewById(R.id.btn_delete_note)).setOnClickListener(btn_delete_note__OnClickListener);

        ((Button)findViewById(R.id.btn_add_note)).setOnClickListener(btn_add_note__OnClickListener);
        ((Button)findViewById(R.id.btn_load_notes)).setOnClickListener(btn_load_notes__OnClickListener);

        //executeBatchedWrite();
        executeTransaction();
    }



    /*
    @Override
    protected void onStart() {
        super.onStart();


        Timber.d("onStart...");

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {

                    Timber.e("FirebaseFirestoreException:" + e.toString());
                    return;
                }

                for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                    Note note = dc.getDocument().toObject(Note.class);

                    note.setDocumentId(dc.getDocument().getId());
                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();

                    switch (dc.getType()) {
                        case ADDED:
                            textViewData.append("\nAdded: " + note.getDocumentId() +
                                    "\nOld Index: " + oldIndex + "  New Index: " + newIndex);
                            break;
                        case MODIFIED:
                            textViewData.append("\nModified: " + note.getDocumentId() +
                                    "\nOld Index: " + oldIndex + "  New Index: " + newIndex);
                            break;
                        case REMOVED:
                            textViewData.append("\nRemoved: " + note.getDocumentId() +
                                    "\nOld Index: " + oldIndex + "  New Index: " + newIndex);
                            break;
                    }
                }

                Timber.d("Loaded onStart");
            }
        });

    }
*/


    public void addNote(View v) {

        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

        Note note = new Note(editTextTitle.getText().toString(), editTextDescription.getText().toString(), priority);

        notebookRef.add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
                        Timber.d("Note saved: " + documentReference);
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

    public void loadNotes(View v) {

        Query query;

        if(lastResult == null){
            query = notebookRef.orderBy("priority")
                    .limit(3);
        }
        else {
            query = notebookRef.orderBy("priority")
                    .startAfter(lastResult)
                    .limit(3);
        }

        query.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String data = "";

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());

                        data += "ID: " + note.getDocumentId()
                                + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription()
                                + "\nPriority: " + note.getPriority() + "\n\n";
                    }

                    if (queryDocumentSnapshots.size() > 0) {
                        data += "___________\n\n";
                        textViewData.append(data);

                        lastResult = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                }
            });
    }




    public void saveNote(View v) {

        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

        Note note = new Note(editTextTitle.getText().toString(), editTextDescription.getText().toString(), priority);


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


    private void executeBatchedWrite() {

        WriteBatch batch = db.batch();

        DocumentReference doc1 = notebookRef.document("New note");
        batch.set(doc1, new Note("New Note", "New Note", 1));

        DocumentReference doc2 = notebookRef.document("8iULAw5yVBf9E3aV8LXu");
        batch.update(doc2, "title", "Updated note");

        DocumentReference doc3 = notebookRef.document("4MEJwQknrnTMVMDJvDAH");
        batch.delete(doc3);

        DocumentReference doc4 = notebookRef.document();
        batch.set(doc4, new Note("Added Note", "Added Note", 1));

        batch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Timber.e("Error batch: " + e.toString());
                textViewData.setText(e.toString());
            }
        });
    }


    private void executeTransaction() {
        db.runTransaction(new Transaction.Function<Long>() {


            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentReference exampleNoteRef = notebookRef.document("New note");

                DocumentSnapshot exampleNoteSnapshot = transaction.get(exampleNoteRef);
                long newPriority = exampleNoteSnapshot.getLong("priority") + 1;
                transaction.update(exampleNoteRef, "priority", newPriority);
                return newPriority;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                Timber.d("New Priority: " + result);
            }
        });
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

    Button.OnClickListener btn_add_note__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Adding note...");
            addNote(v);
        }
    };

    Button.OnClickListener btn_load_notes__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timber.d("Loading note...");
            loadNotes(v);
        }
    };

}
