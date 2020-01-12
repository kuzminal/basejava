import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinValueFromArray {
    public static void main(String[] args) {
        int[] arr = {6, 3, 1, 7, 4, 6, 7, 1};
        List<Integer> listValue = IntStream.of(arr)
                .boxed()
                .collect(Collectors.toList());
        System.out.println(minValue(arr));
        List<Integer> oddOrEven = oddOrEven(listValue);
        for (Integer ineg : oddOrEven) {
            System.out.println(ineg);
        }
    }

    public static int minValue(int[] values) {
        return IntStream.of(values)
                .boxed()
                .distinct()
                .sorted()
                .reduce(0, (a, b) -> a * 10 + b);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .reduce(0, Integer::sum);
        return integers.stream()
                .filter(i -> (sum % 2 != 0) == (i % 2 != 0))
                .collect(Collectors.toList());
    }
}
