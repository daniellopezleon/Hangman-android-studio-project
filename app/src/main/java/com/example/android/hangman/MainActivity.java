
package com.example.android.hang;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String START_MESSAGE = "Find as many words as possible that can be formed by adding one letter to <big>%s</big> (but that do not contain the substring %s).";
    private com.example.android.hang.Hangdiction dictionary;
    private TextView word;
    int sc=0,max=0;
    private TextView high;
    private TextView score;
    public String result = " ";
    private EditText letter;
    public String currentWord;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.letter);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    checkLetter(editText);
                }
                return true;
            }
        });

        ((Button) findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                int r_id = getResources().getIdentifier("hang0", "drawable", getApplication().getPackageName());
                ((ImageView) findViewById(R.id.hang)).setImageDrawable(getDrawable(r_id));
                currentWord = null;
                currentWord = dictionary.pickGoodStarterWord();
                word = ((TextView) findViewById(R.id.word));
                result = "";
                word.setText("");
                for (int i = 0; i < currentWord.length(); i += 1) {
                    result += "_ ";
                }
                word.setText(result);
                EditText editText = (EditText) findViewById(R.id.letter);
                editText.setEnabled(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        ((Button) findViewById(R.id.solve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word = ((TextView) findViewById(R.id.word));
                word.setText(currentWord);
            }
        });
        ((Button) findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentWord == null) {
                    currentWord = dictionary.pickGoodStarterWord();
                    word = ((TextView) findViewById(R.id.word));
                    for (int i = 0; i < currentWord.length(); i += 1) {
                        result += "_ ";
                    }
                    word.setText(result);
                }
                EditText editText = (EditText) findViewById(R.id.letter);
                editText.setEnabled(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
        });
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new com.example.android.hang.Hangdiction(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void checkLetter(EditText editText) {
        int i=0,flag=0,k=0;
        TextView word = (TextView) findViewById(R.id.word);
        String letter = editText.getText().toString().trim().toLowerCase();
        for(i=0;i<currentWord.length();++i){
            if (currentWord.indexOf(letter,i)!=-1) {
                k = currentWord.indexOf(letter,i);
                result = result.substring(0, 2 * k) + letter+" "+ result.substring(2 * k + 2);
                word.setText(result);
                i=k;
                flag=1;
                if(result.indexOf("_")==-1) {
                    word.setText("You won !!");
                    sc+=1;
                    TextView score = (TextView) findViewById(R.id.score);
                    score.setText(""+sc);
                    if(max<sc){
                        max=sc;
                        TextView high = (TextView) findViewById(R.id.high);
                        high.setText(""+max);
                    }
                }
            }
        }
         if(flag==0){
                count=count+1;
                if(count>=6) {
                    word.setText("You lost !!");
                    sc=0;
                    TextView score = (TextView) findViewById(R.id.score);
                    score.setText(""+sc);
                }
                int r_id = getResources().getIdentifier("hang" + count, "drawable", getApplication().getPackageName());
                ((ImageView) findViewById(R.id.hang)).setImageDrawable(getDrawable(r_id));
         }
         editText.setText("");
    }
}


