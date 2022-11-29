package com.example.protein_pricer;

import static com.example.protein_pricer.R.id.logout_button;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.protein_pricer.SearchHandler.LocalBinder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText search_box;
    Button search_button;
    TextView search_page_title;
    Button logout_button;
    Button saved_button;

    String term = "";

    SearchHandler searchHandler = new SearchHandler();
    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link page elements
        search_box = (EditText)findViewById(R.id.search_box);
        search_button = (Button)findViewById(R.id.search_button);
        search_page_title = (TextView)findViewById(R.id.search_page_title);
        logout_button = (Button)findViewById(R.id.logout_button);
        saved_button = (Button)findViewById(R.id.saved_button);

        // Setup Buttons
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update parameters
                term = search_box.getText().toString();
                searchHandler.resetResults();

                // Perform search
                try {
                    searchHandler.search(term, 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Go to next page
                opensearchResult();
            }
        });


        // Connect to Search Handler service
        Intent intentSH = new Intent(this, SearchHandler.class);
        bindService(intentSH, serviceConnection, Context.BIND_AUTO_CREATE);
        System.out.println("1st page connected: " + isConnected);

        // To do a search
        /*
        try {
            searchHandler.search("pizza", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
         */

        //System.out.println("success: " + searchHandler.getResults().toString());
        //searchHandler.printItems();

        // After searching, access each food item by using:
        /*
        ArrayList<FoodItem> search_results = searchHandler.getItems();
        for(int i = 0; i < search_results.size(); i++){

            FoodItem item = search_results.get(i);

            // Then call the specific method to get specific field
            // For example:
            System.out.println("-------------------");
            System.out.println("id: " + item.getId());
            System.out.println("desc: " + item.getDescription());
            System.out.println("brand: " + item.getBrand());
            System.out.println("serving: " + item.getServing());
            System.out.println("protein " + item.getProtein());
            System.out.println("carbs " + item.getCarbs());
            System.out.println("fat " + item.getFat());
            System.out.println("energy " + item.getCalories());
            System.out.println("===================");
        }

         */
    }

    public void opensearchResult(){
        Intent intentNext = new Intent(this, SearchResultsActivity.class);
        startActivity(intentNext);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalBinder binder = (LocalBinder) iBinder;
            searchHandler = binder.getBoundService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
        }
    };
}