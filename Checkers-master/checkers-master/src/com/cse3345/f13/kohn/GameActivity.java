package com.cse3345.f13.kohn;

import com.example.dragdropgridexample.ExampleActivity;

import ca.laplanete.mobile.example.ExamplePagedDragDropGridAdapter;
import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGrid;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class GameActivity extends Activity implements OnClickListener {
	GridView bg;
	PagedDragDropGrid gameBoard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameBoard = (PagedDragDropGrid) findViewById(R.id.gameboard);
		CheckersAdapter adapter = new CheckersAdapter(this, gameBoard);
		
		gameBoard.disableScroll();
        
        gameBoard.setAdapter(adapter);
        gameBoard.setOnClickListener(this);
        gameBoard.setBackgroundColor(Color.TRANSPARENT);
        
        bg = (GridView) findViewById(R.id.background);
        bg.setAdapter(new BoardAdapter(this));
		
//		for (int i = 1; i < 65; i++) {
//			ImageView curr = new ImageView(this);
//			if (i % 2 == 0)
//				curr.setBackgroundColor(Color.BLACK);
//			else
//				curr.setBackgroundColor(Color.WHITE);
//			bg.addView(curr);
//		}
		
	}

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Reset").setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                gameBoard.setAdapter(new CheckersAdapter(GameActivity.this, gameBoard));
                gameBoard.notifyDataSetChanged();
                
                return true;
            }
        });

        return true;
    }
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
