package org.example;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.example.standard.NextFit.reverseArray;
import static org.example.standard.RandomArrayGenerator.generateRandomArrays;

public class BinPackingSolver {

    static {
        Loader.loadNativeLibraries();
    }

    public static void solveBinPackingProblem(int[] itemSizes, int binCapacity) {
        int numItems = itemSizes.length;
        int numBins = numItems; // In this simple example, we use a bin for each item

        // 创建线性规划求解器
        MPSolver solver = new MPSolver("BinPackingSolver", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

        // 创建变量
        MPVariable[][] x = new MPVariable[numItems][numBins];
        for (int i = 0; i < numItems; i++) {
            for (int j = 0; j < numBins; j++) {
                x[i][j] = solver.makeIntVar(0, 1, "x_" + i + "_" + j);
            }
        }

        // 添加约束和目标函数，与之前的代码相同

        // 求解
        MPSolver.ResultStatus resultStatus = solver.solve();

        // 输出结果
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            for (int j = 0; j < numBins; j++) {
                System.out.print("箱子 " + j + ": ");
                for (int i = 0; i < numItems; i++) {
                    if (x[i][j].solutionValue() == 1) {
                        System.out.print("物品" + i + " (" + itemSizes[i] + ") ");
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("求解失败。");
        }
    }

    public static void main(String[] args) {

        String filePath = "/Users/fengzhongjie/plot/ortools100.txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            int[][] arrays = generateRandomArrays(100, 100);

            for (int j = 0; j < 100; j++) {
                int[] items = arrays[j];

                Arrays.sort(items);
                reverseArray(items);

                int binCapacity = 100;
                long startTime = System.nanoTime(); // 或者使用 System.currentTimeMillis();

                solveBinPackingProblem(items, binCapacity);

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
