package com.Fibonacci;

public class OneDimensionalSearch_Fibonacci {
    private static final double ε = 0.01;
    private static final double initialLeft = -1;
    private static final double initialRight = 3;
    private static final double δ = 0.01;

    public static void main(String[] args) {
        //生成一个30项的斐波那契数组，已经足够应付很多情况了
        double[] fibonacciArray = new double[30];
        for (int i = 0; i < fibonacciArray.length; i++) {
            fibonacciArray[i] = Fibonacci(i);
        }
        //只要1 / δ不超过Fibonacci(30)都能用
        System.out.println("Fibonacci(30)= " + fibonacciArray[fibonacciArray.length - 1]);
        //根据输入的a,b,δ确定对应的n，当n没有对应的Fibonacci数列值时
        //就与Fibonacci数列进行比较取与F(n)最近的Fibonacci值对应的n
        int n = fixN(δ, fibonacciArray);
        System.out.println("搜索次数n = " + n);

        //想==保留每次的迭代值==，所以用数组储存，而不是定义一个变量然后覆盖
        //确定左端点数组，右端点数组，左搜索点数组，右搜索点数组的规模（从a0-a5一共六个(n)元素）
        //并对左、右端点数组初始化
        double[] left = new double[n];
        left[0] = initialLeft;
        double[] right = new double[n];
        right[0] = initialRight;
        //说明：由于左右搜索点下标从1开始，以n-1结束，所以一共只有n-1个值，所以0这个索引是空白的
        double[] t_left = new double[n];
        double[] t_right = new double[n];

        //k代表指针，循环退出条件为 k >= 5 (6-1)
        int k = 1;
        //对左、右搜索点数组初始化
        t_left[k] = right[0] - (Fibonacci(n - 1) / Fibonacci(n)) * (right[0] - left[0]);
        t_right[k] = left[0] + (Fibonacci(n - 1) / Fibonacci(n)) * (right[0] - left[0]);
        //k从1开始
        //打印迭代过程中的函数值
        System.out.println("==============================================================");
        System.out.println("迭代过程中的函数值：");
        //进入while循环
        while (k < n - 1) {
            System.out.printf("f(t_left[%d]) = %.3f\t", k, f(t_left[k]));
            System.out.printf("f(t_right[%d]) = %.3f\n", k, f(t_right[k]));
            if (f(t_left[k]) < f(t_right[k])) {
                left[k] = left[k - 1];
                right[k] = t_right[k];
                //k=0即Fibonacci(n - 1) / Fibonacci(n)的情况在进入循环前已经计算了
                t_left[k + 1] = right[k] - (Fibonacci(n - k - 1) / Fibonacci(n - k)) * (right[k] - left[k]);
                t_right[k + 1] = t_left[k];
            } else {
                left[k] = t_left[k];
                right[k] = right[k - 1];
                t_left[k + 1] = t_right[k];
                t_right[k + 1] = left[k] + (Fibonacci(n - k - 1) / Fibonacci(n - k)) * (right[k] - left[k]);
            }
            k++;
        }
        //循环退出，说明k=n-1了
        double x_result;
        double fx_result;
        //此处有对t_left[n - 1]和t_right[n - 1]覆盖赋值
        t_left[n - 1] = (left[n - 2] + right[n - 2]) * 0.5;
        t_right[n - 1] = left[n - 2] + (0.5 + ε) * (right[n - 2] - left[n - 2]);

        System.out.printf("f(t_left[%d]) = %.3f\t", n - 1, f(t_left[n - 1]));
        System.out.printf("f(t_right[%d]) = %.3f\n", n - 1, f(t_right[n - 1]));
        if (f(t_left[n - 1]) < f(t_right[n - 1])) {
            left[n - 1] = left[n - 2];
            right[n - 1] = t_right[n - 1];
            x_result = t_left[n - 1];
            fx_result = f(t_left[n - 1]);
        } else {
            left[n - 1] = t_left[n - 1];
            right[n - 1] = right[n - 2];
            x_result = t_right[n - 1];
            fx_result = f(t_right[n - 1]);
        }
        //打印计算结果
        System.out.println("==============================================================");
        System.out.print("左端点的迭代情况：");
        printPoints(left);
        System.out.print("右端点的迭代情况：");
        printPoints(right);
        System.out.print("左搜索点的迭代情况：");
        printSearchPoints(t_left);
        System.out.print("右搜索点的迭代情况：");
        printSearchPoints(t_right);
        System.out.println("近似极小值点x_result = " + x_result);
        System.out.println("近似极小值fx_result = " + fx_result);
    }

    //求具体例子中的函数值的方法，如果要修改函数也在这里修改
    public static double f(double x) {
        return Math.pow(x, 2) -  x + 2;
    }

    //确定n的值的方法，由于F(n)≥1 / δ，所以求出来的F如果没有刚好对应的值，那就往更大的方向找
    public static int fixN(double δ, double[] fibonacciArray) {
        double F = 1 / δ;  //n与δ呈负相关，δ越小，n越大
        int n = -1;
        for (int i = 0; i < fibonacciArray.length; i++) {
            if (fibonacciArray[i] == F) {
                n = i;
            }
            //找到F被夹在哪两个Fibonacci数中间，然后返回它右边的那个，同时要保证指针i+1没有超出数组的边界
            if (i + 1 < fibonacciArray.length && fibonacciArray[i] < F && fibonacciArray[i + 1] > F) {
                n = i + 1;
            }
        }
        return n;
    }

    //求斐波那契数列值的方法
    public static double Fibonacci(double n) {
        if (n == 0.0 || n == 1.0) {
            return 1;
        } else {
            return Fibonacci(n - 1) + Fibonacci(n - 2);
        }
    }

    //格式化打印左右端点的迭代情况的方法，从索引为0开始打印
    public static void printPoints(double[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            System.out.printf("%.3f ==> ", array[i]);
        }
        System.out.printf("%.3f", array[array.length - 1]);
        System.out.println();
    }

    //格式化打印左右搜索点的迭代情况的方法，从索引为1开始打印
    public static void printSearchPoints(double[] array) {
        for (int i = 1; i < array.length - 1; i++) {
            System.out.printf("%.3f ==> ", array[i]);
        }
        System.out.printf("%.3f", array[array.length - 1]);
        System.out.println();
    }
}
