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

package com.ltdev;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.ltdev.cc.event.*;

/**
 * A subclass of GLSurfaceView that contains Circuit Crawler-specific code.
 * @author Lightning Development Studios
 */
public class LevelSurfaceView extends GLSurfaceView
{
	private Renderer renderer;
	private Object syncObj;
	
	/**
	 * Initializes a new instance of the Graphics class.
	 * @param context An Android context.
	 * @param r A Renderer that the GLSurfaceView will use.
	 * @param syncObj An object to sync between the main thread and the rendering thread.
	 */
	public LevelSurfaceView(Context context, Renderer r, Object syncObj)
	{
		super(context);
		this.syncObj = syncObj;
		renderer = r;
		
		this.setEGLConfigChooser(true);
		//this.setDebugFlags(DEBUG_CHECK_GL_ERROR);
		
		setRenderer(r);
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		//grab touch input, pass it through to the generic renderer (in this case, com.ltdev.cc.LevelRenderer
		queueEvent(new Runnable() 
		{
			public void run()
			{
				renderer.onTouchInput(event);
			}
		});
		
		//sync with OpenGL thread
		synchronized (syncObj)
		{
			try
			{
				syncObj.wait(500);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
     * Called when a puzzle event returns successfully.
     */
	public void onPuzzleWon()
	{
		queueEvent(new Runnable()
		{
			public void run()
			{
				renderer.onPuzzleWon();
			}
		});
	}
	
	/**
     * Called when a puzzle event returns unsuccessfully.
     */
	public void onPuzzleFailed()
	{
		queueEvent(new Runnable()
		{
			public void run()
			{
				renderer.onPuzzleFailed();
			}
		});
	}
	
	/**
	 * Unload the renderer.
	 */
	public void unload()
	{
	    queueEvent(new Runnable()
	    {
	        public void run()
	        {
	            renderer.onUnload();
	        }
	    });
	}
	
	/**
	 * A sub-interface of GLSurfaceView.Renderer for Circuit Crawler.
	 * Handles touch input and puzzle activity hooks.
	 * @author Lightning Development Studios
	 */
	public interface Renderer extends GLSurfaceView.Renderer
	{
	    /**
	     * Called when the user touches the touchscreen.
	     * @param event The Android MotionEvent describing the input.
	     */
		void onTouchInput(MotionEvent event);
		
		/**
		 * Sets a delegate to be called when the game is over.
		 * @param listener The delegate container interface with an anonymous inner method.
		 */
		void setGameOverEvent(GameOverListener listener);
		
		/**
		 * Sets the delegate to be called when the game is initialized.
		 * @param listener The delegate container interface with an anonymous inner method.
		 */
		void setGameInitializedEvent(GameInitializedListener listener);
		
		/**
		 * Sets the delegate to be called when the game is initialized.
		 * @param listener The delegate container interface with an anonymous inner method.
		 */
		void setPuzzleActivatedEvent(PuzzleActivatedListener listener);
		
		/**
		 * Called when a puzzle event returns successfully.
		 */
		void onPuzzleWon();
		
		/**
		 * Called when a puzzle event returns unsuccessfully.
		 */
		void onPuzzleFailed();
		
		/**
		 * Called when the surface is destroyed.
		 */
		void onUnload();
	}
}
