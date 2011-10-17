package com.lds;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.lds.game.event.*;

/**
 * A subclass of GLSurfaceView that contains Circuit Crawler-specific code.
 * @author Lightning Development Studios
 */
public class Graphics extends GLSurfaceView
{
	private Renderer renderer;
	private Object syncObj;
	
	/**
	 * Initializes a new instance of the Graphics class.
	 * @param context An Android context.
	 * @param r A Renderer that the GLSurfaceView will use.
	 * @param syncObj An object to sync between the main thread and the rendering thread.
	 */
	public Graphics(Context context, Renderer r, Object syncObj) 
	{
		super(context);
		this.syncObj = syncObj;
		renderer = r;
		setRenderer(r);
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		//grab touch input, pass it through to the generic renderer (in this case, com.lds.GameRenderer
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
	}
}
