package tinker.apk.parser.cli;

import tinker.net.dongliu.apk.parser.ApkParser;
import tinker.net.dongliu.apk.parser.struct.resource.ResourceTable;

import java.io.File;

/**
 * Main method for parser apk
 *
 * @author Liu Dong {@literal <im@dongliu.net>}
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String apkFile = args[0];

        System.out.println("apkFile:" + apkFile);
        ApkParser parser = new ApkParser(new File(apkFile));
        parser.parseResourceTable();
        ResourceTable resourceTable = parser.getResourceTable();
    }

}
