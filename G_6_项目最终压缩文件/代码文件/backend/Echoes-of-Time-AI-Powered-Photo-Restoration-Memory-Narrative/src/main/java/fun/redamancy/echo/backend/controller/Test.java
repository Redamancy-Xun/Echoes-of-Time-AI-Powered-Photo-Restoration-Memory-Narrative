package fun.redamancy.echo.backend.controller;

import java.util.ArrayList;
import java.util.List;

public class Test {
//    public static void main(String[] args) {
//
//    }
//
//    public int maxInList(int n, List<Integer> list) {
//        int max = list.get(0);
//        for (int i = 1; i < n; i++) {
//            if (list.get(i) > max) {
//                max = list.get(i);
//            }
//        }
//
//        for (int i = 0; i < n - 1; i++) {
//            if (list.get(i).equals(list.get(i + 1))) {
//                int k = i;
//                int j = i + 1;
//                List<Integer> tempList = new ArrayList<>(list);
//                tempList.set(k, tempList.get(k) + 1);
//                tempList.set(j, tempList.get(j) + 1);
//                while ((k - 1 >= 0 && tempList.get(k - 1).equals(tempList.get(k))) || (j + 1 <= n - 1 && tempList.get(j + 1).equals(tempList.get(j)))) {
//                    if ((k - 1 >= 0 && tempList.get(k - 1).equals(tempList.get(k))) && (j + 1 <= n - 1 && tempList.get(j + 1).equals(tempList.get(j)))) {
//                        List<Integer> tempList1 = new ArrayList<>(list);
//                        while (k - 1 >= 0 && tempList1.get(k - 1).equals(tempList1.get(k))) {
//                            k--;
//                            tempList1.set(k, tempList1.get(k) + 1);
//                            tempList1.set(j, tempList1.get(j) + 1);
//                            max = Math.max(max, tempList1.get(k));
//                        }
//                        List<Integer> tempList2 = new ArrayList<>(list);
//                        while (j + 1 <= n - 1 && tempList2.get(j + 1).equals(tempList2.get(j))) {
//                            j++;
//                            tempList2.set(k, tempList2.get(k) + 1);
//                            tempList2.set(j, tempList2.get(j) + 1);
//                            max = Math.max(max, tempList2.get(j));
//                        }
//                    } else if (k - 1 >= 0 && tempList.get(k - 1).equals(tempList.get(k))) {
//                        k--;
//                        tempList.set(k, tempList.get(k) + 1);
//                        tempList.set(j, tempList.get(j) + 1);
//                        max = Math.max(max, tempList.get(k));
//                    } else if (j + 1 <= n - 1 && tempList.get(j + 1).equals(tempList.get(j))) {
//                        j++;
//                        tempList.set(k, tempList.get(k) + 1);
//                        tempList.set(j, tempList.get(j) + 1);
//                        max = Math.max(max, tempList.get(j));
//                    }
//                }
//                List<Integer> tempList1 = new ArrayList<>(list);
//                tempList1.set(k, tempList.get(k) + 1);
//                tempList1.set(j, tempList.get(j) + 1);
//                while  {
//
//                }
//            }
//        }
//
//        return max;
//    }
}
