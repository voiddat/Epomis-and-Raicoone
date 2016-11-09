package net.elenx.raicoone.repository.samba;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import lombok.Data;
import lombok.SneakyThrows;
import no.uis.nio.smb.SMBDirectoryStream;
import no.uis.nio.smb.SMBFileSystemProvider;
import no.uis.nio.smb.SMBPath;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

@Data
class SambaHelper
{
    private final SMBFileSystemProvider smbFileSystemProvider;
    private final NtlmPasswordAuthentication ntlmPasswordAuthentication;

    private final DirectoryStream.Filter<Path> filesFilter = entry -> file(entry).isFile();
    private final DirectoryStream.Filter<Path> foldersFilter = entry -> file(entry).isDirectory();

    @SneakyThrows
    InputStream inputStreamOf(SmbFile file)
    {
        return new BufferedInputStream(new SmbFileInputStream(file));
    }

    @SneakyThrows
    OutputStream outputStreamOf(SmbFile file)
    {
        return new BufferedOutputStream(new SmbFileOutputStream(file));
    }

    @SneakyThrows
    SmbFile file(Path path)
    {
        return file(path.toString());
    }

    @SneakyThrows
    SmbFile file(String filePath)
    {
        return new SmbFile(filePath, ntlmPasswordAuthentication);
    }

    @SneakyThrows
    SMBPath path(String filePath)
    {
        return new SMBPath(smbFileSystemProvider, new URI(filePath));
    }

    SMBDirectoryStream filesStream(String filePath)
    {
        return itemsStream(filePath, filesFilter);
    }

    SMBDirectoryStream foldersStream(String filePath)
    {
        return itemsStream(filePath, foldersFilter);
    }

    private SMBDirectoryStream itemsStream(String filePath, DirectoryStream.Filter<Path> itemsFilter)
    {
        return new SMBDirectoryStream(smbFileSystemProvider, path(filePath), itemsFilter);
    }
}