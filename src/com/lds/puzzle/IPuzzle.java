package com.lds.puzzle;

import com.lds.puzzle.event.*;

import android.opengl.GLSurfaceView;

public interface IPuzzle extends GLSurfaceView.Renderer
{
	public void init();
	public void run();
	public boolean end();
	
	public void setOnPuzzleInitializedListener(OnPuzzleInitializedListener listener);
	public void setOnPuzzleSuccessListener(OnPuzzleSuccessListener listener);
	public void setOnPuzzleFailListener(OnPuzzleFailListener listener);
}