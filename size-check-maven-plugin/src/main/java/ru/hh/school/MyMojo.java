package ru.hh.school;

import org.apache.maven.plugin.AbstractMojo;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This plugin finds large files in a directory by comparing their size with threshold.
 * It shows path to such files.
 *
 * @author AndreyPonorenko
 */
@Mojo(name = "check-sizes")
public class MyMojo extends AbstractMojo {

    /**
     * Path to the top directory.
     */
    @Parameter(property = "path", defaultValue = "${basedir}")
    private File path;

    /**
     * Threshold value of bytes to compare with file size in the directory.
     */
    @Parameter(property = "threshold", defaultValue = "1024")
    private long threshold;

    /**
     * Recursive search to analyze files in directories.
     */
    @Parameter(property = "recursiveDescent", defaultValue = "true")
    private boolean recursiveDescent;

    public void execute() {
        if (path.exists()) {
            searchLargeFiles(path);
        } else {
            getLog().warn(String.format("Chosen directory: %s does not exists", path));
        }
    }

    void searchLargeFiles(File dir) {
        if (!dir.canRead()) {
            getLog().warn(dir + " cannot be read");
            return;
        }
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory() && recursiveDescent) {
                searchLargeFiles(file);
            } else if (file.isFile()){
                check(file);
            }
        }
    }

    void check(File file) {
        if (file.length() > threshold)
            getLog().warn(String.format("%s = %s bytes (more then %s bytes) ",
                    file.getAbsolutePath(), file.length(), threshold));
    }
}
