package test.net.elenx.raicoone.repository.samba

import jcifs.smb.SmbFile
import net.elenx.raicoone.repository.StorageService
import no.uis.nio.smb.SMBDirectoryStream
import spock.lang.Specification
import jcifs.smb.SmbException

import java.nio.file.Path
import java.nio.file.Paths

class SambaServiceTest extends Specification
{

    void "file uploading test"()
    {

        given:

        boolean uploadFlag = false
        SambaHelper sambaSMBFactory = Mock()
        BufferedOutputStream bufferedOutputStream = Mock()
        SmbFile smbFile = Mock()
        smbFile.exists() >> true
        smbFile.getParent() >> ""
        sambaSMBFactory.file(_) >> smbFile
        sambaSMBFactory.outputStreamOf(smbFile) >> bufferedOutputStream
        bufferedOutputStream.write(_) >> {
            uploadFlag = true
        }

        SambaService sambaService = new SambaService(sambaSMBFactory);

        when:

        sambaService.put("a", "a".getBytes())

        then:

        uploadFlag
    }

    void "file downloading test"()
    {

        given:

        SmbFile smbFile = Mock()
        SambaHelper sambaSMBFactory = Mock()
        sambaSMBFactory.file("") >> smbFile
        smbFile.length() >> "123456789".getBytes().length
        InputStream inputStream = SambaServiceTest.class.getResourceAsStream("downloadTest.txt")
        byte[] b = null;
        sambaSMBFactory.inputStreamOf(smbFile) >> inputStream

        SambaService sambaService = new SambaService(sambaSMBFactory)

        when:

        b = sambaService.take("")

        then:

        b == "123456789".getBytes()
    }

    void "file deleting test"()
    {

        given:

        SmbFile smbFile = Mock()
        SambaHelper sambaSMBFactory = Mock()
        smbFile.delete()
        sambaSMBFactory.file(_) >> smbFile

        SambaService sambaService = new SambaService(sambaSMBFactory)

        when:

        sambaService.remove("")

        then:

        1 * smbFile.delete()
    }

    void "contains() should return true"()
    {

        given:

        SmbFile smbFile = Mock()
        SambaHelper sambaSMBFactory = Mock()
        smbFile.exists() >> true
        sambaSMBFactory.file(_) >> smbFile

        SambaService sambaService = new SambaService(sambaSMBFactory)

        when:

        boolean flag = sambaService.contains("")

        then:

        flag
    }

    void "contains() should return false"()
    {

        given:

        SmbFile smbFile = Mock()
        SambaHelper sambaSMBFactory = Mock()
        smbFile.exists() >> false
        sambaSMBFactory.file(_) >> smbFile

        SambaService sambaService = new SambaService(sambaSMBFactory)

        when:

        boolean flag = sambaService.contains("")

        then:

        !flag
    }

    void "filesIn() test"() {

        given:

        Set<Path> test = new HashSet<Path>()
        test.add(Paths.get("a"))
        SambaHelper sambaHelper = Mock()
        SmbFile smbFile = Mock()
        sambaHelper.file(_) >> smbFile
        SMBDirectoryStream smbDirectoryStream = Mock()
        smbDirectoryStream.spliterator() >> test.spliterator()
        sambaHelper.filesStream("") >> smbDirectoryStream

        StorageService sambaService = new SambaService(sambaHelper)

        when:

        Set<String> testSet = sambaService.filesIn("")

        then:

        testSet.size() == 1
    }

    void "foldersIn() test"() {
        given:

        Set<Path> test = new HashSet<Path>()
        test.add(Paths.get("a"))
        SambaHelper sambaHelper = Mock()
        SmbFile smbFile = Mock()
        sambaHelper.file(_) >> smbFile
        SMBDirectoryStream smbDirectoryStream = Mock()
        smbDirectoryStream.spliterator() >> test.spliterator()
        sambaHelper.foldersStream("") >> smbDirectoryStream

        StorageService sambaService = new SambaService(sambaHelper)

        when:

        Set<String> testSet = sambaService.foldersIn("")

        then:

        testSet.size() == 1
    }

    void "move() should throw SmbException if destination doesnt exist"() {

        given:

        SmbFile smbFile = Mock()
        smbFile.renameTo(_) >> {
            throw new SmbException(404, false)
        }
        SambaHelper sambaHelper = Mock()
        sambaHelper.file(_) >> smbFile

        StorageService sambaService = new SambaService(sambaHelper)

        when:

        sambaService.move("","")

        then:

        thrown(SmbException)
    }
}