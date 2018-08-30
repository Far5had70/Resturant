package ir.waspar.resturanttestapp;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rec)
    RecyclerView recyclerView;

    private FoodAdapter adapter;

    @BindView(R.id.textSwitcher)
    TextSwitcher mSwitcherCompanyName;

    @BindView(R.id.textSwitcher_Name)
    TextSwitcher mSwitcherFoodName;

    @BindView(R.id.textSwitcher_Calories)
    TextSwitcher mSwitcherCalory;

    @BindView(R.id.textSwitcher_weight)
    TextSwitcher mSwitcherWeight;

    @BindView(R.id.textSwitcher_materials)
    TextSwitcher mSwitcherMaterials;

    @BindView(R.id.textSwitcher_time)
    TextSwitcher mSwitcherTime;

    @BindView(R.id.textSwitcher_ingredient)
    TextSwitcher mSwitcherIngredient;

    @BindView(R.id.textSwitcher_kKol)
    TextSwitcher mSwitcherKKAL;

    @BindView(R.id.price)
    TextView textView;

    @BindView(R.id.basket_size)
    TextView BasketTextView;

    private static final String TAG = "MainActivity";

    private TurnLayoutManager layoutManager;

    List<Food> list = new ArrayList<>();

    private int price = 1;

    private int basketSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        setupTextSwitcher();
        setupRecyclerview();
    }

    private void setupTextSwitcher() {

        setupTextViews(mSwitcherCompanyName , 15 , Gravity.CENTER_HORIZONTAL , 5 , 30 , Color.WHITE , true);
        setupTextViews(mSwitcherFoodName , 25 , Gravity.CENTER_HORIZONTAL , 0 , 0 , getResources().getColor(R.color.orange) , false);
        setupTextViews(mSwitcherCalory , 14 , Gravity.LEFT , 0 , 0 , getResources().getColor(R.color.gray), false);
        setupTextViews(mSwitcherWeight , 14 , Gravity.RIGHT , 0 , 0 , getResources().getColor(R.color.gray), false);
        setupTextViews(mSwitcherMaterials , 18 , Gravity.CENTER_HORIZONTAL , 0 , 0 , getResources().getColor(R.color.gray), false);
        setupTextViews(mSwitcherTime , 25 , Gravity.CENTER_HORIZONTAL , 0 , 0 , getResources().getColor(R.color.orange), false);
        setupTextViews(mSwitcherIngredient , 25 , Gravity.CENTER_HORIZONTAL , 0 , 0 , getResources().getColor(R.color.orange), false);
        setupTextViews(mSwitcherKKAL , 25 , Gravity.CENTER_HORIZONTAL , 0 , 0 , getResources().getColor(R.color.orange), false);

    }

    private void setupTextViews(TextSwitcher textSwitcher , final int FontSize , final int TextGravity , final int PaddintTop , final int PaddingRight , final int TextColor , final boolean backGround) {
        textSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {

                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.TOP | TextGravity);
                myText.setTextSize(FontSize);
                myText.setPadding(PaddingRight, PaddintTop, PaddingRight, PaddintTop);
                myText.setTypeface(Typeface.createFromAsset(getAssets(), "font.otf"));
                if (backGround)
                    myText.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                myText.setTextColor(TextColor);
                return myText;
            }
        });
    }

    private void setupRecyclerview() {
        DataFakeGenerator dataFakeGenerator = new DataFakeGenerator();
        list = dataFakeGenerator.foods();
        adapter = new FoodAdapter(list);
        recyclerView.setAdapter(adapter);
        layoutManager = new TurnLayoutManager(this,
                TurnLayoutManager.Gravity.START,
                TurnLayoutManager.Orientation.HORIZONTAL,
                1000,
                300,
                true);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.getLayoutManager().scrollToPosition(50);
        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean scrolled;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    scrolled = true;
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE && scrolled) {
                    scrolled = false;
                    int Position = layoutManager.findFirstVisibleItemPosition();
                    loadDescriptions(Position);
                }
            }
        });

        loadDescriptions(0);

    }

    private void loadDescriptions(int position) {
        if (list != null) {
            mSwitcherCompanyName.setText(list.get(position).getProvider());
            mSwitcherFoodName.setText(list.get(position).getName());
            mSwitcherCalory.setText(String.valueOf(list.get(position).getCalory() + " KKAL"));
            mSwitcherWeight.setText(String.valueOf(list.get(position).getWeight() + " G"));

            String materils = "";
            for (int i = 0; i < list.get(position).getMaterials().size(); i++) {
                materils += " " + list.get(position).getMaterials().get(i) + " ,";
            }
            mSwitcherMaterials.setText(removeLastChar(materils, ","));
            mSwitcherTime.setText(String.valueOf(list.get(position).getCookTime()));
            mSwitcherIngredient.setText(String.valueOf(list.get(position).getMaterials().size()));
            mSwitcherKKAL.setText(String.valueOf(list.get(position).getCalory()));
            setPriceVariable(price , list.get(position).getPrice() , textView);
        }
    }

    private void setPriceVariable(final int oldPrice , final int newPrice , final TextView textView) {

        ValueAnimator animator = ValueAnimator.ofInt(oldPrice, newPrice);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("SetTextI18n")
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(animation.getAnimatedValue().toString() + " $");
                price = newPrice;
            }
        });
        animator.start();

    }

    private ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {

        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //TODO
            Log.e(TAG, "onSwiped: viewHolder" + viewHolder.getLayoutPosition());
            basketSize = ++basketSize;
            BasketTextView.setText(String.valueOf(basketSize));

            new CountDownTimer(700, 700) { //40000 milli seconds is total time, 1000 milli seconds is time interval

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    adapter.notifyDataSetChanged();
                }
            }.start();
        }

        //defines the enabled move directions in each state (idle, swiping, dragging).
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.DOWN);
        }
    };

    private String removeLastChar(String stringText, String endingChar) {
        if (!stringText.equals("") && stringText != null) {
            if (stringText.endsWith(endingChar)) {
                stringText = stringText.substring(0, stringText.length() - 1);
            }
        }
        return stringText;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
