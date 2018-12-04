package app;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
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

        Graphite graphite = new Graphite(new InetSocketAddress(hostedGraphiteHostname, 2003));
        GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                .prefixedWith(hostedGraphiteApiKey)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        reporter.start(1, TimeUnit.SECONDS);
        return reporter;
    }
}
