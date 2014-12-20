package com.josevieraledo.tresenraya;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Jugadores extends Activity{
	Tablero tablero;
	
	//0 juego finalizado porque hay ganador
	//1 jugador 1
	//2 jugador 2
	int turno;
	int ganador;
	
	int contador_jugador1 = 0;
	int contador_jugador2 = 0;
	int contador_empate = 0;
	
	TextView mInfoTextView;
	TextView mJugador1;
	TextView mJugador2;
	TextView mEmpate;
	
	public OnClickListener createCeldaOnClickListener()
	{
		return new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
				
				ImageButton ib=(ImageButton)v;
				int posicion=ib.getId();
				
				if(turno!=0)
				{
					if(turno==1)
					{
						if(tablero.getEstadoCelda(posicion)==0)
						{
							tablero.setX(posicion);
							turno=2;
							if(tablero.hayGanador(1))
							{
								ganador=1;
								turno=0;
							}
						}
						else
							Toast.makeText(getApplicationContext(), "CASILLA OCUPADA", Toast.LENGTH_SHORT).show();
					}
					else
					{
						if(tablero.getEstadoCelda(posicion)==0)
						{
							tablero.setO(posicion);
							turno=1;
							if(tablero.hayGanador(2))
							{
								ganador=2;
								turno=0;
							}
						}
						else
							Toast.makeText(getApplicationContext(), "CASILLA OCUPADA", Toast.LENGTH_SHORT).show();
						
					}
				}
				if(tablero.tableroLleno())
				{
					ganador=0;
					turno=0;
					Log.d("Tablero lleno","hay empate");
				}
				if(turno==0)
				{
					Log.d("TURNO 0","El turno es 0");
					//verifico si el jugador anterior gano el juego
					if(ganador==1){
						Toast.makeText(getApplicationContext(), "GANA JUGADOR 1", Toast.LENGTH_SHORT).show();
						contador_jugador1++;
						mJugador1.setText(Integer.toString(contador_jugador1));
						
						}
					if (ganador==2){
						Toast.makeText(getApplicationContext(), "GANA JUGADOR 2", Toast.LENGTH_SHORT).show();
						contador_jugador2++;
						mJugador2.setText(Integer.toString(contador_jugador2));
					
						}
						else if (ganador==0){
							Toast.makeText(getApplicationContext(), "EMPATE", Toast.LENGTH_SHORT).show();
							contador_empate++;
							mEmpate.setText(Integer.toString(contador_empate));
							}
					}
				}
			};
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_jugadores);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		OnClickListener celdaClick = createCeldaOnClickListener();
	    ArrayList<Celda> celdas=new ArrayList<Celda>();
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton1),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton2),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton3),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton4),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton5),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton6),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton7),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton8),celdaClick));
	    celdas.add(new Celda((ImageButton)findViewById(R.id.imageButton9),celdaClick));
		tablero=new Tablero(celdas);
		turno=1;
		mInfoTextView = (TextView) findViewById(R.id.informacion);
		mJugador1 = (TextView) findViewById(R.id.jugador1Contador);
		mJugador2 = (TextView) findViewById(R.id.jugador2Contador);
		mEmpate = (TextView) findViewById(R.id.empateContador);
		
		mJugador1.setText(Integer.toString(contador_jugador1));
		mJugador2.setText(Integer.toString(contador_jugador2));
		mEmpate.setText(Integer.toString(contador_empate));
	}
	
	public void reinicio(View v)
	{
		tablero.reiniciaTablero();
		turno=1;
		ganador=0;
	}
}

