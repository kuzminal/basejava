import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MinValueFromArray {
    public static void main(String[] args) {
        int[] arr = new int[]{6, 3, 9, 4, 4, 9, 1};
        List<Integer> listValue = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toList());
        int val = minValue(arr);
        System.out.println(val);
        List<Integer> oddOrEven = oddOrEven(listValue);
        for (Integer ineg : oddOrEven) {
            System.out.println(ineg);
        }
    }

    public static int minValue(int[] values) {
        List<Integer> listValue = Arrays.stream(values)
                .boxed()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        int count = listValue.size();
        int num = 0;
        for (int i = 0; i < listValue.size() - 1; i++){
            num += Math.pow(10, listValue.size() - (1 + i)) * listValue.get(i);
        }
        num += listValue.get(count -1);
        return num;
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .reduce(0, Integer::sum);
        if (sum % 2 == 0) {
            return integers.stream()
                    .filter(i -> i % 2 == 0)
                    .collect(Collectors.toList());
        } else {
            return integers.stream()
                    .filter(i -> i % 2 != 0)
                    .collect(Collectors.toList());
        }

    }
}
