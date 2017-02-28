package com.awesomedroidapps.inappstoragereader.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.awesomedroidapps.inappstoragereader.entities.AppDataStorageItem;
import com.awesomedroidapps.inappstoragereader.ErrorMessageHandler;
import com.awesomedroidapps.inappstoragereader.ErrorType;
import com.awesomedroidapps.inappstoragereader.Utils;
import com.awesomedroidapps.inappstoragereader.interfaces.AppStorageItemClickListener;
import com.awesomedroidapps.inappstoragereader.Constants;
import com.awesomedroidapps.inappstoragereader.R;
import com.awesomedroidapps.inappstoragereader.SqliteDatabaseReader;
import com.awesomedroidapps.inappstoragereader.adapters.IconWithTextListAdapter;
import com.awesomedroidapps.inappstoragereader.interfaces.ErrorMessageInterface;

import java.util.List;

/**
 * Activity for showing the list of all the tables in a particular database.
 * Created by anshul on 11/2/17.
 */

public class TableListActivity extends AppCompatActivity implements AppStorageItemClickListener,
    ErrorMessageInterface {

  RecyclerView tablesRecylerView;
  private String databaseName;
  private RelativeLayout errorHandlerLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.com_awesomedroidapps_inappstoragereader_activity_tables_list);
    tablesRecylerView = (RecyclerView) findViewById(R.id.tables_recycler_view);
    errorHandlerLayout = (RelativeLayout) findViewById(R.id.error_handler);

    Bundle bundle = getIntent().getExtras();
    databaseName = bundle.getString(Constants.BUNDLE_DATABASE_NAME);
  }

  @Override
  public void onStart() {
    super.onStart();
    List tablesList = SqliteDatabaseReader.readTablesList(this, databaseName);
    if (Utils.isEmpty(tablesList)) {
      handleError(ErrorType.NO_TABLES_FOUND);
    }
    IconWithTextListAdapter adapter = new IconWithTextListAdapter(tablesList, this);
    tablesRecylerView.setLayoutManager(new LinearLayoutManager(this));
    tablesRecylerView.setAdapter(adapter);
  }

  @Override
  public void onItemClicked(AppDataStorageItem appDataStorageItem) {
    if (appDataStorageItem == null) {
      return;
    }
    Intent intent = new Intent(this, TableDataActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.BUNDLE_DATABASE_NAME, databaseName);
    bundle.putString(Constants.BUNDLE_TABLE_NAME, appDataStorageItem.getStorageName());
    intent.putExtras(bundle);
    startActivity(intent);
  }

  @Override
  public void handleError(ErrorType errorType) {
    tablesRecylerView.setVisibility(View.GONE);
    errorHandlerLayout.setVisibility(View.VISIBLE);
    ErrorMessageHandler handler = new ErrorMessageHandler();
    handler.handleError(errorType, errorHandlerLayout);
  }
}
