package app;

import app.db.Bucketlist;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BucketlistApi
{
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/get-list")
    public Bucketlist getList()
    {
        return new Bucketlist();
    }

    @RequestMapping("/post-item")
    public Bucketlist setItem()
    {
        return new Bucketlist();
    }
}
