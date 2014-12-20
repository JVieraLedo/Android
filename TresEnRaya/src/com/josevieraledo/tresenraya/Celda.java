package com.josevieraledo.tresenraya;

import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Celda {

	//estado 0 vacio
	//estado 1 x
	//estado 2 o
	private int estado;
	ImageButton ib;
	public Celda(ImageButton ib,OnClickListener celdaClick)
	{
		this.ib=ib;
		this.estado=0;
		ib.setImageResource(R.drawable.vacio);
		ib.setOnClickListener(celdaClick);
	}
	public int getId()
	{
		return ib.getId();
	}
	public void setEstado(int estado)
	{
		this.estado=estado;
		if(estado==1)
			ib.setImageResource(R.drawable.circulo);
		else if(estado==2)
			ib.setImageResource(R.drawable.cruz);
	}
	public int getEstado()
	{
		return estado;
	
	}
	public void reiniciar()
	{
		ib.setImageResource(R.drawable.vacio);
		estado=0;
	}
}
