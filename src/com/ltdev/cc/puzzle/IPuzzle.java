/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.puzzle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.ltdev.cc.event.PuzzleFailListener;
import com.ltdev.cc.event.PuzzleInitializedListener;
import com.ltdev.cc.event.PuzzleSuccessListener;

/**
 * An interface that all puzzles implement.
 * @author Lightning Development Studios
 */
public interface IPuzzle extends GLSurfaceView.Renderer
{
    /**
     * Receives touch input.
     * @param event The Android touch event data.
     */
	void onTouchEvent(MotionEvent event);
	
	/**
	 * Puzzles will receive the system Context some time between the initializaiton and onSurfaceCreated.
	 * @param context An Android Context.
	 */
	void setContext(Context context);
	
	/**
	 * Receives an object to synchronize the rendering thread with the main system thread.
	 * @param o The syncronized object.
	 */
	void setSyncObj(Object o);
	
	/**
	 * Puzzles will receive an initialized event listener. Not using the listener may result in unexpected behavior.
	 * @param listener The event callback for when the puzzle is done initializing.
	 */
	void setPuzzleInitializedEvent(PuzzleInitializedListener listener);
	
	/**
	 * Puzzles will receive an event listener to be called if the player successfully completes the puzzle. The listener is guaranteed to end the calling Activity.
	 * @param listener The event callback for when the player wins the puzzle.
	 */
	void setPuzzleSuccessEvent(PuzzleSuccessListener listener);
	
	/**
	 * Puzzles will receive an event listener to be called if the player fails to complete the puzzle. The listener is guaranteed to end the calling Activity.
	 * @param listener The event callback for when the player wins the puzzle.
	 */
	void setPuzzleFailEvent(PuzzleFailListener listener);
}
