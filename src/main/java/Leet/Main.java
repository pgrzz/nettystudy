package Leet;

import java.lang.reflect.Method;

/**
 * Created by lenovo on 2016/11/20.
 */
public class Main {
    boolean[][] visited;
    int count = 0;
    final int[][] direction = {
            {-1, 0},
            {0, -1},
            {1, 0},
            {0, 1}
    };

    public int islandPerimeter(int[][] grid) { // 访问过的是ture 没有的是 flase


        int m = grid[0].length;
        int n=grid.length;
        visited = new boolean[n][m];
        boolean flag = false;
        for (int i = 0; i < n && !flag; i++) {
            for (int j = 0; j < m && !flag; j++) {
                if (grid[i][j] == 1) {
                    dfs(i, j, grid, n,m);
                    flag = true;
                } else {
                    visited[i][j] = true;
                }
            }
        }
        return count;

    }

    public void dfs(int x, int y, int[][] grid, int n,int m) {
            visited[x][y]=true;
        if (grid[x][y] == 1) {
            int result = 4;
            int[] temp;
            for (int i = 0; i < 4; i++) {
                temp = direction[i];
                if (x + temp[0] < n && y + temp[1] < m && x + temp[0] >= 0 && y + temp[1] >= 0) {
                    if (grid[x + temp[0]][y + temp[1]] == 1) {
                        result--;
                        if(!visited[x+temp[0]][y+temp[1]])
                        dfs(x + temp[0], y + temp[1], grid, n,m);
                    }
                }
            }
            count += result;
        }

    }





}

