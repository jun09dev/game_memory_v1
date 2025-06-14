package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameView extends View {

    private static final int NUM_COLUMNS = 4;
    private static final int NUM_ROWS = 4;

    private int cardSize;
    private Card[][] cards;
    private Card selectedCard1 = null;
    private Card selectedCard2 = null;

    private boolean isBusy = false;
    private Paint paint = new Paint();
    private Handler handler = new Handler();

    private GameEndListener gameEndListener;

    private Bitmap[] numberBitmaps;

    public MemoryGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadNumberBitmaps();
        initGame();
    }

    private void loadNumberBitmaps() {
        numberBitmaps = new Bitmap[10];
        for (int i = 0; i < 10; i++) {
            int resId = getResources().getIdentifier("num_" + i, "drawable", getContext().getPackageName());
            numberBitmaps[i] = BitmapFactory.decodeResource(getResources(), resId);
        }
    }

    public interface GameEndListener {
        void onGameEnd();
    }

    public void setGameEndListener(GameEndListener listener) {
        this.gameEndListener = listener;
    }

    public void restartGame() {
        selectedCard1 = null;
        selectedCard2 = null;
        isBusy = false;
        initGame();
        invalidate();
    }

    private void initGame() {
        cards = new Card[NUM_ROWS][NUM_COLUMNS];
        List<Integer> symbols = new ArrayList<>();
        for (int i = 0; i < (NUM_COLUMNS * NUM_ROWS) / 2; i++) {
            symbols.add(i);
            symbols.add(i);
        }
        Collections.shuffle(symbols);

        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                cards[row][col] = new Card(symbols.get(index++));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cardSize = getWidth() / NUM_COLUMNS;

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                float left = col * cardSize;
                float top = row * cardSize;
                Card card = cards[row][col];

                // Vẽ khung đen
                paint.setColor(0xFF000000);
                canvas.drawRect(left, top, left + cardSize, top + cardSize, paint);

                // Vẽ nội dung
                if (card.isMatched || card.isFaceUp) {
                    canvas.drawBitmap(
                            Bitmap.createScaledBitmap(numberBitmaps[card.symbol], cardSize - 10, cardSize - 10, false),
                            left + 5,
                            top + 5,
                            null
                    );
                } else {
                    paint.setColor(0xFF888888);
                    canvas.drawRect(left + 5, top + 5, left + cardSize - 5, top + cardSize - 5, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isBusy || event.getAction() != MotionEvent.ACTION_DOWN) return true;

        int col = (int) (event.getX() / cardSize);
        int row = (int) (event.getY() / cardSize);

        if (row >= NUM_ROWS || col >= NUM_COLUMNS) return true;

        Card tapped = cards[row][col];
        if (tapped.isFaceUp || tapped.isMatched) return true;

        tapped.isFaceUp = true;
        invalidate();

        if (selectedCard1 == null) {
            selectedCard1 = tapped;
        } else if (selectedCard2 == null && tapped != selectedCard1) {
            selectedCard2 = tapped;
            isBusy = true;

            handler.postDelayed(() -> {
                checkMatch();
                invalidate();
                isBusy = false;
            }, 500);
        }

        return true;
    }

    private void checkMatch() {
        if (selectedCard1 != null && selectedCard2 != null) {
            if (selectedCard1.symbol == selectedCard2.symbol) {
                selectedCard1.isMatched = true;
                selectedCard2.isMatched = true;
                checkGameEnd();
            } else {
                selectedCard1.isFaceUp = false;
                selectedCard2.isFaceUp = false;
            }
        }
        selectedCard1 = null;
        selectedCard2 = null;
    }

    private void checkGameEnd() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                if (!cards[row][col].isMatched) return;
            }
        }
        if (gameEndListener != null) {
            gameEndListener.onGameEnd();
        }
    }

    private static class Card {
        int symbol;
        boolean isFaceUp = false;
        boolean isMatched = false;

        Card(int symbol) {
            this.symbol = symbol;
        }
    }
}
