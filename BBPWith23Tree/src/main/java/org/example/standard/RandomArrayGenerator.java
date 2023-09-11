package org.example.standard;

import java.util.Random;

public class RandomArrayGenerator {

    public static void main(String[] args) {

        int[][] arrays = generateRandomArrays(1000, 100);
        // 打印生成的数组（这里只打印第一个数组作为示例）
        printArray(arrays[0]);
    }

    // 生成指定数量的随机数组，大小从startSize到endSize逐渐增加
    public static int[][] generateRandomArrays(int numArrays, int startSize) {
        int[][] arrays = new int[numArrays][];
        Random random = new Random();

        for (int i = 0; i < numArrays; i++) {
            int arraySize = startSize + i; // 逐渐增加数组大小
            arrays[i] = new int[arraySize];
            for (int j = 0; j < arraySize; j++) {
                arrays[i][j] = random.nextInt(99) + 1;
            }
        }

        return arrays;
    }

    // 打印数组
    public static void printArray(int[] array) {
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
