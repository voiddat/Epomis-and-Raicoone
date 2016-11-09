package net.elenx.epomis.provider.pl.pracagratka;

import lombok.Data;
import net.elenx.connection2.ConnectionRequest;
import net.elenx.connection2.ConnectionService2;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
class PracaGratkaPlCrawler implements JobOfferProvider{

    private final ConnectionService2 connectionService2;

    @Override
    public Set<JobOffer> offers() {
        ConnectionRequest connectionRequest = ConnectionRequest
                .builder()
                .url("http://praca.gratka.pl/mazowieckie/elektryk/")
                .method(Connection.Method.GET)
                .build();

        return connectionService2.submit(connectionRequest)
                .getDocument()
                .select("li.row.linkDoKarty")
                .stream()
                .map(this::extractOffer)
                .collect(Collectors.toSet());
    }

    private JobOffer extractOffer(Element jobOffer) {
        Elements element = jobOffer.getElementsByTag("a");
        String jobTitle = element.text();
        String company = jobOffer.getElementsByClass("listaOgloszenPracodawca").text();
        String city = jobOffer.select("dt:contains(Lokalizacja:) + dd").text();
        String href = element.attr("href");

        return JobOffer
                .builder()
                .providerType(ProviderType.PRACA_GRATKA)
                .title(jobTitle)
                .company(company)
                .location(city)
                .href(href)
                .build();
    }
}
