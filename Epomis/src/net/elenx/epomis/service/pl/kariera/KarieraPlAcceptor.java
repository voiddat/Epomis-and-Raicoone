package net.elenx.epomis.service.pl.kariera;

import lombok.Data;
import net.elenx.connection2.ConnectionRequest;
import net.elenx.connection2.ConnectionResponse;
import net.elenx.connection2.ConnectionService2;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.service.JobOfferService;
import net.elenx.epomis.service.mail.MailService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Data
@Service
public class KarieraPlAcceptor implements JobOfferService {

    private final ConnectionService2 connectionService2;
    private final MailService mailService;

    private static final String CSS_SELECT_WITH_EMAIL = "#tekst:contains(@)";
    private static final String E_MAIL = "e-mail: ";
    private static final String SPACE = " ";

    @Override
    public void accept(JobOffer jobOffer) {
        ConnectionRequest connectionRequest = ConnectionRequest
                .builder()
                .url(jobOffer.getHref())
                .method(Connection.Method.GET)
                .build();

        ConnectionResponse connectionResponse = connectionService2.submit(connectionRequest);
        Document document = connectionResponse.getDocument();

        Elements element = document.select(CSS_SELECT_WITH_EMAIL);
        String email = element.text();
        email = StringUtils.substringBetween(email, E_MAIL, SPACE);
        mailService.send(email);
    }
}
