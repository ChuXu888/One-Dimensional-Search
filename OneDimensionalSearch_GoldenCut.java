package com.Fibonacci;

import java.util.ArrayList;
import java.util.List;

public class OneDimensionalSearch_GoldenCut {
    private static final double φ = 0.618;
    private static final double initialLeft = 0;
    private static final double initialRight = 10;
    private static final double δ = 0.03;

    public static void main(String[] args) {
        //由于此方法==可以（并不是必须，但是不求n更灵活，也省事）==不用先求n，所以此处使用无需指定规模的更灵活的ArrayList，而不是用数组
        //对左、右端点列表初始化
        List<Double> left = new ArrayList<>();
        left.add(initialLeft);
        List<Double> right = new ArrayList<>();
        right.add(initialRight);
        //对左、右搜索点列表初始化，后续使用的时候编号都要-1，因为列表的索引都是从0开始的
        List<Double> t_left = new ArrayList<>();
        t_left.add(left.get(0) + (1 - φ) * (right.get(0) - left.get(0)));
        List<Double> t_right = new ArrayList<>();
        t_right.add(left.get(0) + φ * (right.get(0) - left.get(0)));
        //初始化k = 1
        int k = 1;
        //打印迭代过程中的函数值
        System.out.println("迭代过程中的函数值：");
        //循环退出的条件为φ^k < δ，从第一次判断处开始写循环，往前都是初始化阶段
        while (Math.pow(φ, k) >= δ) {
            System.out.printf("f(t_left.get(%d)) = %.3f\t", k - 1, f(t_left.get(k - 1)));
            System.out.printf("f(t_rightt.get(%d)) = %.3f\n", k - 1, f(t_right.get(k - 1)));
            if (f(t_left.get(k - 1)) < f(t_right.get(k - 1))) {
                //描述定理3.1
                left.add(left.get(k - 1));  //相当于添加了left[k]
                right.add(t_right.get(k - 1));  //相当于添加了right[k]
                //原左赋给右，左重新算
                t_left.add(left.get(k) + (1 - φ) * (right.get(k) - left.get(k)));  //相当于添加了t_left[k]
                t_right.add(t_left.get(k - 1));  //相当于添加了t_right[k]
            } else {
                //描述定理3.1
                left.add(t_left.get(k - 1));
                right.add(right.get(k - 1));
                //原右赋给左，右重新算
                t_left.add(t_right.get(k - 1));
                t_right.add(left.get(k) + φ * (right.get(k) - left.get(k)));
            }
            k++;
        }
        //循环退出时，φ^k < δ
        //此时判断f(t_left.get(k-1)与f(t_right.get(k-1)的大小，
        //取较小者对应的自变量为近似极小值点，其函数值为近似极小值
        double x_result;
        double fx_result;
        if (f(t_left.get(k - 1)) < f(t_right.get(k - 1))) {
            x_result = t_left.get(k - 1);
            fx_result = f(t_left.get(k - 1));
        } else {
            x_result = t_right.get(k - 1);
            fx_result = f(t_right.get(k - 1));
        }
        //打印计算结果
        System.out.println("==============================================================");
        System.out.print("左端点的迭代情况：");
        print(left);
        System.out.print("右端点的迭代情况：");
        print(right);
        System.out.print("左搜索点的迭代情况：");
        print(t_left);
        System.out.print("右搜索点的迭代情况：");
        print(t_right);
        System.out.println("近似极小值点x_result = " + x_result);
        System.out.println("近似极小值fx_result = " + fx_result);
    }

    //求具体例子中的函数表达式的方法，如果要修改函数也在这里修改
    public static double f(double x) {
        return Math.pow(x, 2) - 6 * x + 2;
    }

    //格式化打印ArrayList的方法
    public static void print(List<Double> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            System.out.printf("%.3f ==> ", list.get(i));
        }
        System.out.printf("%.3f", list.get(list.size() - 1));
        System.out.println();
    }
}
