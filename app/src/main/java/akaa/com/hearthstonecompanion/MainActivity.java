package akaa.com.hearthstonecompanion;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import akaa.com.hearthstonecompanion.adapter.CardRecyclerViewAdapter;
import akaa.com.hearthstonecompanion.model.Card;
import akaa.com.hearthstonecompanion.model.CardResponse;
import akaa.com.hearthstonecompanion.rest.CardApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView cardsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    List<Card> mCards = null;

    MenuItem searchDismiss;
    InputMethodManager imm;

    private Boolean searchFlag = false;
    private Boolean viewReady = false;

    private static final String BASE_URL = "https://omgvamp-hearthstone-v1.p.mashape.com/";
    private static Retrofit retrofit = null;

    private Button retryConnectionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.my_toolbar) ;
        setSupportActionBar(mToolbar);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        retryConnectionBtn = findViewById(R.id.retryConnectionButton);
        retryConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryConnectionBtn.setVisibility(View.GONE);
                TextView loadingTxtView = findViewById(R.id.loadingTextView);
                loadingTxtView.setText("Loading Cards..."); //these strings would usually be put in the strings.xml but for the sake of this demo
                connectAndGetCardData();
            }
        });

        cardsRecyclerView = findViewById(R.id.cardsRecyclerView);
        cardsRecyclerView.setHasFixedSize(true);

        int numberOfColumns = 2;
        cardsRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        mContext = this;
        connectAndGetCardData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action, menu);
        searchDismiss = menu.findItem(R.id.action_search_dismiss);
        searchDismiss.setVisible(false);
        findViewById(R.id.search_field).setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewReady) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    if(!searchFlag) {
                        EditText editText = findViewById(R.id.search_field);
                        searchFlag = true;
                        searchDismiss.setVisible(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        editText.setVisibility(View.VISIBLE);
                        editText.requestFocus();
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        EditText editText = findViewById(R.id.search_field);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                        String userSearchInput = editText.getText().toString();
                        if (userSearchInput.trim().length() > 0) {
                            filterCards(userSearchInput);
                        }
                    }
                    break;


                case R.id.action_search_dismiss:
                    EditText editText = findViewById(R.id.search_field);
                    editText.setText("");
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    refreshCardRecyclerView(mCards); //resetting the card orders on the grid
                    searchFlag = false;
                    invalidateOptionsMenu();
                    break;
                default:
                    break;
            }
        }

        return true;
    }

    public void connectAndGetCardData(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        CardApiService cardApiService = retrofit.create(CardApiService.class);

        Call<CardResponse> call = cardApiService.getCards();
        call.enqueue(new Callback<CardResponse>() {
            @Override
            public void onResponse(Call<CardResponse> call, Response<CardResponse> response) {
                mCards = response.body().getCards();

                mAdapter = new CardRecyclerViewAdapter(mContext, mCards, new InfoViewListener() {
                    @Override
                    public void hideLoadingView() {
                        findViewById(R.id.loadingTextView).setVisibility(View.GONE);
                        viewReady = true;
                    }

                    @Override
                    public void showNoCardsView() {
                        findViewById(R.id.noCardsFoundView).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void hideNoCardsView() {
                        findViewById(R.id.noCardsFoundView).setVisibility(View.GONE);
                    }
                });
                cardsRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<CardResponse> call, Throwable t) {
                TextView loadingTxtView = findViewById(R.id.loadingTextView);
                loadingTxtView.setText("Unable to connect to endpoint");
                retryConnectionBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    public void filterCards(String userInput){
        String keyword = userInput.toLowerCase().replaceAll("\\s", ""); //getting rid of all spaces and nextlines
        List<Card> filteredCards = new ArrayList<>();
        for (int i=0; i<mCards.size(); i++){
            Card myCard = mCards.get(i);
            String cardName = myCard.getCardName().trim().toLowerCase();
            if (cardName.contains(keyword)){
                if (cardName.startsWith(keyword)){
                    filteredCards.add(0, myCard);
                } else {
                    filteredCards.add(myCard);
                }
            }
        }

        refreshCardRecyclerView(filteredCards);
    }

    public void refreshCardRecyclerView(List<Card> cardList){
        viewReady = false;
        mAdapter = new CardRecyclerViewAdapter(mContext, cardList, new InfoViewListener() {
            @Override
            public void hideLoadingView() {
                viewReady = true;
                findViewById(R.id.loadingTextView).setVisibility(View.GONE);
            }

            @Override
            public void showNoCardsView() {
                findViewById(R.id.noCardsFoundView).setVisibility(View.VISIBLE);
            }

            @Override
            public void hideNoCardsView() {
                findViewById(R.id.noCardsFoundView).setVisibility(View.GONE);
            }
        });
        cardsRecyclerView.setAdapter(mAdapter);
        cardsRecyclerView.invalidate();
    }
}
