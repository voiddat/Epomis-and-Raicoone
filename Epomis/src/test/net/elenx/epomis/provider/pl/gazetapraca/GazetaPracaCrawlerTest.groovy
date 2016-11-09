package test.net.elenx.epomis.provider.pl.gazetapraca

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import org.jsoup.Jsoup
import spock.lang.Specification

class GazetaPracaCrawlerTest extends Specification {
    void "should extract job offers"() {
        given:

        InputStream inputStream = GazetaPracaCrawler.class.getResourceAsStream("gazetaPraca.html")
        ConnectionService2 connectionService = Mock()
        ConnectionResponse connectionResponse = Mock()
        connectionService.submit(_) >> connectionResponse
        connectionResponse.getDocument() >> Jsoup.parse(inputStream, "UTF-8",
                "http://gazetapraca.pl/szukaj/s-java/w-mazowieckie/m-warszawa")

        GazetaPracaCrawler gazetaPracaCrawler = new GazetaPracaCrawler(connectionService)

        when:

        Set<JobOffer> jobOffers = gazetaPracaCrawler.offers()

        then:

        jobOffers.size() == 25

    }
}
