import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FracNumber {

    private final int nBefore;
    private final int nAfter;
    private final List<Integer> number;

    public FracNumber(int before, int after, List<Integer> num){
        this.nBefore = before;
        this.nAfter = after;
        this.number = num;
    }

    public FracNumber(String number) throws NumberFormatException{
        List<Integer> result = new ArrayList<>();
        int before = 0;
        int after = 0;
        int i = 0;
        Pattern p = Pattern.compile("((^-)|(^))((\\d+$)|(\\d+\\.\\d+$))");
        Matcher m = p.matcher(number);
        if (!m.find()) throw new NumberFormatException();
        if (m.group().charAt(0) =='-'){
            i = 1;
            result.add(1);
        }
        else result.add(0);
        for(; i<m.group().length();i++){
            if (m.group().charAt(i)=='.') continue;
            if (after!=0||(i!=0 && m.group().charAt(i-1)=='.')){
                after++;
                result.add(m.group().codePointAt(i)-'0');
            }
            else {
                before++;
                result.add(m.group().codePointAt(i)-'0');
            }
        }
        this.nBefore = before;
        this.nAfter = after;
        this.number = result;
    }

    public FracNumber(long n){
        List<Integer> result = new ArrayList<>();
        int befor = 0;
        this.nAfter = 0;
        if (n<0) result.add(1);
        else result.add(0);
        while (n!=0){
            result.add(1,Math.abs((int)n%10));
            n = n /10;
            befor++;
        }
        this.nBefore = befor;
        this.number = result;
    }

    public FracNumber(int n){
        this((long)n);
    }

    public FracNumber(double n){
        this(Double.toString(n));
    }

    public FracNumber(float n){
        this((double)n);
    }

    private int power(int x, int a){
        int result = 1;
        for (int i = 1; i<=a; i++){
            result = result*x;
        }
        return result;
    }

    private int moreOrLess(List<Integer> first, List<Integer> second){
        if (first.get(0)==0 && second.get(0)==1) return 1;
        if (first.get(0)==1 && second.get(0)==0) return -1;
        for (int i =1; i<Math.min(first.size(),second.size()); i++){
            if (first.get(i)>second.get(i)) return 1;
            if (first.get(i)<second.get(i)) return -1;
        }
        return 0;
    }

    private List<Integer> excess(List<Integer> list, int before, int after){
        List<Integer> result = new ArrayList<>();
        result.addAll(list);
        result.remove(0);
        while (result.lastIndexOf(0) == result.size() - 1 && result.size()!= 1 && after!=0) {
            result.remove(result.size()-1);
            after--;
        }
        before = result.size() - after;
        while (result.indexOf(0) == 0 && before!=1) {
            result.remove(0);
            before--;
        }
        result.add(0,list.get(0));
        result.add(before);
        result.add(after);
        return result;
    }

    private List<Integer> zero(List<Integer> list, int maxBefore, int maxAfter, int before, int after){
        List<Integer> result = new ArrayList<>();
        result.addAll(list);
        for (int i =0; i<maxBefore-before; i++){
            result.add(1,0);
        }
        for (int i =0; i<maxAfter-after; i++){
            result.add(0);
        }
        return result;
    }

    public FracNumber rounding(int order) {
        List<Integer> result = new ArrayList<>();
        List<Integer> thisResult = new ArrayList<>();
        if (nAfter <= order) {
            return new FracNumber(nBefore, nAfter, number);
        } else {
            thisResult.addAll(number.subList(0,nBefore+order+1));
            int resultBefore = 0;
            int resultAfter = 0;
            if (number.get(nBefore + order+1) >= 5) {
                resultBefore = 1;
                resultAfter = order;
                result.add(1);
                for (int i = order; i > 0; i--) {
                    result.add(0, 0);
                }
                result.add(0,number.get(0));
                return new FracNumber(nBefore,order,thisResult).plus(new FracNumber(resultBefore,resultAfter,result));
            }
            else{
                resultBefore = nBefore;
                resultAfter = order;
                result.addAll(number.subList(0,nBefore+order+1));
                return new FracNumber(resultBefore,resultAfter,result);
            }
        }
    }

    public FracNumber plus(FracNumber other){
        if (number.size()==0) {
            number.clear();
            number.addAll(number);
        }
        if (other.number.size()==0) {
            other.number.clear();
            other.number.addAll(other.number);
        }
        if (number.get(0)==0 && other.number.get(0)==1) {
            other.number.set(0,0);
            return this.minus(other);
        }
        if (number.get(0)==1 && other.number.get(0)==0) {
            number.set(0,0);
            return other.minus(this);
        }
        List<Integer> result = new ArrayList<>();
        result.add(number.get(0));
        int maxBefore = Math.max(nBefore,other.nBefore);
        int maxAfter = Math.max(nAfter,other.nAfter);
        List<Integer> workList = new ArrayList<>();
        List<Integer> workOther = new ArrayList<>();
        workList.addAll(number);
        workOther.addAll(other.number);
        workList = zero(workList,maxBefore,maxAfter,nBefore,nAfter);
        workOther = zero(workOther,maxBefore,maxAfter,other.nBefore,other.nAfter);
        int maxSize = workList.size()-1;
        int c = 0;
        for (int i = maxSize; i>0; i--){
            result.add(1,(workList.get(i)+workOther.get(i)+c)%10);
            c = (workList.get(i)+workOther.get(i)+c)/10;
        }
        if (c>0){
            result.add(1,c);
        }
        int resultAfter = maxAfter;
        int resultBefor = maxSize-resultAfter;
        result = excess(result,resultBefor,resultAfter);
        resultAfter = result.get(result.size()-1);
        result.remove(result.size()-1);
        resultBefor = result.get(result.size()-1);
        result.remove(result.size()-1);
        return new FracNumber(resultBefor,resultAfter,result);
    }

    public FracNumber minus(FracNumber other){
        if (number.size() == 0) {
            number.clear();
            number.addAll(number);
        }
        if (other.number.size()==0) {
            other.number.clear();
            other.number.addAll(other.number);
        }
        if (number.get(0)==0 && other.number.get(0)==1){
            other.number.set(0,0);
            return this.plus(other);
        }
        if (number.get(0)==1 && other.number.get(0)==0){
            other.number.set(0,1);
            return this.plus(other);
        }
        if (number.get(0)==1 && other.number.get(0)==1){
            other.number.set(0,0);
        }
        List<Integer> result = new ArrayList<>();
        int maxAfter = Math.max(nAfter,other.nAfter);
        int maxBefor = Math.max(nBefore,other.nBefore);
        List<Integer> workList = new ArrayList<>();
        List<Integer> workOther = new ArrayList<>();
        workList.addAll(number);
        workOther.addAll(other.number);
        workList = zero(workList,maxBefor,maxAfter,nBefore, nAfter);
        workOther = zero(workOther,maxBefor,maxAfter,other.nBefore, other.nAfter);
        List<Integer> maxNumber = new ArrayList<>();
        List<Integer> minNumber = new ArrayList<>();
        if (moreOrLess(workList,workOther) ==1 || moreOrLess(workList,workOther) ==0){
            maxNumber = workList;
            minNumber = workOther;
            result.add(0);
        }
        else {
            maxNumber = workOther;
            minNumber = workList;
            result.add(1);
        }
        int maxSize = maxNumber.size()-1;
        Collections.reverse(maxNumber);
        Collections.reverse(minNumber);
        int c = 0;
        for (int i = 0; i < maxSize; i++){
            if (i < maxSize-1){
                maxNumber.set(i+1,maxNumber.get(i+1)-1);
                result.add(10+maxNumber.get(i)+c);
                result.set(i+1,result.get(i+1)-minNumber.get(i));
            }
            else{

                result.add(Math.abs(maxNumber.get(i)+c-minNumber.get(i)));
            }
            c = 0;
            if (result.get(i+1) / 10 > 0){
                c = 1;
                result.set(i+1,result.get(i+1)%10);
            }
        }
        if (c!=0) result.add(c+maxNumber.get(maxSize-1));
        int resultAfter = Math.max(nAfter,other.nAfter);
        int resultBefor = result.size() - resultAfter-1;
        result.add(result.get(0));
        result.remove(0);
        Collections.reverse(result);
        result = excess(result,resultBefor,resultAfter);
        resultAfter = result.get(result.size()-1);
        result.remove(result.size()-1);
        resultBefor = result.get(result.size()-1);
        result.remove(result.size()-1);
        return new FracNumber(resultBefor,resultAfter,result);
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
        Collections.reverse(workList);
        Collections.reverse(workOther);
        int p = 0;
        int rez = 0;
        for (int i = 0; i < workList.size(); i++) {
            p = 0;
            for (int j = 0; j < workOther.size(); j++) {
                if (i + j > result.size() - 1) {
                    rez = workList.get(i) * workOther.get(j) + p;
                    result.add(rez % 10);
                } else {
                    rez = workList.get(i) * workOther.get(j) + p + result.get(i + j);
                    result.set(i + j, rez % 10);
                }
                p = rez / 10;
            }
            if (i + workOther.size() > result.size() - 1) {
                result.add(p);
            } else {
                result.set(i + workOther.size(), p);
            }
        }
        int resultAfter = nAfter + other.nAfter;
        int resultBefor = result.size() - resultAfter;
        Collections.reverse(result);
        if (number.get(0)==1 && other.number.get(0)==0 || other.number.get(0)==1 && number.get(0)==0) result.add(0,1);
        if (number.get(0)==1 && other.number.get(0)==1) result.add(0,0);
        if (number.get(0)==0 && other.number.get(0)==0) result.add(0,0);
        result = excess(result,resultBefor,resultAfter);
        resultAfter = result.get(result.size()-1);
        result.remove(result.size()-1);
        resultBefor = result.get(result.size()-1);
        result.remove(result.size()-1);
        return new FracNumber(resultBefor, resultAfter, result);
    }

    public double toDouble(){
        double result = 0.0;
        for (int i =1; i <= nBefore; i++){
            result = result+number.get(i)*power(10,nBefore-i);
        }
        for (int i =1; i<=nAfter; i++){
            result = result + (double)number.get(i + nBefore) / (double)power(10, i);
        }
        if (number.get(0)==1) result *= -1;
        return result;
    }

    public float toFloat(){
        float result = 0;
        for (int i =1; i <= nBefore; i++){
            result = result+number.get(i)*power(10,nBefore-i);
        }
        for (int i =1; i<=nAfter; i++){
            result = result + (float)number.get(i + nBefore) / (float) power(10, i);
        }
        if (number.get(0)==1) result *= -1;
        return result;
    }

    public int toInt() throws IllegalArgumentException{
        if (nAfter!=0) throw new IllegalArgumentException();
        int result = 0;
        for (int i =1; i <= nBefore; i++){
            result = result+number.get(i)*power(10,nBefore-i);
        }
        if (number.get(0)==1) result *= -1;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof FracNumber) {
            FracNumber other = (FracNumber) obj;
            if (number.containsAll(other.number)) return true;
            else  return false;
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
    public String toString(){
        StringBuilder result = new StringBuilder();
        if(number.get(0)==1) result.append('-');
        for (int i = 1; i <= nBefore; i++){
            result.append(number.get(i));
        }
        if (nAfter!=0) result.append('.');
        for (int i = nBefore+1; i <= nBefore+nAfter; i++){
            result.append(number.get(i));
        }
        return result.toString();
    }
}