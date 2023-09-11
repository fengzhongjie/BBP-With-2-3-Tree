package org.example.standard;

import org.example.Model;

import java.io.*;
import java.util.*;

import static org.example.standard.RandomArrayGenerator.generateRandomArrays;

public class NextFit {
    public static List<Integer> nextFit(int[] items, int binCapacity) {
            List<Integer> bins = new ArrayList<>();
            bins.add(binCapacity);
            Integer latBin = 0;
            Integer latBinSize = -1;
            for (int item : items) {
                latBinSize = bins.get(latBin);
                if(item <= bins.get(latBin)){
                    bins.set(latBin, latBinSize-item);
                }
                else {
                    bins.add(binCapacity-item);
                    latBin++;
                }
            }
            return bins;
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

    public static void reverseArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    public static void main(String[] args) {

        /*Model model;
        //n=60
        String path = "/Users/fengzhongjie/instances/Falkenauer_t60_00.txt";
        //n=250
//        String path = "/Users/fengzhongjie/Downloads/Falkenauer/Falkenauer U/Falkenauer_u250_11.txt";
        //n = 501
//        String path = "/Users/fengzhongjie/Downloads/Falkenauer/Falkenauer_T/Falkenauer_t501_19.txt";
        //n=1000
//        String path = "/Users/fengzhongjie/Downloads/Falkenauer/Falkenauer U/Falkenauer_u1000_19.txt";

        model = readFile(path);
        int[] items = model.weights;
        Random random = new Random();

        for (int i = items.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // 交换 array[i] 和 array[j]
            int temp = items[i];
            items[i] = items[j];
            items[j] = temp;
        }

        //        Integer[] sortedItems = Arrays.stream(items)
//                .boxed()
//                .sorted(Comparator.reverseOrder())
//                .toArray(Integer[]::new);

        */
        String filePath = "/Users/fengzhongjie/plot/nfd.txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            int[][] arrays = generateRandomArrays(1000, 100);

            for (int j = 0; j < 1000; j++) {
                int[] items = arrays[j];

                Arrays.sort(items);
                reverseArray(items);

                int binCapacity = 100;
                long startTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();

                List<Integer> bins = nextFit(items, binCapacity);

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
