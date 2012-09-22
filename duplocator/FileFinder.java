package duplocator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileFinder {

    private final List<File> files = new ArrayList<File>();

    public void findFiles(final File dir, final int minBytes) {
        final File[] entries = dir.listFiles();

        for (final File entry : entries) {
            if (!isSymlink(entry)) {
                if (entry.isFile()) {
                    if (entry.length() >= minBytes) {
                        this.files.add(entry);
                    }
                } else if (entry.isDirectory()) {
                    if (entry.canRead()) {
                        findFiles(entry, minBytes);
                    } else {
                        System.err.println("Error reading directory " + entry);
                    }
                } else {
                    System.err.println("Skipping " + entry);
                }
            } else {
                System.err.println("Skipping " + entry );
            }
        }
    }

    private boolean isSymlink(final File file) {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        try {
            File canon;
            if (file.getParent() == null) {
                canon = file;
            } else {
                final File canonDir = file.getParentFile().getCanonicalFile();
                canon = new File(canonDir, file.getName());
            }
            return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> getFiles() {
        return this.files;
    }
}

class FileSizeComparator implements Comparator<File> {

    @Override
    public int compare(final File first, final File second) {
        return (int) (second.length() - first.length());
    }
}
