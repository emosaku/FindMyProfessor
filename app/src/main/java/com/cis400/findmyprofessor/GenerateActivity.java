package com.cis400.findmyprofessor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class GenerateActivity extends AppCompatActivity implements View.OnClickListener{

    //Where we have the catalogs
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Button back;

    RecyclerView recyclerView;
    ArrayList<Professor> profList;
    DatabaseReference databaseReference;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_activity);

        back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycleview);
        //databaseReference = FirebaseDatabase.getInstance().getReference("___");
        profList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this, profList);
        recyclerView.setAdapter(adapter);

        //Get data from Firestore
        firestore.collection("profCatalog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot returnedProfessors = task.getResult();

                    //Loop to add professors
                    /*
                    for(QueryDocumentSnapshot doc : returnedProfessors){
                        //Add name, email, office, title
                        profList.add(new Professor(doc.getId(), doc.getString("Email"),
                                                    doc.getString("Office"), doc.getString("Title")));
                    }
                    */

                    //Just print first 4 professors
                    List<DocumentSnapshot> documentSnapshotList = returnedProfessors.getDocuments();
                    for(int i=0; i<4; i++){
                        DocumentSnapshot doc = documentSnapshotList.get(i);
                        //Add name, email, office, title
                        profList.add(new Professor(doc.getId(), doc.getString("Email"),
                                doc.getString("Office"), doc.getString("Title")));
                    }

                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(GenerateActivity.this,"Something Went Wrong Retrieving Professors.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                //Take us back to home page
                startActivity(new Intent(this, HomePage.class));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

}
