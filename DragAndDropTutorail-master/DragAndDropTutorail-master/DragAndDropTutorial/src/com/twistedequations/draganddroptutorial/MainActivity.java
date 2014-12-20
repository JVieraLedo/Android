package com.twistedequations.draganddroptutorial;

import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.appleText).setOnLongClickListener(longListener);
		findViewById(R.id.bananaText).setOnLongClickListener(longListener);
		findViewById(R.id.pearText).setOnLongClickListener(longListener);
		findViewById(R.id.orangeText).setOnLongClickListener(longListener);
		
		findViewById(R.id.dropTarget).setOnDragListener(dragListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	OnLongClickListener longListener = new OnLongClickListener()
	{
		@Override
		public boolean onLongClick(View v) 
		{
			TextView fruit = (TextView) v;
			Toast.makeText(MainActivity.this, "Text long clicked - " +fruit.getText() , Toast.LENGTH_SHORT).show();
			
			View.DragShadowBuilder myShadowBuilder = new MyShadowBuilder(v);
			
			ClipData data = ClipData.newPlainText("", "");
			v.startDrag(data, myShadowBuilder, fruit, 0);
			
			return true;
		}
		
	};
	
	OnDragListener dragListener = new OnDragListener()
	{
		@Override
		public boolean onDrag(View v, DragEvent event) 
		{
			int dragEvent = event.getAction();
			TextView dropText = (TextView) v;
			
			switch(dragEvent)
			{
				case DragEvent.ACTION_DRAG_ENTERED:
					//dropText.setTextColor(Color.GREEN);
					break;
					
				case DragEvent.ACTION_DRAG_EXITED:
					//dropText.setTextColor(Color.RED);
					break;
					
				case DragEvent.ACTION_DROP:
					TextView draggedText = (TextView)event.getLocalState();
					dropText.setText(draggedText.getText());
					break;
			}
			
			return true;
		}
		
	};
	
	private class MyShadowBuilder extends View.DragShadowBuilder
	{
		private Drawable shadow;
		
		public MyShadowBuilder(View v)
		{
			super(v);
			shadow = new ColorDrawable(Color.LTGRAY);
		}

		@Override
		public void onDrawShadow(Canvas canvas) 
		{
			shadow.draw(canvas);
		}

		@Override
		public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) 
		{
			int height, width;
			height = (int) getView().getHeight()/2;
			width = (int) getView().getHeight()/2;
			
			shadow.setBounds(0, 0, width, height);
			
			shadowSize.set(width, height);
			shadowTouchPoint.set(width/2, height/2);
		}
		
	}

}
