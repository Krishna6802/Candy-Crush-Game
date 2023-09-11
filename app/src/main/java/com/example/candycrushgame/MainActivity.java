package com.example.candycrushgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] candies = {
            R.drawable.bluecandy,
            R.drawable.orangecandy,
            R.drawable.redcandy,
            R.drawable.yellowcandy,
            R.drawable.purplecandy,
            R.drawable.greencandy
    };

    int widthOfBlock, noOfBlocks=8, widthOfScreen;
    ArrayList<ImageView> candy = new ArrayList<>();
    int candyToBeDragged, candyToBeReplaced;
    int notCandy=R.drawable.ic_launcher_background;
    Handler handler;
    int interval = 100;
    TextView txtScore;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtScore = findViewById(R.id.txtScore);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen = displayMetrics.widthPixels;
        int heightOfScreen = displayMetrics.heightPixels;
        widthOfBlock = widthOfScreen / noOfBlocks;
        createBlock();

        for(final ImageView imageView : candy)
        {
            imageView.setOnTouchListener(new OnSwipeListener(this){
                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - 1;
                    candyInterChange();
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + 1;
                    candyInterChange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - noOfBlocks;
                    candyInterChange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + noOfBlocks;
                    candyInterChange();
                }
            });
        }
        handler = new Handler();
        startRepeat();
    }

    private void checkRowForThree() {
        for(int i=0; i<62; i++)
        {
            int choosedCandy = (int)candy.get(i).getTag();
            boolean isBlank = (int)candy.get(i).getTag() == notCandy;
            Integer[] notValid = {6,7,14,15,22,23,30,31,38,39,46,47,54,55};
            List<Integer> list = Arrays.asList(notValid);
            if(!list.contains(i))
            {
                int x = i;
                if((int)candy.get(x++).getTag() == choosedCandy && !isBlank && (int)candy.get(x++).getTag() == choosedCandy
                        && (int)candy.get(x).getTag() == choosedCandy)
                {
                    score = score + 3;
                    txtScore.setText(String.valueOf(score));

                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
            }
        }
        moveDownCandies();
    }

    private void checkColumnForThree() {
        for(int i=0; i<47; i++)
        {
            int choosedCandy = (int)candy.get(i).getTag();
            boolean isBlank = (int)candy.get(i).getTag() == notCandy;

            int x = i;
            if((int)candy.get(x).getTag() == choosedCandy && !isBlank && (int)candy.get(x+noOfBlocks).getTag() == choosedCandy
                    && (int)candy.get(x+2*noOfBlocks).getTag() == choosedCandy)
            {
                score = score + 3;
                txtScore.setText(String.valueOf(score));
                candy.get(x).setImageResource(notCandy);
                candy.get(x).setTag(notCandy);
                x+=noOfBlocks;
                candy.get(x).setImageResource(notCandy);
                candy.get(x).setTag(notCandy);
                x+=noOfBlocks;
                candy.get(x).setImageResource(notCandy);
                candy.get(x).setTag(notCandy);
            }
        }
        moveDownCandies();
    }

    private void moveDownCandies()
    {
        Integer[] firstRow = {0,1,2,3,4,5,6,7};
        List<Integer> list = Arrays.asList(firstRow);
        for(int i=55; i>=0; i--)
        {
            if((int) candy.get(i+noOfBlocks).getTag() == notCandy)
            {
                candy.get(i+noOfBlocks).setImageResource((int)candy.get(i).getTag());
                candy.get(i+noOfBlocks).setTag((int)candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);

                if(list.contains(i) && (int) candy.get(i).getTag() == notCandy)
                {
                    int randomColor = (int) Math.floor(Math.random() * candies.length);
                    candy.get(i).setImageResource(candies[randomColor]);
                    candy.get(i).setTag(candies[randomColor]);
                }
            }
        }
        for (int i=0; i<8; i++)
        {
            if ((int) candy.get(i).getTag() == notCandy)
            {
                int randomColor = (int) Math.floor(Math.random() * candies.length);
                candy.get(i).setImageResource(candies[randomColor]);
                candy.get(i).setTag(candies[randomColor]);
            }
        }
    }

    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try{
                checkRowForThree();
                checkColumnForThree();
            }
            finally {
                handler.postDelayed(repeatChecker,interval);
            }
        }
    };

    void startRepeat()
    {
        repeatChecker.run();
    }

    private void candyInterChange(){
        int background = (int) candy.get(candyToBeReplaced).getTag();
        int background1 = (int) candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candyToBeReplaced).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candyToBeReplaced).setTag(background1);
    }

    private void createBlock() {
        GridLayout gridLayout = findViewById(R.id.Board);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.getLayoutParams().width = widthOfScreen;
        gridLayout.getLayoutParams().height = widthOfScreen;

        for(int i=0; i< noOfBlocks * noOfBlocks; i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfBlock,widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);

            int randomCandy = (int)Math.floor(Math.random() * candies.length);
            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);
            gridLayout.addView(imageView);

        }
    }
}
