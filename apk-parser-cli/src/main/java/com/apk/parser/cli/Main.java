package com.apk.parser.cli;

import net.dongliu.apk.parser.ApkParser;

import java.io.File;

/**
 * Main method for parser apk
 *
 * @author Liu Dong {@literal <im@dongliu.net>}
 */
public class Main {
    public static void main(String[] args) throws Exception{
        String apkFile = args[0];

        System.out.println("apkFile:"+apkFile);
        ApkParser parser = new ApkParser(new File(apkFile));
        parser.getApkMeta();
        System.out.println(parser.getManifestXml());
    }
}
