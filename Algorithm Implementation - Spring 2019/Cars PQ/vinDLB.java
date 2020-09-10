public class vinDLB
    {
        private Node root;
        private Node current;
    
        vinDLB()
        {}
    
        boolean add(Car newCar, String key)
        {
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
            if (current == null || current.getData() != null)
            {
                return false;
            }
            current.setData(newCar);
            return true;
        }
    
        boolean remove(String key)
        {
            current = root;
    
            for (int i = 0; i<key.length(); i++)
            {
                if (root == null)
                {
                    return false;
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
                                    return false;
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
                                        return false;
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
                            return false;
                        }
                    }
                }
            }
            if (current == null || current.getData() == null)
            {
                //System.out.println(current == null);
                //System.out.println(current.getData() == null);
                return false;
            }
            current.setData(null);
            return true;
        }
    
        Car search(String key)
        {
            //System.out.println("vinDLB key = " + key);
            current = root;
    
            for (int i = 0; i<key.length(); i++)
            {
                //System.out.println("Key at i = " + i + " = " + key.charAt(i));
                if (root == null)
                {
                    //System.out.println("root == null");
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
                                    //System.out.println("current.getRight() == null");
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
                                    //System.out.println(current.getChar()+":"+key.charAt(i));
                                }
                                else
                                {
                                    if (current.getRight() == null)
                                    {
                                        //System.out.println("other current.getRight() == null");
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
                            //System.out.println("else null");
                            return null;
                        }
                    }
                }
            }
            if (current == null || current.getData() == null)
            {
                if (current == null)
                {
                    //System.out.println("currnet == null in compound expression case");
                }
                else
                {
                    //System.out.println("currnet.getData() == null in compound expression case");
                }
                
                return null;
            }
            return current.getData();
        }
    
        class Node
            {
                private Car data;
                private Node down;
                private Node right;
                private char ch;
        
                Node(char letter)
                {
                    ch = letter;
                }
    
                void setData(Car d)
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
    
                Car getData()
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