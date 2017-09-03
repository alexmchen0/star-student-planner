package testing.ver1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/*import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date; */

public class ToDoListFragment extends ListFragment {

	private static final int ADD_TODO_ITEM_REQUEST = 0;

	private static final String FILE_NAME = "TodoManagerActivityData.txt";
	private static final String TAG = "-----------------------";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

	ToDoListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mAdapter = new ToDoListAdapter(getActivity());
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ToDoItem toDoItem = (ToDoItem) getListView().getItemAtPosition(position);

		String category = toDoItem.getCategory();
		Intent intent;
		if (category.contains("Task"))
			intent = new Intent(getActivity(), AddTaskActivity.class);
		else if (category.contains("Exam"))
			intent = new Intent(getActivity(), AddExamActivity.class);
		else if (category.contains("Event"))
			intent = new Intent(getActivity(), AddEventActivity.class);
		else
			intent = new Intent(getActivity(), AddReminderActivity.class);

		ToDoItem.packageIntent(intent, toDoItem.getCategory(), toDoItem.getTitle(), toDoItem.getSubject(),
				toDoItem.getType(), toDoItem.getPriority(), toDoItem.getStatus(), toDoItem.getDate(), toDoItem.getDetails());
		intent.putExtra("position", position);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "Entered onActivityResult for fragment");

		if (resultCode == Activity.RESULT_OK && requestCode == ADD_TODO_ITEM_REQUEST) {
			ToDoItem newItem = new ToDoItem(data);
			if (newItem.getCategory().contains("Edit")) {
				Bundle extras = data.getExtras();
				mAdapter.edit(data.getIntExtra("position", 0), extras);
			} else
				mAdapter.add(newItem);
			Log.i(TAG, "added item to adapter");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		//	if (mAdapter.getCount() == 0)
		//loadItems();
	}

	@Override
	public void onPause() {
		super.onPause();

		//saveItems();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_DELETE:
				mAdapter.clear();
				Log.i(TAG, "cleared");
				return true;
			case MENU_DUMP:
				dump();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void dump() {

		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
			Log.i(TAG, "Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
		}

	}

	// Load stored ToDoItems
/*	private void loadItems() {
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(fis));

			String title = null;
			String priority = null;
			String status = null;
			String date = null;

			while (null != (title = reader.readLine())) {
				priority = reader.readLine();
				status = reader.readLine();
				date = reader.readLine();
				mAdapter.add(new ToDoItem(title, ToDoItem.Priority.valueOf(priority),
						ToDoItem.Status.valueOf(status), date));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Save ToDoItems to file
	private void saveItems() {
        PrintWriter writer = null;
		try {
			FileOutputStream fos = new FileOutputStream(FILE_NAME);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));

			for (int idx = 0; idx < mAdapter.getCount(); idx++) {

				writer.println(mAdapter.getItem(idx));

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	} */
}