package com.gmail.krawiec.teodor.guessmynumber;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int myNumber = 50, userNumber, guessCount = 0, numberRange = 100;
    String listOfGuesses = "", massage;
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        sharedPreferences = getSharedPreferences("com.gmail.krawiec.teodor.guessmynumber", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        textView2.setText("Best score: " + sharedPreferences.getInt("BestScore", 100));
        textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
        numberRange = sharedPreferences.getInt("NumberRange", 100);
        myNumber = rand.nextInt(numberRange) + 1;
        EditText editText2 = (EditText)findViewById(R.id.editText2);
        editText2.setText(numberRange+"");
    }

    public void takeTheGuess(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);

        if (editText.getText().toString().equals("")) {

        } else {
            guessCount++;
            userNumber = Integer.parseInt(editText.getText().toString());
            String massage;

            if (userNumber > myNumber) {
                massage = "My number is lesser than yours.";
                textView5.setText("Last guess was: too big");
            } else if (userNumber < myNumber) {
                massage = "My number is bigger than yours.";
                textView5.setText("Last guess was: too small");
            } else {
                massage = "Congrats! You guessed my number";
                textView5.setText("You nailed it! My number is " + myNumber);
                if (guessCount < sharedPreferences.getInt("BestScore", 100)) {
                    editor.putInt("SecondBestScore", sharedPreferences.getInt("BestScore", 100));
                    editor.putInt("BestScore", guessCount);
                    editor.commit();
                    textView2.setText("Best score: " + sharedPreferences.getInt("BestScore", 100));
                    textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
                } else if (guessCount < sharedPreferences.getInt("SecondBestScore", 100)) {
                    editor.putInt("SecondBestScore", guessCount);
                    editor.commit();
                    textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
                }
            }

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, massage, duration);
            toast.show();

            textView.setText("Times guessed: " + guessCount);
            listOfGuesses += userNumber + ", ";
            editor.putString("GuessList", "Your guesses: " + listOfGuesses);
            editor.commit();
            textView4.setText(sharedPreferences.getString("GuessList", "error"));
            editText.setText(null);
        }
    }

    public void resetBestScore(View view) {
        resetBestScoreCode();
    }

    public void resetBestScoreCode(){
        editor.putInt("BestScore", 100);
        editor.putInt("SecondBestScore", 100);
        editor.commit();
        textView2.setText("Best score: " + sharedPreferences.getInt("BestScore", 100));
        textView3.setText("Second best score: " + sharedPreferences.getInt("SecondBestScore", 100));
    }

    public void setCustomRange(View view) {
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        if (!editText2.getText().toString().equals("") && !editText2.getText().toString().equals("0")) {
            numberRange = Integer.parseInt(editText2.getText().toString());
            editor.putInt("NumberRange", numberRange);
            newGameCode();
            resetBestScoreCode();
        } else {
            //   Toast toast = new Toast.makeText();
        }
    }

    public void newGame(View view) {
        newGameCode();
    }

    public void newGameCode() {
        myNumber = rand.nextInt(numberRange) + 1;
        guessCount = 0;
        listOfGuesses = "";
        textView.setText("Times guessed: " + guessCount);
        editor.putString("GuessList", "Your guesses: ");
        editor.commit();
        textView4.setText(sharedPreferences.getString("GuessList", "error"));
        textView5.setText("Last guess was: ");
    }
}
