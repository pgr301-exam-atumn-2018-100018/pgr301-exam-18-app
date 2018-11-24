package app.api;

import app.db.Bucketlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
    1. Stop any running applications that run on port 8080
    2. Run the Application class, generate target
    3. Spring-boot:run
    4. Go to one of the API port listed below

    http://localhost:8080/add-item?item=Go skydiving
    http://localhost:8080/get-list
    http://localhost:8080/get-item?id=1
    http://localhost:8080/update-item?id=1&item=Dance all the Fortnite dances
    http://localhost:8080/delete-item?id=1
    http://localhost:8080/delete-all
*/

@RestController
public class BucketlistApi
{
    private Bucketlist bl = new Bucketlist();

    @RequestMapping("/add-item")
    public Object addItem(@RequestParam(value="item", defaultValue="error") String item)
    {
        return bl.addItem(item);
    }

    @RequestMapping("/get-list")
    public Object getList()
    {
        return bl.getAll();
    }

    @RequestMapping("/get-item")
    public Object getItem(@RequestParam(value="id", defaultValue="0") int id)
    {
        Object item = bl.getItem(id);
        if (item != null)
            return item;
        else
            return "Item not found";
    }

    @RequestMapping("/update-item")
    public Object updateItem(@RequestParam(value="id", defaultValue="0") int id, @RequestParam(value="item", defaultValue="error") String item)
    {
        return bl.updateItem(id, item);
    }

    @RequestMapping("/delete-item")
    public Object deleteItem(@RequestParam(value="id", defaultValue="0") int id)
    {
        return bl.deleteItem(id);
    }

    @RequestMapping("/delete-all")
    public boolean deleteAll()
    {
        return bl.clearBucketList();
    }
}
