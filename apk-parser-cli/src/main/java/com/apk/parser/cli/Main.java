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
        String oldApkFile = args[0];
        String newApkFile = args[1];

        System.out.println("old apkFile:"+oldApkFile);
        System.out.println("new apkFile:"+newApkFile);

        ApkParser parser = new ApkParser(new File(oldApkFile));
        ApkParser newParser = new ApkParser(new File(newApkFile));
        parser.parseResourceTable();
        newParser.parseResourceTable();

        System.out.println("resource table is equal: " + (parser.getResourceTable().equals(newParser.getResourceTable())));
    }
}
