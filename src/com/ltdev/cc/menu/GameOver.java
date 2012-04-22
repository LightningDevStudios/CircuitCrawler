package com.ltdev.cc.menu;

import com.ltdev.cc.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GameOver extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);
        
        ImageButton button = (ImageButton)findViewById(R.id.imageButton1);
        OnClickListener click = new OnClickListener()
        {
            public void onClick(View v) 
            {
                finish();
            }
        };
        
        button.setOnClickListener(click);
    }
}
