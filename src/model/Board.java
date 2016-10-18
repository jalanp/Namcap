package model;

import java.util.ArrayList;
import view.BoardView;

public class Board {

	BoardView view;
	public boolean[][] barriers;
	
	public Board(){
		this.barriers = new boolean[21][21];
		
		for (int i = 0; i < 21; i++){
			for (int j = 0; j < 21; j++){
				this.barriers[i][j] = true;
			}
		}
	}
	
	public void setView(BoardView view){
		this.view = view;
	}
	
	public void updateBarrier(int x, int y){
		this.barriers[x][y] = true;
	}
	
	public void updateBarrier(ArrayList<int[][]> points){
		for (int[][] list : points){
			for (int[] location : list){
				this.barriers[location[0]][location[1]] = true;
			}
		}
	}
}