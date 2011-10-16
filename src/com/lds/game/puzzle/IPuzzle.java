package com.lds.game.puzzle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.lds.game.event.OnPuzzleFailListener;
import com.lds.game.event.OnPuzzleInitializedListener;
import com.lds.game.event.OnPuzzleSuccessListener;

public interface IPuzzle extends GLSurfaceView.Renderer
{	
	void onTouchEvent(MotionEvent event);
	void setContext(Context context);
	public void setSyncObj(Object o);
	
	void setPuzzleInitializedEvent(OnPuzzleInitializedListener listener);
	void setPuzzleSuccessEvent(OnPuzzleSuccessListener listener);
	void setPuzzleFailEvent(OnPuzzleFailListener listener);
}
