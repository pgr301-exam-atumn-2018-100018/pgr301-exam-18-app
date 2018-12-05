package app.api;

import app.Bucketlist;

import app.GraphiteMetricsConfig;
import com.codahale.metrics.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

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
    final MetricRegistry metrics = new MetricRegistry();
    private final Meter apiCalls = metrics.meter("API calls");
    private final Timer responses = metrics.timer("Response time");
    private final Counter itemsInList = metrics.counter("Number of items in list");
    private GraphiteMetricsConfig gmc;

    //Counts all request made to the API
    private void reportToConsole()
    {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    @RequestMapping("/add-item")
    public Object addItem(@RequestParam(value="item", defaultValue="error") String item)
    {
        reportToConsole();
        itemsInList.inc();
        apiCalls.mark();

        return bl.addItem(item);
    }

    @RequestMapping("/get-list")
    public Object getList()
    {
        final Timer.Context context = responses.time();
        reportToConsole();
        apiCalls.mark();

        try { return bl.getAll(); }
        finally { context.stop(); }
    }

    @RequestMapping("/get-item")
    public Object getItem(@RequestParam(value="id", defaultValue="0") int id)
    {
        reportToConsole();
        apiCalls.mark();

        Object item = bl.getItem(id);
        if (item != null)
            return item;
        else
            return "Item not found";
    }

    @RequestMapping("/update-item")
    public Object updateItem(@RequestParam(value="id", defaultValue="0") int id, @RequestParam(value="item", defaultValue="error") String item)
    {
        reportToConsole();
        apiCalls.mark();

        return bl.updateItem(id, item);
    }

    @RequestMapping("/delete-item")
    public Object deleteItem(@RequestParam(value="id", defaultValue="0") int id)
    {
        reportToConsole();
        apiCalls.mark();

        if (bl.deleteItem(id))
        {
            itemsInList.dec();
            return true;
        }
        else
            return false;
    }

    @RequestMapping("/delete-all")
    public boolean deleteAll()
    {
        reportToConsole();
        itemsInList.dec(itemsInList.getCount());
        apiCalls.mark();

        return bl.clearBucketList();
    }
}
