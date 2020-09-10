public class minHEAP //Used parts of live-coded recitation material. Based mainly off of it.
    {
        int type = 0;
        int items = 0;
        Car[] heap = new Car[25];

        minHEAP(int t)
        {
            type = t;
        }

        public void insert(Car c)
        {
            if (items+1 > heap.length-1)
            {
                resize();
            }
            items++;
            heap[items] = c;

            updateIndex(heap[items], items);
            
            int index = swim(items);
            sink(index);
        }

        public void change(int i)
        {
            int index = swim(i);
            sink(index);
        }

        public void delete(int i)
        {
            if (i<=items)
            {
                //System.outprintln(i+":"+items);
                exchange(i, items);
                ////////System.out.println("O");
                items--;
                int index = swim(i);
                sink(index);
                //Counting on the main controller of the car (like that controls the other heaps) to fully delete the car. this just removes it from the heap.
            }
        }

        public Car min()
        {
            if (items<1)
            {
                return null;
            }
            return heap[1];
        }



        public void resize()
        {
            Car[] newheap = new Car[heap.length*2];
            for (int i=1; i<heap.length; i++)
            {
                newheap[i] = heap[i];
            }
            heap = newheap;
        }

        private boolean greater(int i, int j) {
            if (type == 1 || type == 3) //If this is the price heap.
            {
                return heap[i].comparePrice(heap[j]) == 1;
            }
            return heap[i].compareMiles(heap[j]) == 1;
        }

        private void exchange(int a, int b)
        {
            Car temp = heap[a];
            heap[a] = heap[b];
            heap[b] = temp;
            ////////System.out.println("heap[b] == null"+(heap[b]==null));

            updateIndex(heap[a], a);
            updateIndex(heap[b], b);
        }

        private int swim(int k) { //Taken from recitation
            while (k > 1 && greater(k/2, k)) {
                ////////System.out.println("Swim");
                ////////System.out.println(k);
                exchange(k, k/2);
                k = k/2;
                ////////System.out.println(k);
            }
            return k;
        }

        private void sink(int k) { //Taken from recitation
            while (2*k <= items) {
                ////////System.out.println("Sink");
                ////////System.out.println(k);
                //set to_swap to the left child
                int to_swap = 2*k;
                //if we have a right child and it is less than the left child, set to_swap to the right child.
                if (to_swap < items && greater(to_swap, to_swap+1)) to_swap++;
                //check whether we should swap k with to_swap
                if (!greater(k, to_swap)) break;
                //swap
                exchange(k, to_swap);
                //continue to sink
                k = to_swap;
                //////System.out.println(k);
            }
        }

        private void updateIndex(Car c, int i)
        {
            //////System.out.println("C == null"+(c==null));
            if (type == 1) //If this is the price heap.
            {
                c.index1 = i;
            }
            else if(type == 2) //If this is the miles heap.
            {
                c.index2 = i;
            }
            else if(type == 3) //If this is the make model price heap.
            {
                c.index3 = i;
            }
            else if(type == 4) //If this is the make model miles heap.
            {
                c.index4 = i;
            }
        }

    }