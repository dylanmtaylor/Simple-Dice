/*  This file is part of Simple Dice.
 *
 *  Simple Dice is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Simple Dice is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Simple Dice.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dylantaylor.simpledice;

import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RollDice extends Activity {
	
	private final int rollAnimations = 50;
	private final int delayTime = 15;
	private Resources res;
	private final int[] diceImages = new int[]{R.drawable.d1,R.drawable.d2,R.drawable.d3,R.drawable.d4,R.drawable.d5,R.drawable.d6};
	private Drawable dice[] = new Drawable[6];
	private final Random randomGen = new Random();
    @SuppressWarnings("unused")
	private int diceSum;
    private int roll[] = new int[]{6,6};
	private ImageView die1;
	private ImageView die2;
	private LinearLayout diceContainer;
	private Handler animationHandler;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        setTitle(getString(R.string.app_title));
        res = getResources();
        for (int i = 0; i < 6; i++) {
        	dice[i] = res.getDrawable(diceImages[i]);
        }
        diceContainer = (LinearLayout) findViewById(R.id.diceContainer);
        diceContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rollDice();
			}
		});
        die1 = (ImageView) findViewById(R.id.die1);
        die2 = (ImageView) findViewById(R.id.die2);
        animationHandler = new Handler() {
            public void  handleMessage(Message msg) {
            	die1.setImageDrawable(dice[roll[0]]);
         	    die2.setImageDrawable(dice[roll[1]]);
            }
       };
       rollDice();
    }
 
    private void rollDice() {
    	new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < rollAnimations; i++) {
					doRoll();
				}
			}
		}).start();   	
    }
    
    private void doRoll() { //only does a single roll
    	roll[0] = randomGen.nextInt(6);
		roll[1] = randomGen.nextInt(6);
	    diceSum = roll[0] + roll[1] + 2; //2 is added because the values of the rolls start with 0 not 1
	    synchronized (getLayoutInflater()) {
	    	animationHandler.sendEmptyMessage(0);
		}
	    try { //delay to alloy for smooth animation
	    	Thread.sleep(delayTime);
	    } catch (final InterruptedException e) {
	    	e.printStackTrace();
	    }
    }
    
    public void onResume() {
    	super.onResume(); 
    	rollDice();
    }
}
