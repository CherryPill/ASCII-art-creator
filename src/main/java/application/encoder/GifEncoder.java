package application.encoder;

import application.constants.AppConstants;
import application.utility.GifUtility;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

public class GifEncoder {

    private ImageWriter writer;
    private ImageWriteParam writerParam;
    private IIOMetadata metadata;

    private ImageOutputStream outputStream;
    private int imgType;
    private int frameDelay;

    private ImageWriter getWriter() throws IIOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
        if (!iter.hasNext()) {
            throw new IIOException(AppConstants.System.NO_WRITERS_FOUND);
        } else {
            return iter.next();
        }
    }

    public GifEncoder(ImageOutputStream outputStream,
                      int imgType, int frameDelay) {
        this.outputStream = outputStream;
        this.imgType = imgType;
        this.frameDelay = frameDelay;

    }

    public void encode() throws IOException {

        writer = getWriter();
        writerParam = writer.getDefaultWriteParam();
        ImageTypeSpecifier imageTypeSpecifier =
                ImageTypeSpecifier.createFromBufferedImageType(this.imgType);

        this.metadata =
                writer.getDefaultImageMetadata(imageTypeSpecifier,
                        writerParam);

        String metaFormatName = this.metadata.getNativeMetadataFormatName();

        IIOMetadataNode root = (IIOMetadataNode)
                metadata.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = GifUtility.getNode(
                root,
                "GraphicControlExtension");

        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute(
                "transparentColorFlag",
                "FALSE");
        graphicsControlExtensionNode.setAttribute(
                "delayTime",
                Integer.toString(this.frameDelay / 10));
        graphicsControlExtensionNode.setAttribute(
                "transparentColorIndex",
                "0");

        IIOMetadataNode commentsNode = GifUtility.getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by MAH");

        IIOMetadataNode appEntensionsNode = GifUtility.getNode(
                root,
                "ApplicationExtensions");

        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");


        child.setUserObject(new byte[]{0x1, (byte) (0), (byte)
                ((0) & 0xFF)});
        appEntensionsNode.appendChild(child);

        this.metadata.setFromTree(metaFormatName, root);
        this.writer.setOutput(this.outputStream);
        this.writer.prepareWriteSequence(null);
    }

    public void write(BufferedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), writerParam);
    }

    public void dispose() throws IOException {
        writer.endWriteSequence();
    }
}
