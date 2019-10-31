package application.utility;

import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

public class GifUtility {

    public static int getGifDelay(ImageReader ir) throws IOException {
        IIOMetadata imageMetaData = ir.getImageMetadata(0);
        String metaFormat = imageMetaData.getNativeMetadataFormatName();
        IIOMetadataNode rootNode = (IIOMetadataNode) imageMetaData.getAsTree(metaFormat);
        IIOMetadataNode graphicsControlExtensionMode = getNode(rootNode, "GraphicControlExtension");
        return Integer.valueOf(graphicsControlExtensionMode.getAttribute("delayTime"));
    }

    public static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return ((IIOMetadataNode) rootNode.item(i));
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return (node);
    }

}
