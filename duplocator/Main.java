package duplocator;

import java.io.File;
import java.util.List;

public class Main {

    private static int minBytes;

    public static void main(final String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Usage: <dir> <minbytes>");
                System.exit(1);
            }

            minBytes = Integer.parseInt(args[1]);
            System.out.println("Finding files larger than " + minBytes + " bytes in " + args[0]);

            final FileFinder fileFinder = new FileFinder();
            fileFinder.findFiles(new File(args[0]), minBytes);
            final List<File> files = fileFinder.getFiles();
            System.out.println("Found " + files.size() + " files lange than " + minBytes + " bytes.");

            final DuplicateFinder duplicateFinder = new DuplicateFinder();
            duplicateFinder.findDuplicates(files);
        } catch (final RuntimeException e) {
            e.printStackTrace(System.err);
        }
    }
}
