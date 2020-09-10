public class mmDLB
    {
        private Node root;
        private Node current;
    
        mmDLB()
        {}
    
        public minHEAP[] add(Car newCar)
        {
            String key = newCar.getMake()+newCar.getModel();
            current = root;
    
            for (int i = 0; i<key.length(); i++)
            {
                if (root == null)
                {
                    root = new Node(key.charAt(i));
                    current = root;
                }
                else
                {
                    if (i == 0)
                    {
                        boolean found = false;
                        while (!found)
                        {
                            if (current.getChar() == key.charAt(i))
                            {
                                found = true;
                            }
                            else
                            {
                                if (current.getRight() == null)
                                {
                                    Node next = new Node(key.charAt(i));
                                    current.setRight(next);
                                    current = current.getRight();
                                    found = true;
                                }
                                else
                                {
                                    current = current.getRight();
                                }
                            }
                        }
                    }
                    else
                    {
                        if (current.getDown() != null)
                        {
                            current = current.getDown();
                            boolean found = false;
                            while (!found)
                            {
                                if (current.getChar() == key.charAt(i))
                                {
                                    found = true;
                                }
                                else
                                {
                                    if (current.getRight() == null)
                                    {
                                        Node next = new Node(key.charAt(i));
                                        current.setRight(next);
                                        current = current.getRight();
                                        found = true;
                                    }
                                    else
                                    {
                                        current = current.getRight();
                                    }
                                }
                            }
                        }
                        else
                        {
                            Node next = new Node(key.charAt(i));
                            current.setDown(next);
                            current = current.getDown();
                        }
                    }
                }
            }
            if (current == null)
            {
                return null;
            }
            else if (current.getData() != null)
            {
                return current.getData();
            }
            minHEAP newHEAP1 = new minHEAP(3);
            minHEAP newHEAP2 = new minHEAP(4);
            minHEAP[] both = new minHEAP[2];
            both[0] = newHEAP1;
            both[1] = newHEAP2;

            current.setData(both);
            return both;
        }
    
        public minHEAP[] search(String key)
        {
            current = root;
    
            for (int i = 0; i<key.length(); i++)
            {
                if (root == null)
                {
                    return null;
                }
                else
                {
                    if (i == 0) // Root already exists, but still on first character.
                    {
                        boolean found = false;
                        while (!found)
                        {
                            if (current.getChar() == key.charAt(i))
                            {
                                found = true;
                            }
                            else
                            {
                                if (current.getRight() == null)
                                {
                                    return null;
                                }
                                else
                                {
                                    current = current.getRight();
                                }
                            }
                        }
                    }
                    else
                    {
                        if (current.getDown() != null)
                        {
                            current = current.getDown();
                            boolean found = false;
                            while (!found)
                            {
                                if (current.getChar() == key.charAt(i))
                                {
                                    found = true;
                                }
                                else
                                {
                                    if (current.getRight() == null)
                                    {
                                        return null;
                                    }
                                    else
                                    {
                                        current = current.getRight();
                                    }
                                }
                            }
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            }
            if (current == null || current.getData() == null)
            {
                return null;
            }
            return current.getData();
        }
    
        class Node
            {
                private minHEAP[] data;
                private Node down;
                private Node right;
                private char ch;
        
                Node(char letter)
                {
                    ch = letter;
                }
    
                void setData(minHEAP[] d)
                {
                    data = d;
                }
    
                void setDown(Node below)
                {
                    down = below;
                }
    
                void setRight(Node side)
                {
                    right = side;
                }
    
                minHEAP[] getData()
                {
                    return data;
                }
    
                Node getRight()
                {
                    return right;
                }
    
                Node getDown()
                {
                    return down;
                }
    
                char getChar()
                {
                    return ch;
                }
            }
    
    }