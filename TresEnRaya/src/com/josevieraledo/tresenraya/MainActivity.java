package com.josevieraledo.tresenraya;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;


import com.josevieraledo.tresenraya.Jugadores;
import com.josevieraledo.tresenraya.ActivityJuego;
import com.josevieraledo.tresenraya.VistaJuego.Estado;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.comienza_jugador).setOnClickListener(
                new OnClickListener() {
            public void onClick(View v) {
                startGame(true);
            }
        });

        findViewById(R.id.comienza_movil).setOnClickListener(
                new OnClickListener() {
            public void onClick(View v) {
                startGame(false);
            }
        });
        
        findViewById(R.id.dos_jugadores).setOnClickListener(
                new OnClickListener() {
            public void onClick(View v) {
                startPlayers(v);
            }
        });
    }

	private void startPlayers(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), Jugadores.class);
        startActivity(intent);		
	}

	private void startGame(boolean startWithHuman) {
        Intent i = new Intent(this, ActivityJuego.class);
        i.putExtra(ActivityJuego.EXTRA_START_PLAYER,
                startWithHuman ? Estado.JUGADOR1.getValue() : Estado.JUGADOR2.getValue());
        startActivity(i);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
