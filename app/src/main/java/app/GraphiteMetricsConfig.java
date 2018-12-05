package app;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Configuration
public class GraphiteMetricsConfig
{
    @Bean
    public MetricRegistry getRegistry()
    {
        return new MetricRegistry();
    }

    @Bean
    public GraphiteReporter getReporter(MetricRegistry registry)
    {
        String hostedGraphiteHostname = ""; //TODO add Hosted Graphite hostname ("abcdefgh.carbon.hostedgraphite.com")
        String hostedGraphiteApiKey = ""; //TODO add Hosted Graphite API key (long string of chars and numbers)

        //Report to Hosted Graphite
        Graphite graphite = new Graphite(new InetSocketAddress(hostedGraphiteHostname, 2003));
        GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                .prefixedWith(hostedGraphiteApiKey)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        reporter.start(1, TimeUnit.SECONDS);
        sendReportTcp(hostedGraphiteHostname, hostedGraphiteApiKey);

        //Report to console
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);

        return reporter;
    }

    private void sendReportTcp(String hostedGraphiteHostname, String msg)
    {
        try
        {
            Socket conn = new Socket(hostedGraphiteHostname, 2003);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(msg + ".100018 1.2\n");
            conn.close();
        }
        catch (Exception e)
        {
            //deal with it
            System.out.println("error: " + e);
        }
    }
}
