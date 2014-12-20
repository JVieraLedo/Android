
package com.cse3345.f13.kohn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cse3345.f13.kohn.R;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.laplanete.mobile.example.Item;
import ca.laplanete.mobile.example.Page;
import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGrid;
import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGridAdapter;

public class CheckersAdapter implements PagedDragDropGridAdapter {

	private Context context;
	private PagedDragDropGrid gridview;
	
	List<Page> pages = new ArrayList<Page>();
	
	public CheckersAdapter(Context context, PagedDragDropGrid gridview) {
		super();
		this.context = context;
		this.gridview = gridview;
		
		Page page1 = new Page();
		List<Item> items = new ArrayList<Item>();
		for (int i = 1; i < 9; i+=2) {
			items.add(new Item(i, R.drawable.empty));
			items.add(new Item(i+1, R.drawable.red_piece));
		}
		for (int i = 9; i < 17; i+=2) {
			items.add(new Item(i, R.drawable.red_piece));
			items.add(new Item(i+1, R.drawable.empty));
		}
		for (int i = 17; i < 25; i+=2) {
			items.add(new Item(i, R.drawable.empty));
			items.add(new Item(i+1, R.drawable.red_piece));
		}
		for (int i = 25; i < 41; i++)
			items.add(new Item(i, R.drawable.empty));
		for (int i = 41; i < 49; i+=2) {
			items.add(new Item(i, R.drawable.black_piece));
			items.add(new Item(i+1, R.drawable.empty));
		}
		for (int i = 49; i < 57; i+=2) {
			items.add(new Item(i, R.drawable.empty));
			items.add(new Item(i+1, R.drawable.black_piece));
		}
		for (int i = 57; i < 64; i+=2) {
			items.add(new Item(i, R.drawable.black_piece));
			items.add(new Item(i+1, R.drawable.empty));
		}
		page1.setItems(items);
		pages.add(page1);
		
		
	}

	@Override
	public int pageCount() {
		return pages.size();
	}

	private List<Item> itemsInPage(int page) {
		if (pages.size() > page) {
			return pages.get(page).getItems();
		}	
		return Collections.emptyList();
	}

    @Override
	public View view(int page, int index) {
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		ImageView icon = new ImageView(context);
		Item item = getItem(page, index);
		icon.setImageResource(item.getDrawable());
		icon.setPadding(0, 0, 0, 0);
		
		layout.addView(icon);

		layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		// only set selector on every other page for demo purposes
		// if you do not wish to use the selector functionality, simply disregard this code
		if(page % 2 == 0) {
    		setViewBackground(layout);
    		layout.setClickable(true);
    		layout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return gridview.onLongClick(v);
                }
    		});
		}

		return layout;
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setViewBackground(LinearLayout layout) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            layout.setBackground(context.getResources().getDrawable(R.drawable.list_selector_holo_light));
        }
    }

	private Item getItem(int page, int index) {
		List<Item> items = itemsInPage(page);
		return items.get(index);
	}

	@Override
	public int rowCount() {
		return 8;
	}

	@Override
	public int columnCount() {
		return 8;
	}

	@Override
	public int itemCountInPage(int page) {
		return itemsInPage(page).size();
	}

	public void printLayout() {
		int i=0;
		for (Page page : pages) {
			Log.d("Page", Integer.toString(i++));
			
			for (Item item : page.getItems()) {
				Log.d("Item", Long.toString(item.getId()));
			}
		}
	}

	private Page getPage(int pageIndex) {
		return pages.get(pageIndex);
	}

	@Override
	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
		getPage(pageIndex).swapItems(itemIndexA, itemIndexB);
	}

	@Override
	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
	}

	@Override
	public void moveItemToNextPage(int pageIndex, int itemIndex) {

	}

	@Override
	public void deleteItem(int pageIndex, int itemIndex) {
		getPage(pageIndex).deleteItem(itemIndex);
	}

    @Override
    public int deleteDropZoneLocation() {        
        return BOTTOM;
    }
	
}
