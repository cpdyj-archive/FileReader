package file2str;
import java.util.*;
import java.io.*;

public class File2StrReader {
	private LinkedList<String> paths;
	private String currentPath;
	private String fileString;
	public File2StrReader(String path) {
		addPossiblePath(path);
	}
	public File2StrReader() {}
	public File2StrReader go() {
		return go(null);
	}
	public File2StrReader go(String charset) {
		FileInputStream fis=null;
		if (paths == null) {
			throw new RuntimeException("Must add files. Use addPossiblePath(String path);");
		}
		for (String path:paths) {
			currentPath = path;
			fis = openFile(path);
			if (fis != null) {
				break;
			}
		}
		if (fis == null) {
			currentPath = null;
			throw new RuntimeException("No file can read.");
		}
		try {
			BufferedInputStream bis=new BufferedInputStream(fis);
			byte[] b=new byte[fis.available()];
			int pos=0;
			while (true) {
				int size=bis.read(b, pos, Integer.MAX_VALUE);
				if (size == -1) {
					break;
				}
				pos += size;
			}
			fileString = new String(b, 0, pos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	public String getString() {
		return fileString;
	}
	public File2StrReader release() {
		fileString = null;
		return this;
	}
	private FileInputStream openFile(String path) {
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException ex) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public File2StrReader addPossiblePath(String path) {
		if (path == null) {
			throw new RuntimeException("path couldn't be null.");
		}
		if (paths == null) {
			paths = new LinkedList<String>();
		}
		paths.add(path);
		return this;
	}
}
