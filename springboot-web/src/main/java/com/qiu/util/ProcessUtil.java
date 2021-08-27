package com.qiu.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * 用于执行外部命令
 */
public class ProcessUtil {

    public static ProcessResult process(String... command) throws IOException, InterruptedException {
        ProcessBuilder pbuilder = new ProcessBuilder(command);
        pbuilder.redirectErrorStream(true);
        Process process = pbuilder.start();
        InputStream scriptIn = process.getInputStream();
        byte[] scriptReturnBytes = IOUtils.toByteArray(scriptIn);
        IOUtils.closeQuietly(scriptIn);
        int exitCode = process.waitFor();
        return new ProcessResult(exitCode, new String(scriptReturnBytes));
    }

    public static ProcessResult process(String command, File dir) throws IOException, InterruptedException {
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
        }
        ProcessBuilder pbuilder = new ProcessBuilder(cmdarray).directory(dir).redirectErrorStream(true);
        Process process = pbuilder.start();
        InputStream scriptIn = process.getInputStream();
        byte[] scriptReturnBytes = IOUtils.toByteArray(scriptIn);
        IOUtils.closeQuietly(scriptIn);
        int exitCode = process.waitFor();
        return new ProcessResult(exitCode, new String(scriptReturnBytes));
    }

    public static ProcessResult process(String command, File dir, long timeout, TimeUnit unit)
            throws IOException, InterruptedException {
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
        }
        ProcessBuilder pbuilder = new ProcessBuilder(cmdarray).directory(dir).redirectErrorStream(true);
        Process process = pbuilder.start();
        final InputStream scriptIn = process.getInputStream();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final boolean[] ioRunning = new boolean[]{true};
        new Thread(() -> {
            byte[] buffer = new byte[64];
            try {
                int n = 0;
                while (ioRunning[0] && -1 != (n = scriptIn.read(buffer))) {
                    output.write(buffer, 0, n);
                }
            } catch (IOException e) {
            }
        }).start();
        boolean terminated = process.waitFor(timeout, unit);
        ioRunning[0] = false;
        IOUtils.closeQuietly(scriptIn);
        byte[] scriptReturnBytes = output.toByteArray();
        if (terminated) {
            return new ProcessResult(process.exitValue(), new String(scriptReturnBytes));
        } else {
            return new ProcessResult(Integer.MIN_VALUE, new String(scriptReturnBytes));
        }
    }

    public static class ProcessResult {
        public int    exit;
        public String output;

        public ProcessResult(int exit, String output) {
            super();
            this.exit = exit;
            this.output = output;
        }

    }

}
