package net.elenx.raicoone.repository.samba;

import jcifs.smb.SmbFile;
import lombok.Data;
import lombok.SneakyThrows;
import net.elenx.raicoone.repository.StorageService;
import no.uis.nio.smb.SMBDirectoryStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
class SambaService implements StorageService
{
    private static final boolean IS_FOLDER = false;
    private static final boolean IS_FILE = true;
    private static final boolean IS_PARALLEL = false;

    private final SambaHelper sambaHelper;

    @SneakyThrows
    @Override
    public void put(String filename, byte[] data)
    {
        SmbFile file = sambaHelper.file(filename);
        SmbFile parent = sambaHelper.file(file.getParent());

        if(!parent.exists())
        {
            parent.mkdirs();
        }

        try(OutputStream outputStream = sambaHelper.outputStreamOf(file))
        {
            outputStream.write(data);
        }
    }

    @SneakyThrows
    @Override
    public byte[] take(String filePath)
    {
        SmbFile file = sambaHelper.file(filePath);
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        try(InputStream inputStream = sambaHelper.inputStreamOf(file))
        {
            inputStream.read(bytes);
        }

        return bytes;
    }

    @SneakyThrows
    @Override
    public void remove(String filename)
    {
        sambaHelper.file(filename).delete();
    }

    @SneakyThrows
    @Override
    public boolean contains(String filename)
    {
        return sambaHelper.file(filename).exists();
    }

    @SneakyThrows
    @Override
    public void move(String from, String to)
    {
        SmbFile source = sambaHelper.file(from);
        SmbFile destination = sambaHelper.file(to);
        SmbFile destinationParent = sambaHelper.file(destination.getParent());

        if(!destinationParent.exists())
        {
            destinationParent.mkdirs();
        }

        source.renameTo(destination);
    }

    @SneakyThrows
    @Override
    public Set<String> filesIn(String folderPath)
    {
        try(SMBDirectoryStream smbDirectoryStream = sambaHelper.filesStream(folderPath))
        {
            return itemsIn(smbDirectoryStream);
        }
    }

    @SneakyThrows
    @Override
    public Set<String> foldersIn(String folderPath)
    {
        try(SMBDirectoryStream smbDirectoryStream = sambaHelper.foldersStream(folderPath))
        {
            return itemsIn(smbDirectoryStream);
        }
    }

    @SneakyThrows
    private Set<String> itemsIn(SMBDirectoryStream smbDirectoryStream)
    {
        return StreamSupport
                .stream(smbDirectoryStream.spliterator(), IS_PARALLEL)
                .map(sambaHelper::file)
                .map(SmbFile::toString)
                .collect(Collectors.toSet());
    }
}