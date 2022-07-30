package converter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static String convert(BigInteger n, int base_) {
        StringBuilder str = new StringBuilder();
        BigInteger base = new BigInteger(String.valueOf(base_));
        while (n.compareTo(base) >= 0) {
            if (n.remainder(base).intValue() > 9)
                str.append((char)('A' + (n.remainder(base).intValue() - 10)));
            else
                str.append(n.remainder(base).intValue());
            n = n.divide(base);
        }
        if (n.remainder(base).intValue() > 9 && n.remainder(base).intValue() != 0)
            str.append((char)('A' + (n.remainder(base).intValue() - 10)));
        else
            str.append(n.remainder(base).intValue());
        return (str.reverse().toString());
    }

    public static String convertFloat(BigDecimal n2, int base_) {
        StringBuilder str = new StringBuilder();
        String s = n2.toString();
        str.append(convert(new BigInteger(s.substring(0, s.indexOf('.'))), base_));
        str.append(".");
        BigDecimal base = new BigDecimal(String.valueOf(base_));
        n2 = n2.remainder(BigDecimal.ONE);
        for (int i = 0; i < 5; i++) {
            if (n2.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            n2 = n2.multiply(base);
            if (n2.toBigInteger().intValue() > 9)
                str.append((char)('A' + (n2.toBigInteger().intValue() - 10)));
            else
                str.append(n2.toBigInteger().intValue());
            n2 = n2.remainder(BigDecimal.ONE);
        }
        StringBuilder result = new StringBuilder(str.toString());
        while (result.indexOf(".") + 5 >= result.length()) {
            result.append("0");
        }
        return (result.toString());
    }

    public static BigInteger convertToDec(String src, int base_) {
        BigInteger result = new BigInteger("0");
        BigInteger base = new BigInteger(String.valueOf(base_));
        src = src.toUpperCase();
        String s;
        for (int i = (src.length() - 1), j = 0; i >= 0; i--, j++) {
            if (src.charAt(i) <= '9' && src.charAt(i) >= '0')
                result = result.add(new BigInteger(String.valueOf(new BigInteger(String.valueOf(src.charAt(i))).multiply(base.pow(j)))));
            else
                result = result.add(new BigInteger(String.valueOf(new BigInteger(String.valueOf(10 + src.charAt(i) - 'A')).multiply(base.pow(j)))));
        }
        return result;
    }

    public static BigDecimal convertToDecFloat(String src, int base_) {
        StringBuilder str = new StringBuilder();
        str.append(convertToDec(src.substring(0, src.indexOf('.')), base_));
        str.append(".");
        str.append(convertToDecFloat2(src.substring(src.indexOf('.')), base_).toString().substring(2));
        return new BigDecimal(str.toString());
    }

    public static BigDecimal convertToDecFloat2(String src, int base_) {
        BigDecimal result = new BigDecimal("0");
        BigDecimal base = new BigDecimal(String.valueOf(base_));
        src = src.toUpperCase();
        for (int i = 1; i < src.length(); i++) {
            if (src.charAt(i) <= '9' && src.charAt(i) >= '0')
                result = result.add(new BigDecimal(String.valueOf(new BigDecimal(String.valueOf(src.charAt(i))).divide(base.pow(i), 5, RoundingMode.HALF_DOWN))));
            else
                result = result.add(new BigDecimal(String.valueOf(new BigDecimal(String.valueOf(10 + src.charAt(i) - 'A')).divide(base.pow(i), 5, RoundingMode.HALF_DOWN))));
        }
        if (result.compareTo(BigDecimal.ZERO) == 0)
            return new BigDecimal("0.0");
        return result;
    }

    public static String getInput(int[] nums) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
        String str = scanner.nextLine();
        String[] s;
        if (!str.equals("/exit")) {
            s = str.split(" ");
            nums[0] = Integer.parseInt(s[0]);
            nums[1] = Integer.parseInt(s[1]);
        }
        return str;
    }

    public static void doFrom(int[] nums) {
        Scanner scanner = new Scanner(System.in);
        String str;
        BigInteger n;
        BigDecimal n2;
        while (true) {
            System.out.println("Enter number in base " + nums[0] + " to convert to base " + nums[1] + " (To go back type /back)");
            str = scanner.nextLine();
            if (str.equals("/back"))
                break;
            if (!str.contains(".")) {
                n = convertToDec(str, nums[0]);
                System.out.println("Conversion result: " + convert(n, nums[1]));
            } else {
                n2 = convertToDecFloat(str, nums[0]);
                System.out.println("Conversion result: " + convertFloat(n2, nums[1]));
            }
        }
    }
    public static void main(String[] args) {
        String input;
        int [] nums = new int[2];
        while (true) {
            input = getInput(nums);
            if (input.equals("/exit"))
                break;
            else
                doFrom(nums);
        }


    }
}