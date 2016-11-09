package net.elenx.raicoone.repository.samba;

import jcifs.smb.NtlmPasswordAuthentication;
import lombok.Data;
import net.elenx.raicoone.repository.StorageService;
import no.uis.nio.smb.SMBFileSystemProvider;
import org.springframework.stereotype.Component;

@Data
@Component
public class SambaFactory
{
    private final SMBFileSystemProvider smbFileSystemProvider;

    public StorageService createSamba(String domainUserPassword) {
        NtlmPasswordAuthentication ntlmPasswordAuthentication = new NtlmPasswordAuthentication(domainUserPassword);
        SambaHelper sambaHelper = new SambaHelper(smbFileSystemProvider, ntlmPasswordAuthentication);

        return new SambaService(sambaHelper);
    }
}
