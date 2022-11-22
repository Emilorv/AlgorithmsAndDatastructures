public class LinkedListMath {

    public static void main(String[] args) {
        LinkedListMath arbeid = new LinkedListMath();

        String number1 = "155560000000000";
        String number2 = "233300111111111111111";


        DoublyLinkedList linkedList = arbeid.ConvertToDoublyLinkedList(number1);
        DoublyLinkedList linkedList2 = arbeid.ConvertToDoublyLinkedList(number2);
        System.out.println(arbeid.Addition(linkedList, linkedList2));
        System.out.println(arbeid.Subtraction(linkedList,linkedList2));
    }

    public DoublyLinkedList ConvertToDoublyLinkedList(String number){
        DoublyLinkedList newDoublyLinkedList = new DoublyLinkedList();
        for (int i = 0; i < number.length() ; i++) {
                newDoublyLinkedList.addLast(Integer.parseInt(String.valueOf(number.charAt(i))));
        }
        return newDoublyLinkedList;
    }

    public DoublyLinkedList RemoveRedundantZeros(DoublyLinkedList list){

        while(list.getHead()!= null && list.getHead().getElement()==0){
            list.remove(list.head);
        }
        if(list.getHead() == null){
            list.addFirst(0);
        }
        return list;
    }

    public DoublyLinkedList Addition(DoublyLinkedList list1, DoublyLinkedList list2) {
        DoublyLinkedList newList = new DoublyLinkedList();
        int largestNumber;
        int smallestNumber;
        DoublyLinkedList largestList;
        DoublyLinkedList smallestList;
        if (list1.nElements > list2.nElements) {
            largestList = list1;
            largestNumber = list1.nElements;
            smallestList = list2;
            smallestNumber = list2.nElements;
        } else {
            largestList = list2;
            largestNumber = list2.nElements;
            smallestList = list1;
            smallestNumber = list1.nElements;
        }
        for (int i = 0; i < (largestNumber - smallestNumber); i++) {
            smallestList.addFirst(0);
        }
        int remainder = 0;
        for (int i = largestNumber-1; i >=0; i--) {
            int value = largestList.findNumber(i).getElement() + smallestList.findNumber(i).getElement()+remainder;
            if(value>9){
                remainder =1;
                value = value%10;
            } else{
                remainder = 0;
            }
            newList.addFirst(value);

            if (remainder == 1 && i == 0) {
                newList.addFirst(1);
            }
        }
        RemoveRedundantZeros(newList);
        return newList;
    }

    public DoublyLinkedList Subtraction(DoublyLinkedList list1, DoublyLinkedList list2){
        DoublyLinkedList newList = new DoublyLinkedList();

        DoublyLinkedList smallestList;
        DoublyLinkedList largestList;
        boolean negative = false;
        if (list1.nElements > list2.nElements) {
            largestList = list1;
            smallestList = list2;

        }else if(list1.nElements == list2.nElements){
            int i = 0;
            while(list1.findNumber(i).getElement() == list2.findNumber(i).getElement() && i < list1.nElements-1){
                i++;
            }
            if(list1.findNumber(i).getElement()>list2.findNumber(i).getElement()){
                largestList = list1;
                smallestList = list2;
            } else{
                largestList = list2;
                smallestList = list1;
                negative =true;
            }
        }
        else {
            largestList = list2;
            smallestList = list1;
            negative = true;
        }
        int largestNumber = largestList.getnElements();
        int smallestNumber = smallestList.getnElements();

        for (int i = 0; i < (largestNumber - smallestNumber); i++) {
            smallestList.addFirst(0);
        }

        int loan= 0;
        for (int i = largestNumber-1; i >=0; i--) {
            int value = largestList.findNumber(i).getElement() - smallestList.findNumber(i).getElement()-loan;
            if(value<0){
                loan = 1;
                value = 10+(value%10);
            } else{
                loan = 0;
            }
            newList.addFirst(value);
        }
        RemoveRedundantZeros(newList);
        if (negative == true){
            int value = newList.getHead().getElement();
            newList.remove(newList.getHead());
            newList.addFirst(-value);
        }
        return newList;
    }


    public class DoublyLinkedList {
        private Node head = null;
        private Node tail = null;
        private int nElements =0;


        public int getnElements(){
            return nElements;
        }
        public Node getHead(){
            return  head;
        }

        public void addFirst(int value){
            head = new Node(value, head, null);
            if(tail ==null) tail = head;
            else head.next.prev=head;
            nElements++;
        }

        public void addLast(int value){
            Node ny = new Node(value, null, tail);
            if(tail !=null) tail.next = ny;
            else head = ny;
            tail = ny;
            nElements++;
        }

        public Node remove(Node n){
            if(n.prev !=null) {
                n.prev.next = n.next;
            }
                else head=n.next;
                if(n.next !=null){
                    n.next.prev=n.prev;
                }
                else tail = n.prev;
                n.next=null;
                n.prev = null;
                nElements--;
                return n;
        }
        public Node findNumber(int nr){
            Node findNode = head;
            if(nr<nElements){
                for(int i=0; i<nr; i++){
                  findNode = findNode.next;
                }
                return findNode;
            } else return null;
        }

        public void deleteAll(){
            head=null;
            nElements =0;
        }

        @Override
        public String toString() {
            String tall = "";
            Iterator iter = new Iterator(this);
            while(!iter.end()) {
                tall += iter.findElement();
                iter.next();
            }
            return tall;
        }
    }



    public class Node{
        int element;
        Node next;
        Node prev;

        public Node(int e, Node n, Node p){
            element = e;
            next=n;
            prev = p;
        }

        public int getElement(){
            return element;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

    }

    public class Iterator{
        private Node space;
        public Iterator(DoublyLinkedList l){
            space = l.getHead();
        }

        public boolean end(){
            return space ==null;
        }

        public Object findElement(){
            if(!end())
                return space.getElement();
            else return null;
        }
        public void next(){
            if(!end())
                space=space.getNext();
        }
    }

}
