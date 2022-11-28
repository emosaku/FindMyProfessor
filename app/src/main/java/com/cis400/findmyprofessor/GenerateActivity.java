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
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

public class GenerateActivity extends AppCompatActivity implements View.OnClickListener{

    //Where we have the catalogs
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Button back;

    RecyclerView recyclerView;
    ArrayList<Professor> profList;
    ArrayList<Course> courseList;
    DatabaseReference databaseReference;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_activity);

        back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycleview);
        //databaseReference = FirebaseDatabase.getInstance().getReference("");
        profList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this, profList);
        recyclerView.setAdapter(adapter);

        //Get professor data from Firestore
        firestore.collection("profCatalog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot returnedProfessors = task.getResult();

                    //Grab professor info from DB
                    List<DocumentSnapshot> documentSnapshotList = returnedProfessors.getDocuments();
                    for(int i=0; i<documentSnapshotList.size(); i++){
                        DocumentSnapshot doc = documentSnapshotList.get(i);
                        //Create new Professor name, email, office, title
                        Professor prof = new Professor(doc.getId(), doc.getString("Email"),
                                doc.getString("Office"), doc.getString("Title"), 0);

                        profList.add(prof);
                    }

                    adapter.notifyDataSetChanged();
                }
                else { Toast.makeText(GenerateActivity.this,"Something Went Wrong Retrieving Professors.", Toast.LENGTH_LONG).show(); }
            }
        });

        //Get course and keyword data from Firestore
        firestore.collection("KeyWords Catalog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot returnedCourses = task.getResult();

                            List<DocumentSnapshot> documentSnapshotList = returnedCourses.getDocuments();
                            for(int i=0; i<documentSnapshotList.size(); i++){
                                DocumentSnapshot doc = documentSnapshotList.get(i);

                                //Grab keywords and professors
                                String[] tempKeywords = (String[]) doc.get("keywords");
                                String[] tempProfessors =  (String[]) doc.get("professors");;
                                Course course = new Course(tempKeywords, tempProfessors);

                                courseList.add(course);
                            }
                        }
                        else { Toast.makeText(GenerateActivity.this,"Something Went Wrong Retrieving Professors.", Toast.LENGTH_LONG).show(); }
                    }
                });
    }

    //Retrieve filtered words from home page
    Intent intent = getIntent();
    //Eventually get this in String[] or Arraylist<String>
    String filteredKeyWords = intent.getStringExtra("keyFilteredWords");

    //Ranking
    /*
    We have profList full of Professor (String fullName, String email, String office, String title, Integer keywordMatches)
    We have courseList full of Course objects (String[] keywords, String[] professors)
    We have filteredKeyWords
     */


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
