package tinker.net.dongliu.apk.parser.struct.xml;

import tinker.net.dongliu.apk.parser.struct.ChunkHeader;

/**
 * @author dongliu
 */
public class XmlResourceMapHeader extends ChunkHeader {
    public XmlResourceMapHeader(int chunkType, int headerSize, long chunkSize) {
        super(chunkType, headerSize, chunkSize);
    }
}
