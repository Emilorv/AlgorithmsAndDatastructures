import java.util.Date;
import java.util.Random;

public class SortProgram {

    public static void main(String[] args) {
        //Antall elementer i hver tabell
        int n=80000000;
        int[] randomData = new int[n];
        int[] duplikater = new int[n];
        int[] ferdigSortert = new int[n];

        //Fyller tabellene med verdier
        initierData(randomData, duplikater,ferdigSortert,n);
        //Kjører SorterData metoden for hver tabell
        System.out.println("\nRandomData: ");
        SorterData(randomData, n);

        System.out.println("\nDuplikat Data");
        SorterData(duplikater, n);

        System.out.println("\nFerdigsortert: ");
        SorterData(ferdigSortert,n);

    }

    //Tar inn en tabell og antall verdier i tabellen. Kjører Single og dual pivot på tabellen og
    //gjennomfører sjekkSum og sjekkRekkefølge. Tar tiden på begge metodene
    private static void SorterData(int[] data, int n) {
        int[] sjekkeArray;
        double sumStart = sjekkSum(data);

        System.out.println("Sum:"+sumStart);
        System.out.println("Rekkefolge: " + sjekkRekkefølge(data));

        double tid = 0;
        double repeats = 0;
        //Single-pivot fra læreboken
        do {
            sjekkeArray = kopi(data);
            double timeStart = new Date().getTime();
            quicksort(sjekkeArray, 0, n - 1);
            double timeStop = new Date().getTime();
            tid+=(timeStop-timeStart);
            repeats++;
        }while(tid<1000);
        System.out.println("etter quicksort");
        double sumStop = sjekkSum(sjekkeArray);
        if(sumStop == sumStart){
            System.out.print("  Sum: Er den samme ");
        } else{
            System.out.print("Sum er forskjellig: " + sumStop);
        }
        System.out.print("  Rekkefolge:" + sjekkRekkefølge(sjekkeArray));
        System.out.println("   Tid brukt: " + (tid/repeats));

        //Dual Pivot GeeksForGeeks:
        tid = 0;
        repeats = 0;
        do {
            sjekkeArray = kopi(data);
            double timeStart = new Date().getTime();
            GeekForGeekQuickSort(sjekkeArray, 0, n - 1);
            double timeStop = new Date().getTime();
            tid+=timeStop-timeStart;
            repeats++;
        }while (tid<1000);
        System.out.println("etter geekForgeekQuicksort");
        sumStop = sjekkSum(sjekkeArray);
        if(sumStop == sumStart){
            System.out.print("  Sum: Er den samme ");
        } else{
            System.out.print("Sum er forskjellig: " + sumStop);
        }
        System.out.print("  Rekkefolge:"+sjekkRekkefølge(sjekkeArray));
        System.out.println("  Tid brukt: " + (tid/repeats));
    }

    //Summerer alle tall i tabellen
    public static int sjekkSum(int[] rekke){
        int sum = 0;
        for (int i = 0; i < rekke.length; i++) {
            sum += rekke[i];
        }
        return sum;
    }

    //Metode for å sjekke om Tallene står i rekkefølge
    public static boolean sjekkRekkefølge(int[] rekke){
        for (int i = 0; i < rekke.length-1; i++) {
                if(rekke[i+1]<rekke[i]){
                    return false;
                }
            }
        return true;
    }

    //lager en kopi av tabellen
    public static int[] kopi(int[] array){
        int[] kopi = new int[array.length];
        for (int i = 0; i < array.length ; i++) {
            kopi[i]=array[i];
        }
        return kopi;
    }

    //Single-pivot metode
    public static void bytt(int[]t, int i, int j){
        int k = t[j];
        t[j]=t[i];
        t[i]=k;
    }
    private static int
    median3sort(int []t, int v, int h) {
        int m = (v + h) / 2;
        if (t[v] > t[m]) bytt(t, v, m);
        if (t[m] > t[h]) {
            bytt(t, m, h);
            if (t[v] > t[m]) bytt(t, v, m);
        }
        return m;
    }
    public static void
    quicksort(int []t, int v, int h) {
        if (h - v > 2) {
            int delepos = splitt(t, v, h);
            quicksort(t, v, delepos - 1);
            quicksort(t, delepos + 1, h);
        } else median3sort(t, v, h);
    }
    private static int
    splitt(int []t, int v, int h) {
        int iv, ih;
        int m = median3sort(t, v, h);
        int dv = t[m];
        bytt(t, m, h - 1);
        for (iv = v, ih = h - 1;;) {
            while (t[++iv] < dv) ;
            while (t[--ih] > dv) ;
            if (iv >= ih) break;
            bytt(t, iv, ih);
        }
        bytt(t, iv, h-1);
        return iv;
    }




 //Dual-pivot metode
    public static void GeekForGeekQuickSort(int[] arr, int low, int high)
    {
        if (low < high)
        {
            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(arr, low, high);

            GeekForGeekQuickSort(arr, low, piv[0] - 1);
            GeekForGeekQuickSort(arr, piv[0] + 1, piv[1] - 1);
            GeekForGeekQuickSort(arr, piv[1] + 1, high);
        }

    }

    static void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static int[] partition(int[] arr, int low, int high)
    {
        swap(arr, low, low+(high-low)/3);
        swap(arr, high-(high-low)/3, high);
        if (arr[low] > arr[high])
            swap(arr, low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = arr[low], q = arr[high];

        while (k <= g)
        {

            // If elements are less than the left pivot
            if (arr[k] < p)
            {
                swap(arr, k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (arr[k] >= q)
            {
                while (arr[g] > q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if (arr[k] < p)
                {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        swap(arr, low, j);
        swap(arr, high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    public static void initierData(int[] randomData, int[] duplikater, int[]ferdigSortert, int e){
        Random random = new Random();
        for (int i = 0; i<e; i++){
            randomData[i] = random.nextInt(100);
        }

        for (int i = 0; i <e ; i++) {
            if(i%2==0){
                duplikater[i] = 50;
            }else{
                duplikater[i] = random.nextInt(100);
            }
        }
        for (int i = 0; i<e; i++){
            ferdigSortert[i] =i;
        }
    }
}
