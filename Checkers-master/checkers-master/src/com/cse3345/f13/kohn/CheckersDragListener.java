package com.cse3345.f13.kohn;

import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ImageView;

public class CheckersDragListener implements OnDragListener{

    int imagePosition;

    public CheckersDragListener(int position) {
        imagePosition = position;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
    	ImageView imageV = (ImageView) v;
        switch (event.getAction()) {
	        case DragEvent.ACTION_DRAG_STARTED:
	            Log.d("EJK", "DRAG Started " + imagePosition);
	            imageV.setColorFilter(Color.BLUE);
	            imageV.invalidate();
	          break;
	          
	        case DragEvent.ACTION_DRAG_ENTERED:
	            Log.d("EJK", "DRAG Entered " + imagePosition);
	            imageV.setColorFilter(Color.GREEN);
	            imageV.invalidate();
	          break;
	          
	        case DragEvent.ACTION_DRAG_EXITED:        
	            Log.d("EJK", "DRAG Exited " + imagePosition);
	            imageV.setColorFilter(Color.BLUE);
	            imageV.invalidate();
	          break;
	          
	        case DragEvent.ACTION_DROP:
	            Log.d("EJK", "DRAG Dropped " + imagePosition);
	            imageV.clearColorFilter();
	            imageV.invalidate();
	          break;
	          
	        case DragEvent.ACTION_DRAG_ENDED:
	            Log.d("EJK", "DRAG Ended " + imagePosition);
	            imageV.clearColorFilter();
	            imageV.invalidate();
	          default:
	        	  Log.d("EJK", "IDK");
	          break;
	        }
        return true;
    }

}