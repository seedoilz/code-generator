package com.seedoilz.model;

import java.util.Scanner;

/**
 * ACM 输入模版（多数之和）
 * @author seedoilz
 */
public class MainTemplate {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

            int n = scanner.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            int sum = 0;
            for (int num : arr) {
                sum += num;
            }

            System.out.println("求和结果：" + sum);
        scanner.close();
    }
}