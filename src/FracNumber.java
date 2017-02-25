import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FracNumber {

    private final int nBefore;
    private final int nAfter;
    private final List<Integer> before;
    private final List<Integer> after;

    public FracNumber(int nBefor, int nAfter, List before, List after) {
        this.nBefore = nBefor;
        this.nAfter = nAfter;
        this.before = before;
        this.after = after;
    }

    public FracNumber(String number) throws IllegalArgumentException{
        List<Integer> b = new ArrayList<>();
        List<Integer> a = new ArrayList<>();
        Pattern p = Pattern.compile("(^(\\d+)(\\.|,)(\\d+)$)|(^\\d+$)");
        Matcher m = p.matcher(number);
        if (!m.find()) throw new IllegalArgumentException();
        p = Pattern.compile("(\\d+)(?=\\.|,)");
        m = p.matcher(number);
        if (m.find()){
            for (int i =0;i<m.group().length();i++){
                b.add(m.group().codePointAt(i)-'0');
            }
        }
        else{
            p = Pattern.compile("\\d+");
            m = p.matcher(number);
            if (m.find()) {
                for (int i = 0; i < m.group().length(); i++) {
                    b.add(m.group().codePointAt(i) - '0');
                }
            }
        }
        this.before = b;
        p = Pattern.compile("(?<=(\\.|,))\\d+");
        m = p.matcher(number);
        if (m.find()){
            for (int i =0;i<m.group().length();i++){
                a.add(m.group().codePointAt(i)-'0');
            }
        }
        this.after = a;
        this.nBefore = b.size();
        this.nAfter = a.size();
    }

    private List<Integer> reversList(List list) {
        List result = new ArrayList();
        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }
        return result;
    }

    private List<Integer> union(List before, List after) {
        List result = new ArrayList();
        result.addAll(before);
        result.addAll(after);
        return result;
    }

    private List<Integer> maxFracNumber(FracNumber a, FracNumber b){
        List<Integer> result = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        List<Integer> otherList = new ArrayList<>();
        if (a.equals(b)) return union(a.before,a.after);
        if (a.nBefore>b.nBefore) return union(a.before,a.after);
        if (a.nBefore<b.nBefore) return union(b.before,b.after);
        else{
            list = union(a.before,a.after);
            otherList = union(b.before,b.after);
            int max = Math.max(list.size(),otherList.size());
            for (int i = 0; i<max-list.size(); i++){
                list.add(0);
            }
            for (int i =0; i<max-otherList.size(); i++){
                otherList.add(0);
            }
            int i =0;
            while(true){
                if (list.get(i)>otherList.get(i)) return list;
                if (list.get(i)<otherList.get(i)) return otherList;
                i++;
            }
        }
    }

    public String multiplication(FracNumber other) {
        int arrayLength = nAfter + nBefore;
        int otherArrayLength = other.nAfter + other.nBefore;
        List<Integer> array = union(before, after);
        List<Integer> otherArray = union(other.before, other.after);
        array = reversList(array);
        otherArray = reversList(otherArray);
        List<Integer> result = new ArrayList<>();
        int p = 0;
        int rez = 0;
        for (int i = 0; i < arrayLength; i++) {
            p = 0;
            for (int j = 0; j < otherArrayLength; j++) {
                if (i + j > result.size() - 1) {
                    rez = array.get(i) * otherArray.get(j) + p;
                    result.add(rez % 10);
                } else {
                    rez = array.get(i) * otherArray.get(j) + p + result.get(i + j);
                    result.set(i + j, rez % 10);
                }
                p = rez / 10;
            }
            if (i + otherArrayLength > result.size() - 1) {
                result.add(p);
            } else {
                result.set(i + otherArrayLength, p);
            }
        }
        int resultAfterLength = nAfter + other.nAfter;
        while (result.lastIndexOf(0) == result.size() - 1 && result.size() - resultAfterLength != 1) {
            result.remove(result.size() - 1);
        }
        int resultBeforLength = result.size() - resultAfterLength;
        while (result.indexOf(0) == 0) {
            result.remove(0);
            resultAfterLength--;
        }
        List<Integer> resultBefore = new ArrayList<>();
        List<Integer> resultAfter = new ArrayList<>();
        resultBefore = reversList(result.subList(resultAfterLength, result.size()));
        resultAfter = reversList(result.subList(0, resultAfterLength));
        return new FracNumber(resultBeforLength, resultAfterLength, resultBefore, resultAfter).toString();
    }

    public String plus (FracNumber other){
        List<Integer> resultBefore = new ArrayList<>();
        List<Integer> resultAfter = new ArrayList<>();
        int maxBefore = Math.max(nBefore,other.nBefore);
        int maxAfter = Math.max(nAfter,other.nAfter);
        for (int i =0; i<maxAfter-nAfter; i++){
            after.add(0);
        }
        for (int i =0; i<maxAfter-other.nAfter; i++){
            other.after.add(0);
        }
        for (int i = 0; i<maxBefore-nBefore;i++){
            before.add(0,0);
        }
        for (int i = 0; i<maxBefore-other.nBefore;i++){
            other.before.add(0,0);
        }

        int c = 0;
        for (int i = maxAfter-1; i>=0; i--){
            resultAfter.add(0,(after.get(i)+other.after.get(i)+c)%10);
            c = c/10;
        }
        for (int i = maxBefore-1; i>=0; i--){
            resultBefore.add(0,(before.get(i)+other.before.get(i)+c)%10);
            c = c/10;
        }
        if (c>0){
            resultBefore.add(0,c);
        }
        return new FracNumber(resultBefore.size(),resultAfter.size(),resultBefore,resultAfter).toString();
    }

    public String minus(FracNumber other){
        List<Integer> result = new ArrayList<>();
        int maxAfter = Math.max(nAfter,other.nAfter);
        for (int i =0; i<maxAfter-nAfter; i++){
            after.add(0);
        }
        for (int i =0; i<maxAfter-other.nAfter; i++){
            other.after.add(0);
        }
        List<Integer> maxNumber = maxFracNumber(this,other);
        List<Integer> minNumber = new ArrayList<>();
        if (maxFracNumber(this,other).equals(union(this.before,this.after))){
            minNumber = union(other.before,other.after);
        }
        else{
            minNumber = union(before,after);
        }
        maxNumber = reversList(maxNumber);
        minNumber = reversList(minNumber);
        int maxSize = maxNumber.size();
        int c = 0;
        for (int i = 0; i < maxSize; i++){
            if (i < maxSize-1){
                maxNumber.set(i+1,maxNumber.get(i+1)-1);
                result.add(10+maxNumber.get(i)+c);
                result.set(i,result.get(i)-minNumber.get(i));
            }
            else{
                result.add(maxNumber.get(i)+c);
            }
            c = 0;
            if (result.get(i) / 10 > 0){
                c = 1;
                result.set(i,result.get(i)%10);
            }
        }
        if (c!=0) result.add(c+maxNumber.get(maxSize-1));
        int resultAfterLength = Math.max(nAfter,other.nAfter);
        while (result.lastIndexOf(0) == result.size() - 1 && result.size() - resultAfterLength != 1) {
            result.remove(result.size() - 1);
        }
        result = reversList(result);
        int resultBeforLength = result.size() - resultAfterLength;
        while (result.indexOf(0) == 0 && result.size() - resultBeforLength != 0) {
            result.remove(0);
            resultAfterLength--;
        }
        List<Integer> resultBefore = new ArrayList<>();
        List<Integer> resultAfter = new ArrayList<>();
        resultBefore = result.subList(0, resultBeforLength);
        resultAfter = result.subList(resultBeforLength, result.size());
        if (result.size()==1 && result.get(0)==0) return "0";
        if (union(this.before,this.after).equals(maxFracNumber(this,other))) return new FracNumber(resultBeforLength,resultAfterLength,resultBefore,resultAfter).toString();
        return "-" + new FracNumber(resultBeforLength,resultAfterLength,resultBefore,resultAfter).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof FracNumber) {
            FracNumber other = (FracNumber) obj;
            if (nBefore == other.nBefore && nAfter == other.nAfter) {
                for (int i = 0; i < nBefore; i++) {
                    if (before.get(i) != other.before.get(i)) return false;
                }
                for (int i = 0; i < nAfter; i++) {
                    if (after.get(i) != other.after.get(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = nBefore;
        result = 31 * result + nAfter;
        result = 31 * result + before.hashCode();
        result = 31 * result + after.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < nBefore; i++) {
            str = str + before.get(i);
        }
        str += '.';
        for (int i = 0; i < nAfter; i++) {
            str = str + after.get(i);
        }
        return str;
    }
}
