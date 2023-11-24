package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class GenerateMatrix {
    @Test
    public void spiralMatrix(){
        generateMatrix(10);
    }
    public int[][] generateMatrix(int n) {
        int[][] matrix=new int[n][n];
        int start,loop,count,i,j;
        count=1;
        for(loop=0;loop<(n+1)/2;loop++){
            for(j=loop;j<n-loop-1;j++){
                matrix[loop][j]=count++;
                //log.info("count is {},j is {}",count,j);
            }
            for(i=loop;i<n-loop-1;i++){
                matrix[i][n-loop-1]=count++;
            }
            for(j=n-loop-1;j>loop;j--){
                matrix[n-loop-1][j]=count++;
            }
            for(i=n-loop-1;i>loop;i--){
                matrix[i][loop]=count++;
            }
        }
        if(n%2==1){
            matrix[n/2][n/2]=n*n;
        }
        logMatrix(matrix);
        return matrix;
    }

    private static void logMatrix(int[][] matrix) {
        int elementWidth = 4;
        for (int[] row : matrix) {
            StringBuilder rowString = new StringBuilder();
            for (int value : row) {
                rowString.append(String.format("%" + elementWidth + "d", value));
            }
            log.info(rowString.toString().trim());
        }
    }
}
