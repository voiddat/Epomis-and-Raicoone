package test.net.elenx.epomis.provider.pl.pracagratka

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import org.jsoup.Jsoup
import spock.lang.Specification

class PracaGratkaPlCrawlerTest extends Specification {
    void "extract JobOffers"() {
        given:
        InputStream inputStream = PracaGratkaPlCrawlerTest.class.getResourceAsStream("pracaGratka.html")

        ConnectionService2 connectionService2 = Mock()
        ConnectionResponse connectionResponse = Mock()
        connectionService2.submit(_) >> connectionResponse
        connectionResponse.getDocument() >> Jsoup.parse(inputStream, "UTF-8",
                "http://praca.gratka.pl/mazowieckie/elektryk/")

        PracaGratkaPlCrawler pracaGratkaPlCrawler = new PracaGratkaPlCrawler(connectionService2)

        when:
        Set<JobOffer> offers = pracaGratkaPlCrawler.offers()

        then:
        offers.size() == 30
    }

}
