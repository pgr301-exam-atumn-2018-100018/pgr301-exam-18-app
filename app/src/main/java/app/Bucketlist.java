package app;

import java.util.HashMap;
import java.util.Map;

public class Bucketlist
{
    //CRUD = Create, Read, Update, Delete
    Map<Integer, String> map = new HashMap<Integer, String>();

    //Create
    public boolean addItem(String item)
    {
        int id = getSize() + 1;
        map.put(id, item);
        return true;
    }

    //Read
    public Map getAll()
    {
        return map;
    }

    public Object getItem(int id)
    {
        return map.get(id);
    }

    //Update
    public boolean updateItem(int id, String newString)
    {
        if(!map.containsKey(id))
            return false;

        map.replace(id, newString);
        return true;
    }

    //Delete
    public boolean deleteItem(int id)
    {
        if(!map.containsKey(id))
            return false;

        map.remove(id);
        return true;
    }

    public boolean clearBucketList()
    {
        map.clear();
        return true;
    }

    public int getSize()
    {
        return map.size();
    }
}