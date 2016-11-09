package net.elenx.raicoone.configuration;

import lombok.SneakyThrows;
import no.uis.nio.smb.SMBFileSystemProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SambaConfiguration {

    @SneakyThrows
    @Bean SMBFileSystemProvider smbFileSystemProvider() {
        return new SMBFileSystemProvider();
    }
}
