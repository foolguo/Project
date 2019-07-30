package com.test.Cases;

import com.test.Annotations.BenchMark;
import com.test.Annotations.Measurement;
import com.test.Case;

import java.util.Random;

@Measurement(iteration = 10,group = 5)

public class Sort implements Case {

    public void heapSort(int[] n) {
        int len = n.length;
        if (len != 1) {
            int i;
            for(i = (len - 1 - 1) / 2; i >= 0; --i) {
                this.shitDown(n, i, len);
            }

            for(i = len - 1; i >= 0; --i) {
                this.swap(n, 0, i);
                this.shitDown(n, 0, i);
            }

        }
    }

    private void shitDown(int[] n, int index, int len) {
        while(true) {
            if (2 * index + 1 < len) {
                int j = 2 * index + 1;
                if (j + 1 < len && n[j] < n[j + 1]) {
                    ++j;
                }

                if (n[index] <= n[j]) {
                    this.swap(n, index, j);
                    index = j;
                    continue;
                }
            }

            return;
        }
    }

    public void swap(int[] n,int i,int j){
        int t=n[i];
        n[i]=n[j];
        n[j]=t;
    }
    @Measurement(iteration = 10000,group = 5)
    @BenchMark
    public void Sort(){
        int[] arr=new int[10000];
        Random random=new Random(System.currentTimeMillis());
        for (int i=0;i<arr.length;i++){
            arr[i]=random.nextInt(10000);
        }
        this.heapSort(arr);
    }

}
