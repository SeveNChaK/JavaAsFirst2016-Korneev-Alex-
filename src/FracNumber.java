import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.javatuples.*;

public class FracNumber {

    private final int nBefore;
    private final int nAfter;
    private final List<Integer> number;

    public FracNumber(int before, int after, List<Integer> num) {
        this.nBefore = before;
        this.nAfter = after;
        this.number = num;
    }

    private static List<Integer> string2digits(String str) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            result.add(str.charAt(i) - '0');
        }
        return result;
    }

    public FracNumber(String number) throws NumberFormatException {
        Pattern p = Pattern.compile("((^-)|(^))((\\d+$)|(\\d+\\.\\d+$))");
        Matcher m = p.matcher(number);
        if (!m.find()) throw new NumberFormatException();
        List<Integer> result = new ArrayList<>();
        String copyNumber;
        if (number.charAt(0) == '-') {
            copyNumber = number.substring(1, number.length());
            result.add(1);
        } else {
            copyNumber = number;
            result.add(0);
        }
        String[] arrayStr = copyNumber.split("\\.");
        List<Integer> before = new ArrayList<>();
        List<Integer> after = new ArrayList<>();
        if (arrayStr.length == 1) {
            before.addAll(string2digits(arrayStr[0]));
        }
        if (arrayStr.length == 2) {
            before.addAll(string2digits(arrayStr[0]));
            after.addAll(string2digits(arrayStr[1]));
        }
        result.addAll(before);
        result.addAll(after);
        this.nBefore = before.size();
        this.nAfter = after.size();
        this.number = result;
    }

    public FracNumber(long n) {
        List<Integer> result = new ArrayList<>();
        int befor = 0;
        if (n < 0) {
            result.add(1);
            n = -n;
        } else result.add(0);
        while (n != 0) {
            result.add(1, Math.abs((int) n % 10));
            n = n / 10;
            befor++;
        }
        this.nBefore = befor;
        this.nAfter = 0;
        this.number = result;
    }

    public FracNumber(int n) {
        this((long) n);
    }

    public FracNumber(double n) {
        this(Double.toString(n));
    }

    public FracNumber(float n) {
        this((double) n);
    }

    private static int power(int x, int a) {
        int result = 1;
        for (int i = 1; i <= a; i++) {
            result = result * x;
        }
        return result;
    }

    private static int moreOrLess(List<Integer> first, List<Integer> second) {
        if (first.get(0) == 0 && second.get(0) == 1) return 1;
        if (first.get(0) == 1 && second.get(0) == 0) return -1;
        for (int i = 1; i < Math.min(first.size(), second.size()); i++) {
            if (first.get(i) > second.get(i)) return 1;
            if (first.get(i) < second.get(i)) return -1;
        }
        return first.size() > second.size() ? 1
                : first.size() < second.size() ? -1
                : 0;
    }

    private static Triplet<List<Integer>, Integer, Integer> excess(List<Integer> list, int before, int after) {
        List<Integer> result = new ArrayList<>();
        result.addAll(list);
        result.remove(0);
        while (result.lastIndexOf(0) == result.size() - 1 && result.size() != 1 && after != 0) {
            result.remove(result.size() - 1);
            after--;
        }
        before = result.size() - after;
        while (result.indexOf(0) == 0 && before != 1) {
            result.remove(0);
            before--;
        }
        result.add(0, list.get(0));
        return Triplet.with(result, before, after);
    }

    private static List<Integer> zero(List<Integer> list, int maxBefore, int maxAfter, int before, int after) {
        List<Integer> result = new ArrayList<>();
        result.addAll(list);
        for (int i = 0; i < maxBefore - before; i++) {
            result.add(1, 0);
        }
        for (int i = 0; i < maxAfter - after; i++) {
            result.add(0);
        }
        return result;
    }

    private static List<Integer> zeroingOut(int size) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(0);
        }
        return result;
    }

    public FracNumber rounding(int order) {
        List<Integer> result = new ArrayList<>();
        List<Integer> thisResult = new ArrayList<>();
        if (nAfter <= order) {
            return this;
        } else {
            thisResult.addAll(number.subList(0, nBefore + order + 1));
            int resultBefore = 0;
            int resultAfter = 0;
            if (number.get(nBefore + order + 1) >= 5) {
                resultBefore = 1;
                resultAfter = order;
                result.add(1);
                for (int i = order; i > 0; i--) {
                    result.add(0, 0);
                }
                result.add(0, number.get(0));
                return new FracNumber(nBefore, order, thisResult).plus(new FracNumber(resultBefore, resultAfter, result));
            } else {
                resultBefore = nBefore;
                resultAfter = order;
                result.addAll(number.subList(0, nBefore + order + 1));
                return new FracNumber(resultBefore, resultAfter, result);
            }
        }
    }

    public FracNumber plus(FracNumber other) {
        if (number.get(0) == 0 && other.number.get(0) == 1) {
            FracNumber copyOther = new FracNumber(other.toString());
            copyOther.number.set(0, 0);
            return this.minus(copyOther);
        }
        if (number.get(0) == 1 && other.number.get(0) == 0) {
            FracNumber copyThis = new FracNumber(this.toString());
            copyThis.number.set(0, 0);
            return other.minus(copyThis);
        }
        List<Integer> result = new ArrayList<>();
        result.add(number.get(0));
        List<Integer> workList = new ArrayList<>();
        List<Integer> workOther = new ArrayList<>();
        workList.addAll(number);
        workOther.addAll(other.number);
        int maxBefore = Math.max(nBefore, other.nBefore);
        int maxAfter = Math.max(nAfter, other.nAfter);
        workList = zero(workList, maxBefore, maxAfter, nBefore, nAfter);
        workOther = zero(workOther, maxBefore, maxAfter, other.nBefore, other.nAfter);
        int maxSize = workList.size() - 1;
        int c = 0;
        for (int i = maxSize; i > 0; i--) {
            result.add(1, (workList.get(i) + workOther.get(i) + c) % 10);
            c = (workList.get(i) + workOther.get(i) + c) / 10;
        }
        if (c > 0) {
            result.add(1, c);
        }
        int resultAfter = maxAfter;
        int resultBefor = maxSize - resultAfter;
        Triplet<List<Integer>, Integer, Integer> finishResult = excess(result, resultBefor, resultAfter);
        result.clear();
        result.addAll(finishResult.getValue0());
        resultBefor = finishResult.getValue1();
        resultAfter = finishResult.getValue2();
        return new FracNumber(resultBefor, resultAfter, result);
    }

    public FracNumber minus(FracNumber other) {
        if (number.get(0) == 0 && other.number.get(0) == 1) {
            FracNumber copyOther = new FracNumber(other.toString());
            copyOther.number.set(0, 0);
            return this.plus(copyOther);
        }
        if (number.get(0) == 1 && other.number.get(0) == 0) {
            FracNumber copyOther = new FracNumber(other.toString());
            copyOther.number.set(0, 1);
            return this.plus(copyOther);
        }
        if (number.get(0) == 1 && other.number.get(0) == 1) {
            FracNumber copyOther = new FracNumber(other.toString());
            copyOther.number.set(0, 0);
            return copyOther.plus(this);
        }
        List<Integer> result = new ArrayList<>();
        List<Integer> workList = new ArrayList<>();
        List<Integer> workOther = new ArrayList<>();
        workList.addAll(number);
        workOther.addAll(other.number);
        int maxAfter = Math.max(nAfter, other.nAfter);
        int maxBefor = Math.max(nBefore, other.nBefore);
        workList = zero(workList, maxBefor, maxAfter, nBefore, nAfter);
        workOther = zero(workOther, maxBefor, maxAfter, other.nBefore, other.nAfter);
        List<Integer> maxNumber = new ArrayList<>();
        List<Integer> minNumber = new ArrayList<>();
        if (moreOrLess(workList, workOther) == 1 || moreOrLess(workList, workOther) == 0) {
            maxNumber = workList;
            minNumber = workOther;
            result.add(0);
        } else {
            maxNumber = workOther;
            minNumber = workList;
            result.add(1);
        }
        int maxSize = maxNumber.size() - 1;
        Collections.reverse(maxNumber);
        Collections.reverse(minNumber);
        int c = 0;
        for (int i = 0; i < maxSize; i++) {
            if (i < maxSize - 1) {
                maxNumber.set(i + 1, maxNumber.get(i + 1) - 1);
                result.add(10 + maxNumber.get(i) + c);
                result.set(i + 1, result.get(i + 1) - minNumber.get(i));
            } else {

                result.add(Math.abs(maxNumber.get(i) + c - minNumber.get(i)));
            }
            c = 0;
            if (result.get(i + 1) / 10 > 0) {
                c = 1;
                result.set(i + 1, result.get(i + 1) % 10);
            }
        }
        if (c != 0) result.add(c + maxNumber.get(maxSize - 1));
        int resultAfter = Math.max(nAfter, other.nAfter);
        int resultBefor = result.size() - resultAfter - 1;
        result.add(result.get(0));
        result.remove(0);
        Collections.reverse(result);
        Triplet<List<Integer>, Integer, Integer> finishResult = excess(result, resultBefor, resultAfter);
        result.clear();
        result.addAll(finishResult.getValue0());
        resultBefor = finishResult.getValue1();
        resultAfter = finishResult.getValue2();
        return new FracNumber(resultBefor, resultAfter, result);
    }

    public FracNumber multiplication(FracNumber other) {
        List<Integer> workList = new ArrayList<>();
        List<Integer> workOther = new ArrayList<>();
        workList.clear();
        workOther.clear();
        workList.addAll(number);
        workOther.addAll(other.number);
        workList.remove(0);
        workOther.remove(0);
        List<Integer> result = new ArrayList<>();
        result = zeroingOut(nBefore + other.nBefore + nAfter + other.nAfter);
        Collections.reverse(workList);
        Collections.reverse(workOther);
        int p = 0;
        int rez = 0;
        for (int i = 0; i < workList.size(); i++) {
            p = 0;
            for (int j = 0; j < workOther.size(); j++) {
                rez = workList.get(i) * workOther.get(j) + p + result.get(i + j);
                result.set(i + j, rez % 10);
                p = rez / 10;
            }
            result.set(i + workOther.size(), p);
        }
        int resultAfter = nAfter + other.nAfter;
        int resultBefor = result.size() - resultAfter;
        Collections.reverse(result);
        if (number.get(0) == 1 && other.number.get(0) == 0 || other.number.get(0) == 1 && number.get(0) == 0)
            result.add(0, 1);
        if (number.get(0) == 1 && other.number.get(0) == 1) result.add(0, 0);
        if (number.get(0) == 0 && other.number.get(0) == 0) result.add(0, 0);
        Triplet<List<Integer>, Integer, Integer> finishResult = excess(result, resultBefor, resultAfter);
        result.clear();
        result.addAll(finishResult.getValue0());
        resultBefor = finishResult.getValue1();
        resultAfter = finishResult.getValue2();
        return new FracNumber(resultBefor, resultAfter, result);
    }

    public double toDouble() {
        double result = 0.0;
        for (int i = 1; i <= nBefore; i++) {
            result = result + number.get(i) * power(10, nBefore - i);
        }
        for (int i = 1; i <= nAfter; i++) {
            result = result + (double) number.get(i + nBefore) / (double) power(10, i);
        }
        if (number.get(0) == 1) result *= -1;
        return result;
    }

    public float toFloat() {
        return (float) this.toDouble();
    }

    public int toInt() throws IllegalArgumentException {
        return (int) this.toLong();
    }

    public long toLong() throws IllegalArgumentException {
        if (nAfter != 0) throw new IllegalArgumentException();
        long result = 0;
        for (int i = 1; i <= nBefore; i++) {
            result = result + number.get(i) * power(10, nBefore - i);
        }
        if (number.get(0) == 1) result *= -1;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof FracNumber) {
            FracNumber other = (FracNumber) obj;
            if (number.containsAll(other.number) && other.number.size() == number.size()) return true;
            else return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = nBefore;
        result = 31 * result + nAfter;
        result = 31 * result + number.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (number.get(0) == 1) result.append('-');
        for (int i = 1; i <= nBefore; i++) {
            result.append(number.get(i));
        }
        if (nAfter != 0) result.append('.');
        for (int i = nBefore + 1; i <= nBefore + nAfter; i++) {
            result.append(number.get(i));
        }
        return result.toString();
    }
}