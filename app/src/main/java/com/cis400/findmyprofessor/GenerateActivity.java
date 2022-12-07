package com.cis400.findmyprofessor;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenerateActivity extends AppCompatActivity implements View.OnClickListener{

    //Where we have the catalogs
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Button generate_back_button;
    private Button button_email;

    public String filteredKeyWords;
    //public String filteredKeyWords = "Intro to Statistics Guide to Statistics linear algebra";

    public RecyclerView recyclerView;
    public ArrayList<Professor> profList = new ArrayList<>();
    public HashMap<String, Professor> profMap = new HashMap<>(); //Name and Professor object
    public ArrayList<Course> courseList = new ArrayList<>();
    public ArrayList<Professor> displayList = new ArrayList<>();
    public CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_activity);

//        // calling the action bar
//        ActionBar actionBar = getSupportActionBar();
//
//        // showing the back button in action bar
//        actionBar.setDisplayHomeAsUpEnabled(true);

        // back button initializer
        generate_back_button = (Button) findViewById(R.id.generate_back_button);
        generate_back_button.setOnClickListener(this);

        // button email initializer
        button_email = (Button) findViewById(R.id.button_email);
        button_email.setOnClickListener(this);


        // Change color of Status Bar (Top bar)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        //Retrieve filtered words from home page activity
        Intent intent = getIntent();
        if(intent != null) {
            filteredKeyWords = getIntent().getStringExtra("keyFilteredWords");
        }

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this, displayList);
        recyclerView.setAdapter(adapter);

        //Get professor data from Firestore
        firestore.collection("profCatalog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot returnedProfessors = task.getResult();

                            List<DocumentSnapshot> documentSnapshotList = returnedProfessors.getDocuments();
                            for(DocumentSnapshot doc : documentSnapshotList) {

                                //Add name, email, office, title
                                profMap.put(doc.getString("Name"), (new Professor(doc.getString("Name"), doc.getString("Email"),
                                        doc.getString("Office"), doc.getString("Title"), 0)));
                                profList.add(new Professor(doc.getString("Name"), doc.getString("Email"),
                                        doc.getString("Office"), doc.getString("Title"), 0));
                            }
                            //adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(GenerateActivity.this,"Error Retrieving Professors.", Toast.LENGTH_LONG).show();
                       }
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


                            for(DocumentSnapshot doc : documentSnapshotList) {
                                try {
                                    String keywordsStr = Objects.requireNonNull(doc.getData().entrySet().toArray()[0].toString());
                                    String professorsStr = Objects.requireNonNull(doc.getData().entrySet().toArray()[1].toString());

                                    Log.d("TAG", doc.getId() + " => " + Objects.requireNonNull(doc.getData().entrySet().toArray()[0].toString()));
                                    Log.d("TAG", doc.getId() + " => " + Objects.requireNonNull(doc.getData().entrySet().toArray()[1].toString()));
                                    courseList.add(new Course(keywordsStr.split("[\\[\\]]|, "), professorsStr.split("[\\[\\]]|, ")));
                                } catch (NullPointerException e) {
                                    Log.d("TAG", "Null pointer exception receiving from" + doc.getId());
                                }
                            }
                            rankingFunction();
                        }
                        else { Toast.makeText(GenerateActivity.this,"Something Went Wrong Retrieving Professors.", Toast.LENGTH_LONG).show(); }
                    }
                });

        adapter.notifyDataSetChanged();

    }


    /* Rank professors by word match
        We have profMap and courseList
        The professor class can keep track of matches
         */
public void rankingFunction() {

    //Iterate over the filtered text check for matches
    String[] filteredKeyWordsArray =  filteredKeyWords.substring(1, filteredKeyWords.length()-1).split(", ");

    profMap.put("Roger Chen", (new Professor("Roger Chen", "Email",
            "Office", "Title", 0)));

    for(String str : filteredKeyWordsArray) //For every word in filtered text
    {
        Log.d("TAG", "Current str: " + str);
        for(Course course: courseList) { //Check every course for the word
            //Log.d("TAG", "Current course: " + course.getKeywords().toString());
            //Log.d("TAG", "Current course: " + Arrays.asList(course.getProfessors()));
            //if(course.getKeywords().toString().contains(str)) { //If matched with a course
            if(true) { //If matched with a course
                String [] professors = course.getProfessors();
                for(int i=0; i<professors.length; i++) { //Add a point for every prof on the course
                    String prof = professors[i];
                    if(profMap.containsKey(prof)) {
                        Log.d("TAG", "Prof to increment: " + prof);
                        Objects.requireNonNull(profMap.get(prof).keywordMatches++);
                        Log.d("TAG", "New match #: " + Objects.requireNonNull(profMap.get(prof).getKeywordMatches()));
                    }
                }
            }
        }
    }

    Log.d("TAG", "ProfMap: " + profMap.toString());
    Log.d("TAG", "ProfMap Size: " + profMap.size());
    Log.d("TAG", "ProfList: " + profList.toString());
    Log.d("TAG", "ProfList Size: " + profList.size());
    if(profList.size() > 0) Log.d("TAG", "hi: " + profList.get(1).keywordMatches + profList.get(2).keywordMatches + profList.get(3).keywordMatches);

    //Which professors need to be displayed
    for (int i=0; i<profList.size(); i++) {
        if(profMap.get(profList.get(i).name) != null) {
            Professor prof = profMap.get(profList.get(i).name);
            if (prof.keywordMatches > 0) {
                displayList.add(prof);
                Log.d("TAG", "Prof added to displayList: " + prof.toString());
            }
        }
    }

    //Finally, sort the professors in profList descending by points
    Collections.sort(displayList, new Comparator<Professor>() {
        @Override
        public int compare(Professor o1, Professor o2) {
            return Integer.compare(o2.getKeywordMatches(), o1.getKeywordMatches());
        }
    });
    Log.d("TAG", "BRUH|||||||||||||| BRUH ||||||||||||||||||||BRUH");
    Log.d("TAG", "displayList: " + displayList.toString());
    Log.d("TAG", "displayList Size: " + displayList.size());

    adapter.notifyDataSetChanged();

}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_email:
                //Take us to main method
                startActivity(new Intent(GenerateActivity.this, EmailActivity.class));
                break;
            case R.id.generate_back_button:
                //Take us to main method
                startActivity(new Intent(GenerateActivity.this, HomePage.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


