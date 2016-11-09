package net.elenx.epomis.provider.pl.gazetapraca;

import lombok.Data;
import net.elenx.connection2.ConnectionRequest;
import net.elenx.connection2.ConnectionService2;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Data
@Component
class GazetaPracaCrawler implements JobOfferProvider {

    private final ConnectionService2 connectionService;

    @Override
    public Set<JobOffer> offers() {
        ConnectionRequest connectionRequest = ConnectionRequest
                .builder()
                .url("http://gazetapraca.pl/szukaj/s-java/w-mazowieckie/m-warszawa")
                .method(Connection.Method.GET)
                .build();

        return connectionService.submit(connectionRequest)
                .getDocument()
                .select("li[data-id]")
                .stream()
                .map(this::extractOffer)
                .collect(Collectors.toSet());
    }

    private JobOffer extractOffer(Element offer) {
        String jobTitle = offer.getElementsByTag("h3").text();
        String company = offer.getElementsByClass("employer-name").text();
        String city = offer.select("p").attr("data-city");
        String href =  offer.getElementsByTag("a").attr("href");

        return JobOffer
                .builder()
                .providerType(ProviderType.GAZETA_PRACA)
                .title(jobTitle)
                .company(company)
                .location(city)
                .href(href)
                .build();
    }
}
