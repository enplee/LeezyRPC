package github.enplee.compress.gzip;

import github.enplee.compress.Compressor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *  @author: leezy
 *  @Date: 2021/5/15 16:04
 *  @Description: impl fream compress and decompress
 */

public class GzipCompressor implements Compressor {
    private static final int BUFFER_SIZE = 1024 * 4;
    @Override
    public byte[] compress(byte[] bytes) {
        if(bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try(ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(arrayOutputStream)){
            gzip.write(bytes);
            gzip.flush();
            gzip.finish();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error",e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if(bytes == null){
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error", e);
        }
    }
}
