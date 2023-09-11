package org.example.improve;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.example.standard.RandomArrayGenerator.generateRandomArrays;

public class FirstFitWithTree {

    public static Multiset<Integer> firstFitWithTree(int[] items, int binCapacity) {
        Multiset<Integer> binSet = TreeMultiset.create();
        binSet.add(binCapacity);
        for (int item : items) {
            for (Multiset.Entry<Integer> entry : binSet.entrySet())
                if (entry.getElement() >= item) {
                    Integer remain = entry.getElement() - item;
                    binSet.remove(entry.getElement(), 1);
                    binSet.add(remain);
                }
                else {
                    binSet.add(binCapacity - item);
                }
        }
        return binSet;
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
        String filePath = "/Users/fengzhongjie/plot/ff23100.txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            int[][] arrays = generateRandomArrays(100, 100);
            for (int j = 0; j < 100; j++) {
                int[] items = arrays[j];
                int binCapacity = 100;
                long startTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();

                Multiset<Integer> bins = firstFitWithTree(items, binCapacity);
//          List<Bin> bins = nextFit(sortedItems, binCapacity);
//          System.out.println("Bins used: " + bins.size());
                long endTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();
                long duration = (endTime - startTime);
                writer.newLine();
                writer.write((j+10)+","+duration);

                System.out.println("Average time to perform the Next Fit algorithm 1 times took " + duration + " nanoseconds（n = " + (j + 10) + "）");
                System.out.println(bins.size());
                // 记得关闭写入流

            }
            writer.close();
            System.out.println("数据已成功写入文件。");
        } catch (IOException e) {
            System.err.println("写入文件时出现错误：" + e.getMessage());
        }

    }

}
