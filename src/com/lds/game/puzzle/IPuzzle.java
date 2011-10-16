package com.lds.game.puzzle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.lds.game.event.PuzzleFailListener;
import com.lds.game.event.PuzzleInitializedListener;
import com.lds.game.event.PuzzleSuccessListener;

public interface IPuzzle extends GLSurfaceView.Renderer
{	
	void onTouchEvent(MotionEvent event);
	void setContext(Context context);
	public void setSyncObj(Object o);
	
	void setPuzzleInitializedEvent(PuzzleInitializedListener listener);
	void setPuzzleSuccessEvent(PuzzleSuccessListener listener);
	void setPuzzleFailEvent(PuzzleFailListener listener);
}
