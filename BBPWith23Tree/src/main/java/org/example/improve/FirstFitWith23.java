package org.example.improve;

import org.example.Model;
import org.example.standard.Bin;

import java.io.*;
import java.util.*;

import static org.example.standard.NextFit.reverseArray;
import static org.example.standard.RandomArrayGenerator.generateRandomArrays;

public class FirstFitWith23 {
    public static TwoThreeTree firstFitWith23(int[] items, int binCapacity) {
        TwoThreeTree tree = new TwoThreeTree();
        tree.insertCurrent(binCapacity);
        int bestbin = -1;
        for (int item : items) {
            bestbin = tree.findBest(item);
            if(bestbin == -1) {
                tree.insertCurrent(binCapacity - item);
            }
            else {
                tree.remove(bestbin);
                tree.insertCurrent(bestbin-item);
            }
        }
        return tree;
    }


    public static Model readFile(String filePath) {
        Model model = new Model();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

            String line;
            int lineCount = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (lineCount == 0) {
                    model.numItems = Integer.parseInt(line);
                } else if (lineCount == 1) {
                    model.binCapacity = Integer.parseInt(line);
                    model.weights = new int[model.numItems];
                } else if (lineCount <= model.numItems + 1) {
                    model.weights[lineCount - 2] = Integer.parseInt(line);
                }
                lineCount++;
            }

            bufferedReader.close();
            model.numBins = model.numItems;
            System.out.println("Number of items: " + model.numItems);
            System.out.println("Bin capacity: " + model.binCapacity);
            System.out.println("Item sizes: ");
            for (int size : model.weights) {
                System.out.println(size);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle the case where parsing integers fails
        }
        return model;
    }

    public static void main(String[] args) {

      /*
        int binCapacity = model.binCapacity;

        Integer[] sortedItems = Arrays.stream(items)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .toArray(Integer[]::new);


        long startTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();

        for (int i = 1; i <= 1000; i++) {
            List<Bin> bins = bestFit(items, binCapacity);
//            List<Bin> bins = bestFit(sortedItems, binCapacity);
            //System.out.println("Bins used: " + bins.size());
        }
       */
        String filePath = "/Users/fengzhongjie/plot/bf23.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            int[][] arrays = generateRandomArrays(1000, 100);

            for (int j = 0; j < 1000; j++) {
                int[] items = arrays[j];
//                Arrays.sort(items);
//                reverseArray(items);
                int binCapacity = 100;
                long startTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();
                TwoThreeTree tree = firstFitWith23(items, binCapacity);
//          List<Bin> bins = nextFit(sortedItems, binCapacity);
//          System.out.println("Bins used: " + bins.size());
                long endTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();
                long duration = (endTime - startTime);
                writer.newLine();
                writer.write((j+10)+","+duration);

                System.out.println("Average time to perform the Next Fit algorithm 1 times took " + duration + " nanoseconds（n = " + (j + 10) + "）");

                // 记得关闭写入流

            }
            writer.close();
            System.out.println("数据已成功写入文件。");
        } catch (IOException e) {
            System.err.println("写入文件时出现错误：" + e.getMessage());
        }
    }
}

