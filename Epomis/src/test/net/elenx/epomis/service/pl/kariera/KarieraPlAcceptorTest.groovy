package test.net.elenx.epomis.service.pl.kariera

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import net.elenx.epomis.service.mail.MailService
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import spock.lang.Specification

class KarieraPlAcceptorTest extends Specification {
    void "accept test"() {
        given:
        ConnectionResponse connectionResponse = Mock()
        Document document = Mock()
        Elements elements = Mock()
        JobOffer jobOffer = Mock()
        jobOffer.getHref() >> ""
        MailService mailService = Mock()
        ConnectionService2 connectionService2 = Mock()
        connectionService2.submit(_) >> connectionResponse
        connectionResponse.getDocument() >> document
        document.select(_) >> elements
        elements.text() >> ""

        KarieraPlAcceptor karieraPlAcceptor = new KarieraPlAcceptor(connectionService2, mailService)

        when:
        karieraPlAcceptor.accept(jobOffer);

        then:
        1 * mailService.send(_)

    }
}
