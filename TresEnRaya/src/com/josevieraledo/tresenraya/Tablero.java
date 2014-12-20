package com.josevieraledo.tresenraya;

import java.util.ArrayList;

import android.util.Log;

public class Tablero {
	
	ArrayList<Celda> tablero;
	boolean finalizado;
	
	public Tablero(ArrayList<Celda> tablero)
	{
		this.tablero=new ArrayList<Celda>();
		for(int i=0;i<tablero.size();i++)
		{
			this.tablero.add(tablero.get(i));
		}
		finalizado=false;
	}
	
	public int getEstadoCelda(int id)
	{
		for(Celda c:tablero)
		{
			if(c.getId()==id)
				return c.getEstado();
		}
		return -1;
	}
	public void setO(int celda)
	{
		for(int i=0;i<tablero.size();i++)
			if(celda==tablero.get(i).getId())
				tablero.get(i).setEstado(2);
	}
	public void setX(int celda)
	{
		Log.d("tamanho",tablero.size()+"");
		for(int i=0;i<tablero.size();i++)
		{
			if(celda==tablero.get(i).getId())
			{
				tablero.get(i).setEstado(1);
				//Log.d("ENTRAMOS","x");
			}
			//Log.d("NO entramos",tablero.get(i).getId()+"");
			
		}
	}
	//1 es X
	//2 es O
	public boolean hayGanador(int simbolo) {		
		if(tablero.get(4).getEstado()==simbolo)
		{
			if(tablero.get(0).getEstado()==simbolo && tablero.get(8).getEstado()==simbolo){
				return true;
			}
			
			if(tablero.get(2).getEstado()==simbolo && tablero.get(6).getEstado()==simbolo){
				return true;
			}
			
			if(tablero.get(1).getEstado()==simbolo && tablero.get(7).getEstado()==simbolo){
				return true;
			}
			
			if(tablero.get(3).getEstado()==simbolo && tablero.get(5).getEstado()==simbolo){
				return true;
			}
		}
		
		if(tablero.get(0).getEstado()==simbolo){
			if(tablero.get(1).getEstado()==simbolo && tablero.get(2).getEstado()==simbolo){
				return true;
			}
			
			if(tablero.get(3).getEstado()==simbolo && tablero.get(6).getEstado()==simbolo){
				return true;
			}
		}
		
		if(tablero.get(8).getEstado()==simbolo){
			if(tablero.get(6).getEstado()==simbolo && tablero.get(7).getEstado()==simbolo){
				return true;
			}
			
			if(tablero.get(2).getEstado()==simbolo && tablero.get(5).getEstado()==simbolo){
				return true;
			}
		}
		
		return false;
	}

	public boolean tableroLleno() {
		for(Celda c : tablero){
			if(c.getEstado() == 0){
				return false;
			}
		}	
		return true;
	}
	
	public void reiniciaTablero()
	{
		for(Celda c:tablero)
		{
			c.reiniciar();
		}
	}
}
