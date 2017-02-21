import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FracNumber {

    private final int nBefore;
    private final int nAfter;
    private final int[] before;
    private final int[] after;

    public FracNumber (int nBefor, int nAfter, int[] before, int[] after){
        this.nBefore = nBefor;
        this.nAfter = nAfter;
        this.before = before;
        this.after = after;
    }

    private int[] arrayRevert(int length, int[] array){
        int[] result = new int[length];
        for (int i=0; i<length; i++){
            result[i] = array[length-i-1];
        }
        return result;
    }

    private int[] zero(int max, int[] array){
        for (int i = array.length; i<max; i++){
            array[i] = 0;
        }
        return array;
    }

    private int[] associationArrays (int length, int[] a, int[] b){
        int[] result = new int[length];
        for (int i = 0; i<a.length;i++){
            result[i] = a[i];
        }
        for (int i = a.length; i<a.length+b.length;i++){
            result[i]=b[i-a.length];
        }
        return result;
    }

    private int[] miniPartMultiplication (int max, int[] array, int[] otherArray){
        int[] revetArray = zero(max,arrayRevert(array.length,array));
        int[] revertOtherArray = zero(max,arrayRevert(otherArray.length,otherArray));
        int[] result = new int[revetArray.length+revertOtherArray.length];
        int p = 0;
        int rez = 0;
        for (int i=0; i<array.length;i++){
            p=0;
            for (int j = 0; j<otherArray.length;j++){
                rez = revetArray[i]*revertOtherArray[j]+p+result[i+j];
                result[i+j] = rez % 10;
                p = rez / 10;
            }
            result[i+otherArray.length] = p;
        }
        return result;
    }

    public String multiplication (FracNumber other){
        int[] array =  associationArrays(nAfter+nBefore,before,after);
        int[] otherArray = associationArrays(other.nAfter+other.nBefore,other.before,other.after);
        int maxSize = Math.max(array.length,otherArray.length);
        int[] result = miniPartMultiplication(maxSize,array,otherArray);
        int numResultAfter = nAfter+other.nAfter;
        int numResultBefor = result.length-numResultAfter;
        int [] resultResult = arrayRevert(result.length,result); //пробное
        int[] resultBefore = new int[numResultBefor];
        int[] resultAfter = new int[numResultAfter];
        for (int i=0; i<numResultBefor;i++){
            if (resultResult[i]!=0) resultBefore[i] = resultResult[i];
        }
        for (int i=result.length-1; i>=numResultBefor;i--){
            if (resultResult[i]!=0) resultAfter[i-numResultBefor] = resultResult[i];
        }
        return new FracNumber(numResultBefor,numResultAfter,resultBefore,resultAfter).toString();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj instanceof FracNumber){
            FracNumber other = (FracNumber) obj;
            if (nBefore == other.nBefore && nAfter == other.nAfter){
                for (int i=0; i<nBefore; i++){
                    if (before[i]!=other.before[i]) return false;
                }
                for (int i=0; i<nAfter; i++){
                    if (after[i]!=other.after[i]) return false;
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
        result = 31 * result + Arrays.hashCode(before);
        result = 31 * result + Arrays.hashCode(after);
        return result;
    }

    @Override
    public String toString(){
        String str="";
        int i =0;
        while (before[i]==0) {
            i++;
        }
        for (; i < nBefore; i++) {
            str = str + before[i];
        }
        str+='.';
        int a = after.length-1;
        while (after[a]==0) {
            a--;
        }
        for (i = 0; i <= a; i++){
            str = str + after[i];
        }
        return str;
    }
}
